package de.enflexit.awb.tools.core.wizards;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.enflexit.awb.tools.core.SWTResourceManager;

/**
 * The AWB-Projects WizardPage.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbProjectWizardPage3 extends WizardPage {

	private AwbProjectWizard awbProjectWizard;
	
	private Button buttonUseWorkspaceDir;
	private Label labelAlternativePath;
	private Text textAlternativePath;
	private Button buttonBrowseGit;
	
	
	/**
	 * Constructor for SampleNewWizardPage.
	 * @param selection the selection
	 */
	public AwbProjectWizardPage3(AwbProjectWizard awbProjectWizard) {
		super("AWB - Project Destination");
		
		this.awbProjectWizard = awbProjectWizard;
		
		this.setTitle("Destination of the new AWB Project");
		this.setDescription("If not in the current workspace, please configure the destination of the new project.");
		this.setImageDescriptor(ImageDescriptor.createFromImage(SWTResourceManager.getImage(SWTResourceManager.class, "/icons/awb64.png")));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		SWTResourceManager.disposeImages();
	}
	
	/* (non
	 * -Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible==true) {
			this.dialogChanged();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(layout);
		
		// --- New Row ----------------------------------------------
		GridData gd_buttonUseWorkSpace = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_buttonUseWorkSpace.verticalIndent = 10;

		buttonUseWorkspaceDir = new Button(container, SWT.CHECK);
		buttonUseWorkspaceDir.setLayoutData(gd_buttonUseWorkSpace);
		buttonUseWorkspaceDir.setText("Place new AWB-Project in current workspace directory!");
		buttonUseWorkspaceDir.setSelection(true);
		buttonUseWorkspaceDir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				AwbProjectWizardPage3.this.setVisualizationForUseGit();
			}
		});
		
		// --- New Row ----------------------------------------------
		labelAlternativePath = new Label(container, SWT.NULL);
		labelAlternativePath.setText("&Alternative Path (e.g. git):");

		textAlternativePath = new Text(container, SWT.BORDER | SWT.SINGLE);
		textAlternativePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textAlternativePath.addModifyListener(e -> dialogChanged());

		buttonBrowseGit = new Button(container, SWT.PUSH);
		buttonBrowseGit.setText("Browse...");
		buttonBrowseGit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				AwbProjectWizardPage3.this.handleBrowseDirectory();
			}
		});
		
		// --- Finally, the container integration -------------------
		this.setControl(container);
		this.setVisualizationForUseGit();
		this.dialogChanged();
	}
	/**
	 * Sets the use git path.
	 */
	private void setVisualizationForUseGit() {
		
		this.labelAlternativePath.setEnabled(this.isUseAlternativePath());
		this.textAlternativePath.setEnabled(this.isUseAlternativePath());
		this.buttonBrowseGit.setEnabled(this.isUseAlternativePath());
		
		if (this.isUseAlternativePath()==false) this.textAlternativePath.setText("");
		this.dialogChanged();
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for the
	 * container field.
	 */
	private void handleBrowseDirectory() {
		
		DirectoryDialog dirDialog = new DirectoryDialog(getShell());
		dirDialog.setText("Please, select the base destination directory for the new project");
		dirDialog.setMessage("Create the base destination directory ?");
		
		IWorkspace workspace =  ResourcesPlugin.getWorkspace();
		String baseDir = workspace.getRoot().getLocation().toOSString();
		dirDialog.setFilterPath(baseDir);
		
		String destinDir = dirDialog.open();
		
		this.textAlternativePath.setText(destinDir);
	}

	/**
	 * Ensures that everything is configured correctly.
	 */
	private void dialogChanged() {

		this.setTitle("Create: " + this.awbProjectWizard.getSelectedProjectBlueprint().getName() + "");
		
		// --- Place in alternative directory? ---
		if (this.isUseAlternativePath()==true) {
			String altPath = this.getAlternativePath();
			if (altPath==null || altPath.length()==0) {
				this.updateStatus("The alternative destintaion path must be specifeid.");
				return;
			}
		}
	
		this.updateStatus(null);
	}
	/**
	 * Updates the wizard status depending on the specified message.
	 * @param message the message
	 */
	private void updateStatus(String message) {
		this.setErrorMessage(message);
		this.setPageComplete(message==null);
	}
	
	
	/**
	 * Checks if an alternative path is to be used for the new project.
	 * @return true, if is an alternative path is to be used
	 */
	public boolean isUseAlternativePath() {
		return !buttonUseWorkspaceDir.getSelection();
	}
	/**
	 * Return the alternative path to be used.
	 * @return the alternative path to be used
	 */
	public String getAlternativePath() {
		return this.isUseAlternativePath()==true ? textAlternativePath.getText() : null;
	}
	/**
	 * Returns the target directory.
	 * @return the target directory
	 */
	public String getTargetDirectory() {
		if (this.isUseAlternativePath()==true) {
			return this.getAlternativePath();
		} 
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
	}
	
}