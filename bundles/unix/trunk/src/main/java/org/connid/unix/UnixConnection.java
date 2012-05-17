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
import org.connid.unix.commands.SSHClient;

public class UnixConnection {

    private UnixConfiguration unixConfiguration = null;
    SSHClient sshc = null;

    private UnixConnection(final UnixConfiguration unixConfiguration) {
        this.unixConfiguration = unixConfiguration;
        sshc = new SSHClient(
                unixConfiguration.getHostname(), unixConfiguration.getPort(),
                unixConfiguration.getAdmin(), unixConfiguration.getPassword());
    }

    public static UnixConnection openConnection(UnixConfiguration unixConfiguration) {
        return new UnixConnection(unixConfiguration);
    }

    public int testConnection() throws IOException {
        return sshc.authenticate();
    }

    public void create(final String uidstring,
            final String password) throws IOException,
            InvalidStateException, InterruptedException {
        sshc.create(uidstring, password);
    }
}
