// 代码生成时间: 2025-08-12 04:17:14
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.jdbc.JdbcDialect;
import org.apache.spark.sql.jdbc.JdbcDialects;
import org.apache.spark.sql.jdbc.JdbcUtils;

public class DatabaseMigrationTool {

    // Main method
    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("DatabaseMigrationTool")
                .master("local[*]")
                .getOrCreate();

        try {
            // Database source and target configurations
            String sourceDbUrl = "jdbc:mysql://source_host:3306/source_db";
            String targetDbUrl = "jdbc:mysql://target_host:3306/target_db";
            String username = "username";
            String password = "password";
            String tableName = "migration_table";

            // Read data from source database
            Dataset<Row> sourceData = spark.read()
                    .format("jdbc")
                    .option("url", sourceDbUrl)
                    .option("dbtable", tableName)
                    .option("user", username)
                    .option("password", password)
                    .load();

            // Get the JdbcDialect for the target database
            JdbcDialect targetDialect = JdbcDialects.get("jdbc:mysql");

            // Write data to target database
            sourceData.write()
                    .mode("append")
                    .jdbc(targetDbUrl, tableName, new JdbcSaveMode(targetDialect));

            System.out.println("Data migration completed successfully.");

        } catch (Exception e) {
            System.err.println("Error during database migration: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Stop the Spark session
            spark.stop();
        }
    }

    // Inner class to handle custom save mode for JDBC
    public static class JdbcSaveMode extends JdbcDialect {
        private JdbcDialect dialect;

        public JdbcSaveMode(JdbcDialect dialect) {
            this.dialect = dialect;
        }

        @Override
        public boolean canHandle(String url) {
            return dialect.canHandle(url);
        }

        @Override
        public void save(Dataset<Row> data, String tableName, JdbcConnectionProperties connectionProperties) {
            // Custom save logic for the target database
            // For example, handling unique constraints or custom batch size
        }
    }
}