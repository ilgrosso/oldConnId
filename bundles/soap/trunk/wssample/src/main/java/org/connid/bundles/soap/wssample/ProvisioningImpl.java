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
package org.connid.bundles.soap.wssample;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jws.WebService;
import org.apache.commons.lang.StringUtils;
import org.connid.bundles.soap.exceptions.ProvisioningException;
import org.connid.bundles.soap.provisioning.interfaces.Provisioning;
import org.connid.bundles.soap.to.WSAttribute;
import org.connid.bundles.soap.to.WSAttributeValue;
import org.connid.bundles.soap.to.WSChange;
import org.connid.bundles.soap.to.WSUser;
import org.connid.bundles.soap.utilities.Operand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

@WebService(endpointInterface = "org.connid.bundles.soap.provisioning.interfaces.Provisioning",
serviceName = "Provisioning")
public class ProvisioningImpl implements Provisioning {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Provisioning.class);

    @Override
    public String delete(String accountid)
            throws ProvisioningException {

        LOG.debug("Delete request received");

        Connection conn = null;
        try {
            conn = connect();

            Statement statement = conn.createStatement();

            String query = "DELETE FROM user WHERE userId='" + accountid + "';";
            LOG.debug("Execute query: " + query);

            statement.executeUpdate(query);

            return accountid;
        } catch (SQLException e) {
            throw new ProvisioningException("Delete operation failed", e);
        } finally {

            if (conn != null) {
                try {
                    close(conn);
                } catch (SQLException ignore) {
                    // ignore exception
                }
            }
        }
    }

    @Override
    public Boolean isSyncSupported() {
        LOG.debug("isSyncSupported request received");

        return Boolean.FALSE;
    }

    @Override
    public String checkAlive() {
        LOG.debug("checkAlive request received");

        String result;
        try {
            close(connect());

            LOG.debug("Services available");

            result = "OK";
        } catch (Exception e) {
            LOG.debug("Services not available");

            result = "KO";
        }

        return result;
    }

    @Override
    public String update(String accountid, List<WSAttributeValue> data)
            throws ProvisioningException {

        LOG.debug("Update request received");

        if (data == null || data.isEmpty()) {
            LOG.warn("Empty data recevied");
            return accountid;
        }

        List<WSAttribute> schema = schema();
        Set<String> schemaNames = new HashSet<String>();
        for (WSAttribute attr : schema) {
            schemaNames.add(attr.getName());
        }
        schemaNames.add("__NAME__");
        schemaNames.add("__PASSWORD__");

        Connection conn = null;

        try {
            conn = connect();
            Statement statement = conn.createStatement();

            String value;

            StringBuilder set = new StringBuilder();
            for (WSAttributeValue attr : data) {
                if (schemaNames.contains(attr.getName())) {
                    if (attr.getValues() == null || attr.getValues().isEmpty()) {
                        value = null;
                    } else if (attr.getValues().size() == 1) {
                        value = attr.getValues().get(0).toString();
                    } else {
                        value = attr.getValues().toString();
                    }

                    if (!attr.isKey() || !accountid.equals(value)) {
                        if (set.length() > 0) {
                            set.append(",");
                        }

                        if ("__NAME__".equals(attr.getName())) {
                            set.append("userId=");
                        } else if ("__PASSWORD__".equals(attr.getName())) {
                            set.append("password=");
                        } else {
                            set.append(attr.getName()).append('=');
                        }

                        set.append(value == null ? null : "'" + value + "'");
                    }
                }
            }

            if (set.length() > 0) {
                String query = "UPDATE user SET " + set.toString() + " WHERE userId='" + accountid + "';";
                LOG.debug("Execute query: " + query);

                statement.executeUpdate(query);
            }

            return accountid;
        } catch (SQLException e) {
            LOG.error("Update failed", e);
            throw new ProvisioningException("Update operation failed", e);
        } finally {
            if (conn != null) {
                try {
                    close(conn);
                } catch (SQLException ignore) {
                    // ignore exception
                }
            }
        }
    }

    @Override
    public List<WSUser> query(Operand query) {
        LOG.debug("Query request received");

        List<WSUser> results = new ArrayList<WSUser>();

        Connection conn = null;
        try {

            String queryString = "SELECT * FROM user" + (query == null ? "" : " WHERE " + query.toString());

            queryString = queryString.replaceAll("__NAME__", "userId").
                    replaceAll("__UID__", "userId").
                    replaceAll("__PASSWORD__", "password");

            LOG.debug("Execute query: {}", queryString);

            if (queryString == null || queryString.length() == 0) {
                throw new SQLException("Invalid query [" + queryString + "]");
            }

            conn = connect();
            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(queryString);

            ResultSetMetaData metaData = rs.getMetaData();
            LOG.debug("Metadata: {}", metaData);

            while (rs.next()) {
                WSUser user = new WSUser();

                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    WSAttributeValue attr = new WSAttributeValue();
                    attr.setName(metaData.getColumnLabel(i + 1));
                    if (StringUtils.isNotBlank(rs.getString(i + 1))) {
                        attr.setValues(Collections.singletonList(rs.getString(i + 1)));
                    }
                    if ("userId".equalsIgnoreCase(metaData.getColumnName(i + 1))) {
                        attr.setKey(true);
                        user.setAccountid(rs.getString(i + 1));
                    }

                    user.addAttribute(attr);
                }

                results.add(user);
            }

            LOG.debug("Retrieved users: {}", results);
        } catch (SQLException e) {
            LOG.error("Search operation failed", e);
        } finally {
            if (conn != null) {
                try {
                    close(conn);
                } catch (SQLException ignore) {
                    // ignore exception
                }
            }
        }

        return results;
    }

    @Override
    public String create(List<WSAttributeValue> data)
            throws ProvisioningException {

        LOG.debug("Create request received with data {}", data);

        List<WSAttribute> schema = schema();
        Set<String> schemaNames = new HashSet<String>();
        for (WSAttribute attr : schema) {
            schemaNames.add(attr.getName());
        }
        schemaNames.add("__NAME__");
        schemaNames.add("__PASSWORD__");

        Connection conn = null;
        String query = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();

            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();

            String accountid = null;
            String value;

            for (WSAttributeValue attr : data) {
                if (schemaNames.contains(attr.getName())) {
                    LOG.debug("Bind attribute: {}", attr);

                    if (attr.getValues() == null || attr.getValues().isEmpty()) {
                        value = null;
                    } else if (attr.getValues().size() == 1) {
                        value = attr.getValues().get(0).toString();
                    } else {
                        value = attr.getValues().toString();
                    }

                    if (keys.length() > 0) {
                        keys.append(",");
                    }

                    if ("__NAME__".equals(attr.getName())) {
                        keys.append("userId");
                    } else if ("__PASSWORD__".equals(attr.getName())) {
                        keys.append("password");
                    } else {
                        keys.append(attr.getName());
                    }

                    if (values.length() > 0) {
                        values.append(",");
                    }

                    values.append(value == null ? null : "'" + value + "'");

                    if (attr.isKey() && !attr.getValues().isEmpty()) {
                        accountid = attr.getValues().get(0).toString();
                    }
                }
            }

            query = "INSERT INTO user (" + keys.toString() + ")"
                    + "VALUES (" + values.toString() + ");";

            LOG.debug("Execute query: " + query);

            statement.executeUpdate(query);

            return accountid;
        } catch (SQLException e) {
            LOG.error("Creation failed:\n" + query, e);
            throw new ProvisioningException("Create operation failed", e);
        } finally {
            if (conn != null) {
                try {
                    close(conn);
                } catch (SQLException ignore) {
                    // ignore exception
                }
            }
        }
    }

    @Override
    public int getLatestChangeNumber()
            throws ProvisioningException {

        LOG.debug("getLatestChangeNumber request received");

        return 0;
    }

    @Override
    public List<WSChange> sync()
            throws ProvisioningException {

        LOG.debug("sync request received");

        return Collections.EMPTY_LIST;
    }

    @Override
    public String resolve(String username)
            throws ProvisioningException {

        LOG.debug("Resolve request operation received: " + username);

        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();

            String query =
                    "SELECT userId FROM user WHERE userId='" + username + "';";

            LOG.debug("Execute query: " + query);

            ResultSet rs = statement.executeQuery(query);

            return rs.next() ? rs.getString(1) : null;
        } catch (SQLException e) {
            throw new ProvisioningException("Resolve operation failed", e);
        } finally {
            if (conn != null) {
                try {
                    close(conn);
                } catch (SQLException ignore) {
                    // ignore exception
                }
            }
        }
    }

    @Override
    public List<WSAttribute> schema() {
        LOG.debug("schema request received");

        List<WSAttribute> attrs = new ArrayList<WSAttribute>();

        WSAttribute attr = new WSAttribute();
        attr.setName("userId");
        attr.setNullable(false);
        attr.setPassword(false);
        attr.setKey(true);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("password");
        attr.setNullable(false);
        attr.setPassword(true);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("type");
        attr.setNullable(false);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("residence");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("telephone");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("fax");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("preference");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("name");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("surname");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("fullname");
        attr.setNullable(false);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("birthdate");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("Date");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("telephone");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("gender");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("taxNumber");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("state");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("studyTitle");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("studyArea");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("job");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("companyType");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("companyName");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("vatNumber");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("String");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("mandatoryDisclaimer");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("Boolean");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("promoRCSDisclaimer");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("Boolean");
        attrs.add(attr);

        attr = new WSAttribute();
        attr.setName("promoThirdPartyDisclaimer");
        attr.setNullable(true);
        attr.setPassword(false);
        attr.setKey(false);
        attr.setType("Boolean");
        attrs.add(attr);

        return attrs;
    }

    @Override
    public String authenticate(String username, String password)
            throws ProvisioningException {

        LOG.debug("authenticate request received");

        return username;
    }

    @Override
    public Boolean isAuthenticationSupported() {
        LOG.debug("isAuthenticationSupported request received");

        return Boolean.FALSE;
    }

    /**
     * Establish a connection to underlying db.
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection connect()
            throws SQLException {

        if (DefaultContentLoader.localDataSource == null) {
            LOG.error("Data Source is null");
            return null;
        }

        Connection conn = DataSourceUtils.getConnection(
                DefaultContentLoader.localDataSource);

        if (conn == null) {
            LOG.error("Connection is null");
        }

        return conn;

    }

    /**
     * Close connection to underlying db
     *
     * @throws SQLException
     */
    private void close(Connection conn)
            throws SQLException {

        conn.close();
    }
}
