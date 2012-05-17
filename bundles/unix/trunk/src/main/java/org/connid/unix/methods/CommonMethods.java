package org.connid.unix.methods;

import org.identityconnectors.common.security.GuardedString;

public class CommonMethods {
    
    protected final String getPlainPassword(final GuardedString password) {
        final StringBuffer buf = new StringBuffer();

        password.access(new GuardedString.Accessor() {

            @Override
            public void access(final char[] clearChars) {
                buf.append(clearChars);
            }
        });
        return buf.toString();
    }
    
}
