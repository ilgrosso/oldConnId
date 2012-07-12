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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.AdminToken;
import org.connid.openam.utilities.constants.InetUserStatus;
import org.connid.openam.utilities.constants.OpenAMQueryStringParameters;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;

public abstract class CommonMethods {

    protected final OpenAMConfiguration configuration;

    protected static final Log LOG = Log.getLog(CommonMethods.class);

    public CommonMethods(final OpenAMConfiguration configuration) {
        this.configuration = configuration;
    }

    protected final boolean userExists(
            final String uidString,
            final String realm, final String token,
            final OpenAMConnection connection)
            throws IOException {
        return !"NULL".equalsIgnoreCase(connection.userSearch(searchParameters(uidString, realm, token)));
    }

    private String searchParameters(final String uidString,
            final String realm, final String token) {
        StringBuilder parameters = new StringBuilder();
        parameters.append(OpenAMQueryStringParameters.FILTER).append(uidString).
                append(OpenAMQueryStringParameters.A_NAMES + "realm"
                + OpenAMQueryStringParameters.A_VALUES + "_realm=").append(
                realm).append(OpenAMQueryStringParameters.ADMIN).append(token);
        return parameters.toString();
    }

    protected final String getPlainPassword(final GuardedString password) {
        final StringBuffer buf = new StringBuffer();

        password.access(new GuardedString.Accessor() {

            @Override
            public void access(final char[] clearChars) {
                buf.append(clearChars);
            }
        });
        return buf.toString();
    }

    protected StringBuilder createUpdateQueryString(
            final Uid uid, final Set<Attribute> attrs, final AdminToken adminToken)
            throws UnsupportedEncodingException {

        final StringBuilder parameters = new StringBuilder();

        parameters.append(OpenAMQueryStringParameters.IDENTITY_NAME).append(uid.getUidValue());

        for (Attribute attr : attrs) {
            final Map.Entry<String, List<String>> values = getValues(attr);

            if (values != null) {
                for (String singleValue : values.getValue()) {
                    parameters.append(OpenAMQueryStringParameters.I_A_NAMES).append(values.getKey()).
                            append(OpenAMQueryStringParameters.I_A_VALUES).append(values.getKey()).
                            append("=").append(URLEncoder.encode(singleValue, "UTF-8"));
                }
            }

        }

        parameters.append(OpenAMQueryStringParameters.ADMIN).append(adminToken.getToken());

        return parameters;
    }

    protected Map.Entry<String, List<String>> getValues(final Attribute attr) {
        Map.Entry<String, List<String>> values = null;

        final List<Object> objs = attr.getValue();

        if (objs != null && !objs.isEmpty()) {
            if (attr.is(Name.NAME)) {
                // ignore
            } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {

                values = new AbstractMap.SimpleEntry<String, List<String>>(
                        configuration.getOpenamPasswordAttribute(),
                        Collections.singletonList(getPlainPassword((GuardedString) objs.get(0))));

            } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {

                values = new AbstractMap.SimpleEntry<String, List<String>>(
                        configuration.getOpenamStatusAttribute(),
                        Collections.singletonList(Boolean.parseBoolean(objs.get(0).toString())
                        ? InetUserStatus.ACTIVE : InetUserStatus.INACTIVE));

            } else if (attr.is(Uid.NAME)) {

                values = new AbstractMap.SimpleEntry<String, List<String>>(
                        configuration.getOpenamUidAttribute(),
                        Collections.singletonList(objs.get(0).toString()));

            } else {
                values = new AbstractMap.SimpleEntry<String, List<String>>(attr.getName(), new ArrayList<String>());

                for (Object obj : objs) {
                    values.getValue().add(obj.toString());
                }
            }
        }

        return values;
    }
}
