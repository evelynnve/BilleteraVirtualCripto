package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import conexionn.*;
public class CompraDAOjdbc implements CompraDAO {
	
	public String devolverStock (String nomenclatura) throws SQLException {
		DataSource dataSource = MiDataSource.getDataSource();	
		Connection cx = dataSource.getConnection();
		String sql = "SELECT STOCK, NOMBRE FROM MONEDA WHERE NOMENCLATURA = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setString(1, nomenclatura);
        ResultSet rs = pstmt.executeQuery();
		String stock = rs.getString("STOCK") +" "+ rs.getString("NOMBRE") + " (" + nomenclatura + ")";
		pstmt.close();
		rs.close();
		cx.close();
		return stock;
	}
	
	public double simularCompra(String cripto, String fiat, double cantidadFiat, String email, double precio) throws SQLException {
		DataSource dataSource = MiDataSource.getDataSource();	
		Connection cx = dataSource.getConnection();
		
	    String sql = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
	    PreparedStatement pstmt0 = cx.prepareStatement(sql);
	    pstmt0.setString(1, email);
	    ResultSet rs0 = pstmt0.executeQuery();
	    int ID_USUARIO = rs0.getInt("ID");
		
		int idMonedaFiat; int idMonedaCripto;
		sql = "SELECT ID FROM MONEDA WHERE NOMENCLATURA = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
		pstmt.setString(1, fiat);
		ResultSet rs = pstmt.executeQuery();
        idMonedaFiat = rs.getInt("ID");

		
        sql = "SELECT ID FROM MONEDA WHERE NOMENCLATURA = ?";
		PreparedStatement pstmt2 = cx.prepareStatement(sql);
        pstmt.setString(1, cripto);
		ResultSet rs2 = pstmt.executeQuery();
        idMonedaCripto = rs.getInt("ID");
        
        double cantidadFiatBD = obtenerCantidadActivoFiat(cx, ID_USUARIO, idMonedaFiat);

        if (cantidadFiatBD == -1) {
            pstmt0.close();
            pstmt.close();
            pstmt2.close();
            rs.close();
            rs0.close();
            rs2.close();
            cx.close();
            return 0; //la fiat no existe en la base de datos
        }
        
        if (cantidadFiat > cantidadFiatBD) {
            pstmt0.close();
            pstmt.close();
            pstmt2.close();
            rs.close();
            rs0.close();
            rs2.close();
            cx.close();
            return -1; // no hay suficiente fiat
        }
        
        double valorCripto = precio;
        double valorFiat = obtenerValorMoneda(cx, fiat) * cantidadFiat;
        
        if (valorFiat == -1) {
            pstmt0.close();
            pstmt.close();
            pstmt2.close();
            rs.close();
            rs0.close();
            rs2.close();
            cx.close();
            return -2; //la moneda fiat no tiene valor
        }
        double cantidadCriptoComprada = valorFiat / valorCripto;
        
        if (activoExiste(cx, idMonedaCripto, ID_USUARIO)) {
        	actualizarActivo(cx, idMonedaCripto, cantidadCriptoComprada, ID_USUARIO);
        }
        else {
        	insertarNuevoActivo(cx, idMonedaCripto, cantidadCriptoComprada, ID_USUARIO);
        }
        actualizarActivoFiat (cx, idMonedaFiat, cantidadFiat, ID_USUARIO);
        registrarTransaccion(cx, cripto, fiat, cantidadFiat, cantidadCriptoComprada, ID_USUARIO);
        
        pstmt0.close();
        pstmt.close();
        pstmt2.close();
        rs.close();
        rs0.close();
        rs2.close();
        cx.close();
        
        return cantidadCriptoComprada;
    }
	
	private double obtenerCantidadActivoFiat (Connection cx, int ID_USUARIO, int ID_MONEDA) throws SQLException {
		String sql = "SELECT CANTIDAD FROM ACTIVO_FIAT WHERE ID_USUARIO = ? AND ID_MONEDA = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
		pstmt.setLong(1, ID_USUARIO);
		pstmt.setLong(2, ID_MONEDA);
		ResultSet rs = pstmt.executeQuery();
        int cantidad = rs.getInt("CANTIDAD");
		pstmt.close();
		rs.close();
        return cantidad;
	}

