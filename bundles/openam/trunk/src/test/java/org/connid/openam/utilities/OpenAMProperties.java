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

import java.util.ResourceBundle;

public class OpenAMProperties {

    public static String OPENAM_SERVER_PROTOCOL;
    public static String OPENAM_SERVER_BASEURL;
    public static String OPENAM_SERVER_PORT;
    public static String OPENAM_SERVER_CONTEXT;
    public static String OPENAM_SERVER_REALM;
    public static String OPENAM_SERVER_ADMIN_USER;
    public static String OPENAM_SERVER_ADMIN_PASSWORD;
    public static String OPENAM_PASSWORD_ATTRIBUTE;
    public static String OPENAM_UID_ATTRIBUTE;
    public static String OPENAM_STATUS_ATTRIBUTE;

    static {
        
        ResourceBundle openamProperties = ResourceBundle.getBundle("openam");

        OPENAM_SERVER_PROTOCOL =
                openamProperties.getString("openam.server.protocol");
        OPENAM_SERVER_BASEURL =
                openamProperties.getString("openam.server.baseurl");
        OPENAM_SERVER_PORT =
                openamProperties.getString("openam.server.port");
        OPENAM_SERVER_CONTEXT =
                openamProperties.getString("openam.server.context");
        OPENAM_SERVER_REALM =
                openamProperties.getString("openam.server.realm");
        OPENAM_SERVER_ADMIN_USER =
                openamProperties.getString("openam.server.admin.user");
        OPENAM_SERVER_ADMIN_PASSWORD =
                openamProperties.getString("openam.server.admin.password");
        OPENAM_PASSWORD_ATTRIBUTE =
                openamProperties.getString("openam.server.password.attribute");
        OPENAM_UID_ATTRIBUTE =
                openamProperties.getString("openam.server.uid.attribute");
        OPENAM_STATUS_ATTRIBUTE =
                openamProperties.getString("openam.server.status.attribute");
    }
}
