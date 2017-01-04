/*
package deprecated;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;

*/
/**
 * Created by Micha�l van der Veen
 * Date of creation 28-10-2015
 *
 * Authors: Micha�l van der Veen,
 *
 * Version: 2
 * Package: default
 * Class: nl.jozefbv.weatherx.WebClientCOnn
 * Description:
 * This is the initial class that Handles the sessions.
 *
 *
 * Changelog:
 * 2 completion of Filter Class Added documentation
 *
 *
 *//*

@WebSocket
public class WebClientConn {

    */
/**
     * Handling a closed connection
     * @param session session of request
     * @param statusCode status code
     * @param reason closing reason
     *//*

    @OnWebSocketClose
    public void onClose(Session session,int statusCode, String reason){
        System.out.println("Session closed id:"+session.getLocalAddress());
        Filter.stopData(session);

    }

    @OnWebSocketConnect
    public void onConnect(Session session){

    }

    */
/**
     * Handling the command given by Session
     * @param session session with request
     * @param command command of request
     *//*

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
                case "GET_WORLD":
                    for(String a: args){
                        line+=a+ "|";
                    }
                    System.out.println(line);
                    Filter.sendWorld(session,args);
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
*/
