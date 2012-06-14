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
package org.connid.unix.search;

import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Iterator;
import java.util.List;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.UnixConnection;
import org.connid.unix.files.PasswdFile;
import org.connid.unix.files.PasswdRow;
import org.connid.unix.utilities.EvaluateCommandsResultOutput;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.objects.*;

public class Search {

    private UnixConnection unixConnection = null;
    private Operand filter = null;
    private UnixConfiguration unixConfiguration = null;
    private ObjectClass objectClass = null;
    private ResultsHandler handler = null;

    public Search(final UnixConfiguration unixConfiguration,
            final UnixConnection unixConnection,
            final ResultsHandler handler, final ObjectClass oc,
            final Operand filter) {
        this.unixConfiguration = unixConfiguration;
        this.unixConnection = unixConnection;
        this.handler = handler;
        this.objectClass = oc;
        this.filter = filter;
    }

    public void equalSearch()
            throws IOException, InvalidStateException, InterruptedException {
        String nameToSearch = filter.getAttributeValue();
        ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            PasswdFile passwdFile =
                    new PasswdFile(unixConnection.searchAllUser());
            List<PasswdRow> passwdRows =
                    passwdFile.searchRowByAttribute(nameToSearch);
            if (passwdRows == null || passwdRows.isEmpty()) {
                throw new ConnectException("No results found");
            }
            for (Iterator<PasswdRow> it =
                    passwdRows.iterator(); it.hasNext();) {
                PasswdRow passwdRow = it.next();
                if (StringUtil.isNotEmpty(passwdRow.getUsername())
                        && StringUtil.isNotBlank(passwdRow.getUsername())) {
                    bld.setName(passwdRow.getUsername());
                    bld.setUid(passwdRow.getUsername());
                } else {
                    bld.setUid("_W_R_O_N_G_");
                    bld.setName("_W_R_O_N_G_");
                }
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getCommentAttribute(),
                        CollectionUtil.newSet(passwdRow.getComment())));
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getShellAttribute(),
                        CollectionUtil.newSet(passwdRow.getShell())));
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getHomeDirectoryAttribute(),
                        CollectionUtil.newSet(passwdRow.getHomeDirectory())));
                bld.addAttribute(OperationalAttributes.ENABLE_NAME,
                        EvaluateCommandsResultOutput.evaluateUserStatus(
                        unixConnection.userStatus(nameToSearch)));
                handler.handle(bld.build());
            }
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            if (StringUtil.isNotBlank(nameToSearch)
                    && StringUtil.isNotEmpty(nameToSearch)
                    && EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
                    unixConnection.groupExists(nameToSearch))) {
                bld.setName(nameToSearch);
                bld.setUid(nameToSearch);
                handler.handle(bld.build());
            }
        }
    }

    public void startsWithSearch() throws IOException,
            InvalidStateException, InterruptedException {
        String startWithValue = filter.getAttributeValue();
        ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            PasswdFile passwdFile =
                    new PasswdFile(unixConnection.searchAllUser());
            List<PasswdRow> passwdRows =
                    passwdFile.searchRowByStartsWithValue(startWithValue);
            if (passwdRows == null || passwdRows.isEmpty()) {
                throw new ConnectException("No results found");
            }
            for (Iterator<PasswdRow> it =
                    passwdRows.iterator(); it.hasNext();) {
                PasswdRow passwdRow = it.next();
                if (StringUtil.isNotEmpty(passwdRow.getUsername())
                        && StringUtil.isNotBlank(passwdRow.getUsername())) {
                    bld.setName(passwdRow.getUsername());
                    bld.setUid(passwdRow.getUsername());
                } else {
                    bld.setUid("_W_R_O_N_G_");
                    bld.setName("_W_R_O_N_G_");
                }
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getCommentAttribute(),
                        CollectionUtil.newSet(passwdRow.getComment())));
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getShellAttribute(),
                        CollectionUtil.newSet(passwdRow.getShell())));
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getHomeDirectoryAttribute(),
                        CollectionUtil.newSet(passwdRow.getHomeDirectory())));
                bld.addAttribute(OperationalAttributes.ENABLE_NAME,
                        EvaluateCommandsResultOutput.evaluateUserStatus(
                        unixConnection.userStatus(startWithValue)));
                handler.handle(bld.build());
            }
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            if (StringUtil.isNotBlank(startWithValue)
                    && StringUtil.isNotEmpty(startWithValue)
                    && EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
                    unixConnection.groupExists(startWithValue))) {
                bld.setName(startWithValue);
                bld.setUid(startWithValue);
                handler.handle(bld.build());
            }
        }
    }
    
    public void endsWithSearch() throws IOException,
            InvalidStateException, InterruptedException {
        String startWithValue = filter.getAttributeValue();
        ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            PasswdFile passwdFile =
                    new PasswdFile(unixConnection.searchAllUser());
            List<PasswdRow> passwdRows =
                    passwdFile.searchRowByEndsWithValue(startWithValue);
            if (passwdRows == null || passwdRows.isEmpty()) {
                throw new ConnectException("No results found");
            }
            for (Iterator<PasswdRow> it =
                    passwdRows.iterator(); it.hasNext();) {
                PasswdRow passwdRow = it.next();
                if (StringUtil.isNotEmpty(passwdRow.getUsername())
                        && StringUtil.isNotBlank(passwdRow.getUsername())) {
                    bld.setName(passwdRow.getUsername());
                    bld.setUid(passwdRow.getUsername());
                } else {
                    bld.setUid("_W_R_O_N_G_");
                    bld.setName("_W_R_O_N_G_");
                }
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getCommentAttribute(),
                        CollectionUtil.newSet(passwdRow.getComment())));
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getShellAttribute(),
                        CollectionUtil.newSet(passwdRow.getShell())));
                bld.addAttribute(AttributeBuilder.build(
                        unixConfiguration.getHomeDirectoryAttribute(),
                        CollectionUtil.newSet(passwdRow.getHomeDirectory())));
                bld.addAttribute(OperationalAttributes.ENABLE_NAME,
                        EvaluateCommandsResultOutput.evaluateUserStatus(
                        unixConnection.userStatus(startWithValue)));
                handler.handle(bld.build());
            }
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            if (StringUtil.isNotBlank(startWithValue)
                    && StringUtil.isNotEmpty(startWithValue)
                    && EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
                    unixConnection.groupExists(startWithValue))) {
                bld.setName(startWithValue);
                bld.setUid(startWithValue);
                handler.handle(bld.build());
            }
        }
    }
