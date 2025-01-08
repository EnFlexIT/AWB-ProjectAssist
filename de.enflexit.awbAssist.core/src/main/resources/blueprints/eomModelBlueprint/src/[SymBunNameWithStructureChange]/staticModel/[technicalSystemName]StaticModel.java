package [SymBundleName].staticModel;

import java.awt.Window;
import java.io.Serializable;

import energy.OptionModelController;
import energy.optionModel.gui.sysVariables.AbstractStaticModel;
import energy.optionModel.gui.sysVariables.AbstractStaticModelDialog;

public class [technicalSystemName]StaticModel extends AbstractStaticModel {
	
	private [technicalSystemName]StaticDataModel staticModel;

	public [technicalSystemName]StaticModel(OptionModelController optionModelController) {
		super(optionModelController);
	}


	@Override
	public Serializable getStaticDataModel() {
		return this.staticModel;
	}

	@Override
	public void setStaticDataModel(Serializable staticModel) {
		this.staticModel = ([technicalSystemName]StaticDataModel) staticModel;

	}

	@Override
	public AbstractStaticModelDialog getNewModelDialog(Window owner) {
		return new [technicalSystemName]StaticModelDialog(owner, this);
	}
}
