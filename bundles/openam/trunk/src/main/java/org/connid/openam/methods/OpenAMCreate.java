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
import java.util.List;
import java.util.Set;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.AdminToken;
import org.connid.openam.utilities.Constants;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMCreate extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMCreate.class);
    private Set<Attribute> attrs = null;
    private OpenAMConfiguration configuration = null;
    private OpenAMConnection connection = null;
    private String token = "";

    public OpenAMCreate(final OpenAMConfiguration configuration,
            final Set<Attribute> attrs) {
        this.configuration = configuration;
        this.attrs = attrs;
        connection = OpenAMConnection.openConnection(configuration);
        token = AdminToken.getAdminToken(configuration).token;
    }

    public Uid execute() {
        try {
            return executeImpl();
        } catch (Exception e) {
            LOG.error(e, "error during creation");
            throw new ConnectorException(e);
        }
    }

    private Uid executeImpl() throws UnsupportedEncodingException, IOException {
        
        StringBuilder parameters = new StringBuilder();
        String uidString = "";

        if (AttributeUtil.getNameFromAttributes(attrs) == null) {
            throw new IllegalArgumentException("No Name attribute provided"
                    + "in the attributes");
        }

        for (Attribute attr : attrs) {
            if (attr.is(Name.NAME) || attr.is(Uid.NAME)
                    && (!parameters.toString().contains("identity_name="))) {
                uidString = (String) attr.getValue().get(0);
                parameters.append("&identity_name=")
                        .append(uidString);
            } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                parameters.append("&identity_attribute_names=")
                        .append(configuration.getOpenamPasswordAttribute())
                        .append("&identity_attribute_values_")
                        .append(configuration.getOpenamPasswordAttribute())
                        .append("=").append(getPlainPassword(
                        (GuardedString) attr.getValue().get(0)));
            } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                boolean status = false;
                // manage enable/disable status
                if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                    status = Boolean.parseBoolean(
                            attr.getValue().get(0).toString());
                }
                if (!status) {
                    parameters.append("&identity_attribute_names=")
                        .append(configuration.getOpenamStatusAttribute())
                        .append("&identity_attribute_values_")
                        .append(configuration.getOpenamStatusAttribute())
                        .append("=").append("inactive");
                }
            } else {
                List<Object> values = attr.getValue();
                if ((values != null) && (!values.isEmpty())) {
                    parameters.append("&identity_attribute_names=")
                            .append(attr.getName())
                            .append("&identity_attribute_values_")
                            .append(attr.getName()).append("=")
                            .append((String) values.get(0));
                }
            }
        }

        parameters.append("&identity_realm=")
                .append(configuration.getOpenamRealm())
                .append("&identity_type=user")
                .append("&admin=")
                .append(URLEncoder.encode(
                    token, Constants.ENCODING));

        if (userExists(uidString, configuration.getOpenamRealm(),
                token, connection)) {
            throw new ConnectorException("User Exists");
        }

        if (isAlive(connection)) {
            try {
                connection.create(parameters.toString());
                LOG.ok("Creation commited");
            } catch (HttpClientErrorException hcee) {
                throw hcee;
            }
        }
        return new Uid(uidString);
    }
}
