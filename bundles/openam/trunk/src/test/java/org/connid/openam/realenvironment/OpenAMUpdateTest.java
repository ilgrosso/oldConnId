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
import java.util.Set;
import org.connid.openam.OpenAMConnector;
import org.connid.openam.utilities.SharedMethodsForTests;
import org.connid.openam.utilities.TestAccountsValue;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.junit.Assert;
import org.junit.Test;

public class OpenAMUpdateTest extends SharedMethodsForTests {

    @Test
    public final void updateTest() {
        final OpenAMConnector connector = new OpenAMConnector();
        connector.init(createConfiguration());
        Name name = new Name("createtest" + randomNumber());
        Uid newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        connector.update(ObjectClass.ACCOUNT, newAccount,
                setOfAttributes(name), null);
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        Assert.assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test(expected = ConnectorException.class)
    public final void updateTestOfNotExistsUser() {
        final OpenAMConnector connector = new OpenAMConnector();
        connector.init(createConfiguration());
        connector.update(ObjectClass.ACCOUNT, new Uid("notexists"),
                null, null);
        connector.dispose();
    }

    private Set<Attribute> setOfAttributes(final Name name) {
        Set<Attribute> attributes = new HashSet<Attribute>();
        attributes.add(name);
        attributes.add(AttributeBuilder.build(TestAccountsValue.CN, "newcn"));
        return attributes;
    }
}
