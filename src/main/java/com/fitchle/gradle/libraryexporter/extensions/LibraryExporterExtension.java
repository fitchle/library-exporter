package com.fitchle.gradle.libraryexporter.extensions;

import com.fitchle.gradle.libraryexporter.configuration.ExportType;
import org.gradle.api.Project;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryExporterExtension {
    private final List<String> dependencyExcludes;
    private final List<URI> repositoryExcludes;
    private boolean libraryExport;
    private ExportType exportType;
    private final List<Project> modules;
    private Project parentModule;
    private boolean moduleEnabled = false;

    public LibraryExporterExtension() {
        this.dependencyExcludes = new ArrayList<>();
        this.repositoryExcludes = new ArrayList<>();
        this.libraryExport = true;
        this.exportType = ExportType.YAML;
        this.modules = new ArrayList<>();
    }

    public void exportType(ExportType exportType) {
        this.exportType = exportType;
    }

    public void libraryExport(boolean libraryExport) {
        this.libraryExport = libraryExport;
    }

    public void excludeDependency(String id) {
        this.dependencyExcludes.add(id);
    }

    public void excludeRepository(String url) throws URISyntaxException {
        this.excludeRepository(new URI(url));
    }

    public void excludeRepository(URI uri) {
        this.repositoryExcludes.add(uri);
    }

    public void addModules(Project... project) {
        if (!this.moduleEnabled) this.moduleEnabled = true;
        this.modules.addAll(Arrays.asList(project));
    }

    public void parentModule(Project parent) {
        if (!this.moduleEnabled) this.moduleEnabled = true;
        this.parentModule = parent;
    }

    public boolean isModuleEnabled() {
        return moduleEnabled;
    }

    public boolean isLibraryExport() {
        return libraryExport;
    }

    public List<String> getDependencyExcludes() {
        return dependencyExcludes;
    }

    public List<Project> getModules() {
        return modules;
    }

    public Project getParentModule() {
        return parentModule;
    }

    public ExportType getExportType() {
        return exportType;
    }

    public List<URI> getRepositoryExcludes() {
        return repositoryExcludes;
    }
}
