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
import org.connid.openam.utilities.constants.InetUserStatus;
import org.connid.openam.utilities.constants.OpenAMQueryStringParameters;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMCreate extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMCreate.class);
    private Set<Attribute> attrs = null;
    private OpenAMConfiguration configuration = null;
    private OpenAMConnection connection = null;
    private ObjectClass objectClass = null;
    private String uidString = null;
    private AdminToken adminToken = null;

    public OpenAMCreate(final ObjectClass oc,
            final OpenAMConfiguration configuration, final Set<Attribute> attrs)
            throws UnsupportedEncodingException {
        this.configuration = configuration;
        this.attrs = attrs;
        connection = OpenAMConnection.openConnection(configuration);
        objectClass = oc;
        adminToken = new AdminToken(configuration);
    }

    public Uid create() {
        try {
            Uid createdUid = doCreate();
            adminToken.destroyToken();
            return createdUid;
        } catch (Exception e) {
            LOG.error(e, "error during creation");
            throw new ConnectorException(e);
        }
    }

    private Uid doCreate() throws IOException {

        if (AttributeUtil.getNameFromAttributes(attrs) == null) {
            throw new IllegalArgumentException("No Name attribute provided"
                    + "in the attributes");
        }

        uidString = AttributeUtil.getNameFromAttributes(attrs).getNameValue();

        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        if (userExists(uidString, configuration.getOpenamRealm(),
                adminToken.getToken(), connection)) {
            throw new ConnectorException("User " + uidString
                    + " already exists");
        }

        try {
            connection.create(createQueryString());
            LOG.ok("Creation commited");
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
        return new Uid(uidString);
    }

    private String createQueryString() {
        StringBuilder parameters = new StringBuilder();
        for (Attribute attr : attrs) {
            if (!parameters.toString().contains(
                    OpenAMQueryStringParameters.IDENTITY_NAME)) {
                parameters.append(OpenAMQueryStringParameters.IDENTITY_NAME).
                        append(uidString);
            } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                parameters.append(OpenAMQueryStringParameters.I_A_NAMES).append(
                        configuration.getOpenamPasswordAttribute()).append(
                        OpenAMQueryStringParameters.I_A_VALUES).append(
                        configuration.getOpenamPasswordAttribute()).append(
                        "=").append(getPlainPassword(
                        (GuardedString) attr.getValue().get(0)));
            } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                boolean status = false;
                // manage enable/disable status
                if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                    status = Boolean.parseBoolean(
                            attr.getValue().get(0).toString());
                }
                if (!status) {
                    parameters.append(OpenAMQueryStringParameters.I_A_NAMES).
                            append(configuration.getOpenamStatusAttribute()).
                            append(OpenAMQueryStringParameters.I_A_VALUES).
                            append(configuration.getOpenamStatusAttribute()).
                            append("=").append(InetUserStatus.INACTIVE);
                }
            } else {
                List<Object> values = attr.getValue();
                if ((values != null) && (!values.isEmpty())) {
                    parameters.append(OpenAMQueryStringParameters.I_A_NAMES).
                            append(attr.getName()).append(
                            OpenAMQueryStringParameters.I_A_VALUES).append(
                            attr.getName()).append("=").append(
                            (String) values.get(0));
                }
            }
        }
        parameters.append(OpenAMQueryStringParameters.REALM).append(
                configuration.getOpenamRealm()).append(
                OpenAMQueryStringParameters.IDENTITY_TYPE + "user").append(
                OpenAMQueryStringParameters.ADMIN).append(
                adminToken.getToken());
        return parameters.toString();
    }
}
