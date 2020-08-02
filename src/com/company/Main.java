package com.company;

public class Main {

    public static void main(String[] args) {
	Node mainNode = new Node("172.17.86.177",1234,"ali");
	mainNode.addNode(new Node("127.0.0.1",2080,"asghar"));
	mainNode.mainLoop();
    }
}
