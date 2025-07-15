package conexionn;

import java.sql.*;
import app.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;

public class MiDataSource {
	private static DataSource dataSource = null;
		static {
			SQLiteDataSource sqliteDataSource = new SQLiteDataSource();
			sqliteDataSource.setUrl("jdbc:sqlite:BaseDatoApp.db");
			dataSource = sqliteDataSource;
		}
		public static DataSource getDataSource(){
			return dataSource;
		}
		private MiDataSource(){}
}