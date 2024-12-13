package modelo;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.*;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class XMLcontrolador {
    private static XmlMapper xmlMapper = new XmlMapper(); 


    static {
        xmlMapper.registerModule(new JavaTimeModule());
    }
    
    static class TaskList {
        public List<Task> task;
    }

    

    public void exportarXML(List<Task> tareas, String archivo) throws IOException {

        TaskList taskList = new TaskList();
        taskList.task = tareas;

        try (Writer writer = new FileWriter(archivo)) {
            xmlMapper.writeValue(writer, taskList); 
            System.out.println("Tareas exportadas a XML en: " + archivo);
        }catch (IOException e) {
            System.out.println("Error al exportar tareas a XM... ");
            throw e;
        }
    }

    
    public List<Task> importarXML(String archivo) throws IOException {
        try (Reader reader = new FileReader(archivo)) {
            TaskList taskList=xmlMapper.readValue(reader, TaskList.class);
            return taskList.task;
        }catch (IOException e) {
            System.out.println("Error al importar tareas desde XML... " );
            throw e;
        }
    }
}
