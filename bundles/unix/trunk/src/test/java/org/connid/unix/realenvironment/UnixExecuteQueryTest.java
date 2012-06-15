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
package org.connid.unix.realenvironment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.connid.unix.UnixConnector;
import org.connid.unix.search.Operand;
import org.connid.unix.search.Operator;
import org.connid.unix.utilities.AttributesTestValue;
import org.connid.unix.utilities.SharedTestMethods;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnixExecuteQueryTest extends SharedTestMethods {

    private UnixConnector connector = null;
    private Name name = null;
    private Uid newAccount = null;
    private AttributesTestValue attrs = null;

    @Before
    public final void initTest() {
        attrs = new AttributesTestValue();
        connector = new UnixConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());
    }

    @Test
    public final void searchUser() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(
                Operator.EQ, Uid.NAME, newAccount.getUidValue(), false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        for (Iterator it = actual.iterator(); it.hasNext();) {
            Object object = it.next();
            ConnectorObject co = (ConnectorObject) object;
            Assert.assertEquals(name.getNameValue(),
                    co.getName().getNameValue());
        }
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test
    public final void searchStartsWithAttribute() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(Operator.SW, Uid.NAME, "crea", false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        Assert.assertEquals(1, actual.size());
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test
    public final void searchEndsWithAttribute() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(Operator.EW, Uid.NAME,
                newAccount.getUidValue().substring(
                newAccount.getUidValue().length() - 3,
                newAccount.getUidValue().length()), false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        Assert.assertEquals(1, actual.size());
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test
    public final void searchContainsAttribute() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(Operator.C, Uid.NAME, "test", false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        Assert.assertEquals(1, actual.size());
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test
    public final void searchNotEqualsAttribute() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(Operator.EQ, Uid.NAME, "test", true),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        Assert.assertTrue(actual.size() > 1);
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test(expected = ConnectorException.class)
    public final void searchNotExistsUser() {
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(
                Operator.EQ, Uid.NAME, attrs.getWrongUsername(), false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
    }

    @Test
    public final void searchUserWithSameShell() {
        Name name1 = new Name(attrs.getUsername());
        Uid newAccount1 = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name1, attrs.getPassword(), true), null);
        Name name2 = new Name(attrs.getUsername());
        Uid newAccount2 = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name2, attrs.getPassword(), true), null);
        Name name3 = new Name(attrs.getUsername());
        Uid newAccount3 = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name3, attrs.getPassword(), true), null);
        Name name4 = new Name(attrs.getUsername());
        Uid newAccount4 = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name4, attrs.getPassword(), true), null);
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(
                Operator.EQ, "shell", "/bin/csh", false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        Assert.assertEquals(4, actual.size());
        connector.delete(ObjectClass.ACCOUNT, newAccount1, null);
        connector.delete(ObjectClass.ACCOUNT, newAccount2, null);
        connector.delete(ObjectClass.ACCOUNT, newAccount3, null);
        connector.delete(ObjectClass.ACCOUNT, newAccount4, null);
    }

    @Test
    public final void searchGroup() {
        newAccount = connector.create(ObjectClass.GROUP,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.GROUP,
                new Operand(
                Operator.EQ, Uid.NAME, newAccount.getUidValue(), false),
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
        for (Iterator it = actual.iterator(); it.hasNext();) {
            Object object = it.next();
            ConnectorObject co = (ConnectorObject) object;
            Assert.assertEquals(name.getNameValue(),
                    co.getName().getNameValue());
        }
        connector.delete(ObjectClass.GROUP, newAccount, null);
    }

    @Test(expected = ConnectorException.class)
    public void executeQueryWithWrongObjectClass() {
        final Set actual = new HashSet();
        connector.executeQuery(attrs.getWrongObjectClass(), null,
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject co) {
                        actual.add(co);
                        return true;
                    }
                }, null);
    }

    @Test(expected = ConnectorException.class)
    public void executeQueryTestWithAllNull() {
        connector.executeQuery(null, null, null, null);
    }

    @After
    public final void close() {
        connector.dispose();
    }
}
