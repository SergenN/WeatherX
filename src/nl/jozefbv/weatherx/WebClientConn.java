package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;

/**
 * Created by Michael van der Veen on 28-10-2015.
 */
@WebSocket
public class WebClientConn {

    @OnWebSocketClose
    public void onClose(Session session,int statusCode, String reason){
        System.out.println("Session closed id:"+session.getLocalAddress());
        Filter.stopData(session);
    }

    @OnWebSocketConnect
    public void onConnect(Session session){
        try {
            session.getRemote().sendString("Welcome to the WeatherX application");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String command){
        String[] args = command.split(" ");

            switch (args[0]) {
                case "get":
                    Filter.sendData(session, args);
                    break;
                case "stop":
                    System.out.println("Stop command");
                    Filter.stopData(session);
                    break;
                default:
                    try {
                        session.getRemote().sendString("invallid command.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        /*
        if (args[0].equalsIgnoreCase("get")) {
            System.out.println("getting");
            try {
                session.getRemote().sendString("Fetching data from : "+args[1]);
                Filter.sendData(session,args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(args[0].equalsIgnoreCase("get")){
            String station = args[1];
        }*/
    }

    @OnWebSocketError
    public void onError(Throwable t){

    }

}
