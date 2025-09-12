// 代码生成时间: 2025-09-12 08:04:25
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A Text File Analyzer application using Apache Spark framework.
 * This class analyzes the contents of a text file and provides
 * basic statistics such as word count and frequency.
 */
public class TextFileAnalyzer implements Serializable {

    private transient JavaSparkContext sc;
    private SparkSession spark;

    public TextFileAnalyzer(String master, String appName) {
        spark = SparkSession.builder()
                .appName(appName)
                .master(master)
                .getOrCreate();
        sc = new JavaSparkContext(spark);
    }

    /**
     * Analyzes a text file and prints out word count and frequency.
     * @param inputFilePath Path to the input text file.
     */
    public void analyzeTextFile(String inputFilePath) {
        try {
            // Read the text file into an RDD
            JavaRDD<String> textFile = sc.textFile(inputFilePath);

            // Split each line into words
            JavaRDD<String> words = textFile.flatMap(s -> Arrays.asList(s.split(" ")).iterator());

            // Filter out empty strings and map words to (word, 1) tuple
            JavaRDD<Tuple2<String, Integer>> wordCounts = words
                    .filter(s -> !s.isEmpty())
                    .mapToPair(s -> new Tuple2<>(s, 1));

            // Reduce by key to get word count
            JavaRDD<Tuple2<String, Integer>> reducedWordCounts = wordCounts.reduceByKey((a, b) -> a + b);

            // Collect and print the word counts
            List<Tuple2<String, Integer>> output = reducedWordCounts.collect();
            output.forEach(tuple -> System.out.println(tuple._1() + ": " + tuple._2()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Stop the Spark context
            if (sc != null) {
                sc.close();
                spark.stop();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: TextFileAnalyzer <input file path>");
            System.exit(1);
        }

        String inputFilePath = args[0];
        TextFileAnalyzer analyzer = new TextFileAnalyzer("local[*]", "TextFileAnalyzer");
        analyzer.analyzeTextFile(inputFilePath);
    }
}
