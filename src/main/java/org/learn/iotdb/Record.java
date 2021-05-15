package org.learn.iotdb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Record {
    private String storageGroup;
    private String deviceId;
    private Timestamp timestamp;
    private List<Column> columnList = new ArrayList<>();

    public static class Column {
        String columnName;
        String columnType;
        Object columnValue;

        public Column(String columnName, String columnType, Object columnValue) {
            this.columnName = columnName;
            this.columnType = columnType;
            this.columnValue = columnValue;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }
    }

    public void addColumn(Column e) {
        this.columnList.add(e);
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public String getStorageGroup() {
        return storageGroup;
    }

    public void setStorageGroup(String storageGroup) {
        this.storageGroup = storageGroup;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
