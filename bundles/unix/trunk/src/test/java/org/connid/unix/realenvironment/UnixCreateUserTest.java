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

public class UnixCreateUserTest extends SharedTestMethods {

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
    public final void createExistsUser() {
        boolean userExists = false;
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        try {
            connector.create(ObjectClass.ACCOUNT,
                    createSetOfAttributes(name, attrs.getPassword(), true),
                    null);
        } catch (Exception e) {
            userExists = true;
        }
        Assert.assertTrue(userExists);
    }

    @Test(expected = ConnectorException.class)
    public final void createLockedUser() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), false), null);
        connector.authenticate(ObjectClass.ACCOUNT, attrs.getUsername(),
                attrs.getGuardedPassword(), null);
    }

    @Test
    public final void createUnLockedUser() {
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
        connector.authenticate(ObjectClass.ACCOUNT, newAccount.getUidValue(),
                attrs.getGuardedPassword(), null);
    }

    @Test(expected = ConnectorException.class)
    public void createWithWrongObjectClass() {
        connector.create(attrs.getWrongObjectClass(),
                createSetOfAttributes(name, attrs.getPassword(), true), null);
    }

    @Test(expected = ConnectorException.class)
    public void createTestWithNull() {
        connector.create(attrs.getWrongObjectClass(), null, null);
    }

    @Test(expected = ConnectorException.class)
    public void createTestWithNameNull() {
        connector.create(attrs.getWrongObjectClass(),
                createSetOfAttributes(null, attrs.getPassword(), true), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTestWithPasswordNull() {
        connector.create(attrs.getWrongObjectClass(),
                createSetOfAttributes(name, null, true), null);
    }

    @Test(expected = ConnectorException.class)
    public void createTestWithAllNull() {
        connector.create(null, null, null);
    }

    @After
    public final void close() {
        if (newAccount != null) {
            connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        }
        connector.dispose();
    }
}