	public double equivalencias (String cripto, String fiat, double cantidadFiat, double precio) throws SQLException {
	   DataSource dataSource = MiDataSource.getDataSource();	
	   Connection cx = dataSource.getConnection();
       double valorCripto = precio;
       double valorFiat = obtenerValorMoneda(cx, fiat);
       if (valorFiat == -1) {//asumimos que es euro ya que no esta cargado en la base de datos de nuestro ejemplo
    	   valorFiat = 1.05;
       }
       cantidadFiat = cantidadFiat*valorFiat;

       double cantidadEquivalencia = cantidadFiat / valorCripto;
       cx.close();
       return cantidadEquivalencia;
   }
   
   
   
	private double obtenerValorMoneda(Connection cx, String moneda) throws SQLException {
        String sql = "SELECT VALOR_DOLAR FROM MONEDA WHERE NOMENCLATURA = ?";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setString(1, moneda);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
        	double devolver = rs.getDouble("VALOR_DOLAR");
    		pstmt.close();
    		rs.close();
            return devolver;
        } else {
    		pstmt.close();
    		rs.close();
            return -1;
        }
    }
    
    
	private boolean activoExiste(Connection cx, int ID_MONEDA, int ID_USUARIO) throws SQLException {
        String sql = "SELECT ID_MONEDA FROM ACTIVO_CRIPTO WHERE ID_MONEDA = ? AND ID_USUARIO = ? UNION SELECT ID_MONEDA FROM ACTIVO_FIAT WHERE ID_MONEDA = ? AND ID_USUARIO = ?";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setLong(1, ID_MONEDA);
        pstmt.setLong(2, ID_USUARIO);
        pstmt.setLong(3, ID_MONEDA);
        pstmt.setLong(4, ID_USUARIO);
        ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			pstmt.close();
			rs.close();
			return true;
		}
		else {
			pstmt.close();
			rs.close();
			return false;
		}
    }

	private void actualizarActivoFiat (Connection cx, int ID_MONEDA, double cantidad, int ID_USUARIO) throws SQLException {
        String sql = "UPDATE ACTIVO_FIAT SET CANTIDAD = CANTIDAD - ? WHERE ID_MONEDA = ? AND ID_USUARIO = ?";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setDouble(1, cantidad);
        pstmt.setLong(2, ID_MONEDA);
        pstmt.setLong(3, ID_USUARIO);
        pstmt.executeUpdate();
		pstmt.close();
    }
    
	private void actualizarActivo(Connection cx, int ID_MONEDA, double cantidad, int ID_USUARIO) throws SQLException {
        String sql = "UPDATE ACTIVO_CRIPTO SET CANTIDAD = CANTIDAD + ? WHERE ID_MONEDA = ? AND ID_USUARIO = ?";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setDouble(1, cantidad);
        pstmt.setLong(2, ID_MONEDA);
        pstmt.setLong(3, ID_USUARIO);
        pstmt.executeUpdate();
		pstmt.close();
    }

	private void insertarNuevoActivo(Connection cx, int ID_MONEDA, double cantidad, int ID_USUARIO) throws SQLException {
        String sql = "INSERT INTO ACTIVO_CRIPTO (ID_USUARIO, ID_MONEDA, CANTIDAD) VALUES (?, ?, ?)";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setLong(1, ID_USUARIO);
        pstmt.setLong(2, ID_MONEDA);
        pstmt.setDouble(3, cantidad);
        pstmt.executeUpdate();
		
		pstmt.close();
    }

	private void registrarTransaccion(Connection cx, String cripto, String fiat, double cantidadFiat, double cantidadCriptoComprada, int ID_USUARIO) throws SQLException {
        String sql = "INSERT INTO TRANSACCION (RESUMEN, FECHA_HORA, ID_USUARIO) VALUES (?, datetime('now'), ?)";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        String resumen = "Compra de " + cantidadCriptoComprada + " " + cripto + " usando " + cantidadFiat + " " + fiat;
        pstmt.setString(1, resumen);
        pstmt.setLong(2, ID_USUARIO);
        pstmt.executeUpdate();
		pstmt.close();
    }
    
}
