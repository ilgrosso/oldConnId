package org.connid.unix.commands;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.connection.ChannelState;
import com.sshtools.j2ssh.io.IOStreamConnector;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.identityconnectors.common.logging.Log;

public class SSHClient {

    private static final Log LOG = Log.getLog(SSHClient.class);
    private String username;
    private String password;
    private String host;
    private int port;
    private SshClient sshClient = null;

    public SSHClient(String host, int port, String userName, String password) {
        this.host = host;
        this.port = port;
        this.username = userName;
        this.password = password;
        sshClient = new SshClient();
    }

    public final int authenticate() throws IOException {
        sshClient.connect(host, new IgnoreHostKeyVerification());
        PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
        pwd.setUsername(username);
        pwd.setPassword(password);
        return sshClient.authenticate(pwd);
    }

    public final void create(final String uidstring, final String password)
            throws IOException, InvalidStateException, InterruptedException {
        authenticate();
        SshConnectionProperties properties = null;
        SessionChannelClient session = null;
        String encryptPassword = "";
        IOStreamConnector output = new IOStreamConnector();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        session = sshClient.openSessionChannel();
        if (session.executeCommand(
                Commands.getEncryptPasswordCommand(password))) {
            output.connect(session.getInputStream(), bos);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
            encryptPassword = bos.toString();
        } else {
            LOG.error("Error during password encrypt");
        }
        session = sshClient.openSessionChannel();
        if (session.executeCommand(Commands.getUserAddCommand(
                encryptPassword, uidstring))) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during useradd operation");
        }

    }
}
