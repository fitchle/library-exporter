package com.fitchle.gradle.libraryexporter.properties;

import java.io.File;

public final class ConfigurationProperty {
    public static final String BASE_DIR = "reports" + File.separator + "libraryExporter";
    public static final String DEPENDENCY_DATA_KEY="dependencies";
    public static final String DEPENDENCY_DATA_NAME_KEY="name";
    public static final String DEPENDENCY_DATA_GROUP_KEY="group";
    public static final String DEPENDENCY_DATA_VERSION_KEY="version";

    public static final String REPOSITORIES_DATA_KEY="repositories";
    public static final String REPOSITORIES_DATA_URL_KEY="url";
}
