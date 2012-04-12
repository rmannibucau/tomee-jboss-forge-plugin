package org.apache.openejb.forge.plugins;

import org.jboss.forge.maven.MavenPluginFacet;
import org.jboss.forge.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.BaseFacet;

public class TomEEFacet extends BaseFacet {
    public static final String SHORT_TOMEE_MAVEN_PLUGIN = "org.apache.openejb.maven:tomee-maven-plugin";
    public static final String TOMEE_MAVEN_PLUGIN = SHORT_TOMEE_MAVEN_PLUGIN + ":1.0-alpha-1";

    @Override
    public boolean install() {
        project.getFacet(MavenPluginFacet.class).addPlugin(
                MavenPluginBuilder.create().setDependency(DependencyBuilder.create(TOMEE_MAVEN_PLUGIN)));
        return true;
    }

    @Override
    public boolean isInstalled() {
        return project.getFacet(MavenPluginFacet.class).hasPlugin(DependencyBuilder.create(SHORT_TOMEE_MAVEN_PLUGIN));
    }

    @Override
    public boolean uninstall() {
        project.getFacet(MavenPluginFacet.class).removePlugin(DependencyBuilder.create(TOMEE_MAVEN_PLUGIN));
        return true;
    }
}
