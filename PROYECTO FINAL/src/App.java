import com.coti.tools.Esdia;

import controlador.Controller;
import modelo.NotionRepository;
import modelo.BinaryRepository;
import modelo.IRepository;
import view.ConsoleView;


public class App {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación...");
        IRepository repositorio = null;
    
        // Verifica los argumentos
        if (args.length > 0 && args[0].equals("--repository")) {
            
            if (args.length > 1) {
                String repositoryType = args[1];
                
    
                if (repositoryType.equals("notion")) {
                    if (args.length == 4) {
                        String apiKey = args[2];
                        String databaseId = args[3];
                        System.out.println("Conectando al repositorio Notion...");
                        repositorio = new NotionRepository(null, apiKey, databaseId);
                    } else {
                        System.err.println("Error: Para 'notion' se deben proporcionar API_KEY y DATABASE_ID.");
                        return;
                    }
                } else if (repositoryType.equals("bin")) {
                    System.out.println("Conectando al repositorio Binario...");
                    repositorio = new BinaryRepository();
                } else {
                    System.err.println("Error: Tipo de repositorio no reconocido.");
                    return;
                }
            } else {
                System.err.println("Error: Debe especificar un tipo de repositorio después de '--repository'.");
                return;
            }
        } else {
            System.err.println("Error: Debe especificar '--repository' seguido de 'bin' o 'notion'.");
            return;
        }
    
        // Configura el controlador y la vista
        System.out.println("Inicializando controlador y vista...");
        Controller controller = new Controller(repositorio);
    
        if (repositorio instanceof NotionRepository) {
            ((NotionRepository) repositorio).setController(controller);
        }
    
        ConsoleView view = new ConsoleView(controller);
        controller.setConsole(view);
    
        // Inicia la vista
        System.out.println("Iniciando la vista...");
        view.init();
    }
    
}
























/* 
public class App {
    public static void main(String[] args) {

         
        IRepository repositorio;
        Controller controller;

        System.out.println("Selecciona el repositorio que deseas usar:");
        System.out.println("1. Notion");
        System.out.println("2. Binario");

        int opcion = Esdia.readInt("Selecciona una opción: ", 1, 2);

        controller = new Controller(null);

        if (opcion == 1) {
            repositorio = new NotionRepository(controller, "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", "1523c1ff498580b6be09deaa765e4b0e");
        } else {
            repositorio = new BinaryRepository();
        }

        controller = new Controller(repositorio); // Inicializa el controlador con el repositorio seleccionado

        if (repositorio instanceof NotionRepository) {
            ((NotionRepository) repositorio).setController(controller);
        }

        ConsoleView view = new ConsoleView(controller);
        controller.setConsole(view);

       
}
}*/
