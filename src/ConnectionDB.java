
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB 
{
	public Connection getConnection() throws SQLException
	{
		String hostName = "DESKTOP-RG9C7SL\\SQLEXPRESS";
        String url = "jdbc:sqlserver://DESKTOP-RG9C7SL\\SQLEXPRESS;databaseName=senior;integratedSecurity=true;"; 
        Connection connection = null;
        
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

        try {
                connection = DriverManager.getConnection(url);
        }
        catch (Exception e) {
                e.printStackTrace();
        }
        
		return connection;
	}
}
