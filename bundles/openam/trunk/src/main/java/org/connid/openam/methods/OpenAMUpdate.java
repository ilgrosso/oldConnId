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
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMUpdate extends CommonMethods {

    private ObjectClass objectClass = null;

    private OpenAMConnection connection = null;

    private final Uid uid;

    private final Set<Attribute> attrs;

    private final AdminToken adminToken;

    public OpenAMUpdate(
            final ObjectClass oc,
            final OpenAMConfiguration configuration,
            final Uid uid, final Set<Attribute> attrs)
            throws UnsupportedEncodingException {

        super(configuration);

        adminToken = new AdminToken(configuration);

        objectClass = oc;
        this.uid = uid;
        this.attrs = attrs;
        connection = OpenAMConnection.openConnection(configuration);
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

    private Uid doUpdate()
            throws IOException {

        if (!objectClass.equals(ObjectClass.ACCOUNT)
                && (!objectClass.equals(ObjectClass.GROUP))) {
            throw new IllegalStateException("Wrong object class");
        }

        if (!userExists(uid.getUidValue(), configuration.getOpenamRealm(),
                adminToken.getToken(), connection)) {
            LOG.error("User " + uid.getUidValue() + " do not exists");
            throw new ConnectorException("User " + uid.getUidValue() + " do not exists");
        }

        try {
            connection.update(createUpdateQueryString(uid, attrs, adminToken).toString());
            LOG.ok("User " + uid.getUidValue() + " updated");
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
        return uid;
    }
}
