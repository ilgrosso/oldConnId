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
package org.connid.bundles.soap.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class WSAttribute extends AbstractData{

    /**
     * The name of the attribute.
     */
    private String name = null;

    /**
     * The type of the attribute:
     * String, Long, Double, Boolean, Character, Float, Integer, URI,
     * File, GuardedByteArray, GuardedString, Date.
     */
    private String type = "String";

    /**
     * Specifies if the attribute is a key.
     */
    private boolean key = false;

    /**
     * Specifies if the attribute is the password.
     */
    private boolean password = false;

    /**
     * Specifies if the attribute is nullable.
     */
    private boolean nullable = true;

    public WSAttribute() {
    }

    /**
     * Constructor: default attribute is a string nullable.
     * 
     * @param name defines the name of the attribute.
     */
    public WSAttribute(String name) {
        this.name = name;
    }

    /**
     * Constructor: default attribute is nullable.
     *
     * @param name defines the name of the attribute.
     * @param type defines the type of the attribute.
     */
    public WSAttribute(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Constructor: default attribute is not a key nor a password.
     *
     * @param name defines the name of the attribute.
     * @param type defines the type of the attribute.
     * @param isNullable defines if the attribute is nullable.
     */
    public WSAttribute(String name, String type, Boolean nullable) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
        if(key)
            this.nullable = false;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }
}
