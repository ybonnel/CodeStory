package fr.ybonnel.codestory.database;

import com.google.common.base.Throwables;
import fr.ybonnel.codestory.database.modele.LogMessage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogDao extends AbstractDao<LogMessage> {

    public LogDao(DataSource ds) {
        super(ds);
    }

    @Override
    public List<LogMessage> findAll() {
        List<LogMessage> logMessages = new ArrayList<LogMessage>();

        try {
            Connection conn = getConnection();

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT HEURE, TYPE_LOG, MESSAGE FROM LOG ORDER BY HEURE DESC LIMIT 5");
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


    public List<LogMessage> findByType(String type) {
        List<LogMessage> logMessages = new ArrayList<LogMessage>();

        try {
            Connection conn = getConnection();

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

    @Override
    public void insert(LogMessage objectToInsert) {
        try {
            Connection conn = getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO LOG (HEURE, TYPE_LOG, MESSAGE) VALUES (?, ?, ?)");
            preparedStatement.setTimestamp(1, new Timestamp(objectToInsert.getDate().getTime()));
            preparedStatement.setString(2, objectToInsert.getType());
            preparedStatement.setString(3, objectToInsert.getMessage());

            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException sqlException) {
            Throwables.propagate(sqlException);
        }
    }
}