//    public ConnectorObject orSearch()
//            throws IOException, InvalidStateException, InterruptedException {
//        String firstAttribute = filter.getFirstOperand().getAttributeValue();
//        String secondAttribute = filter.getSecondOperand().getAttributeValue();
//        ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
//        if (objectClass.equals(ObjectClass.ACCOUNT)) {
//            PasswdFile passwdFile =
//                    new PasswdFile(unixConnection.searchAllUser());
//            List<PasswdRow> passwdRows =
//                    passwdFile.orSearchRowByAttribute(firstAttribute,
//                    secondAttribute);
//            if (passwdRows == null || passwdRows.isEmpty()) {
//                throw new ConnectException("No results found");
//            }
//            for (Iterator<PasswdRow> it =
//                    passwdRows.iterator(); it.hasNext();) {
//                PasswdRow passwdRow = it.next();
//                if (StringUtil.isNotEmpty(passwdRow.getUsername())
//                        && StringUtil.isNotBlank(passwdRow.getUsername())) {
//                    bld.setName(passwdRow.getUsername());
//                    bld.setUid(passwdRow.getUsername());
//                } else {
//                    bld.setUid("_W_R_O_N_G_");
//                    bld.setName("_W_R_O_N_G_");
//                }
//                bld.addAttribute(AttributeBuilder.build(
//                        unixConfiguration.getCommentAttribute(),
//                        CollectionUtil.newSet(passwdRow.getComment())));
//                bld.addAttribute(AttributeBuilder.build(
//                        unixConfiguration.getShellAttribute(),
//                        CollectionUtil.newSet(passwdRow.getShell())));
//                bld.addAttribute(AttributeBuilder.build(
//                        unixConfiguration.getHomeDirectoryAttribute(),
//                        CollectionUtil.newSet(passwdRow.getHomeDirectory())));
//                bld.addAttribute(OperationalAttributes.ENABLE_NAME,
//                        EvaluateCommandsResultOutput.evaluateUserStatus(
//                        unixConnection.userStatus(nameToSearch)));
//            }
//        } else if (objectClass.equals(ObjectClass.GROUP)) {
//            if (StringUtil.isNotBlank(nameToSearch)
//                    && StringUtil.isNotEmpty(nameToSearch)
//                    && EvaluateCommandsResultOutput.evaluateUserOrGroupExists(
//                    unixConnection.groupExists(nameToSearch))) {
//                bld.setName(nameToSearch);
//                bld.setUid(nameToSearch);
//            }
//        }
//        return bld.build();
//    }
}