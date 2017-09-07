package com.snake.game.util;

import java.util.ArrayList;

public class Packets {

    public static class Packet0LoginRequest {
    }

    public static class Packet1LoginAnswer {
    }

    public static class Packet2ClientScoreMessage {
        String name;
        int score;
        String tableName;
    }

    public static class Packet3ServerScoreMessage {
        ArrayList<String> data;
        String tableName;
    }

    public static class Packet4ClientGetTopScoreMessage {
        String tableName;
    }

    public static class Packet5ServerTopScoreMessage {
        ArrayList<String> data;
        String tableName;
    }
}
