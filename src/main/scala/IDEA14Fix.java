import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by LolHens on 13.12.2014.
 */
public class IDEA14Fix {
    public static void main(String[] args) {
        try {
            // FileUtils is org.apache.commons.io.FileUtils
            FileUtils.copyDirectory(new File("./build/resources/"), new File("./build/classes/"));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}