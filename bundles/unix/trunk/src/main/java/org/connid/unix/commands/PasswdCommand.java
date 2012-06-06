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
    /**
     * This option is used to lock the specified account and it is available to
     * root only. The locking is performed by rendering the encrypted password
     * into an invalid string (by prefixing the encrypted string with an !).
     */
    private static final String LOCK_ACCOUNT = "-l";
    /**
     * This is the reverse of the -l option - it will unlock the account
     * password by removing the ! prefix. This option is avail - able to root
     * only. By default passwd will refuse to create a passwordless account (it
     * will not unlock an account that has only "!" as a password). The force
     * option -f will override this protection.
     *
     */
    private static final String UNLOCK_ACCOUNT = "-u";

    private String createChangeUserPasswordCommand(final String username,
            final String password) {
        return "echo " + password + " | " + PASSWD_COMMAND + " " + username
                + " " + READ_PASSWORD_FROM_STDIN;
    }

    public String setPassword(final String username,
            final String password) {
        return createChangeUserPasswordCommand(username, password);
    }

    public String lockUser(final String username) {
        return PASSWD_COMMAND + " " + LOCK_ACCOUNT + " " + username;
    }

    public String unlockUser(final String username) {
        return PASSWD_COMMAND + " " + UNLOCK_ACCOUNT + " " + username;
    }
}
