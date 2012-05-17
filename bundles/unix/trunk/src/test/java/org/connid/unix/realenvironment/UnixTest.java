package org.connid.unix.realenvironment;

import org.connid.unix.UnixConnector;
import org.connid.unix.utilities.SharedTestMethods;
import org.junit.Test;

public class UnixTest extends SharedTestMethods{

    @Test
    public final void testConnection() {
        final UnixConnector connector = new UnixConnector();
        connector.init(createConfiguration());
        connector.test();
        connector.dispose();
    }
    
}
