// 代码生成时间: 2025-09-21 14:42:54
 * @author [Your Name]
 * @version 1.0
 */

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

public class UserLoginSystem {

    // Map to store user credentials for demonstration purposes
    private static final Map<String, String> userDatabase = new HashMap<>();

    // Initialize the user database
    static {
        userDatabase.put("user1", "password1");
        userDatabase.put("user2", "password2");
    }

    // Main method to start the Spark application
    public static void main(String[] args) {
        // Set Spark to handle port 4567
        port(4567);

        // Define the root path for the application
        get("/", (req, res) -> "Welcome to the User Login System");

        // Define the login path with form to accept user credentials
        get("/login", (req, res) -> {
            res.type("text/html");
            return "<form action='/login' method='post'>Username: <input type='text' name='username'/> <br>Password: <input type='password' name='password'/> <br> <input type='submit' value='Login'/></form>";
        });

        // Define the POST method for login to validate user credentials
        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            try {
                // Check if the provided credentials match the stored ones
                if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                    res.redirect("/welcome");
                } else {
                    res.status(401);
                    return "Invalid username or password";
                }
            } catch (Exception e) {
                // Handle any unexpected exceptions
                res.status(500);
                return "An error occurred: " + e.getMessage();
            }
        }, new spark.template.freemarker.FreeMarkerEngine());

        // Define the welcome path after successful login
        get("/welcome", (req, res) -> {
            return "Welcome! You have successfully logged in.";
        });
    }
}
