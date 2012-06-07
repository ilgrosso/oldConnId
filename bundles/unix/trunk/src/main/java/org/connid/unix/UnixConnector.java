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

import java.io.IOException;
import java.util.Set;
import org.connid.unix.methods.*;
import org.identityconnectors.common.logging.Log;
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

    private static final Log LOG = Log.getLog(UnixConnector.class);
    private UnixConfiguration unixConfiguration;

    @Override
    public final Configuration getConfiguration() {
        return unixConfiguration;
    }

    @Override
    public final void init(final Configuration configuration) {
        unixConfiguration = (UnixConfiguration) configuration;
    }

    @Override
    public final void dispose() {
        //no action
    }

    @Override
    public final void test() {
        LOG.info("Remote connection test");
        try {
            new UnixTest(unixConfiguration).test();
        } catch (IOException ex) {
            LOG.error("Error in connection process", ex);
        }
    }

    @Override
    public final Uid create(final ObjectClass oc, final Set<Attribute> set,
            final OperationOptions oo) {
        LOG.info("Create new user");
        Uid uidResult = null;
        try {
            uidResult = new UnixCreate(oc, unixConfiguration, set).create();
        } catch (IOException ex) {
            LOG.error("Error in connection process", ex);
        }
        return uidResult;
    }

    @Override
    public final void delete(final ObjectClass oc, final Uid uid,
            final OperationOptions oo) {
        try {
            new UnixDelete(oc, unixConfiguration, uid).delete();
        } catch (IOException ex) {
            LOG.error("Error in connection process", ex);
        }
    }

    @Override
    public final Uid authenticate(final ObjectClass oc, final String username,
            final GuardedString gs, final OperationOptions oo) {
        Uid uidResult = null;
        try {
            LOG.info("Authenticate user: " + username);
            uidResult = new UnixAuthenticate(oc,
                    unixConfiguration, username, gs).authenticate();
        } catch (IOException ex) {
            LOG.error("Error in connection process", ex);
        }
        return uidResult;
    }

    @Override
    public final Uid update(final ObjectClass oc, final Uid uid,
            final Set<Attribute> set, final OperationOptions oo) {
        try {
            new UnixUpdate(oc, unixConfiguration, uid, set).update();
        } catch (IOException ex) {
            LOG.error("Error in connection process", ex);
        }
        return uid;
    }

    @Override
    public final void executeQuery(final ObjectClass oc, final String filter,
            final ResultsHandler rh, final OperationOptions oo) {
        LOG.info("Execute query");
        try {
            new UnixExecuteQuery(
                    unixConfiguration, oc, filter, rh).executeQuery();
        } catch (IOException ex) {
            LOG.error("Error in connection process", ex);
        }
    }

    @Override
    public final FilterTranslator<String> createFilterTranslator(
            final ObjectClass oc, final OperationOptions oo) {
        if (oc == null || (!oc.equals(ObjectClass.ACCOUNT))
                && (!oc.equals(ObjectClass.GROUP))) {
            throw new IllegalArgumentException("Invalid objectclass");
        }
        return new UnixFilterTranslator();
    }
}
