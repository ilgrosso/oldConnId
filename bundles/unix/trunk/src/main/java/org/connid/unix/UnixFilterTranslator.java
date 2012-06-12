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
package org.connid.unix;

import org.connid.unix.search.Operand;
import org.connid.unix.search.Operator;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.*;

public class UnixFilterTranslator extends AbstractFilterTranslator<Operand> {
    
    @Override
    protected Operand createEqualsExpression(final EqualsFilter filter,
            final boolean not) {
        if (filter == null) {
            return null;
        }
        String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String name = filter.getAttribute().getName();
        
        return new Operand(Operator.EQ, name, value, not);
    }
    
    private String checkSearchValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        if (value.contains("*") || value.contains("&") || value.contains("|")) {
            throw new IllegalArgumentException(
                    "Value of search attribute contains illegal character(s).");
        }
        return value;
    }
    
    private boolean validateSearchAttribute(final Attribute attribute) {
        //Ignore streamed ( byte[] objects ) from query
        if (byte[].class.equals(AttributeUtil.getSingleValue(attribute).
                getClass())) {
            return false;
        }
        //Otherwise let the database process
        return true;
    }
}
