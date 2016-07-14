package com.snake.game.util;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class MyNetworkListener extends Listener {
	private Client client;
	private MyClient myClient;
	public MyNetworkListener(MyClient myClient){
		this.myClient=myClient;
	}

	public void init(Client client) {

		this.client=client;
	}
	
	public void connected (Connection connection) {
		client.sendTCP(new Packets.Packet0LoginRequest());
		System.out.println("CONNECTED");
	}

	public void disconnected (Connection connection) {

	}

	public void received (Connection connection, Object object) {
		System.out.println("RECEIVED " + object.toString());
		
		if(object instanceof Packets.Packet1LoginAnswer){
			myClient.setConntected(true);
		} else
		if(object instanceof Packets.Packet3ServerScoreMessage){
			myClient.setClientScore(((Packets.Packet3ServerScoreMessage) object).tableName,((Packets.Packet3ServerScoreMessage) object).data);
		} else
		if(object instanceof Packets.Packet5ServerTopScoreMessage){
			myClient.setTopScore(((Packets.Packet5ServerTopScoreMessage) object).tableName,((Packets.Packet5ServerTopScoreMessage) object).data);
		}
	}

	
	
}
