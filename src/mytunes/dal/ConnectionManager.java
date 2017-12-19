package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

// database connection
// @author Dether
public class ConnectionManager {
    private SQLServerDataSource source = new SQLServerDataSource();

    public ConnectionManager() {
        source.setDatabaseName("CS2017B_7_myTunes");
        source.setUser("CS2017B_7_java");
        source.setPassword("javajava");
        source.setPortNumber(1433);
        source.setServerName("10.176.111.31");
    }
    

    // returns error
    public Connection getConnection() throws SQLServerException{
        return source.getConnection();
    }
}
