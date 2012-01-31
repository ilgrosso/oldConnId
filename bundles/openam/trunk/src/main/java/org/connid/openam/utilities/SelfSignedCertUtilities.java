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
package org.connid.openam.utilities;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.identityconnectors.common.logging.Log;

public class SelfSignedCertUtilities {

    private static final Log LOG = Log.getLog(SelfSignedCertUtilities.class);

    public static void trustSelfSignedSSL() {
        try {
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public void checkClientTrusted(final X509Certificate[] xcs,
                        final String string) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] xcs,
                        final String string) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLContext.setDefault(ctx);
        } catch (KeyManagementException kme) {
            LOG.error("Error during SSL configuration", kme);
        } catch (NoSuchAlgorithmException nsae) {
            LOG.error("Error during SSL configuration", nsae);
        }
    }
}
