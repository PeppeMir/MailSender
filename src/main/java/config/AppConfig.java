package config;

public interface AppConfig {
    String getBaseDirPath();

    String getErrorDir();

    String getInputDir();

    String getContentDir();

    boolean isSkipFirstLine();

    String getSplitChar();
}
