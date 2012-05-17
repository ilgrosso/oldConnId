package org.connid.unix.methods;

import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.IOException;
import org.connid.unix.UnixConnection;
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

    protected final boolean userExists(final String username,
            final UnixConnection unixConnection)
            throws IOException, InvalidStateException, InterruptedException {
        return unixConnection.userExists(username);
    }
}
