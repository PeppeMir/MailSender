package config;

import mailsender.Main;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

public class AppConfigImpl implements AppConfig {

    private static final String PROPERTIES_FILE_NAME = "app.properties";
    private final Properties properties = new Properties();

    @Inject
    public AppConfigImpl() throws IOException {
        this.properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
    }

    @Override
    public String getBaseDirPath() {
        return properties.getProperty("directory.base.path");
    }

    @Override
    public String getErrorDir() {
        return getBaseDirPath() + "" + properties.getProperty("directory.error.name");
    }

    @Override
    public String getInputDir() {
        return getBaseDirPath() + "" + properties.getProperty("directory.input.name");
    }

    @Override
    public String getContentDir() {
        return getBaseDirPath() + "" + properties.getProperty("directory.content.name");
    }

    @Override
    public boolean isSkipFirstLine() {
        return Boolean.parseBoolean(properties.getProperty("parsing.skipfirstline"));
    }

    @Override
    public String getSplitChar() {
        return properties.getProperty("parsing.splitchar");
    }
}
