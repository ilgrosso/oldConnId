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

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class WSUser extends AbstractData {

    private String accountid;

    private Set<WSAttributeValue> attributes;

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public Set<WSAttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<WSAttributeValue> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(WSAttributeValue attribute) {
        if (attributes == null)
            attributes = new HashSet<WSAttributeValue>();

        this.attributes.add(attribute);
    }

    public WSUser() {
    }

    public WSUser(String accountid, Set<WSAttributeValue> attributes) {
        this.accountid = accountid;
        this.attributes = attributes;
    }
}
