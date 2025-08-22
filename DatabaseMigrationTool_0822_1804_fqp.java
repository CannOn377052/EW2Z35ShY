// 代码生成时间: 2025-08-22 18:04:05
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseMigrationTool {

    // JDBC URL, username and password of the source and target databases
    private static final String SOURCE_DB_URL = "jdbc:mysql://source_host:3306/source_db";
    private static final String TARGET_DB_URL = "jdbc:mysql://target_host:3306/target_db";
    private static final String DB_USER = "db_user";
    private static final String DB_PASSWORD = "db_password";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("DatabaseMigrationTool")
                .master("local")
                .getOrCreate();

        try {
            // Load data from source database
            Dataset<Row> sourceData = loadDataFromSource(spark);

            // Write data to target database
            writeToTargetDatabase(sourceData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            spark.stop();
        }
    }

    /**
     * Load data from the source database using Spark
     *
     * @param spark Spark session
     * @return Dataset containing the data from the source database
     */
    private static Dataset<Row> loadDataFromSource(SparkSession spark) {
        Dataset<Row> sourceData = spark.read()
                .format("jdbc")
                .option("url", SOURCE_DB_URL)
                .option("dbtable", "source_table")
                .option("user", DB_USER)
                .option("password", DB_PASSWORD)
                .load();

        return sourceData;
    }

    /**
     * Write data to the target database using JDBC
     *
     * @param data Dataset containing the data to be written
     */
    private static void writeToTargetDatabase(Dataset<Row> data) throws SQLException {
        Connection connection = DriverManager.getConnection(TARGET_DB_URL, DB_USER, DB_PASSWORD);
        String query = "INSERT INTO target_table (column1, column2) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        for (Row row : data.collectAsList()) {
            statement.setString(1, row.getString(0));
            statement.setString(2, row.getString(1));
            statement.addBatch();
        }

        statement.executeBatch();
        connection.close();
    }
}
