package type_here.strategies;

import java.util.Vector;

import javax.swing.JComponent;

import energy.OptionModelController;
import energy.evaluation.AbstractEvaluationStrategy;
import energy.evaluation.TechnicalSystemStateDeltaEvaluation;
import energy.helper.NumberHelper;
import energy.optionModel.TechnicalSystemStateEvaluation;

/**
 * The RandomDecisionStrategy will decide randomly for subsequent system states.
 * 
 * @author Christian Derksen - DAWIS - University Duisburg-Essen
 */
public class RandomDecisionStrategy extends AbstractEvaluationStrategy {

	/**
	 * Instantiates a new full evaluation strategy.
	 * @param optionModelController the option model controller
	 */
	public RandomDecisionStrategy(OptionModelController optionModelController) {
		super(optionModelController);
	}
	
	/* (non-Javadoc)
	 * @see energy.evaluation.AbstractEvaluationStrategy#getCustomToolBarElements()
	 */
	@Override
	public Vector<JComponent> getCustomToolBarElements() {
		return null;
	}

	/* (non-Javadoc)
	 * @see energy.evaluation.AbstractEvaluationStrategy#runEvaluation()
	 */
	@Override
	
	public void runEvaluation() {
		
		// --- Initialize search --------------------------------------------------------
		TechnicalSystemStateEvaluation tsse = this.getInitialTechnicalSystemStateEvaluation();
		
		// --- Search by walking through time -------------------------------------------
		while (tsse.getGlobalTime() < this.getEndTime() ) {
			
			// --- Get the possible subsequent steps and states -------------------------
			Vector<TechnicalSystemStateDeltaEvaluation> deltaSteps = this.getAllDeltaEvaluationsStartingFromTechnicalSystemState(tsse);
			if (deltaSteps.size()==0) {
				this.print(this.getEvaluationThread().getName() + ": => No further delta steps possible => interrupt search!", true);
				break;
			}
			
			// --- Make a random decision for the next system state here ----------------
			int decisionIndex = NumberHelper.getRandomInteger(0, deltaSteps.size()-1);
			TechnicalSystemStateDeltaEvaluation tssDeltaDecision = deltaSteps.get(decisionIndex);
			
			// --- Set new current TechnicalSystemStateEvaluation -----------------------
			TechnicalSystemStateEvaluation tsseNext = this.getNextTechnicalSystemStateEvaluation(tsse, tssDeltaDecision);
			if (tsseNext==null) {
				this.print(this.getEvaluationThread().getName() + ": => Error while using selected delta => interrupt search!", true);
				break;
			} else {
				// --- Set next state as new current state ------------------------------
				tsse = tsseNext;
			}
			// --- Stop evaluation ? ----------------------------------------------------
			if (isStopEvaluation()==true) break;
		} // end while
		
		// --- Add the schedule found to the list of results ----------------------------
		this.addStateToResults(tsse);
		// --- Done ! -------------------------------------------------------------------
		
//		// ------------------------------------------------------------------------------
//		// --- Test area for the conversion of Schedules --------------------------------
//		// ------------------------------------------------------------------------------
//		Schedule schedule = new Schedule();
//		schedule.setPriority(1);
//		schedule.setSourceThread(Thread.currentThread().getName());
//		schedule.setCalculationTime(0);
//		schedule.setTechnicalSystemStateEvaluation(tsse);

//		// --- ScheduleTransformerDiscreteTime ------------------------------------------
//		long startTime = DateTimeHelper.getDateForMidnight(this.getStartTime()).getTime();
//		ScheduleTransformer sTransformer = new ScheduleTransformerDiscreteTime(schedule, startTime, 1000 * 60);
//		this.addStateToResults(sTransformer.getTransformedSchedule().getTechnicalSystemStateEvaluation());
		
//		// --- ScheduleTransformerKeyValue ----------------------------------------------	
//		float deltaPercentage = 10;
//		int deltaWattAbsolute = 100;
//		long timePeriodForLiveBit = (1000*60) * 10; // minutes 
//		ScheduleTransformerKeyValueConfiguration stkvConfig = new ScheduleTransformerKeyValueConfiguration(deltaPercentage, timePeriodForLiveBit);
//		ScheduleTransformer sTransformer = new ScheduleTransformerKeyValue(schedule, stkvConfig);
//		this.addStateToResults(sTransformer.getTransformedSchedule().getTechnicalSystemStateEvaluation());
//		// ------------------------------------------------------------------------------
//		// ------------------------------------------------------------------------------

	}

	
}
