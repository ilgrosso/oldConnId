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

import junit.framework.Assert;
import org.connid.openam.OpenAMConnector;
import org.connid.openam.utilities.SharedMethodsForTests;
import org.connid.openam.utilities.TestAccountsValue;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class OpenAMCreateTest extends SharedMethodsForTests {

    private OpenAMConnector connector = null;
    private Name name = null;
    private Uid newAccount = null;
    private TestAccountsValue attrs = null;

    @Before
    public final void initTest() {
        attrs = new TestAccountsValue();
        connector = new OpenAMConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());
    }

    @Test
    public final void createUser() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test
    public final void createExistsUser() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        boolean userExists = false;
        try {
            connector.create(ObjectClass.ACCOUNT,
                    createSetOfAttributes(new Name(newAccount.getUidValue()),
                    attrs.getPassword(), true),
                    null);
        } catch (Exception e) {
            userExists = true;
        }
        Assert.assertTrue(userExists);
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Ignore //It doesn't delete user in real env..
    @Test(expected = ConnectorException.class)
    public final void createLockedUser() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), false), null);
        connector.authenticate(ObjectClass.ACCOUNT, newAccount.getUidValue(),
                attrs.getGuardedPassword(), null);
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @Test
    public final void createUnLockedUser() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertNotNull(connector.authenticate(
                ObjectClass.ACCOUNT, newAccount.getUidValue(),
                attrs.getGuardedPassword(), null));
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
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
}
