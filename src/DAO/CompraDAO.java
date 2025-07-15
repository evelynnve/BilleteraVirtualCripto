package DAO;

import java.sql.SQLException;

public interface CompraDAO extends Transacciones{
	public double simularCompra(String cripto, String fiat, double cantidadFiat, String email, double precio) throws SQLException;
	public double equivalencias (String cripto, String fiat, double cantidadFiat, double precio) throws SQLException;
}
