package type_here.staticModel;

import java.awt.Window;
import java.io.Serializable;

import energy.OptionModelController;
import energy.optionModel.gui.sysVariables.AbstractStaticModel;
import energy.optionModel.gui.sysVariables.AbstractStaticModelDialog;

public class [technical system name]StaticModel extends AbstractStaticModel {
	
	private [technical system name]StaticDataModel staticModel;

	public [technical system name]StaticModel(OptionModelController optionModelController) {
		super(optionModelController);
	}


	@Override
	public Serializable getStaticDataModel() {
		return this.staticModel;
	}

	@Override
	public void setStaticDataModel(Serializable staticModel) {
		this.staticModel = ([technical system name]StaticDataModel) staticModel;

	}

	@Override
	public AbstractStaticModelDialog getNewModelDialog(Window owner) {
		return new [technical system name]StaticModelDialog(owner, this);
	}
}
