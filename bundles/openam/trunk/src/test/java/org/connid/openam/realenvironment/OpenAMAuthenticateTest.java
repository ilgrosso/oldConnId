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

import org.connid.openam.OpenAMConnector;
import org.connid.openam.utilities.SharedMethodsForTests;
import org.connid.openam.utilities.TestAccountsValue;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenAMAuthenticateTest extends SharedMethodsForTests {

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
    public final void authenticateTest() {
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        Assert.assertNotNull(connector.authenticate(
                ObjectClass.ACCOUNT, newAccount.getUidValue(),
                attrs.getGuardedPassword(), null));
    }

    @Test(expected = ConnectorException.class)
    public final void authTestWithWrongPassword() {
        connector.authenticate(ObjectClass.ACCOUNT, name.getNameValue(),
                attrs.getWrongGuardedPassword(), null);
    }

    @Test(expected = ConnectorException.class)
    public final void authTestWithWrongUsername() {
        connector.authenticate(ObjectClass.ACCOUNT,
                attrs.getWrongUsername(), attrs.getGuardedPassword(), null);
    }

    @Test(expected = ConnectorException.class)
    public void authTestWithWrongObjectClass() {
        connector.authenticate(attrs.getWrongObjectClass(),
                attrs.getUsername(), attrs.getGuardedPassword(), null);
    }

    @Test(expected = ConnectorException.class)
    public final void authTestWithNullPassword() {
        connector.authenticate(ObjectClass.ACCOUNT, name.getNameValue(),
                null, null);
    }

    @Test(expected = ConnectorException.class)
    public final void authTestWithNullUsername() {
        connector.authenticate(ObjectClass.ACCOUNT, null,
                attrs.getWrongGuardedPassword(), null);
    }

    @Test(expected = ConnectorException.class)
    public final void authTestWithAllNull() {
        connector.authenticate(null, null, null, null);
    }

    @Test(expected = ConnectorException.class)
    public final void authFailedTest() {
        connector.authenticate(ObjectClass.ACCOUNT, attrs.getWrongUsername(),
                attrs.getWrongGuardedPassword(), null);
    }

    @After
    public final void close() {
        if (newAccount != null) {
            connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        }
        connector.dispose();
    }
}
