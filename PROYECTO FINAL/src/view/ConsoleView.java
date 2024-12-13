package view;

import java.time.LocalDate;
import java.util.*;

import com.coti.tools.Esdia;
import controlador.*;
import modelo.*;

public class ConsoleView extends BaseView {

    Scanner scanner = new Scanner(System.in);
    BinaryRepository repositorio;

    
    public ConsoleView(Controller controller){
        super(controller);
    }

    public void init(){

        boolean salir=false;
        while(!salir){
            System.out.println("\nMENU");
            System.out.println("1. Dar de alta tarea");
            System.out.println("2. Mostrar listado");
            System.out.println("3. Detalle de tarea");
            
            if (!(controller.getRepositorio() instanceof NotionRepository)) {
                System.out.println("4. Importar o exportar tarea");
                
            }

            
            System.out.println("5. Salir");

            int opcion1 = Esdia.readInt("Eliga opcion: ");

            switch (opcion1) {
                case 1:
                    controller.añadirTarea();
                    break;
                case 2:
                    mostrarListado();
                    break;
                case 3:
                    gestionarTarea();
                    break;
                case 4:
                if (!(controller.getRepositorio() instanceof NotionRepository)) {
                    exportarimportar();
                } else {
                    System.out.println("Opción no válida.");
                }
                    break;
                case 5:
                    salir=true;
                    end();
                    break;
                default:
                System.out.println("Opción no valida");
                    break;
            }
        }



    }


    private void mostrarListado(){
        System.out.println("\nLISTADOS");
        System.out.println("1. Listado de tareas pendientes");
        System.out.println("2. Listado completo de tareas");

        int  opcion2= Esdia.readInt("Elegir opcion: ");

        switch (opcion2) {
            case 1:
                controller.mostrarListaPrioridad();
                break;
        
            case 2:
                controller.mostrarListaCompleta();
                break;
                
            default:
             showErrorMessage("Opción no valida");
                break;
        }
    }

    private void gestionarTarea(){
        
        controller.mostrarListaCompleta();

        int idTarea = Esdia.readInt("Selecciona el ID de la tarea");

        System.out.println("\nOPCIONES");
        System.out.println("1. Marcar como completa o incompleta");
        System.out.println("2. Modificar informacion");
        System.out.println("3. Eliminar");

        int opcion3 = Esdia.readInt("Elegir opcion: ");

        switch (opcion3) {
            
            case 1:
                int completar = Esdia.readInt("Marcar como completa o incompleta, 1 para completa y 0 para incompleta");
                if(completar==1){
                    controller.marcarCompleta(idTarea);
                }
                else if(completar==0){
                    controller.marcarIncompleta(idTarea);
                }
                else{
                    showErrorMessage("Opcion incorrecta");
                }
                break;
        
            case 2: 
                controller.editarTarea(idTarea);
                break;
            
            case 3:
                controller.eliminarTarea(idTarea);
            default:
            showErrorMessage("Opcion no valida");
                break;
        }

    }


    private void exportarimportar(){

        System.out.println("\nIMPORTAR O EXPORTAR TAREA");
        System.out.println("1. Importar CSV");
        System.out.println("2. Exportar CSV");
        System.out.println("3. Importar JSON");
        System.out.println("4. Exportar JSON");
        System.out.println("5. Importar XML");
        System.out.println("6. Exportar XML");

        int opcion4 = Esdia.readInt("Elegir opcion: ");

        switch (opcion4) {
            case 1:
                controller.importarTareas("CSV");
                break;
            case 2:
                controller.exportarTareas("CSV");
                break;
            case 3:
                controller.importarTareas("JSON");
                break;
            case 4:
                controller.exportarTareas("JSON");
                break;
            case 5:
                controller.importarTareas("XML");
                break;
            case 6:
                controller.exportarTareas("XML");
                break;
            default:
                System.out.println("Opción no válida.");
                break;
        }       
        }

    public void showErrorMessage(String error){
        System.err.println("error: " + error);
    }

    public void showMessage(String message){
        System.out.println(message);

    
    }

    public void showMessageS(Task task){
        System.out.println(task);

    
    }

    public void end(){
        System.out.println("Saliendo del programa, gracias por ejecutar ");
    }

    public Task pedirDatos(int id){


        if(id==-1){
             id = Esdia.readInt("Introduce ID: ");
        }

        
    
        String title = Esdia.readString("Introduce el titulo de la tarea: ");
        String content = Esdia.readString("Introduce el contenido de la tarea: ");
        int priority = Esdia.readInt("Añade la prioridad, en del 1 minimo y 5 maximo: ");
        int estimatedDuration = Esdia.readInt("Añade la duracion estimada, en minutos: ");
        LocalDate date;
        
        while(true){
            try{
                 date = LocalDate.parse(Esdia.readString("Fecha en formato YYYY-MM-DD: "));
                 break;
            }catch(Exception e){
                showErrorMessage("Fecha invalida el formato debe ser YYYY-MM-DD intentalo de nuevo");
            }
        }
        
        
        int comp =Esdia.readInt("Escriba 1 si está completada o 0 si está acabada : ",0,1);

        boolean completed;
        if(comp==1) completed=true; else completed=false;

        Task task = new Task(id, priority, estimatedDuration, title, content, date, completed);
        return task;


    
    }


    
}

