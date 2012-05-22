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
package org.connid.unix.utilities;

import java.util.ResourceBundle;

public class DefaultProperties {

    public static String UNIX_SHELL;
    public static String UNIX_USER_HOMEDIRECTORY;
    public static int SSH_SOCKET_TIMEOUT;

    static {
        ResourceBundle defaultProperties = ResourceBundle.getBundle("default");

        UNIX_SHELL = defaultProperties.getString("unix.user.shell");
        SSH_SOCKET_TIMEOUT = Integer.valueOf(
                defaultProperties.getString("ssh.socket.timeout")).intValue();
        UNIX_USER_HOMEDIRECTORY =
                defaultProperties.getString("unix.user.homedirectory");
    }
}
