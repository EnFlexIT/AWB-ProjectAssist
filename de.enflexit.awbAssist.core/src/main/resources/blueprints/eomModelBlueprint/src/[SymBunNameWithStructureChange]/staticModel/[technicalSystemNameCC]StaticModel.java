package [SymBundleName].staticModel;

import java.awt.Window;
import java.io.Serializable;

import energy.OptionModelController;
import energy.optionModel.gui.sysVariables.AbstractStaticModel;
import energy.optionModel.gui.sysVariables.AbstractStaticModelDialog;

public class [technicalSystemNameCC]StaticModel extends AbstractStaticModel {
	
	private [technicalSystemNameCC]StaticDataModel staticModel;

	public [technicalSystemNameCC]StaticModel(OptionModelController optionModelController) {
		super(optionModelController);
	}


	@Override
	public Serializable getStaticDataModel() {
		return this.staticModel;
	}

	@Override
	public void setStaticDataModel(Serializable staticModel) {
		this.staticModel = ([technicalSystemNameCC]StaticDataModel) staticModel;

	}

	@Override
	public AbstractStaticModelDialog getNewModelDialog(Window owner) {
		return new [technicalSystemNameCC]StaticModelDialog(owner, this);
	}
}
