package com.taskboard.model;

import java.util.Objects;

public final class Task {
    // Esta clase representa una tarea
    
    private final String title;
    private final String description;
    private final int id;
    private final int teamId;
    private final TaskStatus status;
    private final int position;

    public Task(int id, int teamId, String title, String description, TaskStatus status, int position){
        this.id = id;
        this.teamId = teamId;

        this.title = Objects.requireNonNull(title, "title").trim();
        if (this.title.isBlank()){
            throw new IllegalArgumentException("title must not be blank");
        }

        this.description = description == null ? null : description.trim(); // La descripcion si puede ser nula.
        this.status = Objects.requireNonNull(status, "status");
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
    
    public TaskStatus getStatus() {
        return status;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }

    @Override 
    public String toString(){
        return "Task{id=" + id + ", teamId=" + teamId + ", title='" + title + "', status=" + status + ", position=" + position + "}";
    }

    @Override 
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Task other = (Task) obj;
        return this.id == other.id;
    }

    @Override 
    public int hashCode(){
        return Integer.hashCode(id);
    }
}
