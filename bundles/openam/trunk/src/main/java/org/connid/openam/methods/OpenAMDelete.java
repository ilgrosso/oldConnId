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
package org.connid.openam.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.AdminToken;
import org.connid.openam.utilities.constants.OpenAMQueryStringParameters;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMDelete extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMDelete.class);
    private ObjectClass objectClass = null;
    private OpenAMConfiguration configuration = null;
    private OpenAMConnection connection = null;
    private Uid uid = null;
    private AdminToken adminToken = null;

    public OpenAMDelete(final ObjectClass oc,
            final OpenAMConfiguration openAMConfiguration,
            final Uid uid) throws UnsupportedEncodingException {
        this.configuration = openAMConfiguration;
        objectClass = oc;
        this.uid = uid;
        connection = OpenAMConnection.openConnection(configuration);
        adminToken = new AdminToken(configuration);
    }

    public final void delete() {
        try {
            doDelete();
            adminToken.destroyToken();
        } catch (Exception e) {
            LOG.error(e, "error during delete operation");
            throw new ConnectorException(e);
        }
    }

    private void doDelete() throws IOException {

        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        if (!userExists(uid.getUidValue(), configuration.getOpenamRealm(),
                adminToken.getToken(), connection)) {
            LOG.error("User " + uid.getUidValue() + " do not exists");
            throw new ConnectorException("User " + uid.getUidValue()
                    + " do not exists");
        }

        try {
            connection.delete(deleteParameters());
            LOG.ok("User " + uid.getUidValue() + " deleted");
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
    }

    private String deleteParameters() {
        StringBuilder parameters = new StringBuilder();
        parameters.append(OpenAMQueryStringParameters.IDENTITY_NAME).append(
                uid.getUidValue()).append(
                OpenAMQueryStringParameters.IDENTITY_TYPE + "user").append(
                OpenAMQueryStringParameters.ADMIN).append(
                adminToken.getToken());
        return parameters.toString();
    }
}
