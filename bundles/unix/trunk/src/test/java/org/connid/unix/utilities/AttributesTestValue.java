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

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.ObjectClass;

public class AttributesTestValue extends SharedTestMethods {

    private final static String USERNAME = "createtest" + randomNumber();
    private static final String WRONG_USERNAME = "wronguser";
    private static final String PASSWORD = "password";
    private final GuardedString GUARDED_PASSWORD =
            new GuardedString(getPassword().toCharArray());
    private final GuardedString NEW_GUARDED_PASSWORD =
            new GuardedString(getNewPassword().toCharArray());
    private final GuardedString WRONG_GUARDED_PASSWORD =
            new GuardedString("wrongpassword".toCharArray());
    private static final String NEW_PASSWORD = "password2";
    private final ObjectClass WRONG_OBJECTCLASS =
            new ObjectClass("WRONG_OBJECTCLASS");
    private final static String GROUPNAME = "grouptest" + randomNumber();
    private static final String WRONG_GROUPNAME = "wronggroup";

    public String getUsername() {
        return USERNAME;
    }

    public String getWrongUsername() {
        return WRONG_USERNAME;
    }

    public GuardedString getGuardedPassword() {
        return GUARDED_PASSWORD;
    }

    public GuardedString getNewGuardedPassword() {
        return NEW_GUARDED_PASSWORD;
    }

    public GuardedString getWrongGuardedPassword() {
        return WRONG_GUARDED_PASSWORD;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public String getNewPassword() {
        return NEW_PASSWORD;
    }

    public ObjectClass getWrongObjectClass() {
        return WRONG_OBJECTCLASS;
    }

    public String getGroupName() {
        return GROUPNAME;
    }

    public String getWrongGroupName() {
        return WRONG_GROUPNAME;
    }

    private static int randomNumber() {
        return (int) (Math.random() * 100000);
    }
}
