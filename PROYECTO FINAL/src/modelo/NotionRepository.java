package modelo;

import notion.api.v1.NotionClient;
import notion.api.v1.http.OkHttp5Client;
import notion.api.v1.logging.Slf4jLogger;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.model.pages.PageProperty.RichText;
import notion.api.v1.model.pages.PageProperty.RichText.Text;
import notion.api.v1.request.databases.QueryDatabaseRequest;
import notion.api.v1.request.pages.CreatePageRequest;
import notion.api.v1.request.pages.UpdatePageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import controlador.Controller;

public class NotionRepository implements IRepository {

    Controller controller;
    private final NotionClient client;
    private final String databaseId;

    public NotionRepository(Controller controller, String apiToken, String databaseId) {
        this.controller = controller;
        this.client = new NotionClient(apiToken);
        client.setHttpClient(new OkHttp5Client(60000, 60000, 60000));
        client.setLogger(new Slf4jLogger());
        this.databaseId = databaseId;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void testConnection() {
        try {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults results = client.queryDatabase(queryRequest);
            controller.showMessage("Conexión exitosa. Total de páginas en la base de datos: " + results.getResults().size());
        } catch (Exception e) {
            e.printStackTrace();
            controller.showMessage("Error al conectar con la base de datos de Notion.");
        }
    }

    @Override
    public Task añadirTask(Task task) {
        try {
            controller.showMessage("Creando una nueva página...");
            Map<String, PageProperty> properties = Map.of(
                "Titulo", createTitleProperty(task.getTitle()),
                "Fecha", createDateProperty(task.getDate().toString()),
                "Contenido", createRichTextProperty(task.getContent()),
                "Prioridad", createNumberProperty(task.getPriority()),
                "Duración Estimada", createNumberProperty(task.getEstimatedDuration()),
                "Estado", createCheckboxProperty(task.isCompleted()),
                "Identifier", createNumberProperty(task.getIdentifier())
            );

            PageParent parent = PageParent.database(databaseId);
            CreatePageRequest request = new CreatePageRequest(parent, properties);
            Page response = client.createPage(request);

            controller.showMessage("Página creada con ID (interno Notion): " + response.getId());
            task.setNotionId(response.getId());
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void borrarTarea(int identifier) {
        try {
            String pageId = findNotionIdByIdentifier(identifier);
            if (pageId == null) {
                controller.showMessage("No se encontró una tarea con el identificador: " + identifier);
                return;
            }

            UpdatePageRequest updateRequest = new UpdatePageRequest(pageId, Collections.emptyMap(), true);
            client.updatePage(updateRequest);
            controller.showMessage("Tarea archivada con éxito. ID Notion: " + pageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificarTarea(Task task) {
        try {
            controller.showMessage("Modificando la tarea con Identifier: " + task.getIdentifier());
            String notionId = findNotionIdByIdentifier(task.getIdentifier());
            if (notionId == null) {
                controller.showMessage("No se puede modificar la tarea porque no se encontró el Identifier en Notion.");
                return;
            }

            Map<String, PageProperty> updatedProperties = Map.of(
                "Titulo", createTitleProperty(task.getTitle()),
                "Fecha", createDateProperty(task.getDate().toString()),
                "Contenido", createRichTextProperty(task.getContent()),
                "Prioridad", createNumberProperty(task.getPriority()),
                "Duración Estimada", createNumberProperty(task.getEstimatedDuration()),
                "Estado", createCheckboxProperty(task.isCompleted()),
                "Identifier", createNumberProperty(task.getIdentifier())
            );

            UpdatePageRequest updateRequest = new UpdatePageRequest(notionId, updatedProperties);
            client.updatePage(updateRequest);

            controller.showMessage("Tarea actualizada con éxito. Identifier: " + task.getIdentifier());
        } catch (Exception e) {
            e.printStackTrace();
            controller.showMessage("Error al editar tarea.");
        }
    }

    @Override
    
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);
    
            for (Page page : queryResults.getResults()) {
                Map<String, PageProperty> properties = page.getProperties();
                Task task = mapPageToTask(page.getId(), properties);
    
               
                if (task != null && task.getIdentifier() != 0) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }
    


    @Override
    public List<Task> listaTareasPendientes() {
        List<Task> pendingTasks = new ArrayList<>();
        for (Task task : getTasks()) {
            if (!task.isCompleted()) {
                pendingTasks.add(task);
            }
        }
        return pendingTasks;
    }

    @Override
    public List<Task> listarHistorial() {
        return getTasks();
    }

    private String findNotionIdByIdentifier(int identifier) {
        try {
            controller.showMessage("Buscando NotionId para el Identifier: " + identifier);
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);

            for (Page page : queryResults.getResults()) {
                Map<String, PageProperty> properties = page.getProperties();
                if (properties.containsKey("Identifier") && properties.get("Identifier").getNumber() != null) {
                    if (properties.get("Identifier").getNumber().intValue() == identifier) {
                        controller.showMessage("Encontrado Identifier: " + identifier + " en la página: " + page.getId());
                        return page.getId();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Task mapPageToTask(String pageId, Map<String, PageProperty> properties) {
        Task task = new Task();
        try {
            
            if (properties.containsKey("Identifier") && properties.get("Identifier").getNumber() != null) {
                task.setIdentifier(properties.get("Identifier").getNumber().intValue());
            } else {
                return null;
            }
    
           
            if (properties.containsKey("Titulo") && properties.get("Titulo").getTitle() != null) {
                task.setTitle(properties.get("Titulo").getTitle().get(0).getText().getContent());
            } else {
                task.setTitle("Sin título");
            }
    
           
            if (properties.containsKey("Fecha") && properties.get("Fecha").getDate() != null) {
                task.setDate(LocalDate.parse(properties.get("Fecha").getDate().getStart()));
            } else {
                task.setDate(LocalDate.now());
            }
    
            
            if (properties.containsKey("Contenido") && properties.get("Contenido").getRichText() != null) {
                task.setContent(properties.get("Contenido").getRichText().get(0).getText().getContent());
            } else {
                task.setContent("Sin contenido");
            }
    
            
            if (properties.containsKey("Prioridad") && properties.get("Prioridad").getNumber() != null) {
                task.setPriority(properties.get("Prioridad").getNumber().intValue());
            } else {
                task.setPriority(1);
            }
    
           
            if (properties.containsKey("Duración Estimada") && properties.get("Duración Estimada").getNumber() != null) {
                task.setEstimatedDuration(properties.get("Duración Estimada").getNumber().intValue());
            } else {
                task.setEstimatedDuration(0);
            }
    
            
            if (properties.containsKey("Estado") && properties.get("Estado").getCheckbox() != null) {
                task.setCompleted(properties.get("Estado").getCheckbox());
            } else {
                task.setCompleted(false);
            }
    
            task.setNotionId(pageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }
    
    

    private PageProperty createTitleProperty(String title) {
        RichText idText = new RichText();
        idText.setText(new Text(title));
        PageProperty idProperty = new PageProperty();
        idProperty.setTitle(Collections.singletonList(idText));
        return idProperty;
    }

    private PageProperty createRichTextProperty(String text) {
        RichText richText = new RichText();
        richText.setText(new Text(text));
        PageProperty property = new PageProperty();
        property.setRichText(Collections.singletonList(richText));
        return property;
    }

    private PageProperty createNumberProperty(Integer number) {
        PageProperty property = new PageProperty();
        property.setNumber(number);
        return property;
    }

    private PageProperty createDateProperty(String date) {
        PageProperty property = new PageProperty();
        PageProperty.Date dateProperty = new PageProperty.Date();
        dateProperty.setStart(date);
        property.setDate(dateProperty);
        return property;
    }

    private PageProperty createCheckboxProperty(boolean checked) {
        PageProperty property = new PageProperty();
        property.setCheckbox(checked);
        return property;
    }

    
    






    public void exportarCSV(String fileName) {
        throw new UnsupportedOperationException("Este repositorio no soporta exportación a CSV");
    }

    public List<Task> importarCSV(String fileName) {
        throw new UnsupportedOperationException("Este repositorio no soporta importación desde CSV");
    }
    
    public void exportarJSON(String fileName) {
        throw new UnsupportedOperationException("Este repositorio no soporta exportación a JSON");
    }
    
    public List<Task> importarJSON(String fileName) {
        throw new UnsupportedOperationException("Este repositorio no soporta importación desde JSON");
    }
    
   public void exportarXML(String fileName) {
        throw new UnsupportedOperationException("Este repositorio no soporta exportación a XML");
    }
    
    public List<Task> importarXML(String fileName) {
        throw new UnsupportedOperationException("Este repositorio no soporta importación desde XML");
    }
}