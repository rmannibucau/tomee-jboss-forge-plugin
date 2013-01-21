package org.apache.openejb.forge.plugins;

import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.project.facets.events.RemoveFacets;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.SetupCommand;

import javax.enterprise.event.Event;
import javax.inject.Inject;

@Alias("tomee")
@Help("A plugin helping with the tomee-maven-plugin usage")
public class TomEEPlugin implements Plugin {
    @Inject
    private Shell shell;

    @Inject
    private Event<InstallFacets> install;

    @Inject
    private Event<RemoveFacets> remove;

    @SetupCommand
    public void setup() {
        install.fire(new InstallFacets(TomEEFacet.class));
    }

    @Command("uninstall")
    public void uninstall() throws Exception {
        remove.fire(new RemoveFacets(TomEEFacet.class));
    }

    @Command(value = "run", help = "Start TomEE and wait for Ctrl+C to stop it")
    public void run() throws Exception {
        tomeeMvnPlugin("run");
    }

    @Command(value = "debug", help = "Start TomEE in remote debug mode and wait for Ctrl+C to stop it")
    public void debug() throws Exception {
        tomeeMvnPlugin("debug");
    }

    @Command(value = "start", help = "Start TomEE and forget it")
    public void start() throws Exception {
        tomeeMvnPlugin("start");
    }

    @Command(value = "stop", help = "Stop TomEE (when started with start)")
    public void stop() throws Exception {
        tomeeMvnPlugin("stop");
    }

    private void tomeeMvnPlugin(final String cmd) throws Exception {
        shell.execute("mvn tomee:" + cmd);
    }
}
