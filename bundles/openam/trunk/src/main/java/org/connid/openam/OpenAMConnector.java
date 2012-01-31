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
package org.connid.openam;

import java.util.Set;
import org.connid.openam.methods.OpenAMAuthenticate;
import org.connid.openam.methods.OpenAMCreate;
import org.connid.openam.methods.OpenAMDelete;
import org.connid.openam.methods.OpenAMExecuteQuery;
import org.connid.openam.methods.OpenAMSearch;
import org.connid.openam.methods.OpenAMTest;
import org.connid.openam.methods.OpenAMUpdate;
import org.connid.openam.utilities.SelfSignedCertUtilities;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.AuthenticateOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

@ConnectorClass(configurationClass = OpenAMConfiguration.class,
displayNameKey = "openam.connector.display")
public class OpenAMConnector implements Connector, CreateOp, UpdateOp,
        DeleteOp, TestOp, SearchOp<String>, AuthenticateOp {

    private static final Log LOG = Log.getLog(OpenAMConnector.class);
    private OpenAMConfiguration openAMConfiguration;

    @Override
    public final Configuration getConfiguration() {
        return openAMConfiguration;
    }

    @Override
    public final void init(final Configuration config) {
        openAMConfiguration = (OpenAMConfiguration) config;
        if (openAMConfiguration.isSsl()) {
            SelfSignedCertUtilities.trustSelfSignedSSL();
        }
    }

    @Override
    public void dispose() {
        //no action
    }

    @Override
    public final Uid create(final ObjectClass oc, final Set<Attribute> set,
            final OperationOptions oo) {
        return new OpenAMCreate(openAMConfiguration, set).create();
    }

    @Override
    public final Uid update(final ObjectClass oc, final Uid uid,
            final Set<Attribute> set, final OperationOptions oo) {
        return new OpenAMUpdate(openAMConfiguration, uid, set).update();
    }

    @Override
    public final void delete(final ObjectClass oc, final Uid uid,
            final OperationOptions oo) {
        new OpenAMDelete(openAMConfiguration, uid).delete();
    }

    public final boolean existsUser(final String uid) {
        return new OpenAMSearch(openAMConfiguration, uid).existsUser();
    }

    @Override
    public final void test() {
        LOG.info("Connection test");
        new OpenAMTest(openAMConfiguration).test();
    }

    @Override
    public final FilterTranslator createFilterTranslator(final ObjectClass oc,
            final OperationOptions oo) {
        if (oc == null || (!oc.equals(ObjectClass.ACCOUNT))) {
            throw new IllegalArgumentException("Invalid objectclass");
        }
        return new OpenAMFilterTranslator();
    }

    @Override
    public final void executeQuery(final ObjectClass oc, final String filter,
            final ResultsHandler rh, final OperationOptions oo) {
        new OpenAMExecuteQuery(openAMConfiguration, filter, rh).executeQuery();
    }

    @Override
    public final Uid authenticate(final ObjectClass oc, final String username,
            final GuardedString password, final OperationOptions oo) {
        return new OpenAMAuthenticate(
                openAMConfiguration, username, password).authenticate();
    }
}
