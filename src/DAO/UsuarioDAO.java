package DAO;

import java.sql.SQLException;

import modelo.Persona;

public interface UsuarioDAO {
	public boolean checkLogin (String email, String password) throws SQLException;
	public String obtenerNombre (String email) throws SQLException;
	public String obtenerApellido(String email) throws SQLException;
	public boolean registrar(Persona persona, String email, String password, boolean TC) throws SQLException;
}
