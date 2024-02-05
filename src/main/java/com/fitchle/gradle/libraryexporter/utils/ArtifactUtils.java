package com.fitchle.gradle.libraryexporter.utils;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.artifacts.ResolvedDependency;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ArtifactUtils {
    public static Set<ResolvedArtifact> resolveArtifacts(final List<String> excludes, final Collection<Configuration> configurations) {
        return configurations.stream()
                .map(Configuration::getResolvedConfiguration)
                .map(ResolvedConfiguration::getFirstLevelModuleDependencies)
                .flatMap(Collection::stream)
                .filter(dep -> !excludes.contains(dep.getModule().getId().toString()))
                .map(ResolvedDependency::getAllModuleArtifacts)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
