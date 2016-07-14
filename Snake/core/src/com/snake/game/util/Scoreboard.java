package com.snake.game.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class Scoreboard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4025333258290850818L;

	private static final float NAME_LABEL_WIDTH=300; 
	private static final float SPACE_WIDTH=25; 
	private static final float SCORE_LABEL_WIDTH=75; 
	private static final float LABEL_HEIGHT=25;	
	
	private LinkedList<String> names;
	private LinkedList<Integer> scores;
	private transient Table scoreTable;
	private int size;
	private String name;
	private int startRank=10;
	public Scoreboard(int size,String name){
		this.size=size;
		this.name=name;
		names=new LinkedList<String>();
		scores=new LinkedList<Integer>();
		startRank=0;
		
	}
	
	public Scoreboard() {
	}

	public Scoreboard(ArrayList<String> names, ArrayList<Integer> scores, int startRank) {
		this.names=new LinkedList<String>(names);
		this.scores=new LinkedList<Integer>(scores);
		size= names.size();
		this.startRank=startRank;
	}

	public boolean addScore(String name, int score){
		boolean added = false;

			for(int i =0 ;i<scores.size();i++){
				if(score > scores.get(i)){
					scores.add(i,score);
					names.add(i, name);
					added=true;
					break;
				}
			}
			if(scores.size()<size){
				if(!added){
					scores.add(score);
					names.add(name);
				}
			}
			if(scores.size()>size){
				scores.removeLast();
				names.removeLast();
			}
			if(added){
				MyClient client = new MyClient();
				client.sendHighScore(this.name, name, score);
				client.close();
			}
			return added;
	}
	
	public Table getScoreTable(Skin skin){
		scoreTable=new Table(skin);
		scoreTable.center().top();
		scoreTable.add("NAME").size(NAME_LABEL_WIDTH, LABEL_HEIGHT);
		scoreTable.add().size(SPACE_WIDTH, LABEL_HEIGHT);
		scoreTable.add("SCORE").size(SCORE_LABEL_WIDTH, LABEL_HEIGHT).row();
		for(int i =0 ;i<scores.size();i++){
			int rank = (startRank+i+1);
			if(names.get(i)!=null){
				if(rank >= 10){
					scoreTable.add((rank) + ". " + names.get(i)).size(NAME_LABEL_WIDTH, LABEL_HEIGHT);
				} else {
					scoreTable.add((rank) + ".   " + names.get(i)).size(NAME_LABEL_WIDTH, LABEL_HEIGHT);
				}
			scoreTable.add().size(SPACE_WIDTH, LABEL_HEIGHT);
			scoreTable.add(" " + scores.get(i) + " ").align(Align.center).size(SCORE_LABEL_WIDTH, LABEL_HEIGHT).row();
			} else {
				scoreTable.add(rank + " NONE ").size(NAME_LABEL_WIDTH, LABEL_HEIGHT);
				scoreTable.add().size(SPACE_WIDTH, LABEL_HEIGHT);
				scoreTable.add(" " + "NULL" + " ").align(Align.center).size(SCORE_LABEL_WIDTH, LABEL_HEIGHT).row();
			}
		}
		return scoreTable;
	}

	public void loadScoreTable(){
		
	}
	public static float getNameLabelWidth() {
		return NAME_LABEL_WIDTH;
	}

	public static float getSpaceWidth() {
		return SPACE_WIDTH;
	}

	public static float getScoreLabelWidth() {
		return SCORE_LABEL_WIDTH;
	}

	public static float getLabelHeight() {
		return LABEL_HEIGHT;
	}

	public String getName() {
		return name;
	}
}
