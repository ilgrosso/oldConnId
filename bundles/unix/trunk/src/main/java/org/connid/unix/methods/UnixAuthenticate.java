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

import java.io.IOException;
import java.net.UnknownHostException;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.UnixConnection;
import org.connid.unix.utilities.Utilities;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;

public class UnixAuthenticate extends CommonMethods {

    private static final Log LOG = Log.getLog(UnixAuthenticate.class);
    private UnixConnection unixConnection = null;
    private String username = "";
    private GuardedString password = null;
    private ObjectClass objectClass;

    public UnixAuthenticate(final ObjectClass oc,
            final UnixConfiguration unixConfiguration,
            final String username, final GuardedString password)
            throws IOException {
        unixConnection = UnixConnection.openConnection(unixConfiguration);
        this.username = username;
        this.password = password;
        objectClass = oc;
    }

    public Uid authenticate() {
        try {
            return doAuthenticate();
        } catch (Exception e) {
            LOG.error(e, "error during authentication");
            throw new ConnectorException("error during authentication", e);
        }
    }

    private Uid doAuthenticate() throws UnknownHostException, IOException {
        if (!objectClass.equals(ObjectClass.ACCOUNT)) {
            throw new IllegalStateException("Wrong object class");
        }
        unixConnection.authenticate(username,
                Utilities.getPlainPassword(password));
        return new Uid(username);
    }
}
