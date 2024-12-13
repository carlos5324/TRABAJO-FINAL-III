package modelo;

import java.util.*;

public interface IRepository {
    
    Task añadirTask(Task task); 
    void borrarTarea(int identificador);
    void modificarTarea(Task task);
    List<Task> getTasks();
    List<Task> listaTareasPendientes();
    List<Task> listarHistorial();

    void exportarCSV(String fileName);

    List<Task> importarCSV(String fileName);
    
    void exportarJSON(String fileName);
    
    List<Task> importarJSON(String fileName);
    
    void exportarXML(String fileName);
    
    List<Task> importarXML(String fileName) ;


}
