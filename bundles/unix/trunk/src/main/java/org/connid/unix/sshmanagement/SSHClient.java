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

    public final String userExists(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        String output = "";
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(
                General.searchUserIntoPasswdFile(username));
        if (session.executeCommand(commandToExecute.toString())) {
            output = getOutput(session);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during password encrypt");
        }
        sshClient.disconnect();
        return output;
    }

    public final String searchUser(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        String output = "";
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(
                General.searchUserIntoPasswdFile(username));
        if (session.executeCommand(commandToExecute.toString())) {
            output = getOutput(session);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during password encrypt");
        }
        sshClient.disconnect();
        return output;
    }

    public String groupExists(final String groupname)
            throws IOException, InvalidStateException, InterruptedException {
        String output = "";
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(
                General.searchGroupIntoGroupFile(groupname));
        if (session.executeCommand(commandToExecute.toString())) {
            output = getOutput(session);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during password encrypt");
        }
        sshClient.disconnect();
        return output;
    }

    public String userStatus(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        String output = "";
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(
                General.searchUserStatusIntoShadowFile(username));
        if (session.executeCommand(commandToExecute.toString())) {
            output = getOutput(session);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during password encrypt");
        }
        sshClient.disconnect();
        return output;
    }

    public final void createUser(final String username, final String password,
            final String comment, final String shell,
            final String homeDirectory, final boolean status)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(
                createUserAddCommand(username, password, comment, shell,
                homeDirectory));
        if (session.executeCommand(commandToExecute.toString())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during useradd operation");
        }
        if (!status) {
            lockUser(username);
        }
        sshClient.disconnect();
    }

    private String createUserAddCommand(final String username,
            final String password, final String comment, final String shell,
            final String homeDirectory) {
        UserAdd userAddCommand = new UserAdd(
                unixConfiguration, username, password, comment, shell,
                homeDirectory);
        Passwd passwdCommand = new Passwd();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(userAddCommand.useradd()).append("; ").append(
                passwdCommand.setPassword(username, password));
        return commandToExecute.toString();
    }

    private void lockUser(final String username)
            throws InterruptedException, InvalidStateException, IOException {
        SessionChannelClient session = getSession();
        UserMod userModCommand = new UserMod();
        if (session.executeCommand(userModCommand.lockUser(username))) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during lock user");
        }
    }

    public void createGroup(final String groupName)
            throws IOException, InvalidStateException, InterruptedException {

        SessionChannelClient session = getSession();
        GroupAdd groupAddCommand = new GroupAdd(groupName);
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(groupAddCommand.groupadd());
        if (session.executeCommand(commandToExecute.toString())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during groupadd operation");
        }
        sshClient.disconnect();
    }

    public final void updateUser(final String actualUsername,
            final String newUserName, final String password,
            final boolean status, final String comment, final String shell,
            final String homeDirectory) throws IOException,
            InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(
                createModCommand(actualUsername, newUserName, password, comment,
                shell, homeDirectory));
        if (session.executeCommand(commandToExecute.toString())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during usermod operation");
        }
        if (status) {
            unlockUser(actualUsername);
        } else {
            lockUser(actualUsername);
        }
        sshClient.disconnect();
    }

    private void unlockUser(String username)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        UserMod userModCommand = new UserMod();
        if (session.executeCommand(userModCommand.unlockUser(username))) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during unlock user");
        }
    }

    private String createModCommand(final String actualUsername,
            final String newUserName, final String password,
            final String comment, final String shell,
            final String homeDirectory) {
        UserMod userModCommand =
                new UserMod();
        StringBuilder commandToExecute = new StringBuilder();
        commandToExecute.append(userModCommand.userMod(
                actualUsername, newUserName, comment, shell, homeDirectory));
        if ((StringUtil.isNotBlank(password))
                && (StringUtil.isNotEmpty(password))) {
            Passwd passwdCommand =
                    new Passwd();
            commandToExecute.append("; ").append(passwdCommand.setPassword(
                    newUserName, password));
        }
        return commandToExecute.toString();
    }

    public void updateGroup(final String actualGroupName,
            final String newUserName)
            throws IOException, InvalidStateException, InterruptedException {
        GroupMod groupModCommand =
                new GroupMod(actualGroupName, newUserName);
        SessionChannelClient session = getSession();
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(groupModCommand.groupMod());
        if (session.executeCommand(commandToExecute.toString())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during groupmod operation");
        }
        sshClient.disconnect();
    }

    public final void deleteUser(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        UserDel userDelCommand =
                new UserDel(unixConfiguration, username);
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(userDelCommand.userdel());
        if (session.executeCommand(commandToExecute.toString())) {
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during deleted operation");
        }
        sshClient.disconnect();
    }

    public void deleteGroup(final String groupName)
            throws IOException, InvalidStateException, InterruptedException {
        SessionChannelClient session = getSession();
        GroupDel groupDelCommand = new GroupDel(groupName);
        StringBuilder commandToExecute = new StringBuilder();
        if (!unixConfiguration.isRoot()) {
            Sudo sudoCommand =
                    new Sudo(unixConfiguration.getSudoPassword());
            commandToExecute.append(sudoCommand.sudo()).append("; ");
        }
        commandToExecute.append(groupDelCommand.groupDel());
        if (session.executeCommand(commandToExecute.toString())) {
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
