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

public class OpenamServicesUrl {

    public static final String CREATE_RESTFUL_SERVICE = "/identity/create?";
    public static final String UPDATE_RESTFUL_SERVICE = "/identity/update?";
    public static final String DELETE_RESTFUL_SERVICE = "/identity/delete?";
    public static final String SEARCH_RESTFUL_SERVICE = "/identity/search?";
    public static final String READ_RESTFUL_SERVICE = "/identity/read?";
    public static final String TOKEN_VALID_RESTFUL_SERVICE =
            "/identity/isTokenValid?";
    public static final String LOGOUT_RESTFUL_SERVICE = "/identity/logout?";
    public static final String AUTHENTICATE_RESTFUL_SERVICE =
            "/identity/authenticate?";
    public static final String TEST_SERVICE = "/namingservice";
}
