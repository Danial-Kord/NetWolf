
package com.company;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class UdpSystem{
    public int port;

    public UdpSystem(int port){
        this.port = port;
        Thread recieve = new Thread(new UDPRecieve(port));
        recieve.start();
    }
    public void send(String data,int port,String ip){
        Thread send = new Thread(new UDPSend(port,data,ip));
        send.start();
    }
}


class UDPSend implements Runnable{

    private int port;
    private String data;
    private String ip;
    public UDPSend(int port,String data,String ip){
        this.data = data;
        this.port = port;
    }
    public void sendData(){
        DatagramSocket ds = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            ds = new DatagramSocket();
            /*if(!ds.isConnected()){
                ds.close();
                return;
            }*/

            byte[] sendData = data.getBytes();

            DatagramPacket sender = null;
            int i=0;
            boolean cond = true;
            while (cond)
            {

                // Step 2 : create a DatgramPacket to receive the data.
                int length = i+200;
                if(length > sendData.length) {
                    length = sendData.length;
                    cond = false;
                }
                sender = new DatagramPacket(sendData,i,length,inetAddress,port);

                // Step 3 : revieve the data in byte buffer.
                ds.send(sender);
                i+=200;
            }
        } catch (IOException e) {
            //  e.printStackTrace();
            System.out.println("error on port : "+port);
        }
        if(ds != null)
            ds.close();
    }
    @Override
    public void run() {
        sendData();
    }
}


class UDPRecieve implements Runnable{

    public int port;
    public UDPRecieve(int port){
        this.port = port;
    }
    @Override
    public void run() {
        while (true){
            recieve();
            try {
                TimeUnit.MILLISECONDS.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void recieve(){
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(port);

            byte[] receive = new byte[200];

            DatagramPacket DpReceive = null;

            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);

            // Step 3 : revieve the data in byte buffer.
            ds.receive(DpReceive);
            StringBuilder data = data(receive);
            System.out.printf("recieved :" + data);
            RequestPorotocols.InputHandler(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(ds!=null){
            ds.close();
        }
    }




    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}


