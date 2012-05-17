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
package org.connid.unix.methods;

import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import java.io.IOException;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.UnixConnection;
import org.connid.unix.utilities.Constants;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class UnixTest {

    private static final Log LOG = Log.getLog(UnixTest.class);
    private UnixConnection unixConnection = null;

    public UnixTest(final UnixConfiguration unixConfiguration) {
        unixConnection = UnixConnection.openConnection(unixConfiguration);
    }

    public final void test() {
        try {
            execute();
        } catch (Exception e) {
            LOG.error(e, "error during test connection");
            throw new ConnectorException(e);
        }
    }

    private void execute() throws IOException {
        int statusCode = unixConnection.testConnection();
        if (statusCode != AuthenticationProtocolState.COMPLETE) {
            LOG.error("Connection error");
            throw new IOException("Connection error");
        }
    }
}
