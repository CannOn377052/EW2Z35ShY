// 代码生成时间: 2025-08-06 08:29:03
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextFileAnalyzer {

    // Main method to run the application
    public static void main(String[] args) {

        // Check if the arguments are provided
        if (args.length < 1) {
            System.err.println("Usage: TextFileAnalyzer <path_to_text_file>");
            System.exit(1);
        }

        // Configure Spark
        SparkConf conf = new SparkConf().setAppName("TextFileAnalyzer").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Load the text file into a RDD
            JavaRDD<String> textFile = sc.textFile(args[0]);

            // Perform text analysis
            Map<String, Long> wordCount = analyzeText(textFile);

            // Print the results
            printResults(wordCount);

        } catch (Exception e) {
            System.err.println("Error processing the text file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Stop the Spark context
            sc.close();
        }
    }

    // Method to perform text analysis and return word counts
    private static Map<String, Long> analyzeText(JavaRDD<String> textFile) {
        // Split each line into words and map to a tuple (1, word)
        JavaRDD<String> words = textFile.flatMap(line -> Arrays.asList(line.split("\s+")).iterator());

        // Filter out non-alphabetic words and count occurrences
        JavaRDD<Tuple2<String, Long>> wordCounts = words
                .filter(word -> word.matches("^[a-zA-Z]+$"))
                .map(word -> new Tuple2<>(word, 1L))
                .reduceByKey((a, b) -> a + b);

        // Collect the results and return as a HashMap
        return wordCounts.collect().stream().collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
    }

    // Method to print the word count results
    private static void printResults(Map<String, Long> wordCount) {
        for (Map.Entry<String, Long> entry : wordCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Inner class to represent a tuple (pair)
    public static class Tuple2<K, V> {
        private K _1;
        private V _2;

        public Tuple2(K _1, V _2) {
            this._1 = _1;
            this._2 = _2;
        }

        public K _1() { return _1; }

        public V _2() { return _2; }
    }
}