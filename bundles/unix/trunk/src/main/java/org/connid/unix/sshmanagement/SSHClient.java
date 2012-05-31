/*
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://IdentityConnectors.dev.java.net/legal/license.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing the Covered Code, include this
 * CDDL Header Notice in each file
 * and include the License file at identityconnectors/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.unix.sshmanagement;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.connection.ChannelState;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.commands.*;
import org.connid.unix.utilities.DefaultProperties;
import org.connid.unix.utilities.Utilities;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;

public class SSHClient {

    private static final Log LOG = Log.getLog(SSHClient.class);
    private SshConnectionProperties properties = new SshConnectionProperties();
    private UnixConfiguration unixConfiguration = null;
    private String username;
    private String password;
    private SshClient sshClient = null;

    public SSHClient(final UnixConfiguration unixConfiguration) {
        this.unixConfiguration = unixConfiguration;
        properties.setHost(unixConfiguration.getHostname());
        properties.setPort(unixConfiguration.getPort());
        this.username = unixConfiguration.getAdmin();
        this.password = Utilities.getPlainPassword(
                unixConfiguration.getPassword());
        sshClient = new SshClient();
        sshClient.setSocketTimeout(DefaultProperties.SSH_SOCKET_TIMEOUT);
    }

    public final SessionChannelClient getSession() throws IOException {
        sshClient.connect(properties, new IgnoreHostKeyVerification());
        sshClient.authenticate(getPwdAuthClient(username, password));
        return sshClient.openSessionChannel();
    }

    public final boolean userExists(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        String output = "";
        SessionChannelClient session = getSession();
        if (session.executeCommand(
                GeneralCommands.getUserExistsCommand(username))) {
            output = getOutput(session);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during password encrypt");
        }
        sshClient.disconnect();
        return !output.isEmpty();
    }

    public boolean groupExists(String groupname)
            throws IOException, InvalidStateException, InterruptedException {
        String output = "";
        SessionChannelClient session = getSession();
        if (session.executeCommand(
                GeneralCommands.getGroupExistsCommand(groupname))) {
            output = getOutput(session);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during password encrypt");
        }
        sshClient.disconnect();
        return !output.isEmpty();
    }

    public final void createUser(final String uidstring, final String password,
            final String comment, final boolean status)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        if (session.executeCommand(
                createUserAddCommand(uidstring, password, comment, status))) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during useradd operation");
        }
        sshClient.disconnect();
    }

    private String createUserAddCommand(final String uidstring,
            final String password, final String comment, final boolean status) {
        UserAddCommand userAddCommand = new UserAddCommand(
                unixConfiguration, uidstring, password, comment, status);
        PasswdCommand passwdCommand = new PasswdCommand(uidstring, password);
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            SudoCommand sudoCommand =
                    new SudoCommand(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(userAddCommand.useradd()).append("; ").append(
                passwdCommand.passwd());
        return commandToExecute.toString();
    }

    public void createGroup(String groupName)
            throws IOException, InvalidStateException, InterruptedException {

        SessionChannelClient session = getSession();
        GroupAddCommand groupAddCommand = new GroupAddCommand(groupName);
        if (session.executeCommand(groupAddCommand.groupadd())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during groupadd operation");
        }
        sshClient.disconnect();
    }

    public final void updateUser(final String actualUsername,
            final String username, final String password)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();;
        if (session.executeCommand(
                createModCommand(actualUsername, username, password))) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during usermod operation");
        }
        sshClient.disconnect();
    }

    private String createModCommand(final String actualUsername,
            final String username, final String password) {
        UserModCommand userModCommand =
                new UserModCommand(actualUsername, username);
        StringBuilder commandToExecute = new StringBuilder();
        commandToExecute.append(userModCommand);
        if ((StringUtil.isNotBlank(password))
                && (StringUtil.isNotEmpty(password))) {
            PasswdCommand passwdCommand = new PasswdCommand(username, password);
            commandToExecute.append("; ").append(passwdCommand.passwd());
        }
        return commandToExecute.toString();
    }

    public final void deleteUser(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        UserDelCommand userDelCommand =
                new UserDelCommand(unixConfiguration, username);
        if (session.executeCommand(userDelCommand.userdel())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during deleted operation");
        }
        sshClient.disconnect();
    }

    public void deleteGroup(String groupName)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        GroupDelCommand groupDelCommand = new GroupDelCommand(groupName);
        if (session.executeCommand(groupDelCommand.groupDel())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during deleted operation");
        }
        sshClient.disconnect();
    }

    public final void authenticate(final String username, final String password)
            throws UnknownHostException, IOException {
        sshClient.connect(properties, new IgnoreHostKeyVerification());
        int status =
                sshClient.authenticate(getPwdAuthClient(username, password));
        if (status != AuthenticationProtocolState.COMPLETE) {
            throw new IOException();
        }
        sshClient.disconnect();
    }

    private String getOutput(final SessionChannelClient session)
            throws IOException {
        String line = "";
        BufferedReader br = new BufferedReader(
                new InputStreamReader(session.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    private PasswordAuthenticationClient getPwdAuthClient(
            final String username, final String password) {
        PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
        pwd.setUsername(username);
        pwd.setPassword(password);
        return pwd;
    }
}
