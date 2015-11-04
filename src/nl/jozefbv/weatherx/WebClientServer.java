package nl.jozefbv.weatherx;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by pjvan on 28-10-2015.
 */
public class WebClientServer{

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
