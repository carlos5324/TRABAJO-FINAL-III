package modelo;

import java.io.IOException;
import java.util.List;

public interface IExporter {
    void exportar(List<Task> tasks, String archivo) throws IOException;
}
