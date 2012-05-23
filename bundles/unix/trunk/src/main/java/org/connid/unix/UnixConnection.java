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
import org.connid.unix.commands.SSHClient;
import org.identityconnectors.common.security.GuardedString;

public class UnixConnection {

    private static UnixConfiguration unixConfiguration = null;
    SSHClient sshc = null;

    private UnixConnection(final UnixConfiguration unixConfiguration)
            throws IOException {
        this.unixConfiguration = unixConfiguration;
        sshc = new SSHClient(
                unixConfiguration.getHostname(), unixConfiguration.getPort(),
                unixConfiguration.getAdmin(),
                getPlainPassword(unixConfiguration.getPassword()));
    }

    public static UnixConnection openConnection(
            final UnixConfiguration unixConfiguration) throws IOException {
        return new UnixConnection(unixConfiguration);
    }

    public static UnixConfiguration getConfiguration() {
        return unixConfiguration;
    }

    public boolean userExists(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        return sshc.userExists(username);
    }

    public void testConnection() throws IOException {
        sshc.getSession();
    }

    public void create(final String uidstring,
            final String password, final boolean status) throws IOException,
            InvalidStateException, InterruptedException {
        sshc.createUser(uidstring, password, status);
    }

    public void update(final String actualUsername,
            final String username, final String password)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.updateUser(actualUsername, username, password);
    }

    public void delete(final String username)
            throws IOException, InvalidStateException, InterruptedException {
        sshc.deleteUser(username);
    }

    public void authenticate(final String username, final String password)
            throws UnknownHostException, IOException {
        sshc.authenticate(username, password);
    }

    private final String getPlainPassword(final GuardedString password) {
        final StringBuffer buf = new StringBuffer();

        password.access(new GuardedString.Accessor() {

            @Override
            public void access(final char[] clearChars) {
                buf.append(clearChars);
            }
        });
        return buf.toString();
    }
}
