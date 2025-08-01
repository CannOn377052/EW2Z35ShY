// 代码生成时间: 2025-08-01 19:57:36
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SparkSortAlgorithm {
    
    /**
     * Function to sort a list of numbers using Spark.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Check if the arguments are provided
        if (args.length < 1) {
            System.err.println("Usage: SparkSortAlgorithm <path_to_file>");
            System.exit(1);
        }

        // Set up the Spark configuration and context
        SparkConf conf = new SparkConf().setAppName("SparkSortAlgorithm").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Read the data from the file
        String filePath = args[0];
        JavaRDD<String> file = sc.textFile(filePath);

        // Split each line into words and convert words to integers
        JavaRDD<Integer> numbers = file.flatMap(line -> Arrays.asList(line.split(" ")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf)
                .collect(Collectors.toList().stream())
                .map(JavaRDD::fromJavaList).get(0);

        // Sort the numbers using Spark's sortBy function
        JavaRDD<Integer> sortedNumbers = numbers.sortBy(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return a.compareTo(b);
            }
        });

        // Collect the sorted numbers and print them
        List<Integer> sortedList = sortedNumbers.collect();
        sortedList.forEach(System.out::println);

        // Stop the Spark context
        sc.close();
    }
}
