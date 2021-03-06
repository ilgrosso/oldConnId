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
import java.net.URLEncoder;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.AdminToken;
import org.connid.openam.utilities.Constants;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Uid;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMDelete extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMDelete.class);
    private OpenAMConfiguration configuration = null;
    private OpenAMConnection connection = null;
    private Uid uid = null;
    private String token = "";

    public OpenAMDelete(final OpenAMConfiguration openAMConfiguration,
            final Uid uid) throws UnsupportedEncodingException {
        this.configuration = openAMConfiguration;
        this.uid = uid;
        connection = OpenAMConnection.openConnection(configuration);
        token = AdminToken.getAdminToken(configuration).token;
    }

    public final void delete() {
        try {
            doDelete();
        } catch (Exception e) {
            LOG.error(e, "error during delete operation");
            throw new ConnectorException(e);
        }
    }

    private void doDelete() throws IOException {

        if (!userExists(uid.getUidValue(), configuration.getOpenamRealm(),
                token, connection)) {
            LOG.error("User do not exists");
            throw new ConnectorException("User do not exists");
        }

        if (isAlive(connection)) {
            try {
                connection.delete(deleteParameters());
                LOG.ok("Delete committed");
            } catch (HttpClientErrorException hcee) {
                throw hcee;
            }
        }
    }

    private String deleteParameters() throws UnsupportedEncodingException {
        StringBuilder parameters = new StringBuilder();
        parameters.append("&identity_name=")
                .append(uid.getUidValue())
                .append("&identity_type=user")
                .append("&admin=")
                .append(URLEncoder.encode(
                    token, Constants.ENCODING));
        return parameters.toString();
    }
}
