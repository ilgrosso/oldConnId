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
package org.connid.bundles.soap;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.connid.bundles.soap.cxf.ForceSoapActionOutInterceptor;
import org.connid.bundles.soap.provisioning.interfaces.Provisioning;
import org.identityconnectors.common.logging.Log;

public class WebServiceConnection {

    /**
     * Logger definition.
     */
    private static final Log LOG = Log.getLog(WebServiceConnection.class);

    private final String SUCCESS = "OK";

    private Provisioning provisioning;

    public WebServiceConnection(final WebServiceConfiguration configuration) {
        final JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
        try {
            configuration.validate();

            proxyFactory.setAddress(configuration.getEndpoint());
            proxyFactory.setServiceClass(Class.forName(configuration.getServicename()));

            provisioning = (Provisioning) proxyFactory.create();

            final Client client = ClientProxy.getClient(provisioning);
            if (client != null) {
                final HTTPConduit conduit = (HTTPConduit) client.getConduit();
                final HTTPClientPolicy policy = conduit.getClient();
                policy.setConnectionTimeout(Long.parseLong(configuration.getConnectionTimeout()) * 1000L);
                policy.setReceiveTimeout(Long.parseLong(configuration.getReceiveTimeout()) * 1000L);

                client.getOutInterceptors().add(
                        new ForceSoapActionOutInterceptor(configuration.getSoapActionUriPrefix()));
            }
        } catch (IllegalArgumentException e) {
            LOG.error(e, "Invalid confoguration");
        } catch (ClassNotFoundException e) {
            LOG.error(e, "Provisioning class " + configuration.getServicename() + " not found");
        } catch (Throwable t) {
            LOG.error(t, "Unknown exception");
        }
    }

    /**
     * Release internal resources.
     */
    public void dispose() {
        provisioning = null;
    }

    /**
     * If internal connection is not usable, throw IllegalStateException.
     */
    public void test() {
        if (provisioning == null) {
            throw new IllegalStateException("Service port not found.");
        }

        String res = provisioning.checkAlive();

        if (!SUCCESS.equals(res)) {
            throw new IllegalStateException("Invalid response.");
        }
    }

    public Provisioning getProvisioning() {
        return provisioning;
    }
}
