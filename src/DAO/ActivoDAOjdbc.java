package DAO;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.json.JSONObject;
import conexionn.*;
import excepciones.MonedaException;
import DAO.*;
import GUI.*;
import modelo.*;
import app.*;

public class ActivoDAOjdbc implements ActivoDAO{
	private Map<String, Double> preciosActuales;
    private final Timer timer;

    public ActivoDAOjdbc() {
        preciosActuales = new HashMap<>();
        inicializarPrecios();
        timer = new Timer();
        ejecutarActualizacion();
    }

    private void inicializarPrecios() {
        preciosActuales.put("BTC", 0.0);
        preciosActuales.put("ETH", 0.0);
        preciosActuales.put("USDC", 0.0);
        preciosActuales.put("USDT", 0.0);
        preciosActuales.put("DOGE", 0.0);
    }

    // Actualización periódica cada 5 segundos
    private void ejecutarActualizacion() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                capturarYProcesarSalida();
            }
        }, 0, 5000);
    }

    private void capturarYProcesarSalida() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream originalOut = System.out;
            System.setOut(ps); // salida estándar
            ConsultarPrecioCripto.main(new String[]{});
            System.setOut(originalOut); // Restaura salida estándar
            String salidaCapturada = baos.toString();
            procesarTextoSalida(salidaCapturada); // Procesa la salida
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void procesarTextoSalida(String salida) {
        System.out.println("Salida capturada:\n" + salida);
        Pattern pattern = Pattern.compile("(BTC|ETH|USDC|USDT|DOGE): \\$(\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(salida);
        Map<String, Double> nuevosPrecios = new HashMap<>(preciosActuales);
        while (matcher.find()) {// Procesa las coincidencias encontradas
            String moneda = matcher.group(1);
            double precio = Double.parseDouble(matcher.group(2));
            nuevosPrecios.put(moneda, precio);
        }
        synchronized (preciosActuales) {
            preciosActuales = nuevosPrecios;
        }
    }

    public Map<String, Double> obtenerPreciosActuales() {
        synchronized (preciosActuales) {
            return new HashMap<>(preciosActuales);
        }
    }
    public void cargarActivos(String email) throws SQLException {
        DataSource dataSource = MiDataSource.getDataSource();    
        try (Connection cx = dataSource.getConnection()) {
            String sqlUsuario = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
            int idUsuario = -1;
            try (PreparedStatement pstmtUsuario = cx.prepareStatement(sqlUsuario)) {
                pstmtUsuario.setString(1, email);
                ResultSet rsUsuario = pstmtUsuario.executeQuery();
                if (rsUsuario.next()) {
                    idUsuario = rsUsuario.getInt("ID");
                } else {
                    return; 
                }
            }
            String sqlMoneda = "SELECT ID, TIPO, STOCK FROM MONEDA";
            try (Statement stmtMoneda = cx.createStatement();
                 ResultSet rsMoneda = stmtMoneda.executeQuery(sqlMoneda)) {
                while (rsMoneda.next()) {
                    int idMoneda = rsMoneda.getInt("ID");
                    double stock = rsMoneda.getDouble("STOCK");
                    String tipo = rsMoneda.getString("TIPO");
                    if (stock > 0) {
                        if ("F".equals(tipo)) {
                            actualizarOInsertarActivo(cx, "ACTIVO_FIAT", idUsuario, idMoneda, stock);
                        } else {
                            actualizarOInsertarActivo(cx, "ACTIVO_CRIPTO", idUsuario, idMoneda, stock);
                        }
                    }
                }
            }
        }
    }

    private void actualizarOInsertarActivo(Connection cx, String tabla, int idUsuario, int idMoneda, double stock) throws SQLException {
        String sqlCheck = "SELECT CANTIDAD FROM " + tabla + " WHERE ID_USUARIO = ? AND ID_MONEDA = ?";
        try (PreparedStatement pstmtCheck = cx.prepareStatement(sqlCheck)) {
            pstmtCheck.setInt(1, idUsuario);
            pstmtCheck.setInt(2, idMoneda);
            ResultSet rsCheck = pstmtCheck.executeQuery();
            if (rsCheck.next()) {
                double cantidadExistente = rsCheck.getDouble("CANTIDAD");
                if (cantidadExistente < stock) {
                    String sqlUpdate = "UPDATE " + tabla + " SET CANTIDAD = ? WHERE ID_USUARIO = ? AND ID_MONEDA = ?";
                    try (PreparedStatement pstmtUpdate = cx.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setDouble(1, stock);
                        pstmtUpdate.setInt(2, idUsuario);
                        pstmtUpdate.setInt(3, idMoneda);
                        pstmtUpdate.executeUpdate();
                    }
                }
            } else {
                String sqlInsert = "INSERT INTO " + tabla + " (ID_USUARIO, ID_MONEDA, CANTIDAD) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtInsert = cx.prepareStatement(sqlInsert)) {
                    pstmtInsert.setInt(1, idUsuario);
                    pstmtInsert.setInt(2, idMoneda);
                    pstmtInsert.setDouble(3, stock);
                    pstmtInsert.executeUpdate();
                }
            }
        }
    }
    
    public boolean[] checkActivos(String email) throws SQLException {
    	DataSource dataSource = MiDataSource.getDataSource();	
    	Connection cx = dataSource.getConnection();
    	boolean [] vectorNomenclatura = new boolean[5];
    	String sql = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
    	PreparedStatement pstmtUsuario = cx.prepareStatement(sql);
    	pstmtUsuario.setString(1, email);
    	ResultSet rsUsuario = pstmtUsuario.executeQuery();   	    	
    	String sql2 = "SELECT ID_MONEDA FROM ACTIVO_CRIPTO WHERE ID_USUARIO = ?";
        PreparedStatement pstmtActivos = cx.prepareStatement(sql2);
        pstmtActivos.setInt(1, rsUsuario.getInt("ID"));
        ResultSet rsActivo = pstmtActivos.executeQuery();		
		while (rsActivo.next()) {
			String sql3 = "SELECT NOMENCLATURA FROM MONEDA WHERE ID = ?";
	        PreparedStatement pstmtNomenclatura = cx.prepareStatement(sql3);
	        pstmtNomenclatura.setInt(1, rsActivo.getInt("ID_MONEDA"));
	        ResultSet rsNomenclatura = pstmtNomenclatura.executeQuery();	        
	        if (rsNomenclatura.next()) {
	            switch (rsNomenclatura.getString("NOMENCLATURA").toUpperCase()) {
	                case "BTC": 
	                    vectorNomenclatura[0] = true;
	                    break;
	                case "ETH": 
	                    vectorNomenclatura[1] = true;
	                    break;
	                case "USDC": 
	                    vectorNomenclatura[2] = true;
	                    break;
	                case "USDT": 
	                    vectorNomenclatura[3] = true;
	                    break;
	                case "DOGE": 
	                    vectorNomenclatura[4] = true;
	                    break;
	            }
	        }
	        pstmtNomenclatura.close();
	    }	    
	    pstmtUsuario.close();
	    pstmtActivos.close();
	    rsUsuario.close();
	    rsActivo.close();
	    cx.close();	    
	    return vectorNomenclatura;
	}
    public List<Moneda> obtenerDatosTabla(String email) throws SQLException {
		List<Moneda> list = new ArrayList<>();
		int id = 0;
		 DataSource dataSource = MiDataSource.getDataSource();
		    Connection cx = dataSource.getConnection();
		    String sql0 = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
		    PreparedStatement pstmt0 = cx.prepareStatement(sql0);
		    pstmt0.setString(1, email);
		    ResultSet rs0 = pstmt0.executeQuery();
		    if (rs0.next()) {
		    	id = rs0.getInt("ID");
		    }
		    double cantidad=-1;
		    String sql = "SELECT ID_MONEDA, CANTIDAD FROM ACTIVO_CRIPTO WHERE ID_USUARIO = ?";
		    PreparedStatement pstmt = cx.prepareStatement(sql);
		    pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
		        while (rs.next()) {
		        	cantidad = rs.getDouble("CANTIDAD");
		            String sql2 = "SELECT NOMBRE, NOMENCLATURA, NOMBRE_ICONO FROM MONEDA WHERE ID = ?";
		            try (PreparedStatement pstmt2 = cx.prepareStatement(sql2)) {
		                pstmt2.setLong(1, rs.getLong("ID_MONEDA"));
		                try (ResultSet rs2 = pstmt2.executeQuery()) {
		                    if (rs2.next()) { 
		                    	Moneda moneda = new Moneda();
		                    	moneda.setNombre(rs2.getString("NOMBRE"));
		                    	moneda.setNomenclatura(rs2.getString("NOMENCLATURA"));
		                    	moneda.setStock(cantidad);
		                    	moneda.setIcono(rs2.getString("NOMBRE_ICONO"));
		                    	list.add(moneda);
		                    }
		                }
		            }
		        }
		        String sql3 = "SELECT ID_MONEDA, CANTIDAD FROM ACTIVO_FIAT WHERE ID_USUARIO = ?";
			    PreparedStatement pstmt3 = cx.prepareStatement(sql3);
			    pstmt3.setLong(1, id);
			    ResultSet rs3 = pstmt3.executeQuery();
			        while (rs3.next()) {
			        	cantidad = rs3.getDouble("CANTIDAD");
			            String sql4 = "SELECT NOMBRE, NOMENCLATURA, NOMBRE_ICONO FROM MONEDA WHERE ID = ?";
			            try (PreparedStatement pstmt4 = cx.prepareStatement(sql4)) {
			                pstmt4.setLong(1, rs3.getLong("ID_MONEDA"));
			                try (ResultSet rs4 = pstmt4.executeQuery()) {
			                    if (rs4.next()) {  
			                    	Moneda moneda = new Moneda();
			                    	moneda.setNombre(rs4.getString("NOMBRE"));
			                    	moneda.setNomenclatura(rs4.getString("NOMENCLATURA"));
			                    	moneda.setStock(cantidad);
			                    	moneda.setIcono(rs4.getString("NOMBRE_ICONO"));
			                    	list.add(moneda);
			                    }
			                }
			            }
			        }
			    pstmt0.close();
			    rs0.close();
			    pstmt.close();
			    rs.close();
			    rs3.close();
			    pstmt3.close();
			    cx.close();
		        return list;
	        }
	 
	
public boolean tablaBDVacia(String email) throws SQLException, MonedaException {
	DataSource dataSource = MiDataSource.getDataSource();
	boolean devolver = true;
	Connection cx = dataSource.getConnection();
	String sql = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
	PreparedStatement pstmt = cx.prepareStatement(sql);
	pstmt.setString(1, email);
	ResultSet rs = pstmt.executeQuery();
	int id = rs.getInt("ID");
	String sql1 = "SELECT * FROM ACTIVO_FIAT WHERE ID_USUARIO = ? UNION SELECT * FROM ACTIVO_CRIPTO WHERE ID_USUARIO = ?";
	PreparedStatement pstmt1 = cx.prepareStatement(sql1);
	pstmt1.setLong(1, id);
	pstmt1.setLong(2, id);
	ResultSet rs1 = pstmt1.executeQuery();
	if (rs1.next()) {
		devolver = false;
	}
	pstmt.close();
	cx.close();
	rs.close();
	pstmt1.close();
	rs1.close();
	return devolver;
}

	public String[] misOperaciones (String email) throws SQLException {
		DataSource dataSource = MiDataSource.getDataSource();	
		Connection cx = dataSource.getConnection();
		String sql0 = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
	    PreparedStatement pstmt0 = cx.prepareStatement(sql0);
	    pstmt0.setString(1, email);
		ResultSet rs0 = pstmt0.executeQuery();
		int ID_USUARIO = rs0.getInt("ID");
		String sql = "SELECT * FROM TRANSACCION WHERE ID_USUARIO = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
		pstmt.setInt(1, ID_USUARIO);
		ResultSet rs = pstmt.executeQuery();
		String resumen = null;
		String fechaHora = null;
		List<String> operaciones = new ArrayList<>();
		while (rs.next()) {
			resumen = rs.getString("RESUMEN");
			fechaHora = rs.getString("FECHA_HORA");
			operaciones.add(fechaHora + " - " + resumen);
		}
		pstmt0.close();
		rs0.close();
		pstmt.close();
		rs.close();
		cx.close();		
		return operaciones.toArray(new String[0]);
	}   
}