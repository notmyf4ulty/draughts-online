package com.catnbear.communication;

/**
 * Created by przemek on 17.11.16.
 */
public class ConnectionParams {
    private static final String DEFAULT_HOST_NAME = "localhost";
    private static final int DEFAULT_PORT_NUMBER = 10001;
    private String hostName;
    private int portNumber;

    private static ConnectionParams instance = null;

    private ConnectionParams() {
        hostName = DEFAULT_HOST_NAME;
        portNumber = DEFAULT_PORT_NUMBER;
    }

    public static ConnectionParams getInstance() {
        if (instance == null) {
            instance = new ConnectionParams();
        }
        return instance;
    }

    public void setDefaultParameters() {
        hostName = DEFAULT_HOST_NAME;
        portNumber = DEFAULT_PORT_NUMBER;
    }

    public static String getDefaultHostName() {
        return DEFAULT_HOST_NAME;
    }

    public static int getDefaultPortNumber() {
        return DEFAULT_PORT_NUMBER;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
