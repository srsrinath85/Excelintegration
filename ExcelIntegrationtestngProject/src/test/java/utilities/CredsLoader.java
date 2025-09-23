package utilities;

import java.nio.file.Paths;
import java.util.Properties;

public class CredsLoader {
    private final Properties properties;

    public CredsLoader() {
        // properties =
        // PropertyUtils.propertyLoader(System.getProperty("credsFilePath"));
        String fileData = System.getProperty("credsFilePath", "Default value");
        if (fileData == null) {
            throw new RuntimeException("File location not entered");
        }
        properties = PropertyUtils.propertyLoader(Paths.get(fileData).toString());

    }

    public String getProperty(String property) {
        String prop = properties.getProperty(property);
        if (prop != null)
            return prop;
        else
            throw new RuntimeException("Property " + property + " is not specified in the creds file");
    }

}
