package com.fitchle.gradle.libraryexporter.tasks;


import com.fitchle.gradle.libraryexporter.LibraryExporterPlugin;
import com.fitchle.gradle.libraryexporter.configuration.Configuration;
import com.fitchle.gradle.libraryexporter.configuration.ConfigurationWriter;
import com.fitchle.gradle.libraryexporter.extensions.LibraryExporterExtension;
import com.fitchle.gradle.libraryexporter.properties.ConfigurationProperty;
import com.fitchle.gradle.libraryexporter.utils.ArtifactUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.List;

public abstract class ExportDependencyTask extends DefaultTask {
    private final LibraryExporterExtension extension;
    private final Project project;
    private final Logger logger;

    @Inject
    public ExportDependencyTask(LibraryExporterExtension extension) {
        this.extension = extension;
        this.project = this.getProject();
        this.logger = LibraryExporterPlugin.LOGGER;
    }


    @TaskAction
    public void run() {
        if (!extension.isLibraryExport()) return;

        File buildDir = new File(project.getBuildDir(), ConfigurationProperty.BASE_DIR);
        if (!buildDir.exists()) buildDir.mkdirs();

        export(this.project);
    }

    public void export(Project project) {
        Configuration configuration = handleConfiguration(project, extension.getDependencyExcludes(), extension.getRepositoryExcludes());

        switch (extension.getExportType()) {
            case YAML -> {
                File file = new File(project.getBuildDir() + File.separator + ConfigurationProperty.BASE_DIR , "libs.yml");
                ConfigurationWriter writer = new ConfigurationWriter(file, configuration);
                writer.writeYAML();
            }

            case JSON -> {
                File file = new File(project.getBuildDir() + File.separator + ConfigurationProperty.BASE_DIR, "libs.json");
                ConfigurationWriter writer = new ConfigurationWriter(file, configuration);
                writer.writeJSON();
            }
        }


        this.logger.info("Exported Libraries:");
        configuration.dependencies().forEach(dep -> this.logger.info("-> " + dep.getGroup() + ":" + dep.getName() + ":" + dep.getVersion()));
    }

    public static Configuration handleConfiguration(Project project, List<String> excludes, List<URI> repositoryExcludes) {
        HashSet<URI> repos = new HashSet<>();
        project.allprojects(pr -> pr.getRepositories().stream().map(it -> (MavenArtifactRepository) it).forEach(artifactRepository -> {
            if (!artifactRepository.getUrl().toString().startsWith("file:") && !repositoryExcludes.contains(artifactRepository.getUrl()))
                repos.add(artifactRepository.getUrl());
        }));


        HashSet<Dependency> dependencies = new HashSet<>();

        for (ResolvedArtifact compileOnly : ArtifactUtils.resolveArtifacts(excludes, List.of(project.getConfigurations().getByName("compileOnly")))) {
            ModuleVersionIdentifier identifier = compileOnly.getModuleVersion().getId();
            dependencies.add(new DefaultExternalModuleDependency(identifier.getGroup(), identifier.getName(), identifier.getVersion()));
        }

        return new Configuration(repos, dependencies);
    }
}
