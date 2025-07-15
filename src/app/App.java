package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import DAO.*;
import GUI.*;
import conexionn.*;
import controlador.*;

public class App{
	public static void main(String [] args) throws SQLException, ClassNotFoundException {
		DataSource dataSource = MiDataSource.getDataSource();
		creaciónDeTablasEnBD(dataSource.getConnection());
		VistaLogin vistaL = new VistaLogin();
		VistaRegistrar vistaR = new VistaRegistrar();
		VistaBalance vistaB = new VistaBalance();
		VistaCotizacion vistaC = new VistaCotizacion();
		VistaCompra vistaComp = new VistaCompra();
		VistaHistorial vistaH = new VistaHistorial();
		VistaSwap vistaS = new VistaSwap();
		ActivoDAO activoDAO = new ActivoDAOjdbc();
		CompraDAO compraDAO = new CompraDAOjdbc();
		MonedaDAO monedaDAO = new MonedaDAOjdbc();
		SwapDAO swapDAO = new SwapDAOjdbc();
		UsuarioDAO usuarioDAO = new UsuarioDAOjdbc();
		new Controlador(vistaL, vistaR, vistaB, vistaC, vistaComp, vistaH, vistaS, activoDAO, compraDAO, monedaDAO, swapDAO, usuarioDAO);
		vistaL.setVisible(true);
	}

	/**
	* Este método se encarga de la creación de las tablas.
	*
	* @param connection objeto conexion a la base de datos SQLite
	* @throws SQLException
	*/
	private static void creaciónDeTablasEnBD(Connection connection) throws SQLException {
	    Statement stmt;
	    stmt = connection.createStatement();

	    // Crear tabla PERSONA
	    String sql = "CREATE TABLE IF NOT EXISTS PERSONA "
	            + "("
	            + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
	            + " NOMBRES VARCHAR(50) NOT NULL, "
	            + " APELLIDOS VARCHAR(50) NOT NULL "
	            + ")";
	    stmt.executeUpdate(sql);

	    // Crear tabla USUARIO
	    sql = "CREATE TABLE IF NOT EXISTS USUARIO "
	            + "("
	            + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
	            + " ID_PERSONA INTEGER NOT NULL, "
	            + " EMAIL VARCHAR(50) NOT NULL, "
	            + " PASSWORD VARCHAR(50) NOT NULL, "
	            + " ACEPTA_TERMINOS BOOLEAN NOT NULL, "
	            + " FOREIGN KEY(ID_PERSONA) REFERENCES PERSONA(ID) "
	            + ")";
	    stmt.executeUpdate(sql);

	    // Crear tabla MONEDA
	    sql = "CREATE TABLE IF NOT EXISTS MONEDA "
	            + "("
	            + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
	            + " TIPO VARCHAR(1) NOT NULL, "
	            + " NOMBRE VARCHAR(50) NOT NULL, "
	            + " NOMENCLATURA VARCHAR(10) NOT NULL, "
	            + " VALOR_DOLAR REAL NOT NULL, "
	            + " VOLATILIDAD REAL NULL, "
	            + " STOCK REAL NULL, "
	            + " NOMBRE_ICONO VARCHAR(50) NOT NULL "
	            + ")";
	    stmt.executeUpdate(sql);

	    // Crear tabla ACTIVO_CRIPTO con restricción UNIQUE
	    sql = "CREATE TABLE IF NOT EXISTS ACTIVO_CRIPTO "
	            + "("
	            + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
	            + " ID_USUARIO INTEGER NOT NULL, "
	            + " ID_MONEDA INTEGER NOT NULL, "
	            + " CANTIDAD REAL NOT NULL, "
	            + " FOREIGN KEY(ID_USUARIO) REFERENCES USUARIO(ID),"
	            + " FOREIGN KEY(ID_MONEDA) REFERENCES MONEDA(ID), "
	            + " UNIQUE(ID_USUARIO, ID_MONEDA) "
	            + ")";
	    stmt.executeUpdate(sql);

	    // Crear tabla ACTIVO_FIAT con restricción UNIQUE
	    sql = "CREATE TABLE IF NOT EXISTS ACTIVO_FIAT "
	            + "("
	            + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
	            + " ID_USUARIO INTEGER NOT NULL, "
	            + " ID_MONEDA INTEGER NOT NULL, "
	            + " CANTIDAD REAL NOT NULL, "
	            + " FOREIGN KEY(ID_USUARIO) REFERENCES USUARIO(ID), "
	            + " FOREIGN KEY(ID_MONEDA) REFERENCES MONEDA(ID), "
	            + " UNIQUE(ID_USUARIO, ID_MONEDA) "
	            + ")";
	    stmt.executeUpdate(sql);

	    // Crear tabla TRANSACCION
	    sql = "CREATE TABLE IF NOT EXISTS TRANSACCION "
	            + "("
	            + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
	            + " RESUMEN VARCHAR(1000) NOT NULL, "
	            + " FECHA_HORA DATETIME NOT NULL, "
	            + " ID_USUARIO INTEGER NOT NULL, "
	            + " FOREIGN KEY(ID_USUARIO) REFERENCES USUARIO(ID) "
	            + ")";
	    stmt.executeUpdate(sql);

	    stmt.close();
	}
}
