package modelo;

import java.io.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CSVExporter implements IExporter {
    
    @Override
    public void exportar(List<Task> tasks, String archivo ) throws IOException{
        
       List<String> lineas = new ArrayList<>();

       lineas.add("identifier,title,date,content,priority,estimatedDuration,completed");

       for(Task task: tasks){
        lineas.add(String.format("%d,%s,%s,%s,%d,%d,%b",
                task.getIdentifier(),
                task.getTitle(),
                task.getDate(),
                task.getContent(),
                task.getPriority(),
                task.getEstimatedDuration(),
                task.isCompleted()
            ));
       }

       Path ruta = Path.of(archivo);
        Files.write(ruta, lineas, StandardCharsets.UTF_8);
        System.out.println("Tareas exportadas exitosamente a " + archivo + "... ");
    }

}  