// 代码生成时间: 2025-09-23 09:29:57
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types;

import java.util.Random;

public class TestDataGenerator {

    // 配置SparkSession，确保在创建SparkSession时启用Hive支持
    private static SparkSession sparkSession = SparkSession.builder()
            .appName("TestDataGenerator")
            .master("local[*]")
            .enableHiveSupport()
            .getOrCreate();

    // 定义一个方法来生成测试数据
    public static Dataset<Row> generateTestData(int numRecords) {
        try {
            // 定义数据模式
            types.structType schema = new types.structType()
                    .add("id", types.IntegerType.instance)
                    .add("name", types.StringType.instance)
                    .add("age", types.IntegerType.instance)
                    .add("email", types.StringType.instance);

            // 创建一个Dataset的Row，用于生成测试数据
            Dataset<Row> testDS = sparkSession.sqlContext().createDataFrame(
                    sparkSession.sparkContext().parallelize(generateRandomRows(numRecords), numRecords), schema);

            return testDS;
        } catch (Exception e) {
            throw new RuntimeException("Error generating test data", e);
        }
    }

    // 辅助方法：生成随机行数据
    private static Iterable<Row> generateRandomRows(int numRecords) {
        Random random = new Random();
        return new Iterable<Row>() {
            public java.util.Iterator<Row> iterator() {
                return new java.util.Iterator<Row>() {
                    int count = 0;
                    public boolean hasNext() {
                        return count < numRecords;
                    }

                    public Row next() {
                        count++;
                        int id = random.nextInt();
                        String name = "Name" + count;
                        int age = 18 + random.nextInt(50);  // 年龄在18到67之间
                        String email = name.toLowerCase() + "@example.com";
                        return RowFactory.create(id, name, age, email);
                    }
                };
            }
        };
    }

    // 主方法
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: TestDataGenerator <number of records>