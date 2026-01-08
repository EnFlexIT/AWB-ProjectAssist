package [SymBundleName].staticModel;

import java.io.Serializable;

public class [technicalSystemNameCC]StaticDataModel implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private double motorPower;

	public double getMotorPower() {
		return motorPower;
	}
	public void setMotorPower(double maxPower) {
		this.motorPower = maxPower;
	}
}
