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

import org.connid.unix.UnixConfiguration;
import org.identityconnectors.common.StringUtil;

public class UserAdd {

    private UnixConfiguration unixConfiguration = null;
    /**
     * useradd - create a new user or update default new user information.
     */
    private static final String USERADD_COMMAND = "useradd";
    /**
     * Create the user's home directory if it does not exist. The files and
     * directories contained in the skeleton directory (which can be defined
     * with the -k option) will be copied to the home directory. By default, no
     * home directories are created.
     */
    private static final String CREATE_HOME_DIR_OPTION = "-m";
    /**
     * The default base directory for the system if -d HOME_DIR is not
     * specified. BASE_DIR is concatenated with the account name to define the
     * home directory. If the -m option is not used, BASE_DIR must exist. If
     * this option is not specified, useradd will use the base directory
     * specified by the HOME variable in /etc/default/useradd, or /home by
     * default.
     */
    private static final String BASE_HOME_DIR_OPTION = "-b";
    /**
     * The name of the user's login shell. The default is to leave this field
     * blank, which causes the system to select the default login shell
     * specified by the SHELL variable in /etc/default/useradd, or an empty
     * string by default.
     */
    private static final String SHELL_OPTION = "-s";
    /**
     * Any text string. It is generally a short description of the login, and is
     * currently used as the field for the user's full name.
     *
     */
    private static final String COMMENT = "-c";
    private String username = "";
    private String password = "";
    private String comment = "";
    private String shell = "";
    private String homeDirectory = "";

    public UserAdd(final UnixConfiguration configuration,
            final String username, final String password, final String comment,
            final String shell, final String homeDirectory) {
        unixConfiguration = configuration;
        this.username = username;
        this.password = password;
        this.comment = comment;
        this.shell = shell;
        this.homeDirectory = homeDirectory;
    }

    private String createUserAddCommand() {
        StringBuilder useraddCommand = new StringBuilder(USERADD_COMMAND + " ");
        if (unixConfiguration.isCreateHomeDirectory()) {
            useraddCommand.append(CREATE_HOME_DIR_OPTION + " ");
        }
        useraddCommand.append(BASE_HOME_DIR_OPTION).append(" ");
        if (StringUtil.isNotEmpty(homeDirectory)
                && StringUtil.isNotBlank(homeDirectory)) {
            useraddCommand.append(homeDirectory).append(" ");
        } else {
            useraddCommand.append(
                    unixConfiguration.getBaseHomeDirectory()).append(" ");
        }
        useraddCommand.append(SHELL_OPTION).append(" ");
        if (StringUtil.isNotEmpty(shell)
                && StringUtil.isNotBlank(shell)) {
            useraddCommand.append(shell).append(" ");
        } else {
            useraddCommand.append(
                    unixConfiguration.getShell()).append(" ");
        }
        if ((StringUtil.isNotBlank(comment))
                && (StringUtil.isNotEmpty(comment))) {
            useraddCommand.append(COMMENT + " ").append(comment).append(" ");
        }
        useraddCommand.append(username);
        return useraddCommand.toString();
    }

    public String useradd() {
        return createUserAddCommand();
    }
}
