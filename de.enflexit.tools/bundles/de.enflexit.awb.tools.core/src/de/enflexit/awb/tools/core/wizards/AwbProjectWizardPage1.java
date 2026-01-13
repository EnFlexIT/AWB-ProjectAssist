package de.enflexit.awb.tools.core.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.enflexit.awb.tools.core.SWTResourceManager;
import de.enflexit.awbAssist.core.ProjectBlueprint;

/**
 * The AWB-Projects WizardPage No. 1.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbProjectWizardPage1 extends WizardPage {

	private AwbProjectWizard awbProjectWizard;
	
	private List<String> listOfBlueprintsThatAreFollowedBySeparator;
	private List<Button> radioButtonList;
	
	/**
	 * Constructor for SampleNewWizardPage.
	 * @param selection the selection
	 */
	public AwbProjectWizardPage1(AwbProjectWizard awbProjectWizard) {
		super("AWB - Project Type");

		this.awbProjectWizard = awbProjectWizard;
		
		this.setTitle("New Agent.Workbench Plug-in Project");
		this.setDescription("This wizard creates a new Agent.Workbench Plug-in Project.");
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		this.initializeUI(parent);
		this.dialogChanged();
	}
	
	/**
	 * Returns the of blueprints that are followed by separator.
	 * @return the of blueprints that are followed by separator
	 */
	private List<String> getListOfBlueprintsThatAreFollowedBySeparator() {
		if (listOfBlueprintsThatAreFollowedBySeparator==null) {
			listOfBlueprintsThatAreFollowedBySeparator = new ArrayList<String>();
			listOfBlueprintsThatAreFollowedBySeparator.add("dbBlueprint");
			listOfBlueprintsThatAreFollowedBySeparator.add("restServerBlueprint");
			listOfBlueprintsThatAreFollowedBySeparator.add("featureBlueprint");
		}
		return listOfBlueprintsThatAreFollowedBySeparator;
	}
	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private void initializeUI(Composite parent) {
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 10;

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(layout);
		
		// --- New Row ----------------------------------------------
		Group rButtonGroup = new Group(container, SWT.NONE);
		rButtonGroup.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		rButtonGroup.setText("Please, select the desired project type to create:");
		rButtonGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		rButtonGroup.setLayout(new GridLayout(1, false));
		
		boolean isFistRadioButtonSelected = false;
		List<ProjectBlueprint> bpSorted = this.awbProjectWizard.getBlueprintsAvailableSorted();
		for (ProjectBlueprint bp : bpSorted) {
			// --- Create RadioButton -------------------------------
			Button rButton = new Button(rButtonGroup, SWT.RADIO + SWT.WRAP);
			rButton.setData(bp);
			rButton.setText(bp.getNumber() + ". " + bp.getName() + ": " + bp.getDescription());
			rButton.setSize(200, 50);
			if (isFistRadioButtonSelected==false) {
				rButton.setSelection(true);
				isFistRadioButtonSelected = true;
			}

			// --- Add a separator? ---------------------------------
			if (this.getListOfBlueprintsThatAreFollowedBySeparator().contains(bp.getBaseFolder())==true) {
				Label separator = new Label(rButtonGroup, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
				separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 3));
			}
			
			// --- Remind this RadioButton --------------------------
			this.getRadioButtonList().add(rButton);
		}
		
		// --- Finally, set control ---------------------------------
		this.setControl(container);
	}
	/**
	 * Reminder for the radio buttons created.
	 * @return the radio button list
	 */
	private List<Button> getRadioButtonList() {
		if (radioButtonList==null) {
			radioButtonList = new ArrayList<Button>();
		}
		return radioButtonList;
	}
	
	/**
	 * Ensures that everything is configured correctly.
	 */
	private void dialogChanged() {

		// --- Type of project --------------------------------------
		ProjectBlueprint pbSelected = this.getSelectedProjectBlueprint();
		if (pbSelected==null) {
			this.updateStatus("Please, specify the type of project that you want to create.");
			return;
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
	 * Returns the selected {@link ProjectBlueprint}
	 * @return the ProjectBlueprint
	 */
	public ProjectBlueprint getSelectedProjectBlueprint() {
		
		Button btSelected = null; 
		for (Button bt : this.getRadioButtonList()) {
			if (bt.getSelection()==true) {
				btSelected = bt;
				break;
			}
		}
		
		if (btSelected!=null) {
			return (ProjectBlueprint) btSelected.getData();
		}
		return null;
	}
	
}