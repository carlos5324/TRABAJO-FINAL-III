package modelo;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JSONmanejador {
    
    private static Gson gson = new GsonBuilder()
    
    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
    .create();

    public void exportarJSON(List<Task> tasks, String archivo) throws IOException{
        
        try(Writer writer = new FileWriter(archivo)){
        gson.toJson(tasks,writer);
        System.out.println("Tareas exportadas a JSON... ");
        }
    }

    public List<Task> importarJSON(String archivo) throws IOException{
        try(Reader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());
        }
    }
}