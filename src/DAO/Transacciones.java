package DAO;

import java.sql.SQLException;

public interface Transacciones<T>{
	public String devolverStock (String nomenclatura) throws SQLException;
}
