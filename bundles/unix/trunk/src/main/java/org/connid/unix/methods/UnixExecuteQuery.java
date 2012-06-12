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
import org.connid.unix.files.PasswdFile;
import org.connid.unix.files.PasswdRow;
import org.connid.unix.search.Operand;
import org.connid.unix.utilities.EvaluateCommandsResultOutput;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;

public class UnixExecuteQuery {

    private static final Log LOG = Log.getLog(UnixExecuteQuery.class);
    private UnixConnection connection = null;
    private UnixConfiguration unixConfiguration = null;
    private Operand filter = null;
    private ResultsHandler handler = null;
    private ObjectClass objectClass = null;
    private String nameToSearch = "";

    public UnixExecuteQuery(final UnixConfiguration configuration,
            final ObjectClass oc, final Operand filter,
            final ResultsHandler rh) throws IOException {
        connection = UnixConnection.openConnection(configuration);
        unixConfiguration = configuration;
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

        if (filter == null) {
            throw new ConnectorException("Filter is null");
        }

        if (filter.isUid()) {
            nameToSearch = filter.getAttributeValue();
        }

        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            PasswdFile passwdFile = new PasswdFile(connection.searchAllUser());
            PasswdRow passwdRow = passwdFile.searchRowByUsername(nameToSearch);
            ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
            if (StringUtil.isNotEmpty(passwdRow.getUsername())
                    && StringUtil.isNotBlank(passwdRow.getUsername())) {
                bld.setName(passwdRow.getUsername());
                bld.setUid(passwdRow.getUsername());
            } else {
                bld.setUid("_W_R_O_N_G_");
                bld.setName("_W_R_O_N_G_");
            }
            bld.addAttribute(AttributeBuilder.build(
                    unixConfiguration.getCommentAttribute(),
                    CollectionUtil.newSet(passwdRow.getComment())));
            bld.addAttribute(AttributeBuilder.build(
                    unixConfiguration.getShellAttribute(),
                    CollectionUtil.newSet(passwdRow.getShell())));
            bld.addAttribute(AttributeBuilder.build(
                    unixConfiguration.getHomeDirectoryAttribute(),
                    CollectionUtil.newSet(passwdRow.getHomeDirectory())));
            bld.addAttribute(OperationalAttributes.ENABLE_NAME,
                    EvaluateCommandsResultOutput.evaluateUserStatus(
                    connection.userStatus(nameToSearch)));
            handler.handle(bld.build());
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
            if (StringUtil.isNotBlank(nameToSearch)
                    && StringUtil.isNotEmpty(nameToSearch)
                    && EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
                    connection.groupExists(nameToSearch))) {
                bld.setName(nameToSearch);
                bld.setUid(nameToSearch);
            }
            handler.handle(bld.build());
        }
    }
}
