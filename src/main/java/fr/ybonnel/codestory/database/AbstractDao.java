package fr.ybonnel.codestory.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<T> {

    private DataSource ds;

    protected AbstractDao(DataSource ds) {
        this.ds = ds;
    }

    protected Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public abstract List<T> findAll();

    public abstract void insert(T objectToInsert);
}
