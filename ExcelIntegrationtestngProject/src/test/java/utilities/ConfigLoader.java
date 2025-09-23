package utilities;

import constants.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;
    private final String filePath;

    public ConfigLoader() {

        String environment = System.getProperty("env", String.valueOf(Environment.STAGE));
        this.filePath = switch (Environment.valueOf(environment.toUpperCase())) {
            case STAGE -> "src" + File.separator + "test" + File.separator + "resources" + File.separator
                    + "devconfiguration.properties";
            case TEST -> "src" + File.separator + "test" + File.separator + "resources" + File.separator
                    + "testconfiguration.properties";
        };

        properties = PropertyUtils.propertyLoader(filePath);
    }

    public String getProperty(String property) {
        String prop = properties.getProperty(property);
        if (prop != null) {
            return prop;
        } else {
            throw new RuntimeException("Property " + property + " is not specified in the config file");
        }
    }

    public void setProperty(String property, Object value) throws IOException {
        properties.setProperty(property, value.toString());

        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null);
        }
    }
}
