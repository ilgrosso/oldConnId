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
import java.util.Set;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.AdminToken;
import org.connid.openam.utilities.constants.OpenAMQueryStringParameters;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMCreate extends CommonMethods {

    private OpenAMConnection connection = null;

    private ObjectClass objectClass = null;

    private final Set<Attribute> attrs;

    private final AdminToken adminToken;

    public OpenAMCreate(
            final ObjectClass oc,
            final OpenAMConfiguration configuration,
            final Set<Attribute> attrs)
            throws UnsupportedEncodingException {

        super(configuration);

        adminToken = new AdminToken(configuration);

        connection = OpenAMConnection.openConnection(configuration);
        objectClass = oc;
        this.attrs = attrs;
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

    private Uid doCreate()
            throws IOException {

        if (AttributeUtil.getNameFromAttributes(attrs) == null) {
            throw new IllegalArgumentException("No Name attribute provided"
                    + "in the attributes");
        }

        Uid uid = AttributeUtil.getUidAttribute(attrs);

        if (!objectClass.equals(ObjectClass.ACCOUNT) && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        if (userExists(uid.getUidValue(), configuration.getOpenamRealm(), adminToken.getToken(), connection)) {
            throw new ConnectorException("User " + uid.getUidValue() + " already exists");
        }

        try {
            final StringBuilder parameters = createUpdateQueryString(uid, attrs, adminToken);

            parameters.append(OpenAMQueryStringParameters.REALM).append(configuration.getOpenamRealm()).
                    append(OpenAMQueryStringParameters.IDENTITY_TYPE).append("user").
                    append(OpenAMQueryStringParameters.ADMIN).append(adminToken.getToken());

            connection.create(parameters.toString());

            LOG.ok("Creation commited");
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }

        return uid;
    }
}
