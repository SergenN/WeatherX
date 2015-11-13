package nl.jozefbv.weatherx;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by Michaël van der Veen
 * Date of creation 28-10-2015
 *
 * Authors: Michaël van der Veen,
 *
 * Version: 2
 * Package: default
 * Class: nl.jozefbv.weatherx.Initial
 * Description:
 * This is the initial class that creates properties files and loads the default settings for database savings.
 *
 *
 * Changelog:
 * 2 completion of Filter Class Added documentation
 *
 *
 */
public class WebClientServer{

    /**
     * Creating new server and Initializing a session listener.
     * @throws Exception
     */
    public static void main() throws Exception {
        Server wsServer = new Server(8080);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(WebClientConn.class);
            }

        };
        wsServer.setHandler(wsHandler);
        wsServer.start();
        wsServer.join();
    }
}
