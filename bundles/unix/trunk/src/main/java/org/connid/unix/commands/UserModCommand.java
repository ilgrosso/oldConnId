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
package org.connid.unix.commands;

import org.identityconnectors.common.StringUtil;

public class UserModCommand {

    /**
     * The usermod command modifies the system account files to reflect the
     * changes that are specified on the command line.
     *
     *
     */
    private static final String USERMOD_COMMAND = "usermod";
    /**
     * The name of the user will be changed from LOGIN to NEW_LOGIN. Nothing
     * else is changed. In particular, the user's home directory name should
     * probably be changed manually to reflect the new login name.
     */
    private static final String CHANGE_LOGIN_OPTION = "-l";
    private String actualUsername = "";
    private String username = "";

    public UserModCommand(final String actualUsername, final String username) {
        this.actualUsername = actualUsername;
        this.username = username;
    }

    private String createUserModCommand() {
        StringBuilder usermodCommand = new StringBuilder(USERMOD_COMMAND + " ");
        if ((StringUtil.isNotBlank(username))
                && (StringUtil.isNotEmpty(username))) {
            usermodCommand.append(CHANGE_LOGIN_OPTION).append(
                    " ").append(username);
        }
        usermodCommand.append(actualUsername);
        return usermodCommand.toString();
    }
}
