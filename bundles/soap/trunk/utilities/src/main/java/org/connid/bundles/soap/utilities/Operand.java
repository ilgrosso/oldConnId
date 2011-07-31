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

import java.util.Set;
import org.connid.bundles.soap.to.WSAttribute;

public class Operand extends WSAttribute {

    private Operator op;

    private String value;

    private Set<Operand> operands;

    private boolean logical;

    boolean not;

    public Operand() {
        super(null);
        this.op = Operator.EQ;
        this.value = "*";
        this.not = false;
        this.logical = false;
        this.operands = null;
    }

    public Operand(Operator op, String name, String value) {
        super(name);
        this.op = op;
        this.value = value;
        this.not = false;
        this.logical = false;
        this.operands = null;
    }

    public Operand(Operator op, Set<Operand> operands) {
        super(null);
        this.value = null;
        this.op = op;
        this.operands = operands;
        this.logical = true;
    }

    public Operand(Operator op, String name, String value, boolean not) {
        super(name);
        this.op = op;
        this.value = value;
        this.not = not;
        this.logical = false;
        this.operands = null;
    }

    public Operand(Operator op, Set<Operand> operands, boolean not) {
        super(null);
        this.value = null;
        this.op = op;
        this.operands = operands;
        this.logical = true;
        this.not = not;
    }

    public Operator getOp() {
        return op;
    }

    public Set<Operand> getOperands() {
        return operands;
    }

    public void setOperands(Set<Operand> operands) {
        if (logical)
            this.operands = operands;
    }

    public void setOp(final Operator op) {
        this.op = op;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        if (!logical)
            this.value = value;
    }

    public boolean isLogical() {
        return logical;
    }

    public void setLogical(boolean logical) {
        this.logical = logical;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    @Override
    public String toString() {

        if (Operator.STARTS.equals(op)) {
            return (not ? "NOT" : "") +
                    getName() + " LIKE '" + getValue() + "%'";
        }

        if (Operator.ENDS.equals(op)) {
            return (not ? "NOT" : "") +
                    getName() + " LIKE '%" + getValue() + "'";
        }

        if (Operator.CONTAINS.equals(op)) {
            return (not ? "NOT" : "") +
                    getName() + " LIKE '%" + getValue() + "%'";
        }

        if (Operator.GT.equals(op)) {
            return (not ? "NOT" : "") +
                    getName() + ">'" + getValue() + "'";
        }

        if (Operator.LT.equals(op)) {
            return (not ? "NOT" : "") +
                    getName() + "<'" + getValue() + "'";
        }

        if (Operator.EQ.equals(op)) {
            return (not ? "NOT" : "") +
                    getName() + "='" + getValue() + "'";
        }

        if (operands == null) return null;

        StringBuilder queryBuilder = new StringBuilder();

        for (Operand operand : operands) {

            if (queryBuilder.length() > 0)
                queryBuilder.append(" " + op.toString() + " ");

            queryBuilder.append(operand.toString());
        }

        if (not)
            return "NOT (" + queryBuilder + ")";

        if (operands.size() > 1)
            return "(" + queryBuilder + ")";

        return queryBuilder.toString();
    }
}
