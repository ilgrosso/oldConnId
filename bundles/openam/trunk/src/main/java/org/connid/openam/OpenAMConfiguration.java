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
package org.connid.openam;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class OpenAMConfiguration extends AbstractConfiguration {

    private boolean ssl = false;

    private String openamBaseUrl = "";

    private String openamPort = "";

    private String openamContext = "";

    private String openamRealm = "";

    private String openamAuthService = "ldapService";

    private String openamAuthRealm = "/";

    private String openamAdminUser = "";

    private GuardedString openamAdminPassword = null;

    private String openamUidAttribute = "";

    private String openamPasswordAttribute = "";

    private String openamStatusAttribute = "";

    @ConfigurationProperty(displayMessageKey = "openamprotocol.display",
    helpMessageKey = "openamprotocol.help", order = 1)
    public final boolean isSsl() {
        return ssl;
    }

    public final void setSsl(final boolean ssl) {
        this.ssl = ssl;
    }

    @ConfigurationProperty(displayMessageKey = "openambaseurl.display",
    helpMessageKey = "openambaseurl.help", order = 2)
    public final String getOpenamBaseUrl() {
        return openamBaseUrl;
    }

    public final void setOpenamBaseUrl(final String openamBaseUrl) {
        this.openamBaseUrl = openamBaseUrl;
    }

    @ConfigurationProperty(displayMessageKey = "openamport.display",
    helpMessageKey = "openamport.help", order = 3)
    public final String getOpenamPort() {
        return openamPort;
    }

    public final void setOpenamPort(final String openamPort) {
        this.openamPort = openamPort;
    }

    @ConfigurationProperty(displayMessageKey = "openamcontext.display",
    helpMessageKey = "openamcontext.help", order = 4)
    public final String getOpenamContext() {
        return openamContext;
    }

    public final void setOpenamContext(final String openamContext) {
        this.openamContext = openamContext;
    }

    @ConfigurationProperty(displayMessageKey = "openamrealm.display",
    helpMessageKey = "openamrealm.help", order = 5)
    public final String getOpenamRealm() {
        return openamRealm;
    }

    public final void setOpenamRealm(final String openamRealm) {
        this.openamRealm = openamRealm;
    }

    @ConfigurationProperty(displayMessageKey = "openamAuthRealm.display",
    helpMessageKey = "openamAuthRealm.help", order = 6)
    public String getOpenamAuthRealm() {
        return openamAuthRealm;
    }

    public void setOpenamAuthRealm(String openamAuthRealm) {
        this.openamAuthRealm = openamAuthRealm;
    }

    @ConfigurationProperty(displayMessageKey = "openamAuthService.display",
    helpMessageKey = "openamAuthService.help", order = 7)
    public String getOpenamAuthService() {
        return openamAuthService;
    }

    public void setOpenamAuthService(String openamAuthService) {
        this.openamAuthService = openamAuthService;
    }

    @ConfigurationProperty(displayMessageKey = "openamadminuser.display",
    helpMessageKey = "openamadminuser.help", order = 8)
    public final String getOpenamAdminUser() {
        return openamAdminUser;
    }

    public final void setOpenamAdminUser(final String openamAdminUser) {
        this.openamAdminUser = openamAdminUser;
    }

    @ConfigurationProperty(displayMessageKey = "openamadminpassword.display",
    helpMessageKey = "openamadminpassword.help", order = 9)
    public final GuardedString getOpenamAdminPassword() {
        return openamAdminPassword;
    }

    public final void setOpenamAdminPassword(final GuardedString openamAdminPassword) {
        this.openamAdminPassword = openamAdminPassword;
    }

    @ConfigurationProperty(displayMessageKey =
    "openampasswordattribute.display",
    helpMessageKey = "openampasswordattribute.help", order = 10)
    public final String getOpenamPasswordAttribute() {
        return openamPasswordAttribute;
    }

    public final void setOpenamPasswordAttribute(
            final String openamPasswordAttribute) {
        this.openamPasswordAttribute = openamPasswordAttribute;
    }

    @ConfigurationProperty(displayMessageKey = "openamstatusattribute.display",
    helpMessageKey = "openamstatusattribute.help", order = 11)
    public final String getOpenamStatusAttribute() {
        return openamStatusAttribute;
    }

    public final void setOpenamStatusAttribute(
            final String openamStatusAttribute) {
        this.openamStatusAttribute = openamStatusAttribute;
    }

    @ConfigurationProperty(displayMessageKey = "openamuidattribute.display",
    helpMessageKey = "openamuidattribute.help", order = 12)
    public final String getOpenamUidAttribute() {
        return openamUidAttribute;
    }

    public final void setOpenamUidAttribute(final String openamUidAttribute) {
        this.openamUidAttribute = openamUidAttribute;
    }

    @Override
    public final void validate() {
        if (StringUtil.isBlank(this.openamBaseUrl)) {
            throw new IllegalArgumentException(
                    "OpenAM base url must not be blank!");
        }

        if (StringUtil.isBlank(this.openamPort)) {
            throw new IllegalArgumentException(
                    "OpenAM port must not be blank!");
        }

        if (StringUtil.isBlank(this.openamContext)) {
            throw new IllegalArgumentException(
                    "OpenAM context must not be blank!");
        }

        if (StringUtil.isBlank(this.openamRealm)) {
            throw new IllegalArgumentException(
                    "OpenAM realm must not be blank!");
        }

        if (StringUtil.isBlank(this.openamAdminUser)) {
            throw new IllegalArgumentException(
                    "OpenAM admin user must not be blank!");
        }

        if (StringUtil.isBlank(this.openamAdminPassword.toString())) {
            throw new IllegalArgumentException(
                    "OpenAM admin password must not be blank!");
        }

        if (StringUtil.isBlank(this.openamPasswordAttribute)) {
            throw new IllegalArgumentException(
                    "OpenAM password attribute must not be blank!");
        }

        if (StringUtil.isBlank(this.openamStatusAttribute)) {
            throw new IllegalArgumentException(
                    "OpenAM status attribute must not be blank!");
        }

        if (StringUtil.isBlank(this.openamUidAttribute)) {
            throw new IllegalArgumentException(
                    "OpenAM uid attribute must not be blank!");
        }
    }
}
