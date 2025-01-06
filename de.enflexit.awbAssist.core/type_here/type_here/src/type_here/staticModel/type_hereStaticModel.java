package type_here.staticModel;

import java.awt.Window;
import java.io.Serializable;

import energy.OptionModelController;
import energy.optionModel.gui.sysVariables.AbstractStaticModel;
import energy.optionModel.gui.sysVariables.AbstractStaticModelDialog;

public class type_hereStaticModel extends AbstractStaticModel {
	
	private type_hereStaticDataModel staticModel;

	public type_hereStaticModel(OptionModelController optionModelController) {
		super(optionModelController);
	}


	@Override
	public Serializable getStaticDataModel() {
		return this.staticModel;
	}

	@Override
	public void setStaticDataModel(Serializable staticModel) {
		this.staticModel = (type_hereStaticDataModel) staticModel;

	}

	@Override
	public AbstractStaticModelDialog getNewModelDialog(Window owner) {
		return new type_hereStaticModelDialog(owner, this);
	}
}
