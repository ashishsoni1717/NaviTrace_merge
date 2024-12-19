
package org.navitrace.forward;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class NetworkForwarder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkForwarder.class);

    private final InetAddress destination;
    private final DatagramSocket connectionUdp;
    private final Map<InetSocketAddress, Socket> connectionsTcp = new HashMap<>();

    @Inject
    public NetworkForwarder(Config config) throws IOException {
        destination = InetAddress.getByName(config.getString(Keys.SERVER_FORWARD));
        connectionUdp = new DatagramSocket();
    }

    public void forward(InetSocketAddress source, int port, boolean datagram, byte[] data) {
        try {
            if (datagram) {
                connectionUdp.send(new DatagramPacket(data, data.length, destination, port));
            } else {
                Socket connectionTcp = connectionsTcp.get(source);
                if (connectionTcp == null || connectionTcp.isClosed()) {
                    connectionTcp = new Socket(destination, port);
                    connectionsTcp.put(source, connectionTcp);
                }
                connectionTcp.getOutputStream().write(data);
            }
        } catch (IOException e) {
            LOGGER.warn("Network forwarding error", e);
        }
    }

    public void disconnect(InetSocketAddress source) {
        Socket connectionTcp = connectionsTcp.remove(source);
        if (connectionTcp != null) {
            try {
                connectionTcp.close();
            } catch (IOException e) {
                LOGGER.warn("Connection close error", e);
            }
        }
    }

}
