package DAO;

import javax.sql.DataSource;
import javax.swing.*;

import modelo.Persona;

import java.awt.*;
import java.sql.*;
import conexionn.*;
public class UsuarioDAOjdbc implements UsuarioDAO{
	 public boolean checkLogin (String email, String password) throws SQLException {
		DataSource dataSource = MiDataSource.getDataSource();	
		Connection cx = dataSource.getConnection();
		boolean retorno = true;
		String sql = "SELECT ID_PERSONA FROM USUARIO WHERE EMAIL = ? AND PASSWORD = ?";
		PreparedStatement pstmt = cx.prepareStatement(sql);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
		if (!rs.next()) {
			retorno = false;
		}
		rs.close();
		pstmt.close();
		cx.close();
		return retorno;
	 }
	 
		public boolean registrar(Persona persona, String email, String password, boolean TC) throws SQLException {
			boolean registro = false;
			DataSource dataSource = MiDataSource.getDataSource();	
			Connection cx = dataSource.getConnection();
			String sql = "SELECT ID_PERSONA FROM USUARIO WHERE EMAIL = ?";
			PreparedStatement pstmt = cx.prepareStatement(sql);
	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) { // no encontro un usuario con ese email
				sql = "INSERT INTO PERSONA (NOMBRES, APELLIDOS) "+
					   "VALUES ('"+persona.getNombre()+"','"+persona.getApellido()+"')";
				Statement st = cx.createStatement();
	            st.executeUpdate(sql);
	            sql = "SELECT ID FROM PERSONA WHERE NOMBRES = ? AND APELLIDOS = ?";
	            pstmt = cx.prepareStatement(sql);
	            pstmt.setString(1, persona.getNombre());
	            pstmt.setString(2, persona.getApellido());
	            rs = pstmt.executeQuery();
	            int ID = -1;
	            if (rs.next()) {
	            	ID = rs.getInt("ID");
	                sql = "INSERT INTO USUARIO (ID_PERSONA, EMAIL, PASSWORD, ACEPTA_TERMINOS)" +
	                  	  "VALUES ('"+ ID +"', '"+email+"', '"+password+"', '"+TC+"')";
	                Statement st2 = cx.createStatement();
	                st2.executeUpdate(sql);
	                st2.close();
	                registro = true;
	            }
	            st.close();
			}
	        rs.close();
	        pstmt.close();
			return registro;
		}
		
		public String obtenerNombre (String email) throws SQLException {
		    String nombre = null;
			DataSource dataSource = MiDataSource.getDataSource();
		    String sql = "SELECT ID_PERSONA FROM USUARIO WHERE EMAIL = ?";
		    try ( Connection cx = dataSource.getConnection();
		         PreparedStatement pstmt = cx.prepareStatement(sql) ) {

		        pstmt.setString(1, email);
		        try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		                int idPersona = rs.getInt("ID_PERSONA");

		                String sql2 = "SELECT NOMBRES FROM PERSONA WHERE ID = ?";
		                try (PreparedStatement pstmt2 = cx.prepareStatement(sql2)) {
		                    pstmt2.setInt(1, idPersona);

		                    try (ResultSet rs2 = pstmt2.executeQuery()) {
		                        if (rs2.next()) {
		                            nombre = rs2.getString("NOMBRES");
		                        }
		                    }
		                }
		            }
		        }
		    }
		    return nombre;
		}
		
		public String obtenerApellido (String email) throws SQLException {
		    String apellido = null;
			DataSource dataSource = MiDataSource.getDataSource();
		    String sql = "SELECT ID_PERSONA FROM USUARIO WHERE EMAIL = ?";
		    try ( Connection cx = dataSource.getConnection();
		         PreparedStatement pstmt = cx.prepareStatement(sql) ) {

		        pstmt.setString(1, email);
		        try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		                int idPersona = rs.getInt("ID_PERSONA");

		                String sql2 = "SELECT APELLIDOS FROM PERSONA WHERE ID = ?";
		                try (PreparedStatement pstmt2 = cx.prepareStatement(sql2)) {
		                    pstmt2.setInt(1, idPersona);

		                    try (ResultSet rs2 = pstmt2.executeQuery()) {
		                        if (rs2.next()) {
		                        	apellido = rs2.getString("APELLIDOS");
		                        }
		                    }
		                }
		            }
		        }
		    }
		    return apellido;
		}
}
