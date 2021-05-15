package org.learn.iotdb;

import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.Session;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.write.record.Tablet;
import org.apache.iotdb.tsfile.write.schema.MeasurementSchema;

import java.util.ArrayList;
import java.util.List;

public class IOTDBWriter extends JDBC {
    // Database credentials
    static String username = "root";
    static String password = "root";
    //static String host = "47.94.217.80";
    static String host = "119.3.142.120";
    static int port = 6667;

    // JDBC driver name and database URL
    static String driver = "org.apache.iotdb.jdbc.IoTDBDriver";
    static String url = String.format("jdbc:iotdb://%s:%d/", host, port);


    public Session openSession() throws IoTDBConnectionException {
        Session session = new Session(host, port, username, password);
        session.open(false);
        return session;
    }

    public void insertRecord(Session session, String deviceId, Long timestamp, List<String> measurements, List<TSDataType> types, List<Object> values) throws IoTDBConnectionException, StatementExecutionException {
// 调用一次即可
//        try {
//            session.setStorageGroup(storageGroup);
//        } catch (StatementExecutionException e) {
//            if (e.getStatusCode() != TSStatusCode.PATH_ALREADY_EXIST_ERROR.getStatusCode()) {
//                throw e;
//            }
//        }
        session.insertRecord(deviceId, timestamp, measurements, types, values);
    }

    public void insertTablet(Session session, List<Record> recordList) throws StatementExecutionException, IoTDBConnectionException {
        List<MeasurementSchema> schemaList = new ArrayList<>();
        Record firstRecord=recordList.get(0);
        String deviceId=firstRecord.getDeviceId();
        for (Record.Column column : firstRecord.getColumnList()) {
            schemaList.add(new MeasurementSchema(column.columnName, TSDataType.FLOAT));
        }
        Tablet tablet = new Tablet(deviceId, schemaList, recordList.size());
        for (Record record : recordList) {
            int rowIndex = tablet.rowSize++;
            tablet.addTimestamp(rowIndex, record.getTimestamp().getTime());
            for (Record.Column column : record.getColumnList()) {
                //tablet.addValue(column.columnName, rowIndex, ((BigDecimal) column.columnValue).floatValue());
                tablet.addValue(column.columnName, rowIndex, column.columnValue);
            }
        }
        session.insertTablet(tablet, true);
        tablet.reset();
    }
}
