package com.taskboard.model;

import java.awt.geom.FlatteningPathIterator;
import java.security.cert.TrustAnchor;

public class Team {
    // Esta clase representa un equipo simple
    private final int id;
    private final String name;
    private final String icon;

    public Team(int id, String name, String icon){
        this.id = id;
        this.name = name;
        this.icon = icon;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        return id == ((Team) obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override 
    public String toString() {
        return "Team{id="+ id + ", name='" + name + "' , icon='" + icon + "'}";
    }
}

