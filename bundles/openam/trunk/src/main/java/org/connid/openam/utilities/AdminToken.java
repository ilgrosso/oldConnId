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
package org.connid.openam.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.connid.openam.methods.CommonMethods;

public final class AdminToken extends CommonMethods {

    private static OpenAMConnection openAMConnection = null;
    private static AdminToken adminToken = null;
    private static String token = "";

    private AdminToken(final OpenAMConfiguration configuration) {
        openAMConnection = OpenAMConnection.openConnection(configuration);
        String openamToken = openAMConnection.authenticate(
                configuration.getOpenamAdminUser(),
                getPlainPassword(configuration.getOpenamAdminPassword()));
        token = openamToken.substring(9, openamToken.length() - 1);
    }

    public static synchronized AdminToken getAdminToken(
            final OpenAMConfiguration configuration)
            throws UnsupportedEncodingException {
        if ((adminToken == null) || (!isTokenValid())) {
            adminToken = new AdminToken(configuration);
        }
        return adminToken;
    }

    private static boolean isTokenValid() throws UnsupportedEncodingException {
        return openAMConnection.isTokenValid(URLEncoder.encode(
                token, Constants.ENCODING)).contains("true");
    }

    public String getToken() {
        return token;
    }
}
