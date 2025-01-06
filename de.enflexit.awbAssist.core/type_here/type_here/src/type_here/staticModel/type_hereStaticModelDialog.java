package type_here.staticModel;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import energy.optionModel.gui.sysVariables.AbstractStaticModel;
import energy.optionModel.gui.sysVariables.AbstractStaticModelDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;

public class type_hereStaticModelDialog extends AbstractStaticModelDialog implements ActionListener {

	private static final long serialVersionUID = 4944933489538553875L;
	
	private type_hereStaticDataModel staticDataModel;
	private JLabel jLabelMotorPower;
	private JTextField jTextFieldMotorPower;
	private JPanel panel;
	private JButton btnOk;
	private JButton btnCancel;
	private JLabel jLabelErrorMessage;

	/**
	 * Instantiates a new type_here static model dialog.
	 *
	 * @param owner the owner
	 * @param staticModel the static model
	 */
	public type_hereStaticModelDialog(Window owner, AbstractStaticModel staticModel) {
		super(owner, staticModel);
		this.setStaticDataModel((type_hereStaticDataModel) staticModel.getStaticDataModel());
		initialize();
	}
	
	/**
	 * Describes the appearance parameters of the dialog window.
	 */
	private void initialize() {
		this.setTitle("type_here Static Data Model");
		this.setSize(365, 201);
		this.loadDataModelToDialog();
		
		getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelMotorPower = new GridBagConstraints();
		gbc_jLabelMotorPower.anchor = GridBagConstraints.EAST;
		gbc_jLabelMotorPower.insets = new Insets(5, 10, 5, 10);
		gbc_jLabelMotorPower.gridx = 0;
		gbc_jLabelMotorPower.gridy = 1;
		getContentPane().add(getJLabelMotorPower(), gbc_jLabelMotorPower);
		GridBagConstraints gbc_jTextFieldMotorPower = new GridBagConstraints();
		gbc_jTextFieldMotorPower.anchor = GridBagConstraints.WEST;
		gbc_jTextFieldMotorPower.insets = new Insets(5, 5, 5, 0);
		gbc_jTextFieldMotorPower.gridx = 1;
		gbc_jTextFieldMotorPower.gridy = 1;
		getContentPane().add(getJTextFieldMotorPower(), gbc_jTextFieldMotorPower);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 0, 5, 0);
		gbc_panel.gridwidth = 6;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		getContentPane().add(getPanel(), gbc_panel);
		GridBagConstraints gbc_jLabelErrorMessage = new GridBagConstraints();
		gbc_jLabelErrorMessage.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelErrorMessage.anchor = GridBagConstraints.WEST;
		gbc_jLabelErrorMessage.gridx = 1;
		gbc_jLabelErrorMessage.gridy = 4;
		getContentPane().add(getJLabelErrorMessage(), gbc_jLabelErrorMessage);
	}

	/**
	 * Action performed.
	 *
	 * @param ae the ae
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == getBtnOk()) {
			double maxPower = this.getNumericValue(this.getJTextFieldMotorPower());
			this.getStaticDataModel().setMotorPower(maxPower);
			this.dispose();
		} else if (ae.getSource() == getBtnCancel()) {
			this.dispose();
		}
	}
	
	/**
	 * Gets the numeric value from a text field input.
	 * The method doesn't accept negative values
	 * @param textField the text field
	 * @return the numeric value
	 */
	private double getNumericValue(JTextField textField) {
		double num = 0.0;
		String content = this.getTextFieldContent(textField);
		if (content!=null) {
			num = Double.parseDouble(content);
			// added part for excluding negative values
			if (num < 0) {
				this.setErrorMessage("Negative values are not accepted!");
				throw new IllegalArgumentException("Negative values are not accepted!");
			}
		}
		return num;
	}

	
	/**
	 * Gets the text field content.
	 *
	 * @param textField the text field
	 * @return the text field content
	 */
	private String getTextFieldContent(JTextField textField) {
		String content = null;
		if (textField.getText()!=null && textField.getText().equals("")==false) {
			content = textField.getText().trim();
		}
		return content;
	}

	/**
	 * Gets the static data model.
	 * @return the static data model
	 */
	public type_hereStaticDataModel getStaticDataModel() {
		if (staticModel.getStaticDataModel() == null){
			staticDataModel = new type_hereStaticDataModel();
			staticModel.setStaticDataModel(staticDataModel);
		}
		return staticDataModel;
	}
	
	/**
	 * Sets the static data model.
	 *
	 * @param staticDataModel the type_hereDataModel to set
	 */
	public void setStaticDataModel(type_hereStaticDataModel staticDataModel) {
		this.staticDataModel = staticDataModel;
	}
	
	/**
	 * Gets the j label max power.
	 *
	 * @return the j label max power
	 */
	private JLabel getJLabelMotorPower() {
		if (jLabelMotorPower == null) {
			jLabelMotorPower = new JLabel("Motor power [kW]");
			jLabelMotorPower.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelMotorPower;
	}
	
	/**
	 * Gets the j text field max power.
	 *
	 * @return the j text field max power
	 */
	private JTextField getJTextFieldMotorPower() {
		if (jTextFieldMotorPower == null) {
			jTextFieldMotorPower = new JTextField();
			jTextFieldMotorPower.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldMotorPower.setColumns(10);
			jTextFieldMotorPower.addKeyListener(new KeyAdapter4Numbers(true));
		}
		return jTextFieldMotorPower;
	}
	
	
	/**
	 * Gets the panel.
	 *
	 * @return the panel
	 */
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_btnOk = new GridBagConstraints();
			gbc_btnOk.anchor = GridBagConstraints.EAST;
			gbc_btnOk.insets = new Insets(0, 0, 0, 60);
			gbc_btnOk.gridx = 0;
			gbc_btnOk.gridy = 0;
			panel.add(getBtnOk(), gbc_btnOk);
			GridBagConstraints gbc_btnCancel = new GridBagConstraints();
			gbc_btnCancel.anchor = GridBagConstraints.WEST;
			gbc_btnCancel.gridx = 1;
			gbc_btnCancel.gridy = 0;
			panel.add(getBtnCancel(), gbc_btnCancel);
		}
		return panel;
	}
	
	/**
	 * Gets the btn ok.
	 *
	 * @return the btn ok
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.setPreferredSize(new Dimension(85, 26));
			btnOk.setForeground(new Color(0, 153, 0));
			btnOk.setFont(new Font("Dialog", Font.BOLD, 12));
			btnOk.addActionListener(this);
		}
		return btnOk;
	}
	
	/**
	 * Gets the btn cancel.
	 *
	 * @return the btn cancel
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setPreferredSize(new Dimension(85, 26));
			btnCancel.setForeground(new Color(153, 0, 0));
			btnCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	
	/**
	 * Load the data model to the dialog.
	 */
	private void loadDataModelToDialog(){
	}
	
		private void setErrorMessage(String message) {
			type_hereStaticModelDialog.this.getJLabelErrorMessage().setText(message);
		}
	
	private JLabel getJLabelErrorMessage() {
		if (jLabelErrorMessage == null) {
			jLabelErrorMessage = new JLabel("");
			jLabelErrorMessage.setForeground(new Color(128, 0, 0));
			jLabelErrorMessage.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelErrorMessage;
	}
}
