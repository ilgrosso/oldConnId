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
import java.util.List;
import java.util.Set;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.UnixConnection;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;

public class UnixCreate extends CommonMethods {

    private static final Log LOG = Log.getLog(UnixCreate.class);
    private Set<Attribute> attrs = null;
    private UnixConfiguration configuration = null;
    private UnixConnection connection = null;
    private String uidString = "";
    private String password = "";
    private String inactiveOption = "";

    public UnixCreate(final UnixConfiguration unixConfiguration,
            final Set<Attribute> attributes) throws IOException {
        this.configuration = unixConfiguration;
        this.attrs = attributes;
        connection = UnixConnection.openConnection(configuration);
    }

    public Uid create() {
        try {
            return doCreate();
        } catch (Exception e) {
            LOG.error(e, "error during creation");
            throw new ConnectorException(e);
        }
    }

    private Uid doCreate() throws IOException,
            InvalidStateException, InterruptedException {
        boolean status = false;
        for (Attribute attr : attrs) {
            if (attr.is(Name.NAME) || attr.is(Uid.NAME)) {
                uidString = (String) attr.getValue().get(0);
            } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                password = getPlainPassword(
                        (GuardedString) attr.getValue().get(0));
            } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                // manage enable/disable status
                if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                    status = Boolean.parseBoolean(
                            attr.getValue().get(0).toString());
                }
            } else {
                List<Object> values = attr.getValue();
                if ((values != null) && (!values.isEmpty())) {
                }
            }
        }

        if (userExists(uidString, connection)) {
            throw new ConnectorException(
                    "User " + uidString + " already exists");
        }
        connection.create(uidString, password, status);
        return new Uid(uidString);
    }
}
