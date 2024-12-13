package modelo;
import java.sql.Date;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class CSVImporter {
   
   
    public List<String> leerArchivoCSV(String archivo) throws IOException{
        Path ruta = Path.of(archivo);
        return Files.readAllLines(ruta);

        
    }

    public List<Task> importarTasks(String archivo) throws IOException{
        
        List<String> lineas = leerArchivoCSV(archivo);
        List<Task> tareas = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            try {
                Task tarea = Task.fromCSV(linea);
                tareas.add(tarea);
            } catch (IllegalArgumentException e) {
                System.out.println("Error al procesar la línea: " + linea + "... ");
            }
        }
        return tareas;
    }

}
