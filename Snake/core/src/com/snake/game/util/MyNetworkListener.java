package com.snake.game.util;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.snake.game.util.Packets.Packet0LoginRequest;
import com.snake.game.util.Packets.Packet1LoginAnswer;
import com.snake.game.util.Packets.Packet3ServerScoreMessage;
import com.snake.game.util.Packets.Packet5ServerTopScoreMessage;

public class MyNetworkListener extends Listener {
    private Client client;
    private MyClient myClient;

    public MyNetworkListener(MyClient myClient) {
        this.myClient = myClient;
    }

    public void init(Client client) {
        this.client = client;
    }

    public void connected(Connection connection) {
        this.client.sendTCP(new Packet0LoginRequest());
        System.out.println("CONNECTED");
    }

    public void disconnected(Connection connection) {
    }

    public void received(Connection connection, Object object) {
        System.out.println("RECEIVED " + object.toString());
        if (object instanceof Packet1LoginAnswer) {
            MyClient.setConntected(true);
        } else if (object instanceof Packet3ServerScoreMessage) {
            this.myClient.setClientScore(((Packet3ServerScoreMessage) object).tableName, ((Packet3ServerScoreMessage) object).data);
        } else if (object instanceof Packet5ServerTopScoreMessage) {
            MyClient.setTopScore(((Packet5ServerTopScoreMessage) object).tableName, ((Packet5ServerTopScoreMessage) object).data);
        }
    }
}
