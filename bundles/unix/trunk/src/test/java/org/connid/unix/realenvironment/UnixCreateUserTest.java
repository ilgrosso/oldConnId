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

import java.util.Set;
import org.connid.unix.UnixConnector;
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
                createSetOfAttributes(name, attrs.getPassword()), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        try {
            connector.create(ObjectClass.ACCOUNT,
                    createSetOfAttributes(name, attrs.getPassword()), null);
        } catch (Exception e) {
            userExists = true;
        }
        Assert.assertTrue(userExists);
    }

    @Test(expected = ConnectorException.class)
    public final void createLockedUser() {
        Set<Attribute> attributes =
                createSetOfAttributes(name, attrs.getPassword());
        attributes.add(AttributeBuilder.buildEnabled(false));
        newAccount = connector.create(ObjectClass.ACCOUNT, attributes, null);
        connector.authenticate(ObjectClass.ACCOUNT, attrs.getUsername(),
                attrs.getGuardedPassword(), null);
    }

    @Test
    public final void createUnLockedUser() {
        Set<Attribute> attributes =
                createSetOfAttributes(name, attrs.getPassword());
        attributes.add(AttributeBuilder.buildEnabled(true));
        newAccount = connector.create(ObjectClass.ACCOUNT, attributes, null);
        connector.authenticate(ObjectClass.ACCOUNT, attrs.getUsername(),
                attrs.getGuardedPassword(), null);
    }

    @Test(expected = ConnectorException.class)
    public void createWithWrongObjectClass() {
        connector.create(attrs.getWrongObjectClass(),
                createSetOfAttributes(name, attrs.getPassword()), null);
    }

    @Test(expected = ConnectorException.class)
    public void createTestWithNull() {
        connector.create(attrs.getWrongObjectClass(), null, null);
    }

    @Test(expected = ConnectorException.class)
    public void createTestWithNameNull() {
        connector.create(attrs.getWrongObjectClass(),
                createSetOfAttributes(null, attrs.getPassword()), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTestWithPasswordNull() {
        connector.create(attrs.getWrongObjectClass(),
                createSetOfAttributes(name, null), null);
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
