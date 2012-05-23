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
import org.connid.unix.utilities.SharedTestMethods;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.Assert;
import org.junit.Test;

public class UnixUpdateTest extends SharedTestMethods {

    @Test
    public final void createAndDeleteTest() {
        final UnixConnector connector = new UnixConnector();
        connector.init(createConfiguration());
        Name name = new Name("createtest" + randomNumber());
        Uid newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, "password"), null);
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        connector.update(ObjectClass.ACCOUNT,
                newAccount, createSetOfAttributes(name, "password2"), null);
        connector.authenticate(ObjectClass.ACCOUNT, name.getNameValue(),
                new GuardedString("password2".toCharArray()), null);
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        connector.dispose();
    }
}
