// 代码生成时间: 2025-10-08 01:54:27
import spark.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefectTrackingSystem {

    private static final ConcurrentHashMap<Integer, Defect> defectDatabase = new ConcurrentHashMap<>();
    private static int currentId = 0;

    // Represents a defect with id, description, and status
    public static class Defect {
        private int id;
        private String description;
        private String status;

        public Defect(String description) {
            this.id = ++currentId;
            this.description = description;
            this.status = "Open"; // Default status
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static void main(String[] args) {
        // Set up Spark routes
        port(4567);
        get("/defects", (req, res) -> getAllDefects(), new JsonTransformer());
        post("/defects", (req, res) -> addDefect(req), new JsonTransformer());
        get("/defects/:id", (req, res) -> getDefectById(req), new JsonTransformer());
        put("/defects/:id", (req, res) -> updateDefectStatus(req), new JsonTransformer());
        delete("/defects/:id", (req, res) -> deleteDefect(req), new JsonTransformer());
    }

    private static List<Defect> getAllDefects() {
        return new ArrayList<>(defectDatabase.values());
    }

    private static Defect addDefect(Request req) {
        String description = req.queryParams("description");
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        Defect defect = new Defect(description);
        defectDatabase.put(defect.getId(), defect);
        return defect;
    }

    private static Defect getDefectById(Request req) {
        int id = Integer.parseInt(req.params(":id"));
        Defect defect = defectDatabase.get(id);
        if (defect == null) {
            throw new NotFoundException("Defect not found");
        }
        return defect;
    }

    private static Defect updateDefectStatus(Request req) {
        int id = Integer.parseInt(req.params(":id"));
        Defect defect = defectDatabase.get(id);
        if (defect == null) {
            throw new NotFoundException("Defect not found");
        }
        String newStatus = req.queryParams("status");
        defect.setStatus(newStatus);
        return defect;
    }

    private static Defect deleteDefect(Request req) {
        int id = Integer.parseInt(req.params(":id"));
        Defect defect = defectDatabase.remove(id);
        if (defect == null) {
            throw new NotFoundException("Defect not found");
        }
        return defect;
    }
}
