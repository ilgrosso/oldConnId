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
import org.connid.unix.utilities.EvaluateCommandsResultOutput;
import org.connid.unix.utilities.Utilities;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;

public class UnixCreate {

    private static final Log LOG = Log.getLog(UnixCreate.class);
    private Set<Attribute> attrs = null;
    private UnixConnection connection = null;
    private ObjectClass objectClass = null;

    public UnixCreate(final ObjectClass oc,
            final UnixConfiguration unixConfiguration,
            final Set<Attribute> attributes) throws IOException {
        this.attrs = attributes;
        connection = UnixConnection.openConnection(unixConfiguration);
        objectClass = oc;
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
        String comment = "";

        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        final Name name = AttributeUtil.getNameFromAttributes(attrs);

        if (name == null || StringUtil.isBlank(name.getNameValue())) {
            throw new IllegalArgumentException(
                    "No Name attribute provided in the attributes");
        }

        String username = name.getNameValue();

        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            if (EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
                    connection.userExists(username))) {
                throw new ConnectorException(
                        "User " + username + " already exists");
            }

            for (Attribute attr : attrs) {
                if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                    continue;
                } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                    // manage enable/disable status
                    if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                        status = Boolean.parseBoolean(
                                attr.getValue().get(0).toString());
                    }
                } else {
                    List<Object> values = attr.getValue();
                    if ((values != null) && (!values.isEmpty())) {
                        comment = (String) values.get(0);
                    }
                }
            }

            final String password = Utilities.getPlainPassword(
                    AttributeUtil.getPasswordValue(attrs));

            connection.createUser(username, password, comment, status);
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            if (EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
                    connection.groupExists(username))) {
                throw new ConnectorException(
                        "Group " + username + " already exists");
            }
            connection.createGroup(username);
        }

        return new Uid(username);
    }
}
