/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 */
package org.connid.openam;

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

public class OpenAMFilterTranslator extends AbstractFilterTranslator<String> {

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
