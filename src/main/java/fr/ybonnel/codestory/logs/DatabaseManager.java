package fr.ybonnel.codestory.logs;


import com.google.common.base.Throwables;
import fr.ybonnel.codestory.WebServer;
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

        conn.close();
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void insertLog(String type, String message) {
        try {
            Connection conn = ds.getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO LOG (HEURE, TYPE_LOG, MESSAGE) VALUES (?, ?, ?)");
            preparedStatement.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
            preparedStatement.setString(2, type);
            if (message.length() > 450) {
                message = message.substring(0, 450);
            }
            preparedStatement.setString(3, message);

            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException sqlException) {
            Throwables.propagate(sqlException);
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public List<LogMessage> getLogs() {
        List<LogMessage> logMessages = new ArrayList<LogMessage>();

        try {
            Connection conn = ds.getConnection();

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT HEURE, TYPE_LOG, MESSAGE FROM LOG ORDER BY HEURE DESC");
            while (resultSet.next()) {
                logMessages.add(new LogMessage(new Date(resultSet.getTimestamp("HEURE").getTime()),
                        resultSet.getString("TYPE_LOG"),
                        resultSet.getString("MESSAGE")));
            }

            conn.close();
        } catch (SQLException sqlException) {
            Throwables.propagate(sqlException);
        }

        return logMessages;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public List<LogMessage> getLogsByType(String type) {
        List<LogMessage> logMessages = new ArrayList<LogMessage>();

        try {
            Connection conn = ds.getConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT HEURE, TYPE_LOG, MESSAGE FROM LOG WHERE TYPE_LOG = ? ORDER BY HEURE DESC");
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                logMessages.add(new LogMessage(new Date(resultSet.getTimestamp("HEURE").getTime()),
                        resultSet.getString("TYPE_LOG"),
                        resultSet.getString("MESSAGE")));
            }

            conn.close();
        } catch (SQLException sqlException) {
            Throwables.propagate(sqlException);
        }

        return logMessages;
    }


    @SuppressWarnings("UnusedDeclaration")
    public static class LogMessage {
        private java.util.Date date;
        private String type;
        private String message;

        public LogMessage(java.util.Date date, String type, String message) {
            this.date = date;
            this.type = type;
            this.message = message;
        }

        public Date getDate() {
            return date;
        }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }

}
