/*
 * Author: Jack Ferguson
 * CS 225 Spring 2021
 * 
 * Point.java
 * 
 * Functionality class for easy distance and angle calculations, as well as the easy application of vectors
 */
public class Point {
	private double x, y;
	
	public Point(double x, double y) {
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
	
	public void applyVector(Vector vector) {
		this.x += vector.getX();
		this.y += vector.getY();
	}
	
	public void applyVector(Vector vector, double multiplier) {
		this.x += vector.getX() * multiplier;
		this.y += vector.getY() * multiplier;
	}
	
	public double calculateAngleToPoint(Point other) {
		return Math.atan2(other.y-this.y, other.x-this.x);
	}
	
	public double calculateDistanceToPoint(Point other) {
		return Math.sqrt(Math.pow(other.x-this.x, 2)+Math.pow(other.y-this.y, 2));
	}
}
