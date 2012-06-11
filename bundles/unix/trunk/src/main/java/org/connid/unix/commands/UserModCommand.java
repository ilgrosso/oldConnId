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
    /**
     * Lock a user's password. This puts a '!' in front of the encrypted
     * password, effectively disabling the password.
     */
    private static final String LOCK_USER_OPTION = "-L";
    /**
     * Unlock a user's password. This removes the '!' in front of the encrypted
     * password.
     */
    private static final String UNLOCK_USER_OPTION = "-U";
    /**
     * The new value of the user's password file comment field. It is normally
     * modified using the chfn(1) utility.
     */
    private static final String COMMENT_OPTION = "-c";
    /**
     * The name of the user's new login shell. Setting this field to blank
     * causes the system to select the default login shell.
     */
    private static final String SHELL_OPTION = "-s";
    /**
     * The user's new login directory. If the -m option is given, the contents
     * of the current home directory will be moved to the new home directory,
     * which is created if it does not already exist.
     */
    private static final String HOMEDIRECTORY_OPTION = "-d";
    /**
     * Move the content of the user's home directory to the new location. This
     * option is only valid in combination with the -d (or --home) option.
     */
    private static final String MOVE_FILE_OPTION = "-m";

    public String userMod(final String actualUsername,
            final String newUsername, final String comment, final String shell,
            final String homeDirectory) {
        return createUserModCommand(actualUsername, newUsername, comment, shell,
                homeDirectory);
    }

    private String createUserModCommand(final String actualUsername,
            final String newUsername, final String comment, final String shell,
            final String homeDirectory) {
        StringBuilder usermodCommand = new StringBuilder(USERMOD_COMMAND + " ");
        if ((StringUtil.isNotBlank(newUsername))
                && (StringUtil.isNotEmpty(newUsername))) {
            usermodCommand.append(CHANGE_LOGIN_OPTION).append(
                    " ").append(newUsername).append(" ");
        }
        if ((StringUtil.isNotBlank(comment))
                && (StringUtil.isNotEmpty(comment))) {
            usermodCommand.append(COMMENT_OPTION).append(
                    " ").append(comment);
        }
        if ((StringUtil.isNotBlank(shell))
                && (StringUtil.isNotEmpty(shell))) {
            usermodCommand.append(SHELL_OPTION).append(
                    " ").append(shell);
        }
        if ((StringUtil.isNotBlank(homeDirectory))
                && (StringUtil.isNotEmpty(homeDirectory))) {
            usermodCommand.append(HOMEDIRECTORY_OPTION).append(
                    " ").append(homeDirectory).append(
                    MOVE_FILE_OPTION).append(" ");
        }
        usermodCommand.append(actualUsername);
        return usermodCommand.toString();
    }

    public String lockUser(final String username) {
        return USERMOD_COMMAND + " " + LOCK_USER_OPTION + " " + username;
    }

    public String unlockUser(final String username) {
        return USERMOD_COMMAND + " " + UNLOCK_USER_OPTION + " " + username;
    }
}
