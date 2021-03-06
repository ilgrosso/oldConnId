/*
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008-2009 Sun Microsystems, Inc. All rights reserved.     
 * 
 * The contents of this file are subject to the terms of the Common Development 
 * and Distribution License("CDDL") (the "License").  You may not use this file 
 * except in compliance with the License.
 * 
 * You can obtain a copy of the License at 
 * http://IdentityConnectors.dev.java.net/legal/license.txt
 * See the License for the specific language governing permissions and limitations 
 * under the License. 
 * 
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at identityconnectors/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the fields 
 * enclosed by brackets [] replaced by your own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.bundles.db.common;

import org.connid.bundles.db.common.FilterWhereBuilder;
import org.connid.bundles.db.common.SQLParam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * FilterWhereBuilder test class
 * @version $Revision 1.0$
 * @since 1.0
 */
public class FilterWhereBuilderTest {

    private static final SQLParam VALUE = new SQLParam("value", "value");
    private static final String NAME = "name";
    private static final String OPERATOR = "=";

     
    /**
     * Test method for {@link FilterWhereBuilder#FilterWhereBuilder(String, java.util.Map)}.
     */
    @Test
    public void testFilterQueryBuilder() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
    }

    /**
     * Test method for {@link FilterWhereBuilder#join(String, FilterWhereBuilder, FilterWhereBuilder)}.
     */
    @Test
    public void testJoin() {
        FilterWhereBuilder l = new FilterWhereBuilder();
        final SQLParam param = new SQLParam(NAME, VALUE);     
        l.addBind(param, OPERATOR);
        FilterWhereBuilder r = new FilterWhereBuilder();
        r.addBind(param, OPERATOR);
        FilterWhereBuilder actual = new FilterWhereBuilder();
        actual.join("AND", l, r);
        assertNotNull(actual);
        assertNotNull(actual.getParams());
        assertTrue(actual.getParams().contains(param));
        assertEquals("number for binding", 2,actual.getParams().size());
        assertEquals("name = ? AND name = ?", actual.getWhereClause());    
    }

    /**
     * Test method for {@link FilterWhereBuilder#getNames()}.
     */
    @Test
    public void testGetNamesAndValues() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
        assertNotNull(actual.getParams());
        final SQLParam param = new SQLParam(NAME, VALUE);     
        actual.addBind(param, OPERATOR);
        assertTrue(actual.getParams().contains(param));
    }

    /**
     * Test method for {@link FilterWhereBuilder#getWhere()}.
     */
    @Test
    public void testGetWhere() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
        final SQLParam param = new SQLParam(NAME, VALUE);     
        actual.addBind(param, OPERATOR);
        assertNotNull(actual.getWhere());
        assertEquals("name = ?", actual.getWhere().toString());
    }
    
    /**
     * Test method for {@link FilterWhereBuilder#getWhere()}.
     */
    @Test
    public void testEmptyWhere() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
        assertNotNull(actual.getWhere());
        assertEquals("", actual.getWhere().toString());
    }
    

    /**
     * Test method for {@link FilterWhereBuilder#addBind(String, String, Object)}.
     */
    @Test
    public void testAddBind() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
        assertNotNull(actual.getParams());
        final SQLParam param = new SQLParam(NAME, VALUE);     
        actual.addBind(param, OPERATOR);
        assertTrue(actual.getParams().contains(param));
        assertEquals("name = ?", actual.getWhereClause());
    }

    /**
     * Test method for {@link FilterWhereBuilder#getWhereClause()}.
     */
    @Test
    public void testGetWhereClause() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
        final SQLParam param = new SQLParam(NAME, VALUE);     
        actual.addBind(param, OPERATOR);
        assertEquals("name = ?", actual.getWhereClause());
        assertEquals("not one value for binding", 1, actual.getParams().size());
    }

    /**
     * Test method for {@link FilterWhereBuilder#getWhereClause()}.
     */
    @Test
    public void testGetWhereClauseWithWhere() {
        FilterWhereBuilder actual = new FilterWhereBuilder();
        assertNotNull(actual);
        final SQLParam param = new SQLParam(NAME, VALUE);     
        actual.addBind(param, OPERATOR);
        assertEquals("name = ?", actual.getWhereClause());
        assertEquals("not one value for binding", 1, actual.getParams().size());
    }
}
