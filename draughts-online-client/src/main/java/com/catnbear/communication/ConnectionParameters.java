package com.catnbear.communication;

/**
 * Class containing communication parameters.
 */
public class ConnectionParameters {
    /**
     * Constant for default host's name.
     */
    private static final String DEFAULT_HOST_NAME = "localhost";

    /**
     * Constant for default port's number.
     */
    private static final int DEFAULT_PORT_NUMBER = 10001;

    /**
     * Singleton's instance.
     */
    private static ConnectionParameters instance = null;

    /**
     * Host's name.
     */
    private String hostName;

    /**
     * Port's number.
     */
    private int portNumber;

    /**
     * Default constructor. It's private because of the Singleton design pattern's purposes.
     */
    private ConnectionParameters() {
        hostName = DEFAULT_HOST_NAME;
        portNumber = DEFAULT_PORT_NUMBER;
    }

    /**
     * Getter of the Singleton's instance.
     * @return Class' instance.
     */
    public static ConnectionParameters getInstance() {
        if (instance == null) {
            instance = new ConnectionParameters();
        }
        return instance;
    }

    /**
     * Sets connection parameters with default values.
     */
    public void setDefaultParameters() {
        hostName = DEFAULT_HOST_NAME;
        portNumber = DEFAULT_PORT_NUMBER;
    }

    /**
     * Getter of default host's name.
     * @return Default host's name.
     */
    public static String getDefaultHostName() {
        return DEFAULT_HOST_NAME;
    }

    /**
     * Getter of default port's number.
     * @return Default port's number.
     */
    public static int getDefaultPortNumber() {
        return DEFAULT_PORT_NUMBER;
    }

    /**
     * Getter of host's name.
     * @return Host's name.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Setter of host's name.
     * @param hostName New host's name.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Getter of port's number.
     * @return Default port's number.
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Setter of port's number.
     * @param portNumber New port's number.
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
