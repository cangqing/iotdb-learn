package org.learn.iotdb;

import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.session.Session;

public class IOTDBReader extends JDBC {
    // Database credentials
    static String username = "root";
    static String password = "root";
    static String host = "host";
    static int port = 6667;

    // JDBC driver name and database URL
    static String driver = "org.apache.iotdb.jdbc.IoTDBDriver";
    static String url = String.format("jdbc:iotdb://%s:%d/", host, port);


    public Session openSession() throws IoTDBConnectionException {
        Session session = new Session(host, port, username, password);
        session.open(false);
        return session;
    }
}
