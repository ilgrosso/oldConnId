package org.connid.unix.utilities;

import java.util.HashSet;
import java.util.Set;
import org.connid.unix.UnixConfiguration;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Name;

public class SharedTestMethods {

    protected final UnixConfiguration createConfiguration() {
        // create the connector configuration..
        UnixConfiguration config = new UnixConfiguration();
        config.setAdmin(UnixProperties.UNIX_ADMIN);
        config.setPassword(UnixProperties.UNIX_PASSWORD);
        config.setHostname(UnixProperties.UNIX_HOSTNAME);
        config.setPort(Integer.valueOf(UnixProperties.UNIX_PORT).intValue());
        config.validate();
        return config;
    }

    protected final Set<Attribute> createSetOfAttributes(final Name name) {
        final GuardedString password =
                new GuardedString("password".toCharArray());
        final Set<Attribute> attributes =
                CollectionUtil.newSet(AttributeBuilder.buildPassword(password));
        attributes.add(name);
        return attributes;
    }

    protected int randomNumber() {
        return (int) (Math.random() * 100000);
    }
}
