package org.connid.unix.commands;

public class Commands {

    public static String getEncryptPasswordCommand(final String password) {
        return "(perl -e 'print crypt($ARGV[0],"
                + "\"password\")' " + password + ")";
    }

    public static String getUserAddCommand(final String encryptPassword,
            final String uidstring) {
        return "useradd -m -p " + encryptPassword + " " + uidstring;
    }
}
