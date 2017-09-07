package com.snake.game.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.snake.game.states.Highscore;
import com.snake.game.util.Packets.Packet0LoginRequest;
import com.snake.game.util.Packets.Packet1LoginAnswer;
import com.snake.game.util.Packets.Packet2ClientScoreMessage;
import com.snake.game.util.Packets.Packet3ServerScoreMessage;
import com.snake.game.util.Packets.Packet4ClientGetTopScoreMessage;
import com.snake.game.util.Packets.Packet5ServerTopScoreMessage;
import java.io.IOException;
import java.util.ArrayList;

public class MyClient {
    private static Client client;
    private static Highscore highscoreScreen;
    private static boolean isConnected;
    private static MyNetworkListener nl;

    public static void setHighscoreScreen(Highscore highscoreScreen) {
        highscoreScreen = highscoreScreen;
    }

    public MyClient() {
        client = new Client();
        nl = new MyNetworkListener(this);
        client.addListener(nl);
        nl.init(client);
        registerPackets();
        client.start();
        try {
            client.connect(3000, "localhost", 55555);
        } catch (IOException e) {
            System.out.println("ERROR NOT CONNECTED");
            client.stop();
        }
    }

    public MyClient(Highscore highscore) {
        client = new Client();
        nl = new MyNetworkListener(this);
        client.addListener(nl);
        nl.init(client);
        highscoreScreen = highscore;
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
        Kryo kryo = client.getKryo();
        kryo.register(ArrayList.class);
        kryo.register(Packet0LoginRequest.class);
        kryo.register(Packet1LoginAnswer.class);
        kryo.register(Packet2ClientScoreMessage.class);
        kryo.register(Packet3ServerScoreMessage.class);
        kryo.register(Packet4ClientGetTopScoreMessage.class);
        kryo.register(Packet5ServerTopScoreMessage.class);
    }

    public static void sendHighScore(String nameOfTable, String name, int score) {
        if (isConnected) {
            Packet2ClientScoreMessage message = new Packet2ClientScoreMessage();
            message.name = name;
            message.score = score;
            message.tableName = nameOfTable;
            client.sendTCP(message);
        }
    }

    public static void sendGetTopScore(String nameOfTable) {
        if (isConnected) {
            Packet4ClientGetTopScoreMessage message = new Packet4ClientGetTopScoreMessage();
            message.tableName = nameOfTable;
            client.sendTCP(message);
        }
    }

    public static void close() {
        try {
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClientScore(String tableName, ArrayList<String> arrayList) {
    }

    public static boolean IsConnected() {
        return client.isConnected();
    }

    public static void setTopScore(String tableName, ArrayList<String> data) {
        highscoreScreen.setTopScore(tableName, data);
    }

    public static void setConntected(boolean b) {
        isConnected = b;
    }
}
