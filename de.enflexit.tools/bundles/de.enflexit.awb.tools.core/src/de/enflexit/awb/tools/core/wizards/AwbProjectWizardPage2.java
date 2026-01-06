package de.enflexit.awb.tools.core.wizards;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import de.enflexit.awb.tools.core.SWTResourceManager;
import de.enflexit.awbAssist.core.Arguments;
import de.enflexit.awbAssist.core.ProjectBlueprint;
import de.enflexit.awbAssist.core.StartArgument;

/**
 * In this wizard page the core .
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbProjectWizardPage2 extends WizardPage {

	private AwbProjectWizard awbProjectWizard;

	private Label lblsymbolicBundleName;
	private Text textBundleName;

	private Label lblbundleName;
	private Text textSymbolicBundleName;

	private Label lblAdditionalParameter;
	private TableViewer tableViewer;
	private Table tableAdditionalParameter;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public AwbProjectWizardPage2(AwbProjectWizard awbProjectWizard) {
		super("AWB - Project Configuration");
		
		this.awbProjectWizard = awbProjectWizard;
		
		this.setTitle("Parameter Configuration for the new AWB Project");
		this.setDescription("Please, configure the below project parameters for the new project.");
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
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible==true) {
			this.updateAdditionalArguments();
			this.dialogChanged();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		container.setLayout(layout);
		
		// --- Bundle Name ----------------------------------------------------
		lblbundleName = new Label(container, SWT.NULL);
		lblbundleName.setText("&Bundle Name:");

		textBundleName = new Text(container, SWT.BORDER | SWT.SINGLE);
		textBundleName.setText("My Bundle");
		textBundleName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textBundleName.addModifyListener(e -> dialogChanged());

		
		// --- Symbolic Bundle Name -------------------------------------------
		lblsymbolicBundleName = new Label(container, SWT.NULL);
		lblsymbolicBundleName.setText("&Symbolic Bundle Name:");

		textSymbolicBundleName = new Text(container, SWT.BORDER | SWT.SINGLE);
		textSymbolicBundleName.setText("org.example.awb");
		textSymbolicBundleName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textSymbolicBundleName.addModifyListener(e -> dialogChanged());

		
		// --- Additional Parameter ------------------------------------------- 
		lblAdditionalParameter = new Label(container, SWT.NULL);
		lblAdditionalParameter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblAdditionalParameter.setText("&Additional Parameter:");
		
		tableViewer =  new TableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		tableAdditionalParameter = tableViewer.getTable();
		tableAdditionalParameter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tableAdditionalParameter.setHeaderVisible(true);
		tableAdditionalParameter.setLinesVisible(true);

		TableViewerColumn colParameterKey = new TableViewerColumn(tableViewer, SWT.NONE);
		colParameterKey.getColumn().setWidth(200);
		colParameterKey.getColumn().setText("Property");
		colParameterKey.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BluePrintProperty p = (BluePrintProperty) element;
				return p.getKey();
			}
		});
		
		TableViewerColumn colParameterValue = new TableViewerColumn(tableViewer, SWT.NONE);
		colParameterValue.getColumn().setWidth(200);
		colParameterValue.getColumn().setText("Value");
		colParameterValue.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BluePrintProperty p = (BluePrintProperty) element;
				return p.getValue();
			}
		});
		colParameterValue.setEditingSupport(new ParameterValueEditingSupport(this));
		
		// --- Set Container as Control ---------------------------------------
		this.setControl(container);
	}

	/**
	 * The Class ParameterValueEditingSupport.
	 */
	private class ParameterValueEditingSupport extends EditingSupport {

	    private final AwbProjectWizardPage2 wizardPage;
	    private final CellEditor editor;

	    public ParameterValueEditingSupport(AwbProjectWizardPage2 wizardPage) {
	        super(wizardPage.tableViewer);
	        this.wizardPage = wizardPage;
	        this.editor = new TextCellEditor(wizardPage.tableViewer.getTable());
	    }

	    @Override
	    protected CellEditor getCellEditor(Object element) {
	        return editor;
	    }

	    @Override
	    protected boolean canEdit(Object element) {
	        return true;
	    }

	    @Override
	    protected Object getValue(Object element) {
	        return ((BluePrintProperty) element).getValue();
	    }

	    @Override
	    protected void setValue(Object element, Object userInputValue) {
	        ((BluePrintProperty) element).setValue(String.valueOf(userInputValue));
	        wizardPage.tableViewer.update(element, null);
	        wizardPage.dialogChanged();
	    }
	}
	
	
	/**
	 * Updates the table of additional arguments according to the selected blueprint.
	 */
	private void updateAdditionalArguments() {
		
		ProjectBlueprint btSelected = this.awbProjectWizard.getSelectedProjectBlueprint();
		ArrayList<BluePrintProperty> cProps = new ArrayList<BluePrintProperty>();
		for (StartArgument arg : btSelected.getRequiredArguments()) {
			if (arg.getArgumentName().toLowerCase().equals(Arguments.BUNDLE_NAME.toLowerCase())) continue;
			if (arg.getArgumentName().toLowerCase().equals(Arguments.SYMBOLIC_BUNDLE_NAME.toLowerCase())) continue;
			if (arg.getArgumentName().toLowerCase().equals(Arguments.TARGET_DIRECTORY.toLowerCase())) continue;
			cProps.add(new BluePrintProperty(arg.getArgumentName(), arg.getDefaultValue()));
		}
		tableViewer.setInput(cProps);
		tableViewer.getTable().setEnabled(cProps.size()!=0);
		
	}
	

	/**
	 * Check for configuration errors to display to the user.
	 */
	private void dialogChanged() {
		
		this.setTitle("Create: " + this.awbProjectWizard.getSelectedProjectBlueprint().getName() + "");
		
		if (this.getBundleName()==null || this.getBundleName().length() == 0) {
			this.updateStatus("The Bundle Name must be specified");
			return;
		}
		
		
		if (this.getSymbolicBundleName()==null || this.getSymbolicBundleName().length() == 0) {
			this.updateStatus("The Symbolic Bundle Name must be specified");
			return;
		}
		IWorkspace workspace =  ResourcesPlugin.getWorkspace();
		IStatus nameStatus = workspace.validateName(this.getSymbolicBundleName(), IResource.PROJECT);
		if (!nameStatus.isOK()) {
			this.updateStatus(nameStatus.getMessage());
			return ;
		}
		IProject handle = workspace.getRoot().getProject(this.getSymbolicBundleName());
		if (handle.exists()) {
			this.updateStatus("A project with that Symbolic Bundle Name already exists in the workspace.");
			return;
		}
		
		
		HashMap<String, String> additionalParameterHashMap = this.getAdditionalParameter();
		if (additionalParameterHashMap.size()>0) {
			for (String key : additionalParameterHashMap.keySet()) {
				String value = additionalParameterHashMap.get(key);
				if (value==null || value.length() == 0) {
					this.updateStatus("The value for the additional parameter '" + key + "' must be specified");
					return;
				}
			}
		}
		
		this.updateStatus(null);
	}
	/**
	 * Update status.
	 * @param message the message
	 */
	private void updateStatus(String message) {
		this.setErrorMessage(message);
		this.setPageComplete(message == null);
	}

	
	
	public String getBundleName() {
		return textBundleName.getText();
	}

	public String getSymbolicBundleName() {
		return textSymbolicBundleName.getText();
	}
	
	public HashMap<String, String> getAdditionalParameter() {
		
		HashMap<String, String> additionalParameterHashMap = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		ArrayList<BluePrintProperty> cProps = (ArrayList<BluePrintProperty>) tableViewer.getInput();
		for (BluePrintProperty prop : cProps) {
			additionalParameterHashMap.put(prop.getKey(), prop.getValue());
		}
		return additionalParameterHashMap;
	}
	
}