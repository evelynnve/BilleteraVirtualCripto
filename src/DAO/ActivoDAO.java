package DAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import excepciones.MonedaException;
import modelo.Moneda;

public interface ActivoDAO {
	public Map<String, Double> obtenerPreciosActuales();
	public void cargarActivos (String email) throws SQLException;
	public boolean[] checkActivos(String email) throws SQLException;
	public String[] misOperaciones (String email) throws SQLException;
	public List<Moneda> obtenerDatosTabla(String email) throws SQLException;
	public boolean tablaBDVacia (String email) throws SQLException, MonedaException;
}
