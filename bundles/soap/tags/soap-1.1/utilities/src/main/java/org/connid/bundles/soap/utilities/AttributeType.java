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
package org.connid.bundles.soap.utilities;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

public enum AttributeType {

    String("java.lang.String"),
    Long("java.lang.Long"),
    Double("java.lang.Double"),
    Boolean("java.lang.Boolean"),
    Character("java.lang.Character"),
    Float("java.lang.Float"),
    Integer("java.lang.Integer"),
    URI("java.net.uri"),
    File("java.io.file"),
    // Date type is not supported by identityconnectors
    Date("java.lang.String");

    final private String className;

    private Format formatter;

    AttributeType(String className) {
        this.className = className;
        this.formatter = null;
    }

    public String getClassName() {
        return className;
    }

    public Format getBasicFormatter() {
        if (formatter == null) {
            switch (this) {
                case Date:
                    this.formatter = new SimpleDateFormat();
                case Long:
                case Double:
                    this.formatter = new DecimalFormat();
            }
        }

        return formatter;
    }

    public boolean isConversionPatternNeeded() {
        return this == AttributeType.Date || this == AttributeType.Double || this == AttributeType.Long;
    }
}
