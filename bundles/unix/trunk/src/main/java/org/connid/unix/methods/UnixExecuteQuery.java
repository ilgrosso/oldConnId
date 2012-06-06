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

import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.IOException;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.UnixConnection;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;

public class UnixExecuteQuery extends CommonMethods {

    private static final Log LOG = Log.getLog(UnixExecuteQuery.class);
    private UnixConnection connection = null;
    private String filter = null;
    private ResultsHandler handler = null;
    private ObjectClass objectClass = null;

    public UnixExecuteQuery(final UnixConfiguration configuration,
            final ObjectClass oc, final String filter,
            final ResultsHandler rh) throws IOException {
        connection = UnixConnection.openConnection(configuration);
        this.filter = filter;
        handler = rh;
        objectClass = oc;
    }

    public final void executeQuery() {
        try {
            doExecuteQuery();
        } catch (Exception e) {
            LOG.error(e, "error during execute query operation");
            throw new ConnectorException(e);
        }
    }

    private void doExecuteQuery()
            throws IOException, InvalidStateException, InterruptedException {
        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            String username = cleanFilter(filter);
            connection.userExists(username);
        }

    }

    private String cleanFilter(String filter) {
        String username[] = filter.split("=");
        String a = username[1].substring(0, username.length - 1);
        return a;
    }
}
