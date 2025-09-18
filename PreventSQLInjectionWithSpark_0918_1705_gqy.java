// 代码生成时间: 2025-09-18 17:05:33
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Encoders;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StringType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreventSQLInjectionWithSpark {

    // Main method to run the Spark application
    public static void main(String[] args) {
        // Create a Spark session
        SparkSession spark = SparkSession.builder()
                .appName(