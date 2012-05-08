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
package org.connid.unix;

import java.util.Set;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.*;

@ConnectorClass(configurationClass = UnixConfiguration.class,
displayNameKey = "unix.connector.display")
public class UnixConnector implements Connector, CreateOp, UpdateOp,
        DeleteOp, TestOp, SearchOp<String>, AuthenticateOp {

    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init(Configuration c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Uid create(ObjectClass oc, Set<Attribute> set, OperationOptions oo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Uid update(ObjectClass oc, Uid uid, Set<Attribute> set, OperationOptions oo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(ObjectClass oc, Uid uid, OperationOptions oo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void test() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterTranslator<String> createFilterTranslator(ObjectClass oc, OperationOptions oo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void executeQuery(ObjectClass oc, String t, ResultsHandler rh, OperationOptions oo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Uid authenticate(ObjectClass oc, String string, GuardedString gs, OperationOptions oo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
