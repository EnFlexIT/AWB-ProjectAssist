package type_here.calculations;

import energy.calculations.AbstractEvaluationCalculation;
import energy.optionModel.Connectivity;
import energy.optionModel.CostFunctionDataSeries;
import energy.optionModel.FixedVariable;
import energy.optionModel.InputMeasurement;
import energy.optionModel.SystemVariableDefinition;
import energy.optionModel.TechnicalInterface;
import energy.optionModel.TechnicalSystemState;
import energy.optionModel.TechnicalSystemStateEvaluation;

public class [technical system name]EvaluationCalculation extends AbstractEvaluationCalculation {

	@Override
	public double getAmountCosts(TechnicalInterface arg0, Connectivity arg1, long arg2, long arg3, double arg4,	double arg5) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CostFunctionDataSeries getCostFunctionDataSeries(String arg0, Connectivity arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputMeasurement getInputMeasurement(SystemVariableDefinition arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getStateCosts(TechnicalSystemStateEvaluation arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateInputMeasurement(TechnicalSystemState arg0, FixedVariable arg1) {
		// TODO Auto-generated method stub

	}

}
