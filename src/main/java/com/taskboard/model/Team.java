package com.taskboard.model;

public class Team {
    // Esta clase representa un equipo simple
    private int id;
    private String name;
    private String icon;
    private int taskCount;

    public Team(int id, String name, String icon, int taskCount){
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.taskCount = taskCount;


    }

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public String getIcon(){
        return this.icon;
    }

    public int getTaskCount(){
        return this.taskCount;
    }
}

