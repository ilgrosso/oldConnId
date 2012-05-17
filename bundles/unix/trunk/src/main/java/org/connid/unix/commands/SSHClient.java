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
    SshConnectionProperties properties = new SshConnectionProperties();
    private String username;
    private String password;
    private SshClient sshClient = null;

    public SSHClient(final String host, final int port, final String userName,
            final String password) {
        properties.setHost(host);
        properties.setPort(port);
        this.username = userName;
        this.password = password;
        sshClient = new SshClient();
    }

    public final int authenticate() throws IOException {
        sshClient.connect(properties, new IgnoreHostKeyVerification());
        PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
        pwd.setUsername(username);
        pwd.setPassword(password);
        return sshClient.authenticate(pwd);
    }

    public final SessionChannelClient getSession() throws IOException {
        authenticate();
        return sshClient.openSessionChannel();
    }

    public final boolean userExists(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        boolean exists = false;
        authenticate();
        SessionChannelClient session = sshClient.openSessionChannel();
        IOStreamConnector output = new IOStreamConnector();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (session.executeCommand(
                Commands.getUserExistsCommand(username))) {
            output.connect(session.getInputStream(), bos);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
            exists = !bos.toString().isEmpty();
        } else {
            LOG.error("Error during password encrypt");
        }
        bos.close();
        output.close();
        return exists;
    }

    public final void createUser(final String uidstring, final String password)
            throws IOException, InvalidStateException, InterruptedException {
        authenticate();
        SessionChannelClient session = sshClient.openSessionChannel();
        String encryptPassword = "";
        IOStreamConnector output = new IOStreamConnector();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
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
        bos.close();
        output.close();
    }

    public final void deleteUser(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        authenticate();
        SessionChannelClient session = sshClient.openSessionChannel();
        IOStreamConnector output = new IOStreamConnector();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (session.executeCommand(
                Commands.getUserDelCommand(username))) {
            output.connect(session.getInputStream(), bos);
            session.getState().waitForState(ChannelState.CHANNEL_CLOSED);
        } else {
            LOG.error("Error during deleted operation");
        }
        bos.close();
        output.close();
    }
}
