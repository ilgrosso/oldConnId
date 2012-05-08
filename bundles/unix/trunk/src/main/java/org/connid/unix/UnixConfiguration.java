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

import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class UnixConfiguration extends AbstractConfiguration {
    
    private String hostname = "";
    private String port = "";
    private String admin = "";
    private String password = "";

    @ConfigurationProperty(displayMessageKey = "unix.admin.display",
    helpMessageKey = "unix.admin.help", order = 1)
    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @ConfigurationProperty(displayMessageKey = "unix.hostname.display",
    helpMessageKey = "unix.hostname.help", order = 3)
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @ConfigurationProperty(displayMessageKey = "unix.password.display",
    helpMessageKey = "unix.password.help", order = 2)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ConfigurationProperty(displayMessageKey = "unix.port.display",
    helpMessageKey = "unix.port.help", order = 4)
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public void validate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
