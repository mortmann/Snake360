package com.snake.game.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreFileHandler {
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void saveScoreTable(Scoreboard scoreBoard){
		HashMap<String,Scoreboard> map = new HashMap<String,Scoreboard>();
		
		FileHandle f = Gdx.files.external("Snake360"+ File.separator + "Snake360.data");
		if(Gdx.files.external("Snake360").exists()==false){
			Gdx.files.external("Snake360").mkdirs();
		}
		ObjectOutputStream out = null;
		File file = new File(f.path());
		if (file.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(f.read());
				map = (HashMap) ois.readObject();
				ois.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else {
			try {
				
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		map.put(scoreBoard.getName(), scoreBoard);
		try {
			OutputStream fos = (OutputStream)f.write(false);
			out = new ObjectOutputStream(fos);
			out.writeObject(map);
			out.flush();
			out.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	@SuppressWarnings("unchecked")
	public static Scoreboard loadScoreTable(String name){
		FileHandle f = Gdx.files.external("Snake360"+ File.separator + "Snake360.data");
		ObjectInputStream ois;
		HashMap<String,Scoreboard> map = new HashMap<String,Scoreboard>();
		try {
			if(f.exists()==false){
				saveScoreTable(new Scoreboard(10,name));
				return null;
			}
			ois = new ObjectInputStream(f.read());
			map = (HashMap<String, Scoreboard>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map.get(name);
	}
	
}
