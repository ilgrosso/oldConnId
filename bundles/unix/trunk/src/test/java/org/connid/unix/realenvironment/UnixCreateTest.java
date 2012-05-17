package org.connid.unix.realenvironment;

import org.connid.unix.UnixConnector;
import org.connid.unix.utilities.SharedTestMethods;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.Assert;
import org.junit.Test;

public class UnixCreateTest extends SharedTestMethods {

    @Test
    public final void createAndDeleteTest() {
        final UnixConnector connector = new UnixConnector();
        connector.init(createConfiguration());
        Name name = new Name("createtest" + randomNumber());
        Uid newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name), null);
        System.out.println("NAME: " + name.getNameValue());
        System.out.println("UID: " + newAccount.getUidValue());
        Assert.assertEquals(name.getNameValue(), newAccount.getUidValue());
        connector.dispose();
    }
}
