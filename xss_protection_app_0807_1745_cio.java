// 代码生成时间: 2025-08-07 17:45:43
import spark.Spark;
import spark.template.freemarker.FreeMarkerTemplateEngine;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class xss_protection_app {

    // The Safelist allows only certain HTML elements and attributes, which helps in preventing XSS attacks.
    private static final Safelist safelist = Safelist.basic();

    public static void main(String[] args) {

        // Set up the Spark application
        Spark.staticFiles.location("/public");

        // Route to handle GET requests to sanitize input
        Spark.get("/sanitize", (req, res) -> {
            // Retrieve the input from the query parameter
            String userInput = req.queryParams("userInput");

            // Sanitize the input to prevent XSS attacks
            String sanitizedInput = sanitizeInput(userInput);

            // Create a model map to pass data to the template
            Map<String, Object> model = new HashMap<>();
            model.put("sanitizedInput", sanitizedInput);

            // Render the sanitized input using FreeMarker template engine
            return new FreeMarkerTemplateEngine().render(new ModelAndView(model, "sanitize.ftl"));
        });

        // Error handling for any unexpected errors
        Spark.exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.body("An unexpected error occurred: " + e.getMessage());
        });
    }

    /**
     * Sanitizes the input to prevent XSS attacks.
     *
     * @param input The user input to be sanitized.
     * @return The sanitized input.
     */
    private static String sanitizeInput(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return Jsoup.clean(input, safelist);
    }
}
