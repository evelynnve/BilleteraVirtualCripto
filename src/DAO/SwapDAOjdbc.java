package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import conexionn.*;
public class SwapDAOjdbc implements SwapDAO{
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
	
	public double simularSwap (String criptoDestino, String CriptoOrigen, double cantidadOrigen, String email, double precioDestino, double precioOrigen) throws SQLException {
		DataSource dataSource = MiDataSource.getDataSource();	
		Connection cx = dataSource.getConnection();
		
	    String sql = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
	    PreparedStatement pstmt0 = cx.prepareStatement(sql);
	    pstmt0.setString(1, email);
	    ResultSet rs0 = pstmt0.executeQuery();
	    int ID_USUARIO = rs0.getInt("ID");
		
		int idMonedaOrigen; int idMonedaCripto;
		sql = "SELECT ID FROM MONEDA WHERE NOMENCLATURA = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
		pstmt.setString(1, CriptoOrigen);
		ResultSet rs = pstmt.executeQuery();
        idMonedaOrigen = rs.getInt("ID");
		
        sql = "SELECT ID FROM MONEDA WHERE NOMENCLATURA = ?";
		PreparedStatement pstmt2 = cx.prepareStatement(sql);
        pstmt.setString(1, criptoDestino);
		ResultSet rs2 = pstmt.executeQuery();
        idMonedaCripto = rs.getInt("ID");
        
        double cantidadOrigenBD = obtenerCantidadActivoCripto(cx, ID_USUARIO, idMonedaOrigen);

        if (cantidadOrigenBD == -1) {
            return 0; //la moneda origen no existe en la base de datos
        }
        
        if (cantidadOrigen > cantidadOrigenBD) {
            return -1; 
        }
        
        double valorCriptoDestino = precioDestino;
        double valorCriptoOrigen = precioOrigen * cantidadOrigen;
        
        if (valorCriptoOrigen == -1) {
            return -2; 
        }
        double cantidadCriptoComprada = valorCriptoOrigen / valorCriptoDestino;
        
        if (activoExiste(cx, idMonedaCripto, ID_USUARIO)) {
        	actualizarActivo(cx, idMonedaCripto, cantidadCriptoComprada, ID_USUARIO);
        }
        else {
        	insertarNuevoActivo(cx, idMonedaCripto, cantidadCriptoComprada, ID_USUARIO);
        }
        actualizarMoneda(cx, idMonedaOrigen, -cantidadOrigen, ID_USUARIO);
        actualizarMoneda(cx, idMonedaCripto, cantidadCriptoComprada, ID_USUARIO);
        actualizarActivo (cx, idMonedaOrigen, -cantidadOrigen, ID_USUARIO);
        
        registrarTransaccion(cx, criptoDestino, CriptoOrigen, cantidadOrigen, cantidadCriptoComprada, ID_USUARIO);
        
        pstmt0.close();
        pstmt.close();
        pstmt2.close();
        rs.close();
        rs0.close();
        rs2.close();
        cx.close();
        
        return cantidadCriptoComprada;
    }
	
	public double obtenerCantidadActivoCripto (Connection cx, int ID_USUARIO, int ID_MONEDA) throws SQLException {
		String sql = "SELECT CANTIDAD FROM ACTIVO_CRIPTO WHERE ID_USUARIO = ? AND ID_MONEDA = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
		pstmt.setLong(1, ID_USUARIO);
		pstmt.setLong(2, ID_MONEDA);
		ResultSet rs = pstmt.executeQuery();
        int cantidad = rs.getInt("CANTIDAD");
		pstmt.close();
		rs.close();
        return cantidad;
	}
   
    public double obtenerValorMoneda(Connection cx, String moneda) throws SQLException {
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
    
    
    public boolean activoExiste(Connection cx, int ID_MONEDA, int ID_USUARIO) throws SQLException {
        String sql = "SELECT ID_MONEDA FROM ACTIVO_CRIPTO WHERE ID_MONEDA = ? AND ID_USUARIO = ?";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setLong(1, ID_MONEDA);
        pstmt.setLong(2, ID_USUARIO);
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
    
    public void actualizarActivo(Connection cx, int ID_MONEDA, double cantidad, int ID_USUARIO) throws SQLException {
        String sql = "UPDATE ACTIVO_CRIPTO SET CANTIDAD = CANTIDAD + ? WHERE ID_MONEDA = ? AND ID_USUARIO = ?";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setDouble(1, cantidad);
        pstmt.setLong(2, ID_MONEDA);
        pstmt.setLong(3, ID_USUARIO);
        pstmt.executeUpdate();
		pstmt.close();
    }

    public void insertarNuevoActivo(Connection cx, int ID_MONEDA, double cantidad, int ID_USUARIO) throws SQLException {
        String sql = "INSERT INTO ACTIVO_CRIPTO (ID_USUARIO, ID_MONEDA, CANTIDAD) VALUES (?, ?, ?)";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setLong(1, ID_USUARIO);
        pstmt.setLong(2, ID_MONEDA);
        pstmt.setDouble(3, cantidad);
        pstmt.executeUpdate();	
		pstmt.close();
    }


    public void registrarTransaccion(Connection cx, String criptoDestino, String criptoOrigen, double cantidadOrigen, double cantidadCriptoComprada, int ID_USUARIO) throws SQLException {
        String sql = "INSERT INTO TRANSACCION (RESUMEN, FECHA_HORA, ID_USUARIO) VALUES (?, datetime('now'), ?)";
        PreparedStatement pstmt = cx.prepareStatement(sql);
        String resumen = "SWAP de " + cantidadCriptoComprada + " " + criptoDestino + " usando " + cantidadOrigen + " " + criptoOrigen;
        pstmt.setString(1, resumen);
        pstmt.setLong(2, ID_USUARIO);
        pstmt.executeUpdate();
		pstmt.close();
    }
    
    public void actualizarMoneda(Connection cx, int ID_MONEDA, double cantidad, int ID_USUARIO) throws SQLException {
    	 String sql = "UPDATE MONEDA SET STOCK = STOCK + ? WHERE ID = ?";
         PreparedStatement pstmt = cx.prepareStatement(sql);
         pstmt.setDouble(1, cantidad);
         pstmt.setLong(2, ID_MONEDA);
         pstmt.executeUpdate();
 		 pstmt.close();
    }
    
    public double equivalencias (String cripto, String fiat, double cantidadCriptoOrigen, double precio, double precioOrigen) throws SQLException {
 	   DataSource dataSource = MiDataSource.getDataSource();	
 	   Connection cx = dataSource.getConnection();	   
        double valorCripto = precio;
        double valorCriptoOrigen = precioOrigen;       
        cantidadCriptoOrigen = cantidadCriptoOrigen*valorCriptoOrigen;
        double cantidadEquivalencia = cantidadCriptoOrigen / valorCripto;
        cx.close();
        return cantidadEquivalencia;
    }
    
}