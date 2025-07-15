package DAO;

import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.sql.DataSource;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import conexionn.*;
import excepciones.MonedaException;
import modelo.*;

public class MonedaDAOjdbc implements MonedaDAO{
	
	public double balance(String email) throws SQLException {
	    double balance = 0;
	    DataSource dataSource = MiDataSource.getDataSource();
	    Connection cx = dataSource.getConnection();
	    String sql0 = "SELECT ID FROM USUARIO WHERE EMAIL = ?";
		PreparedStatement pstmt0 = cx.prepareStatement(sql0);
		pstmt0.setString(1, email);
		ResultSet rs0 = pstmt0.executeQuery();
		int id = rs0.getInt("ID");
	    String sql = "SELECT ID_MONEDA, CANTIDAD FROM ACTIVO_CRIPTO WHERE ID_USUARIO = ?";
	    PreparedStatement pstmt = cx.prepareStatement(sql);
	    pstmt.setLong(1, id);
	     ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String sql2 = "SELECT NOMENCLATURA FROM MONEDA WHERE ID = ?";
	            try (PreparedStatement pstmt2 = cx.prepareStatement(sql2)) {
	                pstmt2.setLong(1, rs.getLong("ID_MONEDA"));
	                try (ResultSet rs2 = pstmt2.executeQuery()) {
	                    if (rs2.next()) { 
	                        double valorMoneda = obtenerValorMoneda(cx, rs2.getString("NOMENCLATURA"));
	                        balance += valorMoneda * rs.getDouble("CANTIDAD");
	                    }
	                }
	            }
	        }
	        rs0.close();
	        rs.close();
	        pstmt0.close();
	        pstmt.close();
	    cx.close();
	    return balance;
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
	
	public void generarMonedas() throws SQLException {
	    DataSource dataSource = MiDataSource.getDataSource();
	    Connection cx = dataSource.getConnection();
	    
	    // Verificar si las monedas ya existen antes de insertarlas
	    if (!monedasExisten(cx)) {
	        Statement st = cx.createStatement();
	        Statement st2 = cx.createStatement();
	        Statement st3 = cx.createStatement();
	        Statement st4 = cx.createStatement();
	        Statement st5 = cx.createStatement();
	        Statement st6 = cx.createStatement();
	        Statement st7 = cx.createStatement();
	        Statement st8 = cx.createStatement();
	        
	        String sql1 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('C', 'BITCOIN', 'BTC', 100000, 16, 15, 'Bitcoin.gif')";
	        st.executeUpdate(sql1);
	        
	        String sql2 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('C', 'ETHEREUM', 'ETH', 3772, 5, 42, 'Ethereum.gif')";
	        st2.executeUpdate(sql2);
	        
	        String sql3 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('C', 'DOGECOIN', 'DOGE', 0.44, 8.36, 100, 'Dogecoin.gif')";
	        st3.executeUpdate(sql3);
	        
	        String sql4 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('F', 'PESO', 'ARS', 0.001, 40, 10000, 'ars.png.jpg')";
	        st4.executeUpdate(sql4);
	        
	        String sql5 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('F', 'DOLAR', 'USD', 1, 30, 345, 'dolar.png.jpg')";
	        st5.executeUpdate(sql5);
	        
	        String sql6 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('F', 'EURO', 'EUR', 1.05, 28, 0, 'euro.gif')";
	        st6.executeUpdate(sql6);
	        
	        String sql7 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('C', 'TETHER', 'USDT', 1, 30, 0, 'Tether.gif')";
	        st7.executeUpdate(sql7);
	        
	        String sql8 = "INSERT INTO MONEDA (TIPO, NOMBRE, NOMENCLATURA, VALOR_DOLAR, VOLATILIDAD, STOCK, NOMBRE_ICONO) VALUES ('C', 'USDC', 'USDC', 1, 10, 0, 'Usdc.gif')";
	        st8.executeUpdate(sql8);
	        
	        st.close();
	        st2.close();
	        st3.close();
	        st4.close();
	        st5.close();
	        st6.close();
	        st7.close();
	        st8.close();
	    }
	    cx.close();
	}

	private boolean monedasExisten(Connection cx) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM MONEDA";
	    try (Statement stmt = cx.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    }
	    return false;
	}
}