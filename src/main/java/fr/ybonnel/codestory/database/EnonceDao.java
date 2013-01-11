package fr.ybonnel.codestory.database;


import com.google.common.base.Throwables;
import fr.ybonnel.codestory.database.modele.Enonce;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnonceDao extends AbstractDao<Enonce> {

    public EnonceDao(DataSource ds) {
        super(ds);
    }

    @Override
    public List<Enonce> findAll() {
        List<Enonce> enonces = new ArrayList<Enonce>();

        try {
            Connection conn = getConnection();

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ID, TITLE, ENONCE FROM ENONCE ORDER BY ID DESC");
            while (resultSet.next()) {
                enonces.add(new Enonce(resultSet.getInt("ID"),
                        resultSet.getString("TITLE"),
                        resultSet.getString("ENONCE")));
            }

            conn.close();
        } catch (SQLException sqlException) {
            Throwables.propagate(sqlException);
        }

        return enonces;
    }

    @Override
    public void insert(Enonce enonce) {
        try {

            Connection conn = getConnection();
            PreparedStatement preparedStatementSelect = conn.prepareStatement("SELECT 1 FROM ENONCE WHERE ID = ?");
            preparedStatementSelect.setInt(1, enonce.getId());
            ResultSet resultSet = preparedStatementSelect.executeQuery();
            if (resultSet.next()) {
                update(enonce, conn);
            } else {
                insert(enonce, conn);
            }
            conn.close();
        } catch (SQLException sqlException) {
            Throwables.propagate(sqlException);
        }
    }

    private void insert(Enonce enonce, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO ENONCE (ID, TITLE, ENONCE) VALUES (?, ?, ?)");
        preparedStatement.setInt(1, enonce.getId());
        preparedStatement.setString(2, enonce.getTitle());
        preparedStatement.setString(3, enonce.getContent());

        preparedStatement.executeUpdate();
    }

    private void update(Enonce enonce, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE ENONCE SET TITLE = ?, ENONCE = ? WHERE ID = ?");
        preparedStatement.setString(1, enonce.getTitle());
        preparedStatement.setString(2, enonce.getContent());
        preparedStatement.setInt(3, enonce.getId());

        preparedStatement.executeUpdate();
    }
}
