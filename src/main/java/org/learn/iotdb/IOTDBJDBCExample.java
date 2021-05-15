package org.learn.iotdb;

import java.sql.*;
import org.apache.iotdb.jdbc.IoTDBSQLException;

public class IOTDBJDBCExample extends IOTDBReader{
    /**
     * Before executing a SQL statement with a Statement object, you need to create a Statement object using the createStatement() method of the Connection object.
     * After creating a Statement object, you can use its execute() method to execute a SQL statement
     * Finally, remember to close the 'statement' and 'connection' objects by using their close() method
     * For statements with query results, we can use the getResultSet() method of the Statement object to get the result set.
     */
    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection(driver,url,username,password);
        if (connection == null) {
            System.out.println("get connection defeat");
            return;
        }
        Statement statement = connection.createStatement();
        //Create storage group
        try {
            statement.execute("SET STORAGE GROUP TO root.demo");
        }catch (IoTDBSQLException e){
            System.out.println(e.getMessage());
        }


        //Show storage group
        statement.execute("SHOW STORAGE GROUP");
        outputResult(statement.getResultSet());

        //Create time series
        //Different data type has different encoding methods. Here use INT32 as an example
        try {
            statement.execute("CREATE TIMESERIES root.demo.s0 WITH DATATYPE=INT32,ENCODING=RLE;");
        }catch (IoTDBSQLException e){
            System.out.println(e.getMessage());
        }
        //Show time series
        statement.execute("SHOW TIMESERIES root.demo");
        outputResult(statement.getResultSet());
        //Show devices
        statement.execute("SHOW DEVICES");
        outputResult(statement.getResultSet());
        //Count time series
        statement.execute("COUNT TIMESERIES root");
        outputResult(statement.getResultSet());
        //Count nodes at the given level
        statement.execute("COUNT NODES root LEVEL=3");
        outputResult(statement.getResultSet());
        //Count timeseries group by each node at the given level
        statement.execute("COUNT TIMESERIES root GROUP BY LEVEL=3");
        outputResult(statement.getResultSet());


        //Execute insert statements in batch
        statement.addBatch("insert into root.demo(timestamp,s0) values(1,1);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(1,1);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(2,15);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(2,17);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(4,12);");
        statement.executeBatch();
        statement.clearBatch();

        //Full query statement
        String sql = "select * from root.demo";
        ResultSet resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        //Exact query statement
        sql = "select s0 from root.demo where time = 4;";
        resultSet= statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        //Time range query
        sql = "select s0 from root.demo where time >= 2 and time < 5;";
        resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        //Aggregate query
        sql = "select count(s0) from root.demo;";
        resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        //Delete time series
        statement.execute("delete timeseries root.demo.s0");

        //close connection
        statement.close();
        connection.close();
    }

    /**
     * This is an example of outputting the results in the ResultSet
     */
    private static void outputResult(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            System.out.println("--------------------------");
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                System.out.print(metaData.getColumnLabel(i + 1) + " ");
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; ; i++) {
                    System.out.print(resultSet.getString(i));
                    if (i < columnCount) {
                        System.out.print(", ");
                    } else {
                        System.out.println();
                        break;
                    }
                }
            }
            System.out.println("--------------------------\n");
        }
    }
}
