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
import org.apache.commons.httpclient.HttpStatus;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.utilities.Constants;
import org.identityconnectors.common.security.GuardedString;

public class CommonMethods {

    protected final boolean userExists(final String uidString,
            final String realm, final String token,
            final OpenAMConnection connection)
            throws IOException {
        boolean result = false;
        if (isAlive(connection)) {
            result = !"NULL".equalsIgnoreCase(connection.userSearch(
                    searchParameters(uidString, realm, token)));
        }
        return result;
    }

    private String searchParameters(final String uidString,
            final String realm, final String token)
            throws UnsupportedEncodingException {
        StringBuilder parameters = new StringBuilder();
        parameters.append("&filter=")
                .append(uidString)
                .append("&attributes_names=realm&attributes_values_realm=")
                .append(realm)
                .append("&admin=")
                .append(URLEncoder.encode(
                    token, Constants.ENCODING));
        return parameters.toString();
    }

    protected final boolean isAlive(final OpenAMConnection openAMConnection)
            throws IOException {
        if (HttpStatus.SC_OK == openAMConnection.isAlive()) {
            return true;
        } else {
            return false;
        }
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
}
