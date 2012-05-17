package org.connid.unix.utilities;

import java.util.ResourceBundle;

class UnixProperties {

    public static String UNIX_ADMIN;
    public static String UNIX_PASSWORD;
    public static String UNIX_HOSTNAME;
    public static String UNIX_PORT;

    static {
        ResourceBundle unixProperties = ResourceBundle.getBundle("unix");
        UNIX_ADMIN = unixProperties.getString("unix.admin");
        UNIX_PASSWORD = unixProperties.getString("unix.password");
        UNIX_HOSTNAME = unixProperties.getString("unix.hostname");
        UNIX_PORT = unixProperties.getString("unix.port");
    }
}
