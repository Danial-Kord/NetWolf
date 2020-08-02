package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Node {
    public static Node mainNode = null;
    public static int discoveryDelay = 2500;
    public static int myTCPPort = 8000;
    private static boolean isFileRecieving = false;
    private static TCPConnection tcpConnection=null;
    public static String folderPath;
    private ArrayList<Node> myNodes;
    private ArrayList<String> names;
    public String ip;
    public String name;
    public int port;
    private UdpSystem udpSystem = null;


    public Node(String ip,int port,String name){
        this.name = name;
        this.port = port;
        this.ip = ip;
        if(mainNode == null) {
            myNodes = new ArrayList<Node>();
            names = new ArrayList<>();
            names.add(name);
            myNodes.add(this);
            mainNode = this;
        }
    }

    private void startUdp(){
        udpSystem = new UdpSystem(port);
    }

    public void mainLoop(){
        startUdp();

        while (true){
            discovery();
            try {
                TimeUnit.MILLISECONDS.sleep(discoveryDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void discovery(){
        for (int i=1;i<myNodes.size();i++) {
            udpSystem.send(RequestPorotocols.getDiscoveryRequest(myNodes),myNodes.get(i).port,myNodes.get(i).ip);
        }
    }

    public void fileReq(String name){
        for (int i=1;i<myNodes.size();i++) {
            udpSystem.send(RequestPorotocols.getFileRequset(name,this.name),myNodes.get(i).port,myNodes.get(i).ip);
        }
    }


    public void hadleFileReq(String fileName,String clientName){
        if(checkForFile(fileName)){
            for (int i=0;i<myNodes.size();i++){
                if(myNodes.get(i).name.equals(clientName)){
                    tcpConnection = new TCPConnection();
                    tcpConnection.startServer(myTCPPort);
                    udpSystem.send(RequestPorotocols.getFileFoundRequset(myTCPPort,this.name),myNodes.get(i).port,myNodes.get(i).ip);
                    break;
                }
            }
        }
    }


    public void handleFileResponse(int tcpPort,String clientName){
        if(!isFileRecieving){
            for (int i=0;i<myNodes.size();i++){
                if(myNodes.get(i).name.equals(clientName)){
                    //TCP startConnection
                    tcpConnection = new TCPConnection();
                    tcpConnection.startClient(tcpPort,myNodes.get(i).ip);
                    break;
                }
            }
        }
    }


    private boolean checkForFile(String fileName){
        ArrayList<String> names = new ArrayList<>();

        names = FileFinder.getNames(new File(folderPath),names);

        for (int i=0;i<names.size();i++){
            if(names.get(i).equals(fileName)){
                return true;
            }
        }
        return false;
    }



    public void addNodes(ArrayList<Node> nodes){
        for (int i=0;i<nodes.size();i++){
            if(nodes.get(i).name.equals(name)){

                for (int j=0;j<nodes.size();j++) {
                    addNode(nodes.get(j));
                }

                break;
            }
        }
    }

    public void addNode(Node node){
        if(!names.contains(node.name)){
            myNodes.add(node);
            names.add(node.name);
        }
    }
    public ArrayList<Node> getMyNodes() {
        return myNodes;
    }
}
