package com.snake.game.util;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.snake.game.states.Highscore;

public class MyClient {

	private Client client;
	private MyNetworkListener nl;
	private boolean isConnected;
	private Highscore highscoreScreen;


	public MyClient(){
		
		client = new Client();
		nl = new MyNetworkListener(this);
		client.addListener(nl);
		nl.init(client);
		registerPackets();
		
		
		client.start();
		try {
			client.connect(60000, "localhost", 55555);
			
		} catch (IOException e) {
			System.out.println("ERROR NOT CONNECTED");
			client.stop();
		}
		
	}
	public MyClient(Highscore highscore){
		
		client = new Client();
		nl = new MyNetworkListener(this);
		client.addListener(nl);
		nl.init(client);
		
		this.highscoreScreen=highscore;
		
		
		registerPackets();
		client.start();
		try {
			client.connect(60000, "localhost", 55555);
			
		} catch (IOException e) {
			System.out.println("ERROR NOT CONNECTED");
			client.stop();
		}
	}
	
	private void registerPackets() {
		Kryo kryo =client.getKryo();
		kryo.register(java.util.ArrayList.class);
		kryo.register(Packets.Packet0LoginRequest.class);
		kryo.register(Packets.Packet1LoginAnswer.class);
		kryo.register(Packets.Packet2ClientScoreMessage.class);
		kryo.register(Packets.Packet3ServerScoreMessage.class);
		kryo.register(Packets.Packet4ClientGetTopScoreMessage.class);
		kryo.register(Packets.Packet5ServerTopScoreMessage.class);
	}
	

	public void sendHighScore(String nameOfTable,String name, int score){
		if(isConnected){
			Packets.Packet2ClientScoreMessage message = new Packets.Packet2ClientScoreMessage();
			message.name=name;
			message.score=score;
			message.tableName=nameOfTable;
			client.sendTCP(message);
		}
	}
	public void sendGetTopScore(String nameOfTable){
		System.out.println("send 1");
		if(isConnected){
			System.out.println("send 2");
			Packets.Packet4ClientGetTopScoreMessage message = new Packets.Packet4ClientGetTopScoreMessage();
			message.tableName=nameOfTable;
			client.sendTCP(message);
		}
	}

	public void close() {
		try {
			client.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void setClientScore(String tableName, ArrayList<String> data) {
		
	}


	public void setTopScore(String tableName, ArrayList<String> data) {
		highscoreScreen.setTopScore(tableName,data);
	}


	public void setConntected(boolean b) {
		isConnected=b;
	}
}
