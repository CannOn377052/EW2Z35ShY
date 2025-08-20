// 代码生成时间: 2025-08-20 17:28:58
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class PasswordEncryptionDecryptionTool {
    // 加密解密使用的密钥
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "12345678901234567890123456789012";
    private static final String IV = "1234567890123456";
    private static final String CHARSET_NAME = "UTF-8";

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("PasswordEncryptionDecryptionTool").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().appName("PasswordEncryptionDecryptionTool").master("local").getOrCreate();

        // 读取密码文件，这里假设密码文件名为passwords.txt，存放在当前目录
        Dataset<Row> passwords = spark.read().textFile("passwords.txt");

        passwords.show(); // 打印原始密码文件内容

        // 加密密码文件
        Dataset<Row> encryptedPasswords = passwords.map(row -> encrypt(row.getString(0), SECRET_KEY, IV), Encoders.STRING());
        encryptedPasswords.show(); // 打印加密后的密码文件内容

        // 解密密码文件
        Dataset<Row> decryptedPasswords = encryptedPasswords.map(row -> decrypt(row.getString(0), SECRET_KEY, IV), Encoders.STRING());
        decryptedPasswords.show(); // 打印解密后的密码文件内容

        sc.stop();
    }

    // 加密方法
    public static String encrypt(String input, String key, String iv) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET_NAME));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(input.getBytes(CHARSET_NAME));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 解密方法
    public static String decrypt(String input, String key, String iv) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET_NAME));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(input));
            return new String(original, CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
