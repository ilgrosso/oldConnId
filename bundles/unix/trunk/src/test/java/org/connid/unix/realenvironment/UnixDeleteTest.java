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

import org.connid.unix.UnixConnector;
import org.connid.unix.utilities.AttributesTestValue;
import org.connid.unix.utilities.SharedTestMethods;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnixDeleteTest extends SharedTestMethods {

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

    @Test(expected = ConnectorException.class)
    public final void deleteNotExistsUser() {
        connector.delete(ObjectClass.ACCOUNT,
                new Uid(attrs.getWrongUsername()), null);
    }

    @Test(expected = ConnectorException.class)
    public final void deleteNullUser() {
        connector.delete(ObjectClass.ACCOUNT, null, null);
    }
    
    @Test(expected = ConnectorException.class)
    public final void deleteNull() {
        connector.delete(null, null, null);
    }

    @Test(expected = ConnectorException.class)
    public final void deleteWithWrongObjectClass() {
        connector.delete(attrs.getWrongObjectClass(),
                newAccount, null);
    }

    @After
    public final void close() {
        connector.dispose();
    }
}
