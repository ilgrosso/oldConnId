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
import org.connid.unix.UnixConnection;

public class CommandsOptionByConfiguration {

    private UnixConfiguration unixConfiguration =
            UnixConnection.getConfiguration();

    public final String createHomeDirectory() {
        String option = "";
        if (unixConfiguration.isCreateHomeDirectory()) {
            option = "-m";
        }
        return option;
    }

    public final String deleteHomeDirectory() {
        String option = "";
        if (unixConfiguration.isDeleteHomeDirectory()) {
            option = "-r";
        }
        return option;
    }

    public final String baseHomeDirectory() {
        return "-b " + unixConfiguration.getBaseHomeDirectory();
    }
    
    public final String userShell() {
        return "-s " + unixConfiguration.getShell();
    }
}
