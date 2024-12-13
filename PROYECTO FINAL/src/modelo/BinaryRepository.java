package modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;






public class BinaryRepository implements IRepository{
    
    private List<Task> tasks = new ArrayList<>();



    @Override
    public Task añadirTask(Task task){
        
        
        tasks.add(task);
        
        return task; 
    }

    

    @Override
public void modificarTarea(Task nuevaTarea) {
    boolean flag = false;

    for (Task task : tasks) {
        if (task.getIdentifier() == nuevaTarea.getIdentifier()) {
            task.setTitle(nuevaTarea.getTitle());
            task.setContent(nuevaTarea.getContent());
            task.setDate(nuevaTarea.getDate());
            task.setPriority(nuevaTarea.getPriority());
            task.setEstimatedDuration(nuevaTarea.getEstimatedDuration());
            task.setCompleted(nuevaTarea.isCompleted());
            flag = true;
            break;
        }
    }

    if (!flag) {
        throw new IllegalArgumentException("No se encontró una tarea con el identificador: " + nuevaTarea.getIdentifier());
    }
}


    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void borrarTarea(int identificador){
        
        boolean flag=false;

        for(Task task:tasks){
            if(task.getIdentifier()==identificador){
                tasks.remove(task);
                flag=true;
                break;
            }

        }

        if (!flag) {
        throw new IllegalArgumentException("No implementado aún");
    }
}

    @Override
    public List<Task> listaTareasPendientes(){
        List<Task> pendiente = new ArrayList<>();

        for(Task task: tasks){
            if(!task.isCompleted()){
                pendiente.add(task);
            }
        }

        if(pendiente.isEmpty()){
            throw new IllegalArgumentException("No implementado aún ");
        }

        Collections.sort(pendiente, Comparator.comparingInt(Task::getPriority).reversed());

        return pendiente;

    }

    @Override
    public List<Task> listarHistorial(){
        return new ArrayList<>(tasks);

    }

    
    public void guardarTareas(String archivo){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))){
            oos.writeObject(tasks);
            System.out.println("Guardadas perfectamente ");
        }
        catch(IOException e){
            System.out.println("Error al guardar ");
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarTareas(String archivo){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
            tasks = (List<Task>) ois.readObject();
            System.out.println("Cargadas perfectamente... ");
        }
        catch(FileNotFoundException e){
            System.out.println("Error al cargar... ");
        }

       catch (ClassNotFoundException e) {
        System.out.println("Clase no encontrada... ");
        tasks = new ArrayList<>();
        } 
   

        catch(IOException e){
            System.out.println("Error al cargar... ");
            tasks=new ArrayList<>();
        }
    }


    public void exportarCSV(String archivo) {
        CSVExporter csvHandler = new CSVExporter();
        try {
            csvHandler.exportar(tasks, archivo); 
        } catch (IOException e) {
            System.err.println("Error al exportar tareas a CSV... ");
        }
    }

    public List<Task> importarCSV(String archivo) {
        CSVImporter csvHandler = new CSVImporter();
        try {
            List<Task> tareasImportadas = csvHandler.importarTasks(archivo);
            for (Task tarea : tareasImportadas) {
                añadirTask(tarea);
            }
            System.out.println("Tareas importadas correctamente desde CSV... ");
            return tareasImportadas; 
        } catch (IOException e) {
            System.err.println("Error al importar tareas desde CSV... ");
            return new ArrayList<>();
        }
    }



    public void exportarJSON(String archivo){
        JSONmanejador  manejadorJSON = new JSONmanejador ();

        try {
            manejadorJSON.exportarJSON(tasks, archivo);
        } catch (IOException e) {
            System.err.println("Error al exportar tareas... ");
        }
    }

    public List<Task> importarJSON(String archivo) {
        JSONmanejador manejadorJSON = new JSONmanejador();
        try {
            List<Task> tasks = manejadorJSON.importarJSON(archivo);
            for (Task tarea : tasks) {
                añadirTask(tarea); 
            }
            System.out.println("Tareas importadas correctamente desde JSON... ");
            return tasks;
        } catch (IOException e) {
            System.err.println("Error al importar tareas desde JSON ");
            return new ArrayList<>();
        }
    }
    


    public void exportarXML(String archivo){
        XMLcontrolador xmlcontrolador = new XMLcontrolador();
    try {
        xmlcontrolador.exportarXML(tasks, archivo);
        System.out.println("Tareas exportadas correctamente a XML... ");
    } catch (IOException e) {
        System.out.println("Error al exportar tareas a XML... ");
    }
    }

    public List<Task> importarXML(String archivo){

        XMLcontrolador xmlcontrolador = new XMLcontrolador();

        try {
            List<Task> tasks = xmlcontrolador.importarXML(archivo);
            for (Task task : tasks) {
                añadirTask(task);
            }
            System.out.println("Tareas importadas correctamente desde XML... ");
            return tasks;
        } catch (IOException e) {
            System.out.println("Error al importar tareas desde XML... ");
            return new ArrayList<>();
        }

    }

    

}
