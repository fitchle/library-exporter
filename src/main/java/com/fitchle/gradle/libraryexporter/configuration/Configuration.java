package com.fitchle.gradle.libraryexporter.configuration;


import com.fitchle.gradle.libraryexporter.utils.ReaderUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.gradle.api.artifacts.Dependency;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record Configuration(Set<URI> repositories, Set<Dependency> dependencies) {
    public static Configuration readJSONFile(File file) {
        Set<Dependency> dependencies;
        Set<URI> repositories;

        JsonObject json;

        try (FileReader reader = new FileReader(file)) {
            json = new Gson().fromJson(reader, JsonObject.class);

            dependencies = ReaderUtils.readDependencies(json);
            repositories = ReaderUtils.readRepositories(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Configuration(repositories, dependencies);
    }

    public static Configuration readYAMLFile(File file) {
        Set<Dependency> dependencies;
        Set<URI> repositories;

        Yaml yaml = new Yaml();

        Map<String, Object> map;

        try (FileReader reader = new FileReader(file)) {
            map = yaml.load(reader);

            dependencies = ReaderUtils.readDependencies(map);
            repositories = ReaderUtils.readRepositories(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Configuration(repositories, dependencies);
    }

    public static Configuration merge(Configuration conf, Configuration... toMerges) {
        HashSet<URI> repos = new HashSet<>(conf.repositories());
        HashSet<Dependency> dependencies = new HashSet<>(conf.dependencies());


        for (Configuration toMerge : toMerges) {
            repos.addAll(toMerge.repositories());
            dependencies.addAll(toMerge.dependencies());
        }

        return new Configuration(repos, dependencies);
    }
}
