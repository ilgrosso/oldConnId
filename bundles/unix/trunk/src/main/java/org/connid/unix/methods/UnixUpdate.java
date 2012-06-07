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
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;

public class UnixUpdate {

    private static final Log LOG = Log.getLog(UnixUpdate.class);
    private Set<Attribute> attrs = null;
    private UnixConfiguration configuration = null;
    private UnixConnection connection = null;
    private Uid uid = null;
    private String newUserName = "";
    private String password = "";
    private boolean status = true;
    private ObjectClass objectClass = null;

    public UnixUpdate(final ObjectClass oc,
            final UnixConfiguration unixConfiguration,
            final Uid uid, final Set<Attribute> attrs) throws IOException {
        this.configuration = unixConfiguration;
        this.uid = uid;
        this.attrs = attrs;
        connection = UnixConnection.openConnection(configuration);
        objectClass = oc;
    }

    public Uid update() {
        try {
            return doUpdate();
        } catch (Exception e) {
            LOG.error(e, "error during update operation");
            throw new ConnectorException("Error during update", e);
        }
    }

    private Uid doUpdate()
            throws IOException, InvalidStateException, InterruptedException {

        if (uid == null || StringUtil.isBlank(uid.getUidValue())) {
            throw new IllegalArgumentException(
                    "No Uid attribute provided in the attributes");
        }

        LOG.info("Update user: " + uid.getUidValue());

        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            if (!EvaluateCommandsResultOutput.evaluateUserOrGroupExistsCommand(
                    connection.userExists(uid.getUidValue()))) {
                throw new ConnectorException(
                        "User " + uid + " do not exists");
            }
            for (Attribute attr : attrs) {
                if (attr.is(Name.NAME) || attr.is(Uid.NAME)) {
                    newUserName = (String) attr.getValue().get(0);
                } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                    password = Utilities.getPlainPassword(
                            (GuardedString) attr.getValue().get(0));
                } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                    status = Boolean.parseBoolean(
                            attr.getValue().get(0).toString());
                } else {
                    List<Object> values = attr.getValue();
                    if ((values != null) && (!values.isEmpty())) {
                    }
                }
            }
            connection.updateUser(uid.getUidValue(), newUserName, password,
                    status);
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            if (!EvaluateCommandsResultOutput.evaluateUserOrGroupExistsCommand(
                    connection.groupExists(newUserName))) {
                throw new ConnectorException(
                        "Group do not exists");
            }
            connection.updateGroup(uid.getUidValue(), newUserName);
        }
        return uid;
    }
}
