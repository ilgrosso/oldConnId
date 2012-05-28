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

public class PasswdCommand {

    private String username = "";
    private String userPassword = "";
    /**
     * passwd - update user's authentication tokens.
     *
     */
    private static final String PASSWD_COMMAND = "passwd";
    /**
     * This option is used to indicate that passwd should read the new password
     * from standard input, which can be a pipe.
     */
    private static final String READ_PASSWORD_FROM_STDIN = "--stdin";

    public PasswdCommand(final String username, final String password) {
        this.username = username;
        userPassword = password;
    }

    private String createChangeUserPasswordCommand() {
        return "echo " + userPassword + " | " + PASSWD_COMMAND + " " + username
                + " " + READ_PASSWORD_FROM_STDIN;
    }

    public String passwd() {
        return createChangeUserPasswordCommand();
    }
}
