package type_here.staticModel;

import java.io.Serializable;

public class type_hereStaticDataModel implements Serializable {

	private static final long serialVersionUID = 4944933489538553875L;
	
	private double motorPower;

	public double getMotorPower() {
		return motorPower;
	}
	public void setMotorPower(double maxPower) {
		this.motorPower = maxPower;
	}
}
