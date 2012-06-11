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
package org.connid.unix;

import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.IOException;
import java.net.UnknownHostException;
import org.connid.unix.sshmanagement.SSHClient;

public class UnixConnection {

    private static UnixConfiguration unixConfiguration = null;
    SSHClient sshc = null;

    private UnixConnection(final UnixConfiguration unixConfiguration)
            throws IOException {
        this.unixConfiguration = unixConfiguration;
        sshc = new SSHClient(unixConfiguration);
    }

    public static UnixConnection openConnection(
            final UnixConfiguration unixConfiguration) throws IOException {
        return new UnixConnection(unixConfiguration);
    }

    public static UnixConfiguration getConfiguration() {
        return unixConfiguration;
    }

    public String userExists(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        return sshc.userExists(username);
    }

    public String searchUser(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        return sshc.searchUser(username);
    }

    public String userStatus(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        return sshc.userStatus(username);
    }

    public String groupExists(String groupname)
            throws IOException, InvalidStateException, InterruptedException {
        return sshc.groupExists(groupname);
    }

    public void testConnection() throws IOException {
        sshc.getSession();
    }

    public void createUser(final String uidstring,
            final String password, final String comment, final String shell,
            final String homeDirectory, final boolean status)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.createUser(uidstring, password, comment, shell,
                homeDirectory, status);
    }

    public void createGroup(String groupName)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.createGroup(groupName);
    }

    public void updateUser(final String actualUsername,
            final String username, final String password, final boolean status,
            final String comment, final String shell, final String homeDir)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.updateUser(actualUsername, username, password, status, comment,
                shell, homeDir);
    }

    public void updateGroup(String actualGroupName, String newUserName)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.updateGroup(actualGroupName, newUserName);
    }

    public void deleteUser(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.deleteUser(username);
    }

    public void deleteGroup(String groupName)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.deleteGroup(groupName);
    }

    public void authenticate(final String username, final String password)
            throws UnknownHostException, IOException {
        sshc.authenticate(username, password);
    }
}
