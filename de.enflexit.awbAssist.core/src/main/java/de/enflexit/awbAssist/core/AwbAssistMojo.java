package de.enflexit.awbAssist.core;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "run-awb-assist") // The goal for the plugin
public class AwbAssistMojo extends AbstractMojo {

    /**
     * Arguments to be passed to the AWB Assist application.
     */
    @Parameter(property = "args", required = false)
    private String[] args;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Running AWB Assist plugin...");

//      String[] pluginArgs = args != null ? args : new String[]{};
        String[] pluginArgs;
        if (args != null) {
            pluginArgs = args;
        } else {
            pluginArgs = new String[]{};
        }

        try {
            AwbAssist.main(pluginArgs);
        } catch (Exception e) {
            throw new MojoExecutionException("Error running AWB Assist", e);
        }
    }
}
