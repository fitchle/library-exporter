package com.fitchle.gradle.libraryexporter.configuration;


import com.fitchle.gradle.libraryexporter.properties.ConfigurationProperty;
import com.fitchle.gradle.libraryexporter.utils.WriterUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gradle.api.artifacts.Dependency;

import java.io.File;
import java.net.URI;
import java.util.LinkedHashMap;

public final class ConfigurationWriter {

    private final File file;
    private final Configuration configuration;

    public ConfigurationWriter(File file, Configuration configuration) {
        this.file = file;
        this.configuration = configuration;
    }

    public void writeYAML() {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> map = new LinkedHashMap<>();

        if (!configuration.repositories().isEmpty()) {
            LinkedHashMap<String, LinkedHashMap<String, String>> repositoryIdMap = new LinkedHashMap<>();

            int i = 0;
            for (URI repository : configuration.repositories()) {
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                data.put(ConfigurationProperty.REPOSITORIES_DATA_URL_KEY, repository.toString());
                repositoryIdMap.put(i + "", data);
                i++;
            }
            map.put(ConfigurationProperty.REPOSITORIES_DATA_KEY, repositoryIdMap);
        }
        
        if (!configuration.dependencies().isEmpty()) {
            LinkedHashMap<String, LinkedHashMap<String, String>> dependencyIdMap = new LinkedHashMap<>();

            int i = 0;
            for (Dependency dependency : configuration.dependencies()) {
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                data.put(ConfigurationProperty.DEPENDENCY_DATA_GROUP_KEY, dependency.getGroup());
                data.put(ConfigurationProperty.DEPENDENCY_DATA_NAME_KEY, dependency.getName());
                data.put(ConfigurationProperty.DEPENDENCY_DATA_VERSION_KEY, dependency.getVersion());
                dependencyIdMap.put(i + "", data);
                i++;
            }
            map.put(ConfigurationProperty.DEPENDENCY_DATA_KEY, dependencyIdMap);
        }
        if (!map.isEmpty())
            WriterUtils.writeYAML(file, map);
    }

    public void writeJSON() {
        JsonObject object = new JsonObject();

        if (!configuration.repositories().isEmpty()) {
            JsonArray repositoryArr = new JsonArray();
            for (URI repositoryUrl : configuration.repositories()) {
                JsonObject data = new JsonObject();
                data.addProperty(ConfigurationProperty.REPOSITORIES_DATA_URL_KEY, repositoryUrl.toString());
                repositoryArr.add(data);
            }
            object.add(ConfigurationProperty.REPOSITORIES_DATA_KEY, repositoryArr);
        }

        if (!configuration.dependencies().isEmpty()) {
            JsonArray depArr = new JsonArray();
            for (Dependency dependency : configuration.dependencies()) {
                JsonObject data = new JsonObject();
                data.addProperty(ConfigurationProperty.DEPENDENCY_DATA_GROUP_KEY, dependency.getGroup());
                data.addProperty(ConfigurationProperty.DEPENDENCY_DATA_NAME_KEY, dependency.getName());
                data.addProperty(ConfigurationProperty.DEPENDENCY_DATA_VERSION_KEY, dependency.getVersion());

                depArr.add(data);
            }
            object.add(ConfigurationProperty.DEPENDENCY_DATA_KEY, depArr);
        }

        if (!object.isEmpty())
            WriterUtils.writeJSON(file, object);
    }
}
