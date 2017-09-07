package com.snake.game.util;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Scoreboard implements Serializable {
    private static final float LABEL_HEIGHT = 25.0f;
    private static final float NAME_LABEL_WIDTH = 300.0f;
    private static final float SCORE_LABEL_WIDTH = 75.0f;
    private static final float SPACE_WIDTH = 25.0f;
    private static final long serialVersionUID = 4025333258290850818L;
    private String name;
    private LinkedList<String> names;
    private transient Table scoreTable;
    private LinkedList<Integer> scores;
    private int size;
    private int startRank = 10;

    public Scoreboard(int size, String name) {
        this.size = size;
        this.name = name;
        this.names = new LinkedList();
        this.scores = new LinkedList();
        this.startRank = 0;
    }

    public Scoreboard(ArrayList<String> names, ArrayList<Integer> scores, int startRank) {
        this.names = new LinkedList(names);
        this.scores = new LinkedList(scores);
        this.size = names.size();
        this.startRank = startRank;
    }

    public boolean addScore(String name, int score) {
        boolean added = false;
        for (int i = 0; i < this.scores.size(); i++) {
            if (score > ((Integer) this.scores.get(i)).intValue()) {
                this.scores.add(i, Integer.valueOf(score));
                this.names.add(i, name);
                added = true;
                break;
            }
        }
        if (this.scores.size() < this.size && !added) {
            this.scores.add(Integer.valueOf(score));
            this.names.add(name);
        }
        if (this.scores.size() > this.size) {
            this.scores.removeLast();
            this.names.removeLast();
        }
        if (added) {
            MyClient client = new MyClient();
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
        for (int i = 0; i < this.scores.size(); i++) {
            int rank = (this.startRank + i) + 1;
            if (this.names.get(i) != null) {
                if (rank >= 10) {
                    this.scoreTable.add(new StringBuilder(String.valueOf(rank)).append(". ").append((String) this.names.get(i)).toString()).size((float) NAME_LABEL_WIDTH, 25.0f);
                } else {
                    this.scoreTable.add(new StringBuilder(String.valueOf(rank)).append(".   ").append((String) this.names.get(i)).toString()).size((float) NAME_LABEL_WIDTH, 25.0f);
                }
                this.scoreTable.add().size(25.0f, 25.0f);
                this.scoreTable.add(" " + this.scores.get(i) + " ").align(1).size((float) SCORE_LABEL_WIDTH, 25.0f).row();
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
