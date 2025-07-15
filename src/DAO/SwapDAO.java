package DAO;

import java.sql.SQLException;

public interface SwapDAO extends Transacciones {
	public double simularSwap(String criptoDestino, String CriptoOrigen, double cantidadOrigen, String email, double precioDestino, double precioOrigen ) throws SQLException;
	public double equivalencias (String cripto, String fiat, double cantidadCriptoOrigen, double precio, double precioOrigen) throws SQLException;
}
