package fr.ybonnel.codestory.database;


import com.google.common.base.Throwables;
import fr.ybonnel.codestory.database.modele.Enonce;
import fr.ybonnel.codestory.database.modele.LogMessage;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum DatabaseManager {

    INSTANCE;

    public static final String TYPE_NEW = "N";
    public static final String TYPE_Q = "Q";

    private JdbcDataSource ds;

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private DatabaseManager() {
        try {
            Class.forName("org.h2.Driver");

            boolean databaseExists = false;

            try {
                String url = DatabaseUtil.getUrl() + ";IFEXISTS=TRUE";
                Connection connection = DriverManager.getConnection(url, "sa", "sa");
                connection.close();
                databaseExists = true;
            } catch (SQLException ignore) {
            }


            ds = new JdbcDataSource();
            ds.setURL(DatabaseUtil.getUrl());
            ds.setUser("sa");
            ds.setPassword("sa");

            if (!databaseExists) {
                createDatabase();
            }
        } catch (Exception exception) {
            Throwables.propagate(exception);
        }
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

    private LogDao logDao = null;

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
