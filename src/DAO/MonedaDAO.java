package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JTable;

import excepciones.MonedaException;
import modelo.Moneda;

public interface MonedaDAO {
	public double balance(String email) throws SQLException;
	//public boolean tablaBDVacia () throws SQLException, MonedaException;
	public void generarMonedas() throws SQLException;
	//public List<Moneda> obtenerDatosTabla() throws SQLException;
}
