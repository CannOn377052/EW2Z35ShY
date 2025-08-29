// 代码生成时间: 2025-08-29 13:41:13
 * @author [Your Name]
 * @version 1.0
 */

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.util.Random;

public class RandomNumberGenerator {
    // 构造函数
    public RandomNumberGenerator() {
        // 初始化SparkSession
        SparkSession spark = SparkSession.builder().appName("RandomNumberGenerator").getOrCreate();
    }

    /**
     * 生成指定范围内的随机数
     *
     * @param lowerBound 下限（包含）
     * @param upperBound 上限（不包含）
     * @param count 数量
     * @return 包含随机数的Dataset
     */
    public Dataset<Row> generateRandomNumbers(int lowerBound, int upperBound, int count) {
        // 检查参数有效性
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("下界不能大于等于上界");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        // 初始化SparkSession
        SparkSession spark = SparkSession.builder().appName("RandomNumberGenerator").getOrCreate();

        // 使用Spark SQL生成随机数
        return spark.sql("SELECT explode(sequence(0, ?)) AS id, RAND() AS random_number")
                .withColumn("random_number", col("random_number").cast("integer"))
                .filter("random_number BETWEEN ? AND ?", lowerBound, upperBound)
                .limit(count);
    }

    public static void main(String[] args) {
        try {
            // 创建RandomNumberGenerator实例
            RandomNumberGenerator generator = new RandomNumberGenerator();

            // 生成随机数
            Dataset<Row> randomNumbers = generator.generateRandomNumbers(1, 100, 10);

            // 打印随机数
            randomNumbers.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}