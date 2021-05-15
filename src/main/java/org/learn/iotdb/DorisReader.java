package org.learn.iotdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DorisReader extends JDBC {
    // JDBC driver name and database URL
    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://host:9030/gshjlfx_all_dev";

    // Database credentials
    static String username = "root";
    static String password = "root";

    public List<Record> getRecords(String query) throws SQLException {
        Connection connection = getConnection(driver, url, username, password);
        Statement statement = connection.createStatement();
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();

        List<Record> recordList = new ArrayList<>();
        while (resultSet.next()) {
            Record record = new Record();
            String storageGroup="root.org_"+resultSet.getString(1);
            String deviceId=String.format("%s.device_%s",storageGroup,resultSet.getString(2));
            record.setStorageGroup(storageGroup);
            record.setDeviceId(deviceId);
            record.setTimestamp(resultSet.getTimestamp(3));
            for (int i = 4; i <= columnCount; i++) {
                Record.Column column = new Record.Column(metaData.getColumnName(i), metaData.getColumnTypeName(i), resultSet.getObject(i));
                record.addColumn(column);
            }
            recordList.add(record);
        }
        return recordList;
    }
}
