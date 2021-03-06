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
 * https://connid.googlecode.com/svn/base/trunk/legal/license.txt
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
package org.connid.bundles.csvdir;

import java.io.IOException;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.test.common.TestHelpers;
import org.junit.Assert;
import org.junit.Test;

public class CSVDirConnectorSchemaTests extends AbstractTest {

    @Test
    public final void schemaTest()
            throws IOException {

        final ConnectorFacadeFactory factory =
                ConnectorFacadeFactory.getInstance();

        final APIConfiguration impl = TestHelpers.createTestConfiguration(
                CSVDirConnector.class, createConfiguration(".*\\.csv"));

        final ConnectorFacade facade = factory.newInstance(impl);
        final Schema schema = facade.schema();

        Assert.assertNotNull(schema);
        Assert.assertFalse(schema.getObjectClassInfo().isEmpty());
    }
}
