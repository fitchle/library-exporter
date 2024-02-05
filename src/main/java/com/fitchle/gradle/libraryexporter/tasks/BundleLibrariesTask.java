package com.fitchle.gradle.libraryexporter.tasks;

import com.fitchle.gradle.libraryexporter.configuration.Configuration;
import com.fitchle.gradle.libraryexporter.configuration.ConfigurationWriter;
import com.fitchle.gradle.libraryexporter.configuration.ExportType;
import com.fitchle.gradle.libraryexporter.extensions.LibraryExporterExtension;
import com.fitchle.gradle.libraryexporter.properties.ConfigurationProperty;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class BundleLibrariesTask extends DefaultTask {

    private final LibraryExporterExtension extension;

    @Inject
    public BundleLibrariesTask(LibraryExporterExtension extension) {
        this.extension = extension;
    }
    @TaskAction
    public void run() {
        if (!extension.isModuleEnabled() || extension.getParentModule() == null) return;

        for (Project module : extension.getModules()) {
            File buildDir = new File(module.getBuildDir(), ConfigurationProperty.BASE_DIR);
            if (!buildDir.exists()) buildDir.mkdirs();
        }

        if (extension.getExportType().equals(ExportType.YAML)) {
            File parentLibFile = new File(extension.getParentModule().getBuildDir() + File.separator + ConfigurationProperty.BASE_DIR,"libs.yml");
            Configuration bundled = this.bundleYAML(parentLibFile);
            ConfigurationWriter writer = new ConfigurationWriter(parentLibFile, bundled);
            writer.writeYAML();
        }

        if (extension.getExportType().equals(ExportType.JSON)) {
            File parentLibFile = new File(extension.getParentModule().getBuildDir() + File.separator + ConfigurationProperty.BASE_DIR,"libs.json");
            Configuration bundled = this.bundleJSON(parentLibFile);
            ConfigurationWriter writer = new ConfigurationWriter(parentLibFile, bundled);
            writer.writeJSON();
        }
    }

    private Configuration bundleYAML(File parentLibFile) {
        Configuration parentConf = new Configuration(new HashSet<>(), new HashSet<>());
        if (parentLibFile.exists()) {
            parentConf = Configuration.readYAMLFile(parentLibFile);
        }

        List<Configuration> moduleConfigurations = new ArrayList<>();
        for (Project module : extension.getModules()) {
            Configuration moduleConf = ExportDependencyTask.handleConfiguration(module, new ArrayList<>(), new ArrayList<>());
            if (module.getExtensions().findByType(LibraryExporterExtension.class) != null) {
                LibraryExporterExtension ext = module.getExtensions().getByType(LibraryExporterExtension.class);
                moduleConf = ExportDependencyTask.handleConfiguration(module, ext.getDependencyExcludes(), ext.getRepositoryExcludes());
            }
            moduleConfigurations.add(moduleConf);
        }

        return Configuration.merge(parentConf, moduleConfigurations.toArray(Configuration[]::new));
    }

    private Configuration bundleJSON(File parentLibFile) {
        Configuration parentConf = new Configuration(new HashSet<>(), new HashSet<>());
        if (parentLibFile.exists()) {
            parentConf = Configuration.readJSONFile(parentLibFile);
        }

        List<Configuration> moduleConfigurations = new ArrayList<>();
        for (Project module : extension.getModules()) {
            Configuration moduleConf = ExportDependencyTask.handleConfiguration(module, new ArrayList<>(), new ArrayList<>());
            if (module.getExtensions().findByType(LibraryExporterExtension.class) != null) {
                LibraryExporterExtension ext =  module.getExtensions().getByType(LibraryExporterExtension.class);
                moduleConf = ExportDependencyTask.handleConfiguration(module,ext.getDependencyExcludes(), ext.getRepositoryExcludes());
            }
            moduleConfigurations.add(moduleConf);
        }

        return Configuration.merge(parentConf, moduleConfigurations.toArray(Configuration[]::new));
    }
}
