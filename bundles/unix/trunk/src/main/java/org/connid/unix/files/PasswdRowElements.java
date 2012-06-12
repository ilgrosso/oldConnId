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
 * http://connid.googlecode.com/svn/trunk/legal/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at connid/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.unix.files;

public enum PasswdRowElements {

    USERNAME(0),
    PASSWORD_VALIDATOR(1),
    USER_IDENTIFIER(2),
    GROUP_IDENTIFIER(3),
    COMMENT(4),
    HOME_DIRECTORY(5),
    SHELL(6);
    private String comment = "";
    private String homeDirectory = "";
    private String shell = "";
    private int code;

    private PasswdRowElements(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
