import javafx.scene.paint.Color;

public class SpecialParticle extends Kinematic{

	private Color color;
	private Color currentColor;
	
	public SpecialParticle(double mass, Point position, int circleIndex) {
		super(mass, position, circleIndex);
	}

	@Override
	public void updateVelocity(double timeStep) {
		getVelocity().add(getAcceleration(), timeStep);
		setVelocity(Vector.multiply(getVelocity(), .999));
		currentColor = new Color(map(getVelocity().getMagnitude(), 0, 100, 0, color.getRed()), map(getVelocity().getMagnitude(), 0, 100, 0, color.getGreen()), map(getVelocity().getMagnitude(), 0, 100, 0, color.getBlue()), 1);
	}
	
	public Color getColor() {
		return currentColor;
	}
	
	public void setColor(double r, double g, double b) {
		this.color = new Color(r, g, b, 1);
		this.currentColor = this.color;
	}
	
	public String getName() {
		return "SP "+super.getName();
	}
	
	//my mapping function
	private double map(double num, double min, double max, double newMin, double newMax) {
		if (num > max)
			num = max;
		else if (num < min)
			num = min;
		return ((num-min)/(max-min))*(newMax-newMin)+newMin;
	}
}
