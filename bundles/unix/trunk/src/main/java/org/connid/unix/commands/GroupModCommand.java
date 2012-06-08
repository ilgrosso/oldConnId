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

public class GroupModCommand {

    /**
     * The groupmod command modifies the definition of the specified GROUP by
     * modifying the appropriate entry in the group database.
     */
    private static final String GROUPMOD_COMMAND = "groupmod";
    /**
     * The name of the group will be changed from GROUP to NEW_GROUP name.
     *
     */
    private static final String NEW_NAME_OPTION = "-n";
    private String groupName = "";
    private String newGroupName = "";

    public GroupModCommand(final String groupName, final String newGroupName) {
        this.groupName = groupName;
        this.newGroupName = newGroupName;
    }

    private String createGroupModCommand() {
        return GROUPMOD_COMMAND + " " + NEW_NAME_OPTION + " " + newGroupName
                + " " + groupName;
    }

    public String groupMod() {
        return createGroupModCommand();
    }
}
