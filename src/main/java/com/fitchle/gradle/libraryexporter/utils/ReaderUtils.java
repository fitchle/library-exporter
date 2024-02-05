package com.fitchle.gradle.libraryexporter.utils;


import com.fitchle.gradle.libraryexporter.properties.ConfigurationProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ReaderUtils {
    public static Set<Dependency> readDependencies(Map<String, Object> map) {
        HashSet<Dependency> dependencies = new HashSet<>();
        if (map.containsKey(ConfigurationProperty.DEPENDENCY_DATA_KEY)) {
            LinkedHashMap<String, LinkedHashMap<String, String>> idMap = (LinkedHashMap<String, LinkedHashMap<String, String>>) map.get(ConfigurationProperty.DEPENDENCY_DATA_KEY);
            for (Map.Entry<String, LinkedHashMap<String, String>> entry : idMap.entrySet()) {
                String group = entry.getValue().get(ConfigurationProperty.DEPENDENCY_DATA_GROUP_KEY);
                String artifact = entry.getValue().get(ConfigurationProperty.DEPENDENCY_DATA_NAME_KEY);
                String version = entry.getValue().get(ConfigurationProperty.DEPENDENCY_DATA_VERSION_KEY);

                Dependency dependency = new DefaultExternalModuleDependency(group, artifact, version);
                dependencies.add(dependency);
            }
        }
        return dependencies;
    }

    public static Set<Dependency> readDependencies(JsonObject json) {
        HashSet<Dependency> dependencies = new HashSet<>();
        if (json.has(ConfigurationProperty.DEPENDENCY_DATA_KEY)) {
            for (JsonElement jsonElement : json.getAsJsonArray(ConfigurationProperty.DEPENDENCY_DATA_KEY)) {
                JsonObject object = jsonElement.getAsJsonObject();
                String group = object.get(ConfigurationProperty.DEPENDENCY_DATA_GROUP_KEY).getAsString();
                String artifact = object.get(ConfigurationProperty.DEPENDENCY_DATA_NAME_KEY).getAsString();
                String version = object.get(ConfigurationProperty.DEPENDENCY_DATA_VERSION_KEY).getAsString();
                Dependency dependency = new DefaultExternalModuleDependency(group, artifact, version);
                dependencies.add(dependency);
            }
        }
        return dependencies;
    }

    public static Set<URI> readRepositories(Map<String, Object> map) {
        HashSet<URI> repositories = new HashSet<>();
        if (map.containsKey(ConfigurationProperty.REPOSITORIES_DATA_KEY)) {
            LinkedHashMap<String, LinkedHashMap<String, String>> idMap = (LinkedHashMap<String, LinkedHashMap<String, String>>) map.get(ConfigurationProperty.REPOSITORIES_DATA_KEY);
            for (Map.Entry<String, LinkedHashMap<String, String>> entry : idMap.entrySet()) {
                String url = entry.getValue().get(ConfigurationProperty.REPOSITORIES_DATA_URL_KEY);
                try {
                    repositories.add(new URI(url));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return repositories;
    }

    public static HashSet<URI> readRepositories(JsonObject json) {
        HashSet<URI> repositories = new HashSet<>();
        if (json.has(ConfigurationProperty.REPOSITORIES_DATA_KEY)) {
            for (JsonElement jsonElement : json.getAsJsonArray(ConfigurationProperty.REPOSITORIES_DATA_KEY)) {
                JsonObject object = jsonElement.getAsJsonObject();
                String url = object.get(ConfigurationProperty.REPOSITORIES_DATA_URL_KEY).getAsString();

                try {
                    repositories.add(new URI(url));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return repositories;
    }
}
