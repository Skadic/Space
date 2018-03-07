package sebet.space.networking;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import sebet.space.utils.CallableFunction;

/**
 * Created by eti22 on 28.06.2017.
 */

public class SocketClient extends CallableFunction<String, Boolean> {

    private InetAddress host;
    private int port;

    public static final int PORT = 4004;

    public SocketClient(String host, int port) {
        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IP: " + host, e);
        }
        if (port >= 0 && port <= 65536) {
            this.port = port;
        } else {
            throw new IllegalArgumentException("Port out of bounds for PORT: " + port);
        }
    }

    public SocketClient(){port = PORT;}

    @Override
    protected Boolean operate(String s){
        if(host == null)
            throw new IllegalArgumentException("IP has not been set");
        if(port < 0 || port > 65536)
            throw new IllegalArgumentException("invalid PORT: " + port);
        try (
                Socket socket = new Socket(host, port);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter writer = new BufferedWriter(osw)){

            writer.write(s);
            writer.newLine();
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the HOST of the Socket connection
     * @param host the HOST name/IP
     * @throws IllegalArgumentException if the HOST name cannot by resolved
     */
    public void setHost(String host) throws IllegalArgumentException{
        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IP: " + host);
        }
    }

    public void setIp(InetAddress ip) {
        this.host = ip;
    }

    /**
     * Sets the PORT to which data should be sent
     * @param port An Integer in the range of 0 to 65536
     * @throws IllegalArgumentException if PORT is out of bounds
     */
    public void setPort(int port) throws IllegalArgumentException{
        if(port >= 0 && port <= 65536) {
            this.port = port;
        } else {
            throw new IllegalArgumentException("Port out of bounds for PORT: " + port);
        }
    }

    public String getHost() {
        return host.getHostName();
    }

    public int getPort() {
        return port;
    }
}
