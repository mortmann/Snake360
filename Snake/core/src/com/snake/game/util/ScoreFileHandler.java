package com.snake.game.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreFileHandler {
	
	
	
	@SuppressWarnings("unchecked")
	public static void saveScoreTable(Scoreboard scoreBoard){
			HashMap<String,Scoreboard> map = new HashMap<String,Scoreboard>();
			
			FileHandle f = Gdx.files.external("Snake360.data");
			ObjectOutputStream out = null;
			
				File file = new File(f.path());
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(new FileInputStream(f.path()));
					map = (HashMap<String, Scoreboard>) ois.readObject();
					ois.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			map.put(scoreBoard.getName(), scoreBoard);
			try{
				FileOutputStream fis = new FileOutputStream(f.path());
				out = new ObjectOutputStream(fis);  
		        out.writeObject(map);
		        out.flush();
		        out.close();
		        fis.close();
		    }catch(Exception e){
		        e.printStackTrace();
		    }finally {
				try {
					if(out!=null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
	}
	
	@SuppressWarnings("unchecked")
	public static Scoreboard loadScoreTable(String name){
		FileHandle f = Gdx.files.external("Snake360.data");
		ObjectInputStream ois;
		HashMap<String,Scoreboard> map = new HashMap<String,Scoreboard>();
		try {
			if(f.exists()==false){
				saveScoreTable(new Scoreboard(10,name));
				return null;
			}
			FileInputStream fis = new FileInputStream(f.path());
			ois = new ObjectInputStream(fis);
			
			map = (HashMap<String, Scoreboard>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map.get(name);
	}
}
