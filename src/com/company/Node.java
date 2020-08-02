package com.company;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Node {
    public static Node mainNode = null;
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
                TimeUnit.MILLISECONDS.sleep(2500);
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
