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

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String command){
        String[] args = command.split(" ");
        String line="";
            switch (args[0]) {
                case "GET":

                    for(String a: args){
                        line+=a+ "|";
                    }
                    System.out.println(line);
                    Filter.sendData(session, args);
                    break;
                case "GET_COUNTRY":
                    for(String a: args){
                        line+=a+ "|";
                    }
                    args[1].replaceAll("_"," ");
                    System.out.println(line);
                    Filter.sendCountry(session,args);
                    break;
                case "GET_RAD":
                    for(String a: args){
                        line+=a+ "|";
                    }
                    System.out.print(line);
                    Filter.sendRadius(session,args);
                    break;
                case "GET_COAST":
                    for(String a: args){
                        line+=a+ "|";
                    }
                    System.out.println(line);
                    Filter.sendCoast(session,args);
                    break;
                case "STOP":
                    for(String a: args){
                        line+=a+ "|";
                    }
                    System.out.print(line);
                    System.out.println("Stop command");
                    Filter.stopData(session);
                    break;
                default:
                    try {
                        session.getRemote().sendString("invalid command.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
    }

    @OnWebSocketError
    public void onError(Throwable t){

    }

}
