package com.fitchle.gradle.libraryexporter.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class WriterUtils {
    public static void writeJSON(FileWriter writer, Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> conf) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject object = new JsonObject();
        conf.forEach((title, value) -> {
            JsonArray obj1 = new JsonArray();
            value.forEach((id, obj) -> {
                JsonObject obj2 = new JsonObject();
                obj.forEach(obj2::addProperty);
                obj1.add(obj2);
            });
            object.add(title, obj1);
        });

        gson.toJson(object, writer);
    }

    public static void writeJSON(FileWriter writer, JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonObject, writer);
    }

    public static void writeJSON(File file, JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(jsonObject, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeYAML(FileWriter writer, Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> conf) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);
        yaml.dump(conf, writer);
    }

    public static void writeYAML(File file, Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> conf) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        try {
            FileWriter writer = new FileWriter(file);
            yaml.dump(conf, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
