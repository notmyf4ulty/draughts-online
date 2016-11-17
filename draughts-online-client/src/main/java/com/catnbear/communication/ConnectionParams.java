package com.catnbear.communication;

/**
 * Created by przemek on 17.11.16.
 */
public class ConnectionParams {
    private String hostName;
    private int portNumber;

    private static ConnectionParams instance = null;

    private ConnectionParams() {}

    public static ConnectionParams getInstance() {
        if (instance == null) {
            instance = new ConnectionParams();
        }
        return instance;
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
