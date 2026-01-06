package de.enflexit.awb.tools.core.wizards;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

import de.enflexit.awbAssist.core.Arguments;
import de.enflexit.awbAssist.core.AwbAssistArgumentTransfer;
import de.enflexit.awbAssist.core.InternalResourceHandler;
import de.enflexit.awbAssist.core.ProjectBlueprint;

/**
 * The Class AwbProjectWizard.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbProjectWizard extends Wizard implements INewWizard {

	private List<ProjectBlueprint> projectBlueprintAvailable;
	
	private IWorkbench workbench;
	private ISelection selection;

	private AwbProjectWizardPage1 page1BlueprintSelection;
	private AwbProjectWizardPage2 page2BlueprintConfiguraion;
	private AwbProjectWizardPage3 page3BlueprintTarget;

	/**
	 * Constructor for NewAWBProjectWizard.
	 */
	public AwbProjectWizard() {
		super();
		this.setNeedsProgressMonitor(true);
	}
	
	/**
	 * Returns the Blueprints available.
	 * @return the blueprints available
	 */
	protected List<ProjectBlueprint> getBlueprintsAvailable() {
		if (projectBlueprintAvailable==null) {
			projectBlueprintAvailable = InternalResourceHandler.getProjectBlueprintsAvailable();
		}
		return projectBlueprintAvailable;
	}
	
	protected List<ProjectBlueprint> getBlueprintsAvailableSorted() {

		List<ProjectBlueprint> sortedList = new ArrayList<ProjectBlueprint>(this.getBlueprintsAvailable());
		Collections.sort(sortedList, new Comparator<ProjectBlueprint>() {
			@Override
			public int compare(ProjectBlueprint pb1, ProjectBlueprint pb2) {
				Integer ordNo1 = pb1.getNumber();
				Integer ordNo2 = pb2.getNumber();
				return ordNo1.compareTo(ordNo2);
			}
		});
		return sortedList;
	}
	
	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 *
	 * @param workbench the workbench
	 * @param selection the selection
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}
	/**
	 * Returns the current workbench.
	 * @return the workbench
	 */
	public IWorkbench getWorkbench() {
		return workbench;
	}
	/**
	 * Returns the current selection.
	 * @return the selection
	 */
	public ISelection getSelection() {
		return selection;
	}
	
	
	/**
	 * Gets the page 1.
	 * @return the page 1
	 */
	private AwbProjectWizardPage1 getPage1() {
		if (page1BlueprintSelection==null) {
			page1BlueprintSelection = new AwbProjectWizardPage1(this);
		}
		return page1BlueprintSelection;
	}
	/**
	 * Returns the selected ProjectBlueprint.
	 * @return the blue project blueprint
	 */
	public ProjectBlueprint getSelectedProjectBlueprint() {
		return this.getPage1().getSelectedProjectBlueprint();
	}
	
	/**
	 * Gets the page 2.
	 * @return the page 2
	 */
	private AwbProjectWizardPage2 getPage2() {
		if (page2BlueprintConfiguraion==null) {
			page2BlueprintConfiguraion = new AwbProjectWizardPage2(this);
		}
		return page2BlueprintConfiguraion;
	}
	/**
	 * Gets the page 2.
	 * @return the page 2
	 */
	private AwbProjectWizardPage3 getPage3() {
		if (page3BlueprintTarget==null) {
			page3BlueprintTarget = new AwbProjectWizardPage3(this);
		}
		return page3BlueprintTarget;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		this.addPage(this.getPage1());
		this.addPage(this.getPage2());
		this.addPage(this.getPage3());
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		// --- Collect wizard settings ------------------------------
		final ProjectBlueprint pb = this.getSelectedProjectBlueprint();
		
		final String bundleName = this.getPage2().getBundleName();
		final String symbolicBundleName = this.getPage2().getSymbolicBundleName();
		final String targetDirectory = this.getPage3().getTargetDirectory();

		HashMap<String, String> configuration = this.getPage2().getAdditionalParameter();
		
		configuration.put(Arguments.BLUEPRINT, pb.getBaseFolder());
		configuration.put(Arguments.BUNDLE_NAME, bundleName);
		configuration.put(Arguments.SYMBOLIC_BUNDLE_NAME, symbolicBundleName);
		configuration.put(Arguments.TARGET_DIRECTORY, targetDirectory);
		
		// --- Define runnable --------------------------------------
		IRunnableWithProgress op = monitor -> {
			try {
				this.doFinish(pb, configuration, monitor);
				
			} catch (CoreException e) {
				throw new InvocationTargetException(e);
			} finally {
				monitor.done();
			}
		};
		
		// --- Execute the previously defined runnable task ---------
		try {
			this.getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(this.getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing or
	 * just replace its contents, and open the editor on the newly created file.
	 *
	 * @param pb the ProjectBlueprint to deploy
	 * @param configuration the configuration for the deployment 
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	private void doFinish(ProjectBlueprint pb, HashMap<String, String> configuration, IProgressMonitor monitor) throws CoreException {

		monitor.beginTask("Creating " + pb.getName(), 2);

		try {
			// --- Start the creation process of AwbAssist ----------
			AwbAssistArgumentTransfer.passArguments(configuration);
			monitor.worked(1);
			
			// --- Import all created projects ----------------------
			File newDirectory = new Path(configuration.get(Arguments.TARGET_DIRECTORY)).append(configuration.get(Arguments.SYMBOLIC_BUNDLE_NAME)).toFile();
			List<File> projectsCreated = this.findDotProjectFiles(newDirectory);
			projectsCreated.forEach(dotProjectFile -> {
				try {
					this.importGeneratedProject(dotProjectFile);
				} catch (InvocationTargetException | CoreException | InterruptedException ex) {
					ex.printStackTrace();
				}
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		monitor.worked(1);
		monitor.done();
		
	}
	
	/**
	 * Find '.project' files in the specified directory.
	 *
	 * @param directoryToSearchIn the directory to search in
	 * @return the list
	 */
	private List<File> findDotProjectFiles(File directoryToSearchIn) {
		
		if (directoryToSearchIn==null || directoryToSearchIn.isDirectory()==false) return null;

		// --- Search for the '.project' file -----------------------
		List<File> pFilesFound = new ArrayList<File>();
		File dotProjectFile = directoryToSearchIn.toPath().resolve(".project").toFile();
		if (dotProjectFile.exists()==true) {
			pFilesFound.add(dotProjectFile);
		}

		// --- Search for sub directories ---------------------------
		File[] subDirsFound = directoryToSearchIn.listFiles(new FileFilter() {
			@Override
			public boolean accept(File fileToCheck) {
				return fileToCheck.isDirectory();
			}
		});

		// --- Search for further .project files --------------------
		for (File subDir : subDirsFound) {
			List<File> pFileFoundInSubDir = AwbProjectWizard.this.findDotProjectFiles(subDir);
			pFilesFound.addAll(pFileFoundInSubDir);
		}
		return pFilesFound;
	}
	
	/**
	 * Imports a generated AWB-Project.
	 *
	 * @param dotProjectFile the '.project' file
	 * @throws CoreException the core exception
	 * @throws InvocationTargetException the invocation target exception
	 * @throws InterruptedException the interrupted exception
	 */
	private void importGeneratedProject(File dotProjectFile) throws CoreException, InvocationTargetException, InterruptedException {
		
		String dotProjectFilePath = dotProjectFile.toString();
		String newProjectDir  = dotProjectFile.getParentFile().toString();

		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(dotProjectFilePath));
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, null);
		project.open(null);
		
		IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
			public String queryOverwrite(String file) {
				return ALL;
			}
		};
		
		ImportOperation importOperation = new ImportOperation(project.getFullPath(), new File(newProjectDir), FileSystemStructureProvider.INSTANCE, overwriteQuery);
		importOperation.setCreateContainerStructure(false);
		importOperation.run(new NullProgressMonitor());
	}
	
}