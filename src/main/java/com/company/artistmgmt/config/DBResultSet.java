package com.company.artistmgmt.config;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

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

    protected void close(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException var3) {
            }
        }

    }
}
