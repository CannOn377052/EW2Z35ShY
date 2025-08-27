// 代码生成时间: 2025-08-28 00:02:03
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WebContentFetcher {
    // Main method to run the application
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("WebContentFetcher").setMaster("local[*]");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        // Check if URLs are provided as arguments
        if (args.length < 1) {
            System.err.println("Usage: WebContentFetcher <URL> [URLs]");
            System.exit(1);
        }

        // Convert the command-line arguments to a List for easier processing
        List<String> urls = Arrays.asList(args);
        JavaRDD<String> urlRDD = sc.parallelize(urls);

        // Fetch and process web content
        Dataset<Row> contentDataset = urlRDD.map(url -> fetchWebContent(url)).toDF("content");
        contentDataset.show();

        // Stop the Spark Context
        sc.close();
    }

    // Method to fetch web content from a given URL
    private static String fetchWebContent(String url) {
        try {
            // Use Jsoup to connect to the URL and retrieve the document
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.body().children();
            // Extract the content of interest, for example, the body of the document
            return elements.outerHtml();
        } catch (IOException e) {
            // Handle any IO exceptions and return an error message
            return "Error fetching content from URL: " + url + " - " + e.getMessage();
        }
    }
}
