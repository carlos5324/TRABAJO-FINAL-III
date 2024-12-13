package modelo;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task implements Serializable {
   
    private  static final long serialVersionUID = 1L;
    private int identifier,priority,estimatedDuration;
    private String title,content;

    private String notionId;

    public String getNotionId() {
        return notionId;
    }

    public void setNotionId(String notionId) {
        this.notionId = notionId;
    }
    

    @JsonFormat(pattern = "yyyy-MM-dd")  
    private LocalDate date;

    private boolean completed;

    public Task() {}
   
    public Task(int identifier, int priority, int estimatedDuration, String title, String content, LocalDate date,
            boolean completed) {


        if(identifier<0){
            throw new IllegalArgumentException("El identificador no puede ser negativo... ");
        }

        if(priority<1||priority>5){
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5... ");
        }

        if(title==null){
            throw new IllegalArgumentException("El nombre no puede quedar vacio... ");
        }

        if(estimatedDuration <0){
            throw new IllegalArgumentException("La estimación no puede ser negativa... ");
        }

        this.identifier = identifier;
        this.priority = priority;
        this.estimatedDuration = estimatedDuration;
        this.title = title;
        this.content = content;
        this.date = date;
        this.completed = completed;
    }

    

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {

        if(identifier<0){
            throw new IllegalArgumentException("El identificador no puede ser negativo... ");
        }
        this.identifier = identifier;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
       
        if(priority<1||priority>5){
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5... ");
        }
        this.priority = priority;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {

        if(estimatedDuration <0){
            throw new IllegalArgumentException("La estimación no puede ser negativa... ");
        }
        this.estimatedDuration = estimatedDuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        if(title==null){
            throw new IllegalArgumentException("El nombre no puede quedar vacio... ");
        }
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public static Task fromCSV(String linea) {
    String[] partes = linea.split(",");
    if (partes.length != 7) {
        throw new IllegalArgumentException("Error al pasar a CSV... ");
    }

    int identifier = Integer.parseInt(partes[0]);
    String title = partes[1];
    LocalDate date = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String content = partes[3];
    int priority = Integer.parseInt(partes[4]);
    int estimatedDuration = Integer.parseInt(partes[5]);
    boolean completed = Boolean.parseBoolean(partes[6]);

    return new Task(identifier, priority, estimatedDuration, title, content, date, completed);
}

    @Override

    public String toString() {
    return String.format(
        "ID=%d, Title='%s', Date=%s, Content='%s', Priority=%d, Duration=%d mins, Completed=%b",
        identifier, title, date, content, priority, estimatedDuration, completed
    );
}

    



} 