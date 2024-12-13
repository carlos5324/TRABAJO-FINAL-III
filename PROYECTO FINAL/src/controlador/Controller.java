package controlador;

import modelo.*;
import view.ConsoleView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import javax.swing.text.View;

import com.coti.tools.Esdia;



public class Controller {
    
    ConsoleView console;
    Scanner scanner = new Scanner(System.in);

    IRepository repositorio;
    

    public Controller(IRepository repositorio){
        this.repositorio=repositorio;
        
    }

    public void setConsole(ConsoleView console) {
        this.console = console;
    }

    public IRepository getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(IRepository repositorio) {
        this.repositorio = repositorio;
    }

    


public void añadirTarea(){

    try {
        int id = -1; 
        
        Task task;
        boolean idValido = false;

        do {
            id = Esdia.readInt("Introduce ID: ");
            task = console.pedirDatos(id);
            List<Task> tareas = repositorio.getTasks();

            idValido = true;
            for (Task tarea : tareas) {
                if (id == tarea.getIdentifier()) {
                    console.showErrorMessage("ID ocupada, seleccione otra ");
                    idValido = false;
                    break;
                }
            }
        } while (!idValido);

        repositorio.añadirTask(task);
        console.showMessage("Tarea añadida con éxito ");
    } catch(Exception e){
        console.showErrorMessage("Error al añadir tarea ");
    }

}


public void mostrarListaPrioridad(){
    try{
        List<Task> tasks = repositorio.listaTareasPendientes();
        for(Task task: tasks){
            console.showMessageS(task);
        }
    }catch(Exception e){
        console.showErrorMessage("La lista aún no está cargada... ");
    }
}

public void mostrarListaCompleta(){
    try{
        List<Task> tasks = repositorio.listarHistorial();

        for(Task task: tasks){
            console.showMessageS(task);
        }
    }catch(Exception e){
       console.showErrorMessage("No hay información disponible aún... ");
    }
}

public void editarTarea(int id){

    try{

        
        

        

        Task tasknueva = console.pedirDatos(id);
        repositorio.modificarTarea(tasknueva);

    } catch(Exception e){
        console.showErrorMessage("Error al editar tarea ");
    }

}

public void eliminarTarea(int id){

    try{
        repositorio.borrarTarea(id);
        console.showMessage("Tarea eliminada correctamente ");
    } catch(Exception e){
        console.showErrorMessage("Error al eliminar la tarea ");

    }




}

public void exportarTareas(String f){
    try{
        switch (f) {
            case "CSV":
            repositorio.exportarCSV("tareas.csv");
                break;
            case "JSON":
            repositorio.exportarJSON("tareas.json");
                break;
            case "XML":
            repositorio.exportarXML("tareas.xml");
                break;
            default:
                System.out.println("Formato desconocido");
                return;
            
        
            
        }

    }catch (Exception e) {
        console.showErrorMessage("Error al importar tareas: ");
    }
}
public void importarTareas(String f){
    try{
        switch (f) {
            case "CSV":
            repositorio.importarCSV("tareas.csv");
                break;
            case "JSON":
            repositorio.importarJSON("tareas.json");
                break;
            case "XML":
            repositorio.importarXML("tareas.xml");
                break;
            default:
                System.out.println("Formato desconocido");
            
        
            
        }
    }catch (Exception e) {
        console.showErrorMessage("Error al importar tareas: ");
    }
}

public void marcarCompleta(int id) {
    cambiarEstadoTarea(id, true);
}

public void marcarIncompleta(int id) {
    cambiarEstadoTarea(id, false);
}


public void cambiarEstadoTarea(int id, boolean estado) {
    try {
        Task tareaEncontrada = null;
        List<Task> tasks = repositorio.getTasks(); 
        
       
        for (Task task : tasks) {
            if (id == task.getIdentifier()) {
                tareaEncontrada = task;
                break;
            }
        }

       
        if (tareaEncontrada != null) {
            tareaEncontrada.setCompleted(estado);
            repositorio.modificarTarea(tareaEncontrada); 
            console.showMessage("Estado cambiado con éxito.");
        } else {
            console.showErrorMessage("No se encontró la tarea con el ID proporcionado.");
        }
    } catch (Exception e) {
        console.showErrorMessage("Se ha producido un error al cambiar el estado: " + e.getMessage());
    }
}


public void showMessage(String string){
    console.showMessage(string);
}
}