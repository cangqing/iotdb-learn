package org.learn.iotdb;

import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.Session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MockData2IOTDBExample {

    //比如20w设备，每个设备20个测度，每个设备2500个记录点
    static List<Record> mockData(int deviceIndex, int mockRecordSize, int series) {
        List<Record> recordList = new ArrayList<>();
        long timestamp = deviceIndex*mockRecordSize;
        for (int r = 0; r < mockRecordSize; r++) {
            Record record = new Record();
            String storageGroup = "root.sg_1";
            String deviceId = String.format("%s.device_%d", storageGroup, deviceIndex);
            record.setStorageGroup(storageGroup);
            record.setDeviceId(deviceId);
            record.setTimestamp(new Timestamp(timestamp + r));
            for (int seriesIndex = 0; seriesIndex < series; seriesIndex++) {
                Record.Column column = new Record.Column(String.format("s_%d", seriesIndex), "Float", seriesIndex * 1.0f);
                record.addColumn(column);
            }
            recordList.add(record);
        }
        return recordList;
    }

    public static void main(String[] args) throws IoTDBConnectionException, StatementExecutionException {

        IOTDBWriter iotdbWriter = new IOTDBWriter();
        Session session = iotdbWriter.openSession();
        long start =System.currentTimeMillis();
        for (int deviceIndex = 0; deviceIndex < 1000; deviceIndex++) {
            List<Record> recordList = mockData(deviceIndex,10000,20);
            iotdbWriter.insertTablet(session,recordList);
            System.out.println(deviceIndex);
        }
        session.close();
        long end =System.currentTimeMillis();
        System.out.println(end-start);
    }
}
