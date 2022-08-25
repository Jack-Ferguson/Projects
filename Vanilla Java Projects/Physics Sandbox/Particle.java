/*
 * Author: Jack Ferguson
 * CS 225 Spring 2021
 * 
 * Particle.java
 * 
 * The Particle class adds project-specific fields to the Kinematic class
 */
import javafx.scene.paint.Color;

public class Particle extends Kinematic{
	
	private Color color;

	public Particle(double mass, Point position, int circleIndex) {
		super(mass, position, circleIndex);
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getName() {
		return "P "+super.getName();
	}
	
	public void setColor(double r, double g, double b) {
		this.color = new Color(r, g, b, 1);
	}
}
