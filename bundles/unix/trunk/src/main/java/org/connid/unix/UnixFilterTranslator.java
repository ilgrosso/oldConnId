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

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.filter
        .AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter
        .ContainsAllValuesFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter
        .GreaterThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter
        .LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;

public class UnixFilterTranslator extends AbstractFilterTranslator<String> {

    @Override
    protected final String createAndExpression(final String leftExpression,
            final String rightExpression) {
        return "(&(" + leftExpression + ")"
                + "(" + rightExpression + ")";
    }

    @Override
    protected final String createOrExpression(final String leftExpression,
            final String rightExpression) {
        return "(|(" + leftExpression + ")"
                + "(" + rightExpression + ")";
    }

    @Override
    protected final String createStartsWithExpression(final StartsWithFilter filter,
            final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("=")
                .append(attribute.getValue().get(0)).append("*)");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createContainsAllValuesExpression(
            final ContainsAllValuesFilter filter, final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("=*")
                .append(attribute.getValue().get(0)).append("*)");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createContainsExpression(final ContainsFilter filter,
            final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("=*")
                .append(attribute.getValue().get(0)).append("*)");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createEndsWithExpression(final EndsWithFilter filter,
            final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("=*")
                .append(attribute.getValue().get(0)).append(")");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createEqualsExpression(final EqualsFilter filter,
            final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("=")
                .append(attribute.getValue().get(0)).append(")");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createGreaterThanExpression(final GreaterThanFilter filter,
            final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append(">")
                .append(attribute.getValue().get(0)).append(")");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createGreaterThanOrEqualExpression(
            final GreaterThanOrEqualFilter filter, final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append(">=")
                .append(attribute.getValue().get(0)).append(")");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createLessThanExpression(
            final LessThanFilter filter, final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("<")
                .append(attribute.getValue().get(0)).append(")");
        if (not) {
            string.append(")");
        }
        return string.toString();
    }

    @Override
    protected final String createLessThanOrEqualExpression(
            final LessThanOrEqualFilter filter, final boolean not) {
        final Attribute attribute = filter.getAttribute();
        if (!validateSearchAttribute(attribute)) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        if (not) {
            string.append("(!");
        }
        string.append("(").append(attribute.getName()).append("<=")
                .append(attribute.getValue().get(0)).append(")");
        if (not) {
            string.append(")");
        }
        return string.toString();
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
