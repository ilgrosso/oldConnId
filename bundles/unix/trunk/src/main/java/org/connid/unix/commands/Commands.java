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

import org.connid.unix.utilities.Constants;
import org.identityconnectors.common.StringUtil;

public class Commands {

    private static CommandsOptionByConfiguration commandsOption =
            new CommandsOptionByConfiguration();

    public static String getUserAddCommand(final String username,
            final String password, final boolean status) {
        StringBuilder useraddCommand = new StringBuilder("useradd ");
        useraddCommand.append(
                commandsOption.createHomeDirectory()).append(" ").append(
                commandsOption.baseHomeDirectory()).append(" ").append(
                commandsOption.userShell());
        if (status) {
            useraddCommand.append(" -e ").append(Constants.getInactiveDate());
        }
        useraddCommand.append(" ").append(username).append(
                "; echo ").append(password).append(" | passwd ").append(
                username).append(" --stdin");
        return useraddCommand.toString();
    }

    public static String getUserModCommand(final String actualUsername,
            final String username, final String password) {
        StringBuilder usermodCommand = new StringBuilder("usermod ");
        if ((StringUtil.isNotBlank(username))
                && (StringUtil.isNotEmpty(username))) {
            usermodCommand.append("-l ").append(username);
        }
        usermodCommand.append(actualUsername).append(
                "; echo ").append(password).append(" | passwd ").append(
                username).append(" --stdin");
        return usermodCommand.toString();
    }

    public static String getUserExistsCommand(final String username) {
        return "cat /etc/passwd | grep " + username;
    }

    public static String getUserDelCommand(final String username) {
        return "userdel " + commandsOption.deleteHomeDirectory()
                + " " + username;
    }
}