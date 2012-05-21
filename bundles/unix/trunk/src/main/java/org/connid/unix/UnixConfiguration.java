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

import org.connid.unix.utilities.Constants;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class UnixConfiguration extends AbstractConfiguration {

    private String hostname = "";
    private int port = 0;
    private String admin = "";
    private GuardedString password = null;
    private boolean createHomeDirectory = false;
    private boolean deleteHomeDirectory = false;
    private String baseHomeDirectory = "";

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
    public GuardedString getPassword() {
        return password;
    }

    public void setPassword(GuardedString password) {
        this.password = password;
    }

    @ConfigurationProperty(displayMessageKey = "unix.port.display",
    helpMessageKey = "unix.port.help", order = 4)
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @ConfigurationProperty(displayMessageKey = "unix.createhomedir.display",
    helpMessageKey = "unix.createhomedir.help", order = 5)
    public boolean isCreateHomeDirectory() {
        return createHomeDirectory;
    }

    public void setCreateHomeDirectory(boolean createHomeDirectory) {
        this.createHomeDirectory = createHomeDirectory;
    }

    @ConfigurationProperty(displayMessageKey = "unix.deletehomedir.display",
    helpMessageKey = "unix.deletehomedir.help", order = 6)
    public boolean isDeleteHomeDirectory() {
        return deleteHomeDirectory;
    }

    public void setDeleteHomeDirectory(boolean deleteHomeDirectory) {
        this.deleteHomeDirectory = deleteHomeDirectory;
    }

    @ConfigurationProperty(displayMessageKey = "unix.basehomedir.display",
    helpMessageKey = "unix.basehomedir.help", order = 7)
    public String getBaseHomeDirectory() {
        return baseHomeDirectory;
    }

    public void setBaseHomeDirectory(String baseHomeDirectory) {
        this.baseHomeDirectory = baseHomeDirectory;
    }

    @Override
    public void validate() {
        if (StringUtil.isBlank(admin)) {
            throw new ConfigurationException(
                    "Unix admin username must not be blank!");
        }
        if (StringUtil.isBlank(hostname)) {
            throw new ConfigurationException(
                    "Unix hostname must not be blank!");
        }
        if (StringUtil.isBlank(password.toString())) {
            throw new ConfigurationException(
                    "Unix admin password must not be blank!");
        }
        if (StringUtil.isBlank(String.valueOf(port))) {
            port = Constants.getSshDefaultPort();
        }
        if (port < 0 && port <= Constants.getUnixLastPort()) {
            throw new ConfigurationException(
                    "Unix ssh port range: 0 - 65535");
        }
        if (StringUtil.isBlank(baseHomeDirectory)) {
            baseHomeDirectory = "/home";
        }
        if (StringUtil.isBlank(
                Boolean.valueOf(createHomeDirectory).toString())) {
            createHomeDirectory = false;
        }
        if (StringUtil.isBlank(
                Boolean.valueOf(deleteHomeDirectory).toString())) {
            deleteHomeDirectory = false;
        }
    }
}
