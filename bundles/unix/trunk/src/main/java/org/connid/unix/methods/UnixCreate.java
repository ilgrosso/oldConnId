package org.connid.unix.methods;

import com.sshtools.j2ssh.util.InvalidStateException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.connid.unix.UnixConfiguration;
import org.connid.unix.UnixConnection;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;

public class UnixCreate extends CommonMethods {

    private static final Log LOG = Log.getLog(UnixCreate.class);
    private Set<Attribute> attrs = null;
    private UnixConfiguration configuration = null;
    private UnixConnection connection = null;
    private String uidString = null;
    private String password = null;

    public UnixCreate(final UnixConfiguration unixConfiguration,
            final Set<Attribute> attributes) {
        this.configuration = unixConfiguration;
        this.attrs = attributes;
        connection = UnixConnection.openConnection(configuration);
    }

    public Uid create() {
        try {
            return doCreate();
        } catch (Exception e) {
            LOG.error(e, "error during creation");
            throw new ConnectorException(e);
        }
    }

    private Uid doCreate() throws IOException,
            InvalidStateException, InterruptedException {
        for (Attribute attr : attrs) {
            if (attr.is(Name.NAME) || attr.is(Uid.NAME)) {
                uidString = (String) attr.getValue().get(0);
            } else if (attr.is(OperationalAttributes.PASSWORD_NAME)) {
                password = getPlainPassword(
                        (GuardedString) attr.getValue().get(0));
            } else if (attr.is(OperationalAttributes.ENABLE_NAME)) {
                boolean status = false;
                // manage enable/disable status
                if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                    status = Boolean.parseBoolean(
                            attr.getValue().get(0).toString());
                }
            } else {
                List<Object> values = attr.getValue();
                if ((values != null) && (!values.isEmpty())) {
                }
            }
        }
        connection.create(uidString, password);
        return new Uid(uidString);
    }
}
