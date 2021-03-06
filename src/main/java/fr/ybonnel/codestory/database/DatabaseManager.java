package fr.ybonnel.codestory.database;


import com.google.common.base.Throwables;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public enum DatabaseManager {

    INSTANCE;

    public static final String TYPE_Q = "Q";
    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_USER = "sa";

    private JdbcDataSource ds;

    DatabaseManager() {
        try {
            Class.forName(DB_DRIVER);

            boolean databaseExists = doesDatabaseExists();

            ds = new JdbcDataSource();
            ds.setURL(DatabaseUtil.getUrl());
            ds.setUser(DB_USER);
            ds.setPassword(DB_USER);

            if (!databaseExists) {
                createDatabase();
            }
        } catch (Exception exception) {
            Throwables.propagate(exception);
        }
    }

    private static boolean doesDatabaseExists() {
        boolean databaseExists = false;
        try {
            String url = DatabaseUtil.getUrl() + ";IFEXISTS=TRUE";
            Connection connection = DriverManager.getConnection(url, DB_USER, DB_USER);
            connection.close();
            databaseExists = true;
        } catch (SQLException ignore) {
        }
        return databaseExists;
    }

    public void createDatabase() throws SQLException {
        Connection conn = ds.getConnection();

        Statement statementDrop = conn.createStatement();
        statementDrop.executeUpdate("DROP TABLE IF EXISTS LOG");

        Statement statement = conn.createStatement();
        statement.executeUpdate("CREATE TABLE LOG (" +
                "HEURE TIMESTAMP," +
                "TYPE_LOG VARCHAR(10)," +
                "MESSAGE VARCHAR(500))");

        statementDrop = conn.createStatement();
        statementDrop.executeUpdate("DROP TABLE IF EXISTS ENONCE");

        statement = conn.createStatement();
        statement.executeUpdate("CREATE TABLE ENONCE (" +
                "ID INTEGER," +
                "TITLE VARCHAR(100)," +
                "ENONCE VARCHAR(4000))");

        conn.close();
    }

    private LogDao logDao;

    public LogDao getLogDao() {
        if (logDao == null) {
            logDao = new LogDao(ds);
        }
        return logDao;
    }

    private EnonceDao enonceDao;

    public EnonceDao getEnonceDao() {
        if (enonceDao == null) {
            enonceDao = new EnonceDao(ds);
        }
        return enonceDao;
    }
}
