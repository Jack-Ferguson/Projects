public class Flyer {
  
  PVector position;
  PVector velocity;
  PVector acceleration;
  float maxForce = .2;
  int maxSpeed = 3;
  
  float alignmentWeight = 2;
  float cohesionWeight = 1;
  float separationWeight = 3;
  
  int sightRange = 100;
  
  public Flyer(PVector position) {
    this.position = position;
    this.velocity = PVector.random2D().mult(maxSpeed);
    this.acceleration = new PVector();
  }
  
  public void show() {
    fill(255);
    //ellipse(position.x, position.y, 20, 20);
    
    pushMatrix();
    translate(position.x, position.y);
    rotate(velocity.heading());
    triangle(15, 0, -10, -10, -10, 10);
    popMatrix();
  }
  
  public void update() {
    
    velocity.add(acceleration);
    velocity.setMag(maxSpeed);
    position.add(velocity);
    wrap();
  
  }
  
  public void boid(ArrayList<Flyer> flyers) {
    
    acceleration = applyRules(flyers);
    update();
    
  }
  
  public void wrap() {
    if (position.x > width)
      position.x = 0;
    else if (position.x < 0)
      position.x = width;
    if (position.y > height)
      position.y = 0;
    else if (position.y < 0)
      position.y = height;
  }
  
  public PVector applyRules(ArrayList<Flyer> flyers) {
    
    PVector alignmentAcc = new PVector();
    PVector separationAcc = new PVector();
    PVector cohesionAcc = new PVector();
    int total = 0;
    
    for (Flyer f : flyers) {
      
      float d = position.dist(f.position);
      
      if (d <= sightRange) {
        
        if (f != this) {
          alignmentAcc.add(f.velocity);
          separationAcc.add(PVector.sub(position, f.position).div(pow(min(d, abs(d-20)), 2)));
          cohesionAcc.add(f.position);
          total++;
        }
        
      }
      
    } 
    
    if (total != 0) {
      alignmentAcc.div(total);
      alignmentAcc.sub(velocity);
      alignmentAcc.mult(alignmentWeight);
      separationAcc.div(total);
      separationAcc.sub(velocity);
      separationAcc.mult(separationWeight);
      cohesionAcc.div(total);
      cohesionAcc.sub(position);
      cohesionAcc.mult(cohesionWeight);
    }
    
    PVector finalAcc = new PVector();
    
    finalAcc.add(alignmentAcc);
    finalAcc.add(separationAcc);
    finalAcc.add(cohesionAcc);
   
    
    finalAcc.limit(maxForce);
    return finalAcc;
  }
}
