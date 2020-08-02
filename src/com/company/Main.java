package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


	Node mainNode = new Node("172.17.86.177",1234,"ali");
	mainNode.addNode(new Node("127.0.0.1",2080,"asghar"));
		Node.mainNode.mainLoop();
    }
}
class requestReciever implements Runnable{

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter time for discovery check");
		while (true){
			String req = scanner.nextLine();
			if(req.startsWith("GET")){
				Node.mainNode.fileReq(req.split(" ")[1]);
			}
			else if(req.startsWith("TIME")){

				Node.discoveryDelay = Integer.parseInt(req.split(" ")[1]);
			}
			else if(req.startsWith("FOLDER")){
				Node.folderPath = req.split(" ")[1];
			}
			else if(req.startsWith("START")){
				Node.mainNode.mainLoop();
			}

		}
	}
}
