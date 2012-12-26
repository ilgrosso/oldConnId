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
package org.connid.bundles.soap.provisioning.interfaces;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import org.connid.bundles.soap.exceptions.ProvisioningException;
import org.connid.bundles.soap.to.WSAttribute;
import org.connid.bundles.soap.to.WSAttributeValue;
import org.connid.bundles.soap.to.WSChange;
import org.connid.bundles.soap.to.WSUser;
import org.connid.bundles.soap.utilities.Operand;

@WebService
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public interface Provisioning {

    /**
     * Checks if authentication is supported.
     *
     * @return true if the resource support authentication.
     */
    @WebMethod(operationName = "isAuthenticationSupported", action = "isAuthenticationSupported")
    Boolean isAuthenticationSupported();

    /**
     * Checks if synchronization is supported.
     *
     * @return true if the resource support synchronization.
     */
    @WebMethod(operationName = "isSyncSupported", action = "isSyncSupported")
    Boolean isSyncSupported();

    /**
     * Verify user creentials
     *
     * @param username
     * @param password
     * @return the accountid of the first account that match username and password.
     * @throws ProvisioningException in case of authentication failed.
     */
    @WebMethod(operationName = "authenticate", action = "authenticate")
    String authenticate(
            @WebParam(name = "username") final String username,
            @WebParam(name = "password") final String password)
            throws ProvisioningException;

    /**
     * Returns "OK" if the resource is available.
     *
     * @return the string "OK" in case of availability of the resource.
     */
    @WebMethod(operationName = "checkAlive", action = "checkAlive")
    String checkAlive();

    /**
     * Returns the schema.
     *
     * @return a set of attributes.
     */
    @WebMethod(operationName = "schema", action = "schema")
    List<WSAttribute> schema();

    /**
     * Creates user account.
     *
     * @param a set of account attributes.
     * @return accountid of the account created.
     * @throws ProvisioningException in case of failure.
     */
    @WebMethod(operationName = "create", action = "create")
    String create(@WebParam(name = "data") final List<WSAttributeValue> data)
            throws ProvisioningException;

    /**
     * Updates user account.
     *
     * @param accountid.
     * @param a set of attributes to be updated.
     * @return accountid.
     * @throws ProvisioningException in case of failure
     */
    @WebMethod(operationName = "update", action = "update")
    String update(
            @WebParam(name = "accountid") final String accountid,
            @WebParam(name = "data") final List<WSAttributeValue> data)
            throws ProvisioningException;

    /**
     * Deletes user account.
     *
     * @param accountid.
     * @return accountid.
     * @throws ProvisioningException in case of failure.
     */
    @WebMethod(operationName = "delete", action = "delete")
    String delete(@WebParam(name = "accountid") final String accountid)
            throws ProvisioningException;

    /**
     * Searches for user accounts.
     *
     * @param query filter
     * @return a set of user accounts.
     */
    @WebMethod(operationName = "query", action = "query")
    List<WSUser> query(@WebParam(name = "query") final Operand query);

    /**
     * Returns accountid related to the specified username.
     *
     * @param username.
     * @return accountid or null if username not found
     * @throws ProvisioningException in case of failure.
     */
    @WebMethod(operationName = "resolve", action = "resolve")
    String resolve(@WebParam(name = "username") final String username)
            throws ProvisioningException;

    /**
     * Gets the latest change id.
     *
     * @return change id.
     * @throws ProvisioningException in case of failure.
     */
    @WebMethod(operationName = "getLatestChangeNumber", action = "getLatestChangeNumber")
    int getLatestChangeNumber()
            throws ProvisioningException;

    /**
     * Returns changes to be synchronized.
     *
     * @return a set of changes
     * @throws ProvisioningException in case of failure
     */
    @WebMethod(operationName = "sync", action = "sync")
    List<WSChange> sync()
            throws ProvisioningException;
}
