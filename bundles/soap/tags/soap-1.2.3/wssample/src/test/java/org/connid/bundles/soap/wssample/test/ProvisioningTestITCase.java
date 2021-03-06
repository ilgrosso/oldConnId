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
package org.connid.bundles.soap.wssample.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.connid.bundles.soap.to.WSAttributeValue;
import org.connid.bundles.soap.to.WSChange;
import org.connid.bundles.soap.to.WSUser;
import org.connid.bundles.soap.provisioning.interfaces.Provisioning;
import org.connid.bundles.soap.utilities.Operand;
import org.connid.bundles.soap.utilities.Operator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:wssampleContext.xml"})
public class ProvisioningTestITCase {

    private static final Logger log =
            LoggerFactory.getLogger(ProvisioningTestITCase.class);

    @Autowired
    private JaxWsProxyFactoryBean proxyFactory;

    private Provisioning provisioning;

    @Before
    public void init() {
        provisioning = (Provisioning) proxyFactory.create();
    }

    @Test
    public void authenticate() {
        Throwable t = null;

        try {

            String uid = provisioning.authenticate(
                    "TESTUSER",
                    "password");

            assertEquals("TESTUSER", uid);

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void checkAlive() {

        Throwable t = null;

        try {

            provisioning.checkAlive();

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);

    }

    @Test
    public void schema() {

        Throwable t = null;

        try {

            provisioning.schema();

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void create() {

        Throwable t = null;

        try {
            WSAttributeValue uid = new WSAttributeValue();
            uid.setName("userId");
            uid.setValues(Collections.singletonList("john.doe@gmail.com"));
            uid.setKey(true);

            WSAttributeValue password = new WSAttributeValue();
            password.setName("password");
            password.setValues(Collections.singletonList("password"));
            password.setPassword(true);

            WSAttributeValue type = new WSAttributeValue();
            type.setName("type");
            type.setValues(Collections.singletonList("person"));

            WSAttributeValue name = new WSAttributeValue();
            name.setName("name");
            name.setValues(Collections.singletonList("john"));

            WSAttributeValue surname = new WSAttributeValue();
            surname.setName("surname");
            surname.setValues(Collections.singletonList("doe"));

            WSAttributeValue fullname = new WSAttributeValue();
            fullname.setName("fullname");
            fullname.setValues(Collections.singletonList("john doe"));
            
            WSAttributeValue birthdate = new WSAttributeValue();
            birthdate.setName("birthdate");
            birthdate.setValues(Collections.singletonList("01/01/1990"));

            List<WSAttributeValue> attrs = new ArrayList<WSAttributeValue>();
            attrs.add(uid);
            attrs.add(password);
            attrs.add(type);
            attrs.add(name);
            attrs.add(surname);
            attrs.add(fullname);
            attrs.add(birthdate);

            String accountId = provisioning.create(attrs);

            assertNotNull(accountId);
            assertEquals(accountId, "john.doe@gmail.com");

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void update() {

        Throwable t = null;

        try {

            WSAttributeValue surname = new WSAttributeValue();
            surname.setName("surname");
            surname.setValues(Collections.singletonList("verde"));
            surname.setKey(true);

            WSAttributeValue name = new WSAttributeValue();
            name.setName("name");
            name.setValues(Collections.singletonList("pino"));


            List<WSAttributeValue> attrs = new ArrayList<WSAttributeValue>();
            attrs.add(surname);
            attrs.add(name);

            String uid = provisioning.update("test2", attrs);

            assertNotNull(uid);
            assertEquals("test2", uid);

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void delete() {

        Throwable t = null;


        try {

            provisioning.delete("test1");

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void query() {

        Throwable t = null;


        try {

            Operand op1 = new Operand(Operator.EQ, "name", "Pino");
            Operand op2 = new Operand(Operator.EQ, "surname", "Bianchi");
            Operand op3 = new Operand(Operator.EQ, "surname", "Rossi");

            Set<Operand> sop1 = new HashSet<Operand>();
            sop1.add(op1);
            sop1.add(op2);

            Set<Operand> sop2 = new HashSet<Operand>();
            sop2.add(op1);
            sop2.add(op3);

            Operand op4 = new Operand(Operator.AND, sop1);
            Operand op5 = new Operand(Operator.AND, sop2);

            Set<Operand> sop = new HashSet<Operand>();
            sop.add(op4);
            sop.add(op5);

            Operand query = new Operand(Operator.OR, sop, true);

            List<WSUser> results = provisioning.query(query);

            assertNotNull(results);
            assertFalse(results.isEmpty());

            for (WSUser user : results) {
                log.debug("User: " + user);
            }


        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void resolve() {

        Throwable t = null;

        try {

            String uid = provisioning.resolve("test2");

            assertEquals("test2", uid);

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void getLatestChangeNumber() {

        Throwable t = null;

        try {

            int token = provisioning.getLatestChangeNumber();

            assertEquals(0, token);

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }

    @Test
    public void sync() {

        Throwable t = null;

        try {

            List<WSChange> results = null;

            if (provisioning.isSyncSupported()) {

                results = provisioning.sync();
                assertNotNull(results);

                for (WSChange change : results) {
                    log.debug("Delta: " + change.getId());
                }

            }

        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.debug("Unknown exception!", e);
            }

            t = e;
        }

        assertNull(t);
    }
}
