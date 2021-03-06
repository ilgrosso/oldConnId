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
 * http://connid.googlecode.com/svn/trunk/legal/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at connid/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.bundles.soap.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.identityconnectors.common.IOUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConfigurationProperty;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.api.ConnectorKey;
import org.identityconnectors.framework.api.operations.APIOperation;
import org.identityconnectors.framework.api.operations.CreateApiOp;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.connid.bundles.soap.WebServiceConnector;
import org.connid.bundles.soap.provisioning.interfaces.Provisioning;

public class SOAPTestITCase {

    private static final Logger LOG = LoggerFactory.getLogger(SOAPTestITCase.class);

    final private String ENDPOINT_PREFIX = "http://localhost:8888/wssample/services";

    final private String SERVICE = "/provisioning";

    final private String bundlename = "org.connid.bundles.soap";

    private String bundleversion = null;

    final private String bundleclass = WebServiceConnector.class.getName();

    private String bundledirectory;

    private ConnectorFacade connector;

    /**
     * Uses the ConnectorInfoManager to retrieve a ConnectorInfo object for the connector.
     */
    @Before
    public void init() {
        Properties props = new java.util.Properties();
        try {
            InputStream propStream = getClass().getResourceAsStream("/bundle.properties");
            props.load(propStream);
            bundleversion = props.getProperty("bundleversion");
            bundledirectory = props.getProperty("bundledirectory");
        } catch (Throwable t) {
            LOG.error("Could not load bundles.properties", t);
        }
        assertNotNull(bundleversion);
        assertNotNull(bundledirectory);

        File bundleDirectory = new File(bundledirectory);
        List<URL> urls = new ArrayList<URL>();
        for (String file : bundleDirectory.list()) {
            try {
                urls.add(IOUtil.makeURL(bundleDirectory, file));
            } catch (Exception ignore) {
                // ignore exception and don't add bundle
                LOG.debug("\"" + bundleDirectory.toString() + "/" + file + "\""
                        + " is not a valid connector bundle.", ignore);
            }
        }
        assertFalse(urls.isEmpty());
        LOG.debug("URL: " + urls.toString());

        ConnectorInfoManagerFactory connectorInfoManagerFactory = ConnectorInfoManagerFactory.getInstance();
        ConnectorInfoManager manager = connectorInfoManagerFactory.getLocalManager(urls.toArray(new URL[0]));
        assertNotNull(manager);

        // list connectors info
        List<ConnectorInfo> infos = manager.getConnectorInfos();
        assertNotNull(infos);
        LOG.debug("infos size: " + infos.size());

        for (ConnectorInfo i : infos) {
            LOG.debug("Name: " + i.getConnectorDisplayName());
        }

        LOG.debug("\nBundle name: " + bundlename
                + "\nBundle version: " + bundleversion
                + "\nBundle class: " + bundleclass);

        // specify a connector.
        ConnectorKey key = new ConnectorKey(bundlename, bundleversion, bundleclass);
        assertNotNull(key);

        // get the specified connector.
        ConnectorInfo info = manager.findConnectorInfo(key);

        assertNotNull(info);

        // create default configuration
        APIConfiguration apiConfig = info.createDefaultAPIConfiguration();
        assertNotNull(apiConfig);

        // retrieve the ConfigurationProperties.
        ConfigurationProperties properties = apiConfig.getConfigurationProperties();
        assertNotNull(properties);

        // Print out what the properties are (not necessary)
        for (String propName : properties.getPropertyNames()) {
            ConfigurationProperty prop = properties.getProperty(propName);

            LOG.debug("\nProperty Name: " + prop.getName()
                    + "\nProperty Type: " + prop.getType());
        }

        // Set all of the ConfigurationProperties needed by the connector.
        properties.setPropertyValue("endpoint", ENDPOINT_PREFIX + SERVICE);
        properties.setPropertyValue("servicename", Provisioning.class.getName());

        // Use the ConnectorFacadeFactory's newInstance() method to get
        // a new connector.
        connector = ConnectorFacadeFactory.getInstance().newInstance(apiConfig);
        assertNotNull(connector);

        // Make sure we have set up the Configuration properly
        Throwable t = null;
        try {
            connector.validate();
            connector.test();
        } catch (RuntimeException re) {
            t = re;
        }
        assertNull(t);
    }

