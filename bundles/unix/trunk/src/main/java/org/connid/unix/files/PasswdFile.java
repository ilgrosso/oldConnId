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
package org.connid.unix.files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.connid.unix.search.Operand;
import org.connid.unix.utilities.EvaluateCommandsResultOutput;

public class PasswdFile {

    private List<PasswdRow> passwdRows = new ArrayList<PasswdRow>();

    public PasswdFile(final List<String> passwdFile) {
        setPasswdRows(passwdFile);
    }

    private void setPasswdRows(final List<String> passwdFile) {
        for (Iterator<String> it = passwdFile.iterator(); it.hasNext();) {
            passwdRows.add(EvaluateCommandsResultOutput.toPasswdRow(it.next()));
        }
    }

    public final List<PasswdRow> searchRowByAttribute(final String attribute,
            final boolean not) {
        List<PasswdRow> userRow = new ArrayList<PasswdRow>();
        for (Iterator<PasswdRow> it = passwdRows.iterator(); it.hasNext();) {
            PasswdRow passwdRow = it.next();
            if (!not) {
                if (attribute.equalsIgnoreCase(passwdRow.getUsername())) {
                    userRow.add(passwdRow);
                }
                if (attribute.equalsIgnoreCase(passwdRow.getShell())) {
                    userRow.add(passwdRow);
                }
                if (attribute.equalsIgnoreCase(passwdRow.getComment())) {
                    userRow.add(passwdRow);
                }
                if (attribute.equalsIgnoreCase(passwdRow.getHomeDirectory())) {
                    userRow.add(passwdRow);
                }
            } else {
                if (!attribute.equalsIgnoreCase(passwdRow.getUsername())) {
                    userRow.add(passwdRow);
                }
                if (!attribute.equalsIgnoreCase(passwdRow.getShell())) {
                    userRow.add(passwdRow);
                }
                if (!attribute.equalsIgnoreCase(passwdRow.getComment())) {
                    userRow.add(passwdRow);
                }
                if (!attribute.equalsIgnoreCase(passwdRow.getHomeDirectory())) {
                    userRow.add(passwdRow);
                }
            }
        }
        return userRow;
    }

    public List<PasswdRow> searchRowByStartsWithValue(
            final String startWithValue) {
        List<PasswdRow> userRow = new ArrayList<PasswdRow>();
        for (Iterator<PasswdRow> it = passwdRows.iterator(); it.hasNext();) {
            PasswdRow passwdRow = it.next();
            if (passwdRow.getUsername().startsWith(startWithValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getShell().startsWith(startWithValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getComment().startsWith(startWithValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getHomeDirectory().startsWith(startWithValue)) {
                userRow.add(passwdRow);
            }
        }
        return userRow;
    }

    public List<PasswdRow> searchRowByEndsWithValue(
            final String endsWithValue) {
        List<PasswdRow> userRow = new ArrayList<PasswdRow>();
        for (Iterator<PasswdRow> it = passwdRows.iterator(); it.hasNext();) {
            PasswdRow passwdRow = it.next();
            if (passwdRow.getUsername().endsWith(endsWithValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getShell().endsWith(endsWithValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getComment().endsWith(endsWithValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getHomeDirectory().endsWith(endsWithValue)) {
                userRow.add(passwdRow);
            }
        }
        return userRow;
    }

    public List<PasswdRow> searchRowByContainsValue(String containsValue) {
        List<PasswdRow> userRow = new ArrayList<PasswdRow>();
        for (Iterator<PasswdRow> it = passwdRows.iterator(); it.hasNext();) {
            PasswdRow passwdRow = it.next();
            if (passwdRow.getUsername().contains(containsValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getShell().contains(containsValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getComment().contains(containsValue)) {
                userRow.add(passwdRow);
            }
            if (passwdRow.getHomeDirectory().contains(containsValue)) {
                userRow.add(passwdRow);
            }
        }
        return userRow;
    }
}
