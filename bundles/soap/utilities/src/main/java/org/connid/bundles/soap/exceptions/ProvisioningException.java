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
package org.connid.bundles.soap.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.ws.WebFault;

@WebFault(name = "ProvisioningException")
public class ProvisioningException extends Exception {

    private String wrappedCause;

    public ProvisioningException(String msg) {
        super(msg);
    }

    public ProvisioningException(final String msg, final Throwable cause) {
        super(msg, cause);

        StringWriter exceptionWriter = new StringWriter();
        exceptionWriter.write(cause.getMessage() + "\n\n");
        cause.printStackTrace(new PrintWriter(exceptionWriter));
        wrappedCause = exceptionWriter.toString();
    }

    @Override
    public String getMessage() {
        return wrappedCause != null ? wrappedCause : super.getMessage();
    }

    public String getWrappedCause() {
        return wrappedCause;
    }

    public void setWrappedCause(String wrappedCause) {
        this.wrappedCause = wrappedCause;
    }
}
