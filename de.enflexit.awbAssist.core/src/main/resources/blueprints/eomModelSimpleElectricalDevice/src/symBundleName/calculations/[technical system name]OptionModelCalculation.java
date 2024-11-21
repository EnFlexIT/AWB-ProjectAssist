package [SymBundleName].calculations;

import energy.OptionModelController;
import energy.calculations.AbstractOptionModelCalculation;
import energy.optionModel.AbstractInterfaceFlow;
import energy.optionModel.Duration;
import energy.optionModel.EnergyFlowInWatt;
import energy.optionModel.TechnicalInterface;
import energy.optionModel.TechnicalSystemStateEvaluation;

public class [technical system name]OptionModelCalculation extends AbstractOptionModelCalculation {

	public [technical system name]OptionModelCalculation(OptionModelController optionModelController) {
		super(optionModelController);
	}

	@Override
	public Duration getDuration(DurationType arg0, TechnicalSystemStateEvaluation arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnergyFlowInWatt getEnergyFlowForLosses(TechnicalSystemStateEvaluation arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractInterfaceFlow getEnergyOrGoodFlow(TechnicalSystemStateEvaluation arg0, TechnicalInterface arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
