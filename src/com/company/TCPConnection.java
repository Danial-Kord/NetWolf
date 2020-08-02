
package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class TCPConnection {
    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    public static TCPConnection entrySocket;





    public void startServer(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            while (true) {
                System.out.println("Waiting for a client ...");
                socket = server.accept();
                System.out.println("Client accepted");
                Thread client = new Thread(new ClientThreads(socket));
                client.start();


            }

            // close connection

        } catch (IOException i) {
            System.out.println("کلا نشد که بسازمش");
            System.out.println(i);
        }
    }
}

class ClientThreads implements Runnable { // this class use just for making threads in server
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientThreads(Socket socket) throws IOException {
        this.socket = socket;
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public void run() {

    }






    public static String reader(DataInputStream in){
        String out = mainReader(in);
        int i=0;
        while (out == null) {
            out = mainReader(in);
            try {
                TimeUnit.MILLISECONDS.sleep(25); // time to wait for receiving
                i++;
                if(i >= 200)
                    break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(out == null)
            return "";
        return out;
    }

    private static String mainReader(DataInputStream in)  { // receiver need to recognize size of input in first of communication
        synchronized(in) {//inputStream object should be lock not whole method
            try {
                if (in.available() != 0) {
                    try {

                        int length = in.readInt();
                        System.out.println(length);
                        byte[] messageByte = new byte[length];
                        boolean end = false;
                        StringBuilder dataString = new StringBuilder(length);
                        int totalBytesRead = 0;
                        while (!end) {
                            int currentBytesRead = in.read(messageByte);
                            totalBytesRead = currentBytesRead + totalBytesRead;
                            if (totalBytesRead <= length) {
                                dataString
                                        .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                            } else {
                                dataString
                                        .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                                StandardCharsets.UTF_8));
                            }
                            if (dataString.length() >= length) {
                                end = true;
                            }
                        }
                        System.out.println(dataString);
                        return dataString.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("some problem in reading from socket");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void transmitter(DataOutputStream out, String massage) { // server dont say length of massage and client will handle this
        synchronized (out) {
            System.out.println("send : "+massage);
            try {
                byte[] dataInBytes = massage.getBytes(StandardCharsets.UTF_8);
                out.write(dataInBytes);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}