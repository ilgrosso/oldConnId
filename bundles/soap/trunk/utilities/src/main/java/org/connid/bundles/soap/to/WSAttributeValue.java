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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.identityconnectors.framework.common.FrameworkUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class WSAttributeValue extends WSAttribute {

    private List values = null;

    private List evaluatedTypeValues = null;

    public WSAttributeValue() {
        super();
    }

    public WSAttributeValue(WSAttribute wsAttribute) {
        super();

        if (wsAttribute != null) {
            setType(wsAttribute.getType());
            setName(wsAttribute.getName());
            setKey(wsAttribute.isKey());
            setNullable(wsAttribute.isNullable());
            setPassword(wsAttribute.isPassword());
        }
    }

    public List getValues() {
        if (this.values == null) {
            this.values = new ArrayList();
        }

        if (evaluatedTypeValues == null) {
            evaluatedTypeValues = new ArrayList();

            for (Object obj : values) {
                try {
                    FrameworkUtil.checkAttributeValue(obj);
                    getValues().add(obj);
                } catch (IllegalArgumentException e) {
                    getValues().add(obj.toString());
                }
            }
        }

        return this.evaluatedTypeValues;
    }

    public void setValues(final List values) {
        this.values = values;
    }

    public final boolean addValue(Object value) {
        if (this.values == null) {
            this.values = new ArrayList();
        }

        return this.values.add(value);
    }

    public String getStringValue() {
        if (getType() == null || !"String".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        String res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = values.iterator().next().toString();
        }

        return res;
    }

    public Boolean getBooleanValue() {
        if (getType() == null || !"Boolean".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Boolean res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Boolean) values.iterator().next();
        }

        return res;
    }

    public Long getLongValue() {
        if (getType() == null || !"Long".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Long res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Long) values.iterator().next();
        }

        return res;
    }

    public Float getFloatValue() {
        if (getType() == null || !"Float".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Float res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Float) values.iterator().next();
        }

        return res;
    }

    public Double getDoubleValue() {
        if (getType() == null || !"Double".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Double res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Double) values.iterator().next();
        }

        return res;
    }

    public Integer getIntegerValue() {
        if (getType() == null || !"Integer".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Integer res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Integer) values.iterator().next();
        }

        return res;
    }

    public Date getDateValue() {
        if (getType() == null || !"Date".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Date res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Date) values.iterator().next();
        }

        return res;
    }

    public Character getCharacterValue() {
        if (getType() == null || !"Character".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        Character res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (Character) values.iterator().next();
        }

        return res;
    }

    public URI getURIValue() {
        if (getType() == null || !"URI".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        URI res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (URI) values.iterator().next();
        }

        return res;
    }

    public File getFileValue() {
        if (getType() == null || !"File".equals(getType())) {
            throw new IllegalArgumentException("Invalid type declaration");
        }

        File res;

        if (values == null && values.isEmpty()) {
            res = null;
        } else {
            res = (File) values.iterator().next();
        }

        return res;
    }
}
