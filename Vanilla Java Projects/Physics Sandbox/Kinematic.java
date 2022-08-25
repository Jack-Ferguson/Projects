/*
 * Author: Jack Ferguson
 * CS 225 Spring 2021
 * 
 * Particle.java
 * 
 * The Kinematic class handles all physics in the project
 */
import java.util.ArrayList;

public class Kinematic {
	private double mass;
	private Point position;
	private Vector velocity;
	private Vector acceleration;
	private Vector netForce;
	private ArrayList<Vector> currentForces;
	private double forceStrength;
	private String name;
	private double radius;
	private int circleIndex;
	
	public Kinematic(double mass, Point position, int circleIndex) {
		this.mass = mass;
		this.position = position;
		this.velocity = new Vector(random(-100, 100), random(-100, 100));
		this.acceleration = new Vector(0, 0);
		this.netForce = new Vector(0, 0);
		this.currentForces = new ArrayList<Vector>();
		this.circleIndex = circleIndex;
	}
	
	public void updatePosition(double timeStep) {
		position.applyVector(velocity, timeStep);
	}
	
	public void updateVelocity(double timeStep) {
		velocity.add(acceleration, timeStep);
		velocity = Vector.multiply(velocity, .999);
	}
	
	public void updateAcceleration() {
		acceleration = Vector.add(Vector.multiply(Simulation.gravity, mass/Math.abs(mass)), Vector.multiply(netForce, 1/mass));
	}
	
	public void updateNetForce() {
		Vector sum = new Vector(0, 0);
		
		for (Vector force : currentForces) {
			sum.add(force);
		}
		
		netForce = sum;
	}
	
	public void updateAll(double timeStep) {
		updateNetForce();
		updateAcceleration();
		updateVelocity(timeStep);
		updatePosition(timeStep);
		currentForces.clear();
	}
	
	public void collideWith(Kinematic b) {
		Kinematic a = this;
		
		Vector tangentVector = new Vector(b.getPosition().getY()-a.getPosition().getY(),-(b.getPosition().getX()-a.getPosition().getX()));
		tangentVector = tangentVector.normalized();
		Vector relativeVelocity = new Vector(a.getVelocity().getX()-b.getVelocity().getX(), a.getVelocity().getY()-b.getVelocity().getY());
		double length = Vector.dot(relativeVelocity, tangentVector);
		Vector velocityComponentOnTangent = Vector.multiply(tangentVector, length);
		Vector velocityComponentPerpendicularToTangent = Vector.subtract(relativeVelocity, velocityComponentOnTangent);
		
		a.setVelocity(Vector.multiply(Vector.subtract(a.getVelocity(), velocityComponentPerpendicularToTangent), .99));
		b.setVelocity(Vector.multiply(Vector.add(b.getVelocity(), velocityComponentPerpendicularToTangent), .99));
	}
	
	public void applyForce(Vector force) {
		currentForces.add(force);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public double getMass() {
		return mass;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public Vector getAcceleration() {
		return acceleration;
	}
	
	public double getForceStrength() {
		return forceStrength;
	}
	
	public int getCircleIndex() {
		return circleIndex;
	}
	
	public String getName() {
		return name;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public void setCircleIndex(int circleIndex) {
		this.circleIndex = circleIndex;
	}
	
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}
	
	public void setForceStrength(double forceStrength) {
		this.forceStrength = forceStrength;
	}
	
	public void bounceVertical() {
		velocity = Vector.multiply(new Vector(velocity.getX(), -velocity.getY()), .99);
	}
	
	public void bounceHorizontal() {
		velocity = Vector.multiply(new Vector(-velocity.getX(), velocity.getY()), .99);
	}
	
	private double random(double min, double max) {
		return (max-min)*Math.random()+min;
	}
}
