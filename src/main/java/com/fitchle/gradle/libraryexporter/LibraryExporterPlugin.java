package com.fitchle.gradle.libraryexporter;

import com.fitchle.gradle.libraryexporter.extensions.LibraryExporterExtension;
import com.fitchle.gradle.libraryexporter.tasks.BundleLibrariesTask;
import com.fitchle.gradle.libraryexporter.tasks.ExportDependencyTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LibraryExporterPlugin implements Plugin<Project> {
    public static final Logger LOGGER = LoggerFactory.getLogger("LibraryExporter");
    private TaskProvider<ExportDependencyTask> exportDependencyTask;
    private TaskProvider<BundleLibrariesTask> bundleLibrariesTask;

    @Override
    public void apply(Project target) {
        LibraryExporterExtension extension = target.getExtensions().create("libraryExporter", LibraryExporterExtension.class);
        target.allprojects(pr -> {
            pr.getConfigurations().getByName("compileOnly").setCanBeResolved(true);
            pr.getConfigurations().getByName("api").setCanBeResolved(true);
        });

        this.exportDependencyTask = target.getTasks().register("exportDependencies", ExportDependencyTask.class, extension);
        this.bundleLibrariesTask = target.getTasks().register("bundleLibraries", BundleLibrariesTask.class, extension);
        this.exportDependencyTask.configure(t -> t.setGroup("exporting"));
        this.bundleLibrariesTask.configure(t -> t.setGroup("exporting"));

        target.getTasks().named("build", t -> t.finalizedBy(exportDependencyTask));
        exportDependencyTask.get().finalizedBy(bundleLibrariesTask);
    }

    public TaskProvider<BundleLibrariesTask> getBundleLibrariesTask() {
        return bundleLibrariesTask;
    }

    public TaskProvider<ExportDependencyTask> getExportDependencyTask() {
        return exportDependencyTask;
    }
}
