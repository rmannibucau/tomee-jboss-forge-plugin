package org.jboss.forge.tomee;

import org.jboss.forge.maven.MavenPluginFacet;
import org.jboss.forge.maven.plugins.Configuration;
import org.jboss.forge.maven.plugins.ConfigurationElementImpl;
import org.jboss.forge.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.plugins.RequiresFacet;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresFacet({ DependencyFacet.class, ResourceFacet.class })
public class TomEEFacet extends BaseFacet {
    public static final String GID = "org.apache.openejb.maven";
    public static final String AID = "tomee-maven-plugin";

    @Inject
    private Shell shell;

    @Override
    public boolean install() {
        final DependencyBuilder dep = dependency();
        final List<Dependency> versions = project.getFacet(DependencyFacet.class).resolveAvailableVersions(dep);
        final Dependency dependency = shell.promptChoiceTyped("What version do you want to install?", versions);

        final MavenPluginBuilder plugin = MavenPluginBuilder.create().setDependency(dependency);
        final Configuration config = plugin.getConfig();

        {
            final String classifier = shell.promptChoiceTyped("Which distribution do you want to install?",
                    Arrays.asList("webprofile", "jaxrs", "plus"), "webprofile");

            final ConfigurationElementImpl classifierConfig = new ConfigurationElementImpl();
            classifierConfig.setName("tomeeClassifier");
            classifierConfig.setText(classifier);
            config.addConfigurationElement(classifierConfig);
        }
        {
            final String httpPort = shell.prompt("Which http port do you want to use?", "8080");
            final ConfigurationElementImpl httpPortConfig = new ConfigurationElementImpl();
            httpPortConfig.setName("tomeeHttpPort");
            httpPortConfig.setText(httpPort);
            config.addConfigurationElement(httpPortConfig);
        }
        if (isMoreThan(dependency, 1, 5, 2)) {
            final ConfigurationElementImpl simpleLog = new ConfigurationElementImpl();
            simpleLog.setName("simpleLog");
            simpleLog.setText("true");
            config.addConfigurationElement(simpleLog);
        }

        project.getFacet(MavenPluginFacet.class).addPlugin(plugin);

        return true;
    }

    @Override
    public boolean isInstalled() {
        return project.getFacet(MavenPluginFacet.class).hasPlugin(dependency());
    }

    @Override
    public boolean uninstall() {
        project.getFacet(MavenPluginFacet.class).removePlugin(dependency());
        return true;
    }

    private static DependencyBuilder dependency() {
        return DependencyBuilder.create().setGroupId(GID).setArtifactId(AID);
    }

    private static boolean isMoreThan(final Dependency version, final int major, final int minor, final int patch) {
        final Pattern pat = Pattern.compile("([0-9]*).([0-9]*).([0-9]*)(\\-SNAPSHOT)*");
        final Matcher matcher = pat.matcher(version.getVersion());
        return matcher.matches()
                && (Integer.parseInt(matcher.group(1)) > major
                || (Integer.parseInt(matcher.group(1)) == major && Integer.parseInt(matcher.group(2)) > minor)
                || (Integer.parseInt(matcher.group(1)) == major && Integer.parseInt(matcher.group(2)) == minor && Integer.parseInt(matcher.group(3)) > patch));
    }
}
