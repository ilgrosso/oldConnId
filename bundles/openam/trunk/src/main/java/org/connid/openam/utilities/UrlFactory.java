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
import org.connid.openam.utilities.constants.OpenAMQueryStringParameters;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;

public class UrlFactory {

    private static final Log LOG = Log.getLog(UrlFactory.class);

    private OpenAMConfiguration configuration = null;

    public UrlFactory(final OpenAMConfiguration configuration) {
        this.configuration = configuration;
    }

    public final String openAmUrl() {
        StringBuilder openAmRestfulServiceUrl = new StringBuilder();
        if (configuration.isSsl()) {
            openAmRestfulServiceUrl.append("https");
        } else {
            openAmRestfulServiceUrl.append("http");
        }

        openAmRestfulServiceUrl.append("://").
                append(configuration.getOpenamBaseUrl()).append(":").append(configuration.getOpenamPort()).
                append("/").append(configuration.getOpenamContext());

        return openAmRestfulServiceUrl.toString();
    }

    public final String authenticateUrl(final String username, final String password) {
        final StringBuilder authurl = new StringBuilder();

        authurl.append(openAmUrl()).append(OpenamServicesUrl.AUTHENTICATE_RESTFUL_SERVICE).
                append("username=").append(username).append("&password=").append(password);

        final StringBuilder uri = new StringBuilder();

        uri.append(OpenAMQueryStringParameters.AUTHREALM).append(configuration.getOpenamAuthRealm());

        if (StringUtil.isNotBlank(configuration.getOpenamAuthService())) {
            uri.append(OpenAMQueryStringParameters.AUTHSERVICE + configuration.getOpenamAuthService());
        }

        try {
            authurl.append(OpenAMQueryStringParameters.AUTHURI).append(URLEncoder.encode(uri.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e, "Error encoding URI");
        }

        return authurl.toString();
    }

    public final String logoutUrl(final String token) {
        StringBuilder logouturl = new StringBuilder();
        logouturl.append(openAmUrl()).append(
                OpenamServicesUrl.LOGOUT_RESTFUL_SERVICE).append(
                "subjectid=").append(token);
        return logouturl.toString();
    }

    public final String createUrl(final String parameters) {
        StringBuilder creationurl = new StringBuilder();
        creationurl.append(openAmUrl()).append(
                OpenamServicesUrl.CREATE_RESTFUL_SERVICE).append(parameters);
        return creationurl.toString();
    }

    public final String updateUrl(final String parameters) {
        StringBuilder updateurl = new StringBuilder();
        updateurl.append(openAmUrl()).append(
                OpenamServicesUrl.UPDATE_RESTFUL_SERVICE).append(parameters);
        return updateurl.toString();
    }

    public final String deleteUrl(final String parameters) {
        StringBuilder deleteurl = new StringBuilder();
        deleteurl.append(openAmUrl()).append(
                OpenamServicesUrl.DELETE_RESTFUL_SERVICE).append(parameters);
        return deleteurl.toString();
    }

    public final String searchUrl(final String parameters) {
        StringBuilder searchUrl = new StringBuilder();
        searchUrl.append(openAmUrl()).append(
                OpenamServicesUrl.SEARCH_RESTFUL_SERVICE).append(parameters);
        return searchUrl.toString();
    }

    public final String readUrl(final String parameters) {
        StringBuilder readUrl = new StringBuilder();
        readUrl.append(openAmUrl()).append(
                OpenamServicesUrl.READ_RESTFUL_SERVICE).append(parameters);
        return readUrl.toString();
    }

    public final String testUrl() {
        StringBuilder testUrl = new StringBuilder();
        testUrl.append(openAmUrl()).append(OpenamServicesUrl.TEST_SERVICE);
        return testUrl.toString();
    }

    public String isTokenValidUrl(String parameters) {
        StringBuilder isTokenValidUrl = new StringBuilder();
        isTokenValidUrl.append(openAmUrl()).append(
                OpenamServicesUrl.TOKEN_VALID_RESTFUL_SERVICE).append(
                "tokenid=").append(parameters);
        return isTokenValidUrl.toString();
    }
}