    /**
     * Checks if a particular operation is supported.
     */
    @Test
    public void checkForOperation() {
        Set<Class<? extends APIOperation>> ops =
                connector.getSupportedOperations();

        // check to see if the set contains the operation you care about
        assertTrue(ops.contains(CreateApiOp.class));
    }

    /**
     * Gets schema from the target resource.
     */
    @Test
    public void schema() {
        Schema schema = connector.schema();
        assertNotNull(schema);

        Set<ObjectClassInfo> ocis = schema.getObjectClassInfo();
        assertNotNull(ocis);

        for (ObjectClassInfo oci : ocis) {
            Set<AttributeInfo> attrs = oci.getAttributeInfo();
            assertNotNull(attrs);

            for (AttributeInfo attr : attrs) {
                LOG.debug("\nAttribute name: " + attr.getName()
                        + "\nAttribute type: " + attr.getType().getName());
            }
        }
    }

    /**
     * Seraches for user accounts.
     */
    @Test
    public void search() {
        final List<ConnectorObject> results = new ArrayList<ConnectorObject>();

        ResultsHandler resultsHandler = new ResultsHandler() {

            @Override
            public boolean handle(ConnectorObject obj) {
                LOG.debug("Add record {}", obj);

                results.add(obj);
                return true;
            }
        };

        Filter usernameFilter = FilterBuilder.startsWith(
                AttributeBuilder.build("USERID", "test"));

        Filter nameFilter = FilterBuilder.equalTo(
                AttributeBuilder.build("NAME", "jhon"));

        Filter surnameFilter = FilterBuilder.equalTo(
                AttributeBuilder.build("SURNAME", "doe"));

        Filter filter = FilterBuilder.or(
                usernameFilter,
                FilterBuilder.and(nameFilter, surnameFilter));

        connector.search(ObjectClass.ACCOUNT, filter, resultsHandler, null);

        /**
         * Pay attention: results will be returned according to the filter above.
         */
        assertFalse(results.isEmpty());

        if (LOG.isDebugEnabled()) {
            for (ConnectorObject obj : results) {
                LOG.debug("\nName: " + obj.getName()
                        + "\nUID: " + obj.getUid());
            }
        }
    }

    /**
     * Creates user account.
     */
    @Test
    public void create() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(new Name("TESTUSER"));

        attrs.add(AttributeBuilder.buildPassword("TESTPASSWORD".toCharArray()));

        attrs.add(AttributeBuilder.build("name", "John"));
        attrs.add(AttributeBuilder.build("surname", "Doe"));
        attrs.add(AttributeBuilder.build("fullname", "John Doe"));
        attrs.add(AttributeBuilder.build("type", "person"));
        attrs.add(AttributeBuilder.build("birthdate", "12/03/1990"));

        Uid userUid = connector.create(ObjectClass.ACCOUNT, attrs, null);

        assertNotNull(userUid);
        assertEquals("TESTUSER", userUid.getUidValue());
    }

    /**
     * Updates user account.
     */
    @Test
    public void update() {
        Set attrs = new HashSet();
        attrs.add(new Name("TESTUSER"));

        attrs.add(AttributeBuilder.buildPassword("NEWPASSWORD".toCharArray()));

        Uid userUid = connector.update(
                ObjectClass.ACCOUNT, new Uid("TESTUSER"), attrs, null);

        assertNotNull(userUid);
        assertEquals("TESTUSER", userUid.getUidValue());
    }

    /**
     * Deletes user account.
     */
    @Test
    public void delete() {
        Uid userUid = connector.authenticate(
                ObjectClass.ACCOUNT, "TESTUSER", new GuardedString("TESTPASSWORD".toCharArray()), null);
        assertNotNull(userUid);

        connector.delete(ObjectClass.ACCOUNT, userUid, null);
    }
}
