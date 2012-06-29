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
package org.connid.openam.realenvironment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.connid.openam.OpenAMConnector;
import org.connid.openam.utilities.SharedMethodsForTests;
import org.connid.openam.utilities.TestAccountsValue;
import org.identityconnectors.framework.common.objects.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenAMExecuteQueryTest extends SharedMethodsForTests {

    private OpenAMConnector connector = null;
    private Name name = null;
    private Uid newAccount = null;
    private TestAccountsValue attrs = null;
    private final static boolean ACTIVE_USER = true;
    private final static boolean INACTIVE_USER = false;

    @Before
    public final void initTest() {
        attrs = new TestAccountsValue();
        connector = new OpenAMConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());
    }

    @Test
    public final void executeQueryOnlyUidTest() {
        final Set actual = new HashSet();
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(),
                ACTIVE_USER), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());

        connector.executeQuery(ObjectClass.ACCOUNT,
                "(" + Uid.NAME + "=" + newAccount.getUidValue() + ")",
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
        Assert.assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test
    public final void executeQueryWithAndFilterTest() {
        final Set actual = new HashSet();
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(),
                ACTIVE_USER), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());

        connector.executeQuery(ObjectClass.ACCOUNT,
                "(&(cn=" + name.getNameValue() + ")(sn=" + name.getNameValue()
                + ")", new ResultsHandler() {

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
        Assert.assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test
    public final void executeQueryWithOrFilterTest() {
        final Set actual = new HashSet();
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(),
                ACTIVE_USER), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());

        connector.executeQuery(ObjectClass.ACCOUNT,
                "(|(cn=" + name.getNameValue() + ")(sn=" + name.getNameValue()
                + ")", new ResultsHandler() {

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
        Assert.assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test
    public final void executeQueryNotExistingUserTest() {
        final Set actual = new HashSet();
        connector.executeQuery(ObjectClass.ACCOUNT,
                "(" + Uid.NAME + "=notexistsuser)", new ResultsHandler() {

            @Override
            public boolean handle(final ConnectorObject co) {
                actual.add(co);
                return true;
            }
        }, null);
        Assert.assertEquals(0, actual.size());
        connector.dispose();
    }
}
