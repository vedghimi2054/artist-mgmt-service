package com.company.artistmgmt.config;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Component("resultSet")
public class DBResultSet {
    public <E> List<E> getResults(ResultSet resultSet, Function<ResultSet, E> action) throws SQLException {
        if (null != resultSet && null != action) {
            List<E> results = Lists.newArrayList();

            try {
                while (resultSet.next()) {
                    results.add(action.apply(resultSet));
                }
            } finally {
                this.close(resultSet);
            }

            return results;
        } else {
            return Collections.emptyList();
        }
    }

    public <E> E getResult(ResultSet resultSet, Function<ResultSet, E> action) throws SQLException {
        if (resultSet != null && action != null) {
            try {
                if (resultSet.next()) {
                    return action.apply(resultSet);
                }
            } finally {
                this.close(resultSet);
            }
        }
        return null;
    }

    protected void close(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException var3) {
            }
        }

    }

    public long count(ResultSet resultSet) throws SQLException {
        if (null == resultSet) {
            return -1L;
        } else {
            try {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            } finally {
                this.close(resultSet);
            }

            return 0L;
        }
    }

    public void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
            }
        }
    }

    public void commit(Connection connection) {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
            }
        }
    }
}
