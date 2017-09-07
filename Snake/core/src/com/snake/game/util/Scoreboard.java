package com.snake.game.util;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

public class Scoreboard implements Serializable {
    private static final float LABEL_HEIGHT = 25.0f;
    private static final float NAME_LABEL_WIDTH = 300.0f;
    private static final float SCORE_LABEL_WIDTH = 75.0f;
    private static final float SPACE_WIDTH = 25.0f;
    private static final long serialVersionUID = 4025333258290850818L;
    private String name;
    private transient Table scoreTable;
    
    public TreeMap<Integer,String> scores;
    
    private int size = 10;
    private int startRank = 0;

    public Scoreboard(int size, String name) {
        this.size = size;
        this.name = name;
        this.scores = new TreeMap<Integer,String>(Collections.reverseOrder());
        this.startRank = 0;
    }

    public Scoreboard(TreeMap<Integer,String> scores, int startRank) {
        this.scores = scores;
        this.size = scores.size();
        this.startRank = startRank;
    }

    public Scoreboard(ArrayList<String> names, ArrayList<Integer> scores, int startRank) {
    	this.scores = new TreeMap<Integer, String>(Collections.reverseOrder());
    	for (int i = 0; i < names.size(); i++) {
    		this.scores.put(scores.get(i), names.get(i));
		}
        this.startRank = startRank;
    }

	public boolean addScore(String name, int score) {
        boolean added = false;
        if(scores.size()>this.size){
	        for (int i : scores.keySet()) {
	            if (score > i) {
	                this.scores.put(score, name);
	                added = true;
	                scores.remove(scores.lastEntry());
	            }
	        }
        } else {
            this.scores.put(score, name);
            added = true;
        }
        if (added) {
            MyClient.sendHighScore(this.name, name, score);
            MyClient.close();
        }
        return added;
    }

    public Table getScoreTable(Skin skin) {
        this.scoreTable = new Table(skin);
        this.scoreTable.center().top();
        this.scoreTable.add("NAME").size((float) NAME_LABEL_WIDTH, 25.0f);
        this.scoreTable.add().size(25.0f, 25.0f);
        this.scoreTable.add("SCORE").size((float) SCORE_LABEL_WIDTH, 25.0f).row();
        int rank = (this.startRank);
        for (int i : scores.keySet()) {
        	rank++;
            if (this.scores.get(i) != null) {
                if (rank >= 10) {
                    this.scoreTable.add(new StringBuilder(String.valueOf(rank)).append(". ").append((String) this.scores.get(i)).toString()).size((float) NAME_LABEL_WIDTH, 25.0f);
                } else {
                    this.scoreTable.add(new StringBuilder(String.valueOf(rank)).append(".   ").append((String) this.scores.get(i)).toString()).size((float) NAME_LABEL_WIDTH, 25.0f);
                }
                this.scoreTable.add().size(25.0f, 25.0f);
                this.scoreTable.add(" " + i + " ").align(1).size((float) SCORE_LABEL_WIDTH, 25.0f).row();
            } else {
                this.scoreTable.add(new StringBuilder(String.valueOf(rank)).append(" NONE ").toString()).size((float) NAME_LABEL_WIDTH, 25.0f);
                this.scoreTable.add().size(25.0f, 25.0f);
                this.scoreTable.add(" NULL ").align(1).size((float) SCORE_LABEL_WIDTH, 25.0f).row();
            }
        }
        return this.scoreTable;
    }

    public void loadScoreTable() {
    }

    public static float getNameLabelWidth() {
        return NAME_LABEL_WIDTH;
    }

    public static float getSpaceWidth() {
        return 25.0f;
    }

    public static float getScoreLabelWidth() {
        return SCORE_LABEL_WIDTH;
    }

    public static float getLabelHeight() {
        return 25.0f;
    }

    public String getName() {
        return this.name;
    }
}
