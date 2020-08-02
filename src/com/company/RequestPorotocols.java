package com.company;

import java.util.ArrayList;

public class RequestPorotocols {
    private static String endOfReq = ";";
    private static String discoveryBaseReq = "D:";
    private static String fileBaseReq = "F:";
    private static String fileFoundBaseReq = "Found:";
    private static String dataContainer = ",";
    private static String dataSpliter = "_";


    public static String getDiscoveryRequest(ArrayList<Node>myNodes){

        Node target = null;
        String out=discoveryBaseReq;
        for (int i=0;i<myNodes.size();i++) {
            target = myNodes.get(i);
            out +=   target.ip + dataContainer + target.port + dataContainer + target.name ;
            if(i != myNodes.size()-1){
                out+=dataSpliter;
            }
        }
        out+=endOfReq;
        return out;
    }



    public static String getFileRequset(String fileName,String clientName){
        String out=fileBaseReq;
        out +=   fileName + dataContainer + clientName;
        out += endOfReq;
        return out;
    }

    public static String getFileFoundRequset(int tcpPort,String clientName){
        String out=fileFoundBaseReq;
        out +=   tcpPort + dataContainer + clientName;
        out += endOfReq;
        return out;
    }



    public static void InputHandler(StringBuilder input){
        String data= input.toString();
        System.out.println("recieved :" + data);

        if(data.endsWith(";")) {
            data = data.substring(0,data.length()-1);
            if (data.startsWith("D:")) {
                data = data.replace("D:", "");
                ArrayList<Node> nodeArrayList = new ArrayList<>();
                String[] d = data.split("_");
                for (int i = 0; i < d.length; i++) {
                    String[] nodeData = d[i].split(",");
                    nodeArrayList.add(new Node(nodeData[0], Integer.parseInt(nodeData[1]), nodeData[2]));
                }
                Node.mainNode.addNodes(nodeArrayList);
            }
            else if(data.startsWith("F:")){
                data = data.replace("F:", "");
                String[] d = data.split(",");
                Node.mainNode.hadleFileReq(d[0],d[1]);
            }

            else if(data.startsWith("Found:")){
                data = data.replace("Found:", "");
                String[] d = data.split(",");
                Node.mainNode.handleFileResponse(Integer.parseInt(d[0]),d[1]);
            }


        }
    }
}
