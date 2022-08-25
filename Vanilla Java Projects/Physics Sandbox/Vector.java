
public class Vector {
	private double x, y;
	
	public Vector(double x, double y) {	
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void add(Vector other) {
		this.x += other.getX();
		this.y += other.getY();
	}
	
	public Vector subtract(Vector other) {
		return new Vector(this.x-other.getX(), this.y-other.getY());
	}
	
	public void add(Vector other, double multiplier) {
		this.x += other.getX()*multiplier;
		this.y += other.getY()*multiplier;
	}
	
	public Vector add2(double angle, double magnitude) {
		return new Vector(this.x+magnitude*Math.cos(angle), this.y+magnitude*Math.sin(angle));
	}
	
	public double getMagnitude() {
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public double getAngle() {
		return Math.atan2(y, x);
	}
	
	public static Vector multiply(Vector a, double factor) {
		return new Vector(a.x*factor, a.y*factor);
	}
	
	public static Vector add(Vector a, Vector b) {
		return new Vector(a.x+b.x, a.y+b.y);
	}
	
	public static Vector subtract(Vector a, Vector b) {
		return new Vector(a.x-b.x, a.y-b.y);
	}
	
	public Vector normalized() {
		return new Vector(x/getMagnitude(), y/getMagnitude());
	}
	
	public static double dot(Vector a, Vector b) {
		return a.getMagnitude()*b.getMagnitude()*Math.cos((a.getAngle()>b.getAngle() ? a.getAngle()-b.getAngle() : b.getAngle()-a.getAngle()));
	}
}
