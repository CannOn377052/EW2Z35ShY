// 代码生成时间: 2025-09-22 15:29:38
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.ml.image.ImageSchema;
import org.apache.spark.ml.image.ImageResizing;
import org.apache.spark.sql.types.*;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SaveMode;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.spark.ml.feature.ImageUtils;

public class ImageResizerApp {

    // Configuration for the Spark session
    private static final String MASTER = "local[*]";
    private static final String APP_NAME = "ImageResizerApp";

    public static void main(String[] args) throws IOException {
        // Initialize the Spark session
        SparkSession spark = SparkSession.builder()
                .master(MASTER)
                .appName(APP_NAME)
                .getOrCreate();

        // Create a Java Spark context from Spark session
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        if (args.length != 4) {
            System.err.println("Usage: ImageResizerApp <input_dir> <output_dir> <width> <height>");
            System.exit(1);
        }

        // Input directory and output directory paths
        String inputDir = args[0];
        String outputDir = args[1];
        int width = Integer.parseInt(args[2]);
        int height = Integer.parseInt(args[3]);

        // Load images from the input directory
        JavaRDD<String> images = sc.binaryFiles(inputDir);

        // Function to resize the image
        JavaRDD<Row> resizedImages = images.map(image -> {
            try {
                ImageSchema imageSchema = ImageUtils.load(image);
                ImageResizing.resizeImage(imageSchema, width, height);
                byte[] resizedImage = ImageUtils.toBytes(imageSchema);
                return RowFactory.create(image._1(), resizedImage);
            } catch (IOException e) {
                throw new RuntimeException("Error resizing image", e);
            }
        });

        // Define the schema for the resized images
        StructType schema = new StructType(new StructField[]{
                new StructField("image_path", DataTypes.StringType, false, Metadata.empty()),
                new StructField("resized_image", DataTypes.BinaryType, false, Metadata.empty())
        });

        // Convert the JavaRDD to a Dataset and write to the output directory
        Dataset<Row> dataset = spark.createDataFrame(resizedImages, schema);
        dataset.write().mode(SaveMode.Overwrite).parquet(outputDir);

        // Stop the Spark session
        spark.stop();
    }
}
