package [SymBundleName].calculations;

import energy.OptionModelController;
import energy.calculations.AbstractOptionModelCalculation;
import energy.optionModel.AbstractInterfaceFlow;
import energy.optionModel.Duration;
import energy.optionModel.EnergyFlowInWatt;
import energy.optionModel.TechnicalInterface;
import energy.optionModel.TechnicalSystemStateEvaluation;

public class [technicalSystemNameCC]OptionModelCalculation extends AbstractOptionModelCalculation {

	public [technicalSystemNameCC]OptionModelCalculation(OptionModelController optionModelController) {
		super(optionModelController);
	}

	@Override
	public Duration getDuration(DurationType dt, TechnicalSystemStateEvaluation tsse) {
		return null;
	}

	@Override
	public EnergyFlowInWatt getEnergyFlowForLosses(TechnicalSystemStateEvaluation tsse) {
		return null;
	}

	@Override
	public AbstractInterfaceFlow getEnergyOrGoodFlow(TechnicalSystemStateEvaluation tsse, TechnicalInterface ti,
			boolean isManualConfiguration) {
		return null;
	}

}
