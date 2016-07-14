package com.snake.game.util;

import java.util.ArrayList;

public class Packets {
	public static class Packet0LoginRequest { }
	public static class Packet1LoginAnswer { }
	public static class Packet2ClientScoreMessage {String tableName;String name; int score; }
	public static class Packet3ServerScoreMessage {String tableName;ArrayList<String> data;}
	public static class Packet4ClientGetTopScoreMessage { String tableName; }
	public static class Packet5ServerTopScoreMessage {String tableName;ArrayList<String> data;}
}
