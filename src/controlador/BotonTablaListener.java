package controlador;

import java.sql.SQLException;

public interface BotonTablaListener {
	void botonPresionado(int fila, int columna, String accion, String precio, String[] precios) throws SQLException;
}
