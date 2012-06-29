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
import java.util.List;
import java.util.Set;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.AdminToken;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMUpdate extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMUpdate.class);
    private Set<Attribute> attrs = null;
    private ObjectClass objectClass = null;
    private OpenAMConfiguration configuration = null;
    private OpenAMConnection connection = null;
    private Uid uid = null;
    private AdminToken adminToken = null;

    public OpenAMUpdate(final ObjectClass oc,
            final OpenAMConfiguration openAMConfiguration,
            final Uid uid, final Set<Attribute> attrs)
            throws UnsupportedEncodingException {
        this.configuration = openAMConfiguration;
        objectClass = oc;
        this.uid = uid;
        this.attrs = attrs;
        connection = OpenAMConnection.openConnection(configuration);
        adminToken = new AdminToken(configuration);
    }

    public Uid update() {
        try {
            Uid updatedUid = doUpdate();
            adminToken.destroyToken();
            return updatedUid;
        } catch (Exception e) {
            LOG.error(e, "error during update operation");
            throw new ConnectorException(e);
        }
    }

    private Uid doUpdate() throws IOException {

        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        if (!userExists(uid.getUidValue(), configuration.getOpenamRealm(),
                adminToken.getToken(), connection)) {
            LOG.error("User do not exists");
            throw new ConnectorException("User do not exists");
        }

        try {
            connection.update(updateQueryString());
            LOG.ok("User " + uid.getUidValue() + " updated");
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
        return uid;
    }

    private String updateQueryString() {
        StringBuilder parameters = new StringBuilder();
        for (Attribute attr : attrs) {
            List<Object> values = attr.getValue();
            if (values != null && !values.isEmpty()) {
                if (attr.is(Name.NAME)) {
                    parameters.append(
                            "&identity_name=").append(uid.getUidValue());
                } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                    parameters.append("&identity_attribute_names=").append(
                            configuration.getOpenamPasswordAttribute()).append(
                            "&identity_attribute_values_").append(
                            configuration.getOpenamPasswordAttribute()).append(
                            "=").append(getPlainPassword(
                            (GuardedString) values.get(0)));
                } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                    boolean status = false;
                    // manage enable/disable status
                    if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                        status = Boolean.parseBoolean(
                                attr.getValue().get(0).toString());
                    }
                    if (!status) {
                        parameters.append("&identity_attribute_names=").append(
                                configuration.getOpenamStatusAttribute()).append(
                                "&identity_attribute_values_").append(
                                configuration.getOpenamStatusAttribute()).append(
                                "=").append("inactive");
                    } else if (status) {
                        parameters.append("&identity_attribute_names=").append(
                                configuration.getOpenamStatusAttribute()).append(
                                "&identity_attribute_values_").append(
                                configuration.getOpenamStatusAttribute()).append(
                                "=").append("active");
                    }
                } else {
                    parameters.append("&identity_attribute_names=").append(
                            attr.getName()).append(
                            "&identity_attribute_values_").append(
                            attr.getName()).append("=").append(
                            (String) values.get(0));
                }
            }
        }
        parameters.append("&admin=").append(adminToken.getToken());
        return parameters.toString();
    }
}
