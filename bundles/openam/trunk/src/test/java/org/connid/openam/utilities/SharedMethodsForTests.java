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

import java.util.HashSet;
import java.util.Set;
import org.connid.openam.OpenAMConfiguration;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Name;

public class SharedMethodsForTests {

    protected final OpenAMConfiguration createConfiguration() {
        // create the connector configuration..
        OpenAMConfiguration config = new OpenAMConfiguration();
        config.setSsl(Boolean.valueOf(OpenAMProperties.OPENAM_SERVER_PROTOCOL));
        config.setOpenamBaseUrl(OpenAMProperties.OPENAM_SERVER_BASEURL);
        config.setOpenamPort(OpenAMProperties.OPENAM_SERVER_PORT);
        config.setOpenamContext(OpenAMProperties.OPENAM_SERVER_CONTEXT);

        config.setOpenamRealm(OpenAMProperties.OPENAM_SERVER_REALM);
        config.setOpenamAdminUser(OpenAMProperties.OPENAM_SERVER_ADMIN_USER);
        config.setOpenamAdminPassword(new GuardedString(
                OpenAMProperties.OPENAM_SERVER_ADMIN_PASSWORD.toCharArray()));
        config.setOpenamPasswordAttribute(
                OpenAMProperties.OPENAM_PASSWORD_ATTRIBUTE);
        config.setOpenamStatusAttribute(
                OpenAMProperties.OPENAM_STATUS_ATTRIBUTE);
        config.setOpenamUidAttribute(
                OpenAMProperties.OPENAM_UID_ATTRIBUTE);
        config.validate();
        return config;
    }

    protected final Set<Attribute> createSetOfAttributes(final Name name) {
        Set<Attribute> attributes = new HashSet<Attribute>();

        attributes.add(name);
        attributes.add(AttributeBuilder.build(TestAccountsValue.CN,
                name.getNameValue()));
        attributes.add(AttributeBuilder.build(TestAccountsValue.SN,
                name.getNameValue()));
        attributes.add(AttributeBuilder.build(TestAccountsValue.USERPASSWORD,
                "password"));
        return attributes;
    }

    protected final Set<Attribute> createSetOfAttributes(final Name name,
            final String password, final boolean status) {
        GuardedString encPassword = null;
        if (password != null) {
            encPassword = new GuardedString(password.toCharArray());
        }

        final Set<Attribute> attributes = CollectionUtil.newSet(
                AttributeBuilder.buildPassword(encPassword));
        attributes.add(name);
        return attributes;
    }

    protected int randomNumber() {
        return (int) (Math.random() * 100000);
    }
}
