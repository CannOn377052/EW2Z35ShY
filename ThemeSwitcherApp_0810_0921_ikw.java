// 代码生成时间: 2025-08-10 09:21:32
import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import java.util.HashMap;
import java.util.Map;

public class ThemeSwitcherApp {
    // Define the available themes
    private static final String LIGHT_THEME = "light";
    private static final String DARK_THEME = "dark";

    public static void main(String[] args) {
        // Set the port that the application will run on
        port(8080);

        // Set up the Freemarker template engine
        staticFileLocation("/public");
        get("/", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        // Route to switch themes
        get("/switchTheme", (req, res) -> {
            String currentTheme = req.session().attribute("theme");
            String newTheme = (currentTheme != null && currentTheme.equals(LIGHT_THEME)) ? DARK_THEME : LIGHT_THEME;
            req.session().attribute("theme", newTheme);
            res.redirect("/");
            return "";
        }, new FreeMarkerEngine());

        // Define a route to display the current theme
        get("/currentTheme", (req, res) -> {
            String theme = req.session().attribute("theme");
            if (theme == null) {
                theme = LIGHT_THEME; // Default theme
                req.session().attribute("theme", theme);
            }
            return theme;
        });
    }
}
