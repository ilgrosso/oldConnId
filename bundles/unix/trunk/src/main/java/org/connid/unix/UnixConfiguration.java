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
import org.connid.unix.utilities.DefaultProperties;
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
    private String shell = "";
    private boolean root = false;
    private GuardedString sudoPassword = null;

    @ConfigurationProperty(displayMessageKey = "unix.admin.display",
    helpMessageKey = "unix.admin.help", order = 1)
    public final String getAdmin() {
        return admin;
    }

    public final void setAdmin(final String admin) {
        this.admin = admin;
    }

    @ConfigurationProperty(displayMessageKey = "unix.hostname.display",
    helpMessageKey = "unix.hostname.help", order = 3)
    public final String getHostname() {
        return hostname;
    }

    public final void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    @ConfigurationProperty(displayMessageKey = "unix.password.display",
    helpMessageKey = "unix.password.help", order = 2)
    public final GuardedString getPassword() {
        return password;
    }

    public final void setPassword(final GuardedString password) {
        this.password = password;
    }

    @ConfigurationProperty(displayMessageKey = "unix.port.display",
    helpMessageKey = "unix.port.help", order = 4)
    public final int getPort() {
        return port;
    }

    public final void setPort(final int port) {
        this.port = port;
    }

    @ConfigurationProperty(displayMessageKey = "unix.createhomedir.display",
    helpMessageKey = "unix.createhomedir.help", order = 5)
    public final boolean isCreateHomeDirectory() {
        return createHomeDirectory;
    }

    public final void setCreateHomeDirectory(
            final boolean createHomeDirectory) {
        this.createHomeDirectory = createHomeDirectory;
    }

    @ConfigurationProperty(displayMessageKey = "unix.deletehomedir.display",
    helpMessageKey = "unix.deletehomedir.help", order = 6)
    public final boolean isDeleteHomeDirectory() {
        return deleteHomeDirectory;
    }

    public final void setDeleteHomeDirectory(
            final boolean deleteHomeDirectory) {
        this.deleteHomeDirectory = deleteHomeDirectory;
    }

    @ConfigurationProperty(displayMessageKey = "unix.basehomedir.display",
    helpMessageKey = "unix.basehomedir.help", order = 7)
    public final String getBaseHomeDirectory() {
        return baseHomeDirectory;
    }

    public final void setBaseHomeDirectory(final String baseHomeDirectory) {
        this.baseHomeDirectory = baseHomeDirectory;
    }

    @ConfigurationProperty(displayMessageKey = "unix.shell.display",
    helpMessageKey = "unix.shell.help", order = 8)
    public final String getShell() {
        return shell;
    }

    public final void setShell(final String shell) {
        this.shell = shell;
    }

    @ConfigurationProperty(displayMessageKey = "unix.isroot.display",
    helpMessageKey = "unix.isroot.help", order = 9)
    public boolean isRoot() {
        return root;
    }

    public void setIsRoot(boolean root) {
        this.root = root;
    }

    @ConfigurationProperty(displayMessageKey = "unix.sudopwd.display",
    helpMessageKey = "unix.sudopwd.help", order = 10)
    public GuardedString getSudoPassword() {
        return sudoPassword;
    }

    public void setSudoPassword(GuardedString sudoPassword) {
        this.sudoPassword = sudoPassword;
    }
    
    @Override
    public final void validate() {
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
            baseHomeDirectory = DefaultProperties.UNIX_USER_HOMEDIRECTORY;
        }
        if (StringUtil.isBlank(
                Boolean.valueOf(createHomeDirectory).toString())) {
            createHomeDirectory = false;
        }
        if (StringUtil.isBlank(
                Boolean.valueOf(deleteHomeDirectory).toString())) {
            deleteHomeDirectory = false;
        }
        if (StringUtil.isBlank(shell)) {
            shell = DefaultProperties.UNIX_SHELL;
        }
        if ((!root) && (StringUtil.isBlank(sudoPassword.toString()))) {
            throw new ConfigurationException(
                    "Unix connector needs sudo password or root password");
        }
    }
}
