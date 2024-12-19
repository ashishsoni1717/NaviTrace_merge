
package org.navitrace.protocol;

import org.junit.jupiter.api.Test;
import org.navitrace.ProtocolTest;
import org.navitrace.model.Command;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdmProtocolEncoderTest extends ProtocolTest {

    @Test
    public void testEncode() throws Exception {

        var encoder = inject(new AdmProtocolEncoder(null));

        Command command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_GET_DEVICE_STATUS);
        assertEquals("STATUS\r\n", encoder.encodeCommand(command));

        command = new Command();
        command.setDeviceId(1);
        command.setType(Command.TYPE_CUSTOM);
        command.set(Command.KEY_DATA, "INPUT 0");
        assertEquals("INPUT 0\r\n", encoder.encodeCommand(command));
    }

}
