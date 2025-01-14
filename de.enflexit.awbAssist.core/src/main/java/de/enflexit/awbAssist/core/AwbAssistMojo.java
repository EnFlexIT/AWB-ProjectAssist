package de.enflexit.awbAssist.core;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * The Class AwbAssistMojo.
 * This class along with the pom file configuration should enable users to run AWB Assist as a plugin in Maven 
 *
 * @author Omar Ben Chobba - Enflex.IT GmbH
 */
@Mojo(name = "run-awb-assist") 
public class AwbAssistMojo extends AbstractMojo {
	
	/**
	 * ---------------------------- A short explanation on how the plugin could be used ----------------------------
	 * The plugin is nothing but a java project built into a jar file using maven. Adding this MOJO class that should bridge
	 * the java project with the maven's plugin architecture.
	 * (Java project that performs certain tasks) + MojoClass + pom configuration --> maven plugin
	 * 
	 * The plugin could be used in other java projects that are also built with maven by adding the plugin parameters in the build
	 * section of the project's pom file. 
	 * 
	 * Once added to the pom file, it is possible to use the plugin via Powershell by typing: 
	 * mvn theGroupId:artifactId:MojoName -Dargs= "args1,arg2,...,argn"
	 * In our case for example
	 * mvn de.enflexit.awbAssist:de.enflexit.awbAssist.core:run-awb-assist -Dargs="-bp"
	 * 
	 * It is also possible to run the plugin by setting a run configuration in eclipse and passing the plugin parameters along
	 * with the arguments as goal:
	 * theGroupId:artifactId:MojoName -Dargs= "args1,arg2,...,argn"
	 * In our case
	 * de.enflexit.awbAssist:de.enflexit.awbAssist.core:run-awb-assist -Dargs="-bp"
	 * 
	 * In this class the necessity of arguments was set to false as it is possible to run AWB Assist without passing arguments 
	 *  ---------------------------------------------------------------------------------------------------------------------------
	 */

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
