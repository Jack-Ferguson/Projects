ArrayList<Flyer> flyers = new ArrayList<Flyer>();

void setup() {
  size(1920, 1080);
  
  int numFlyers = 50;
  
  for (int i = 0; i < numFlyers; i++) {
    flyers.add(new Flyer(new PVector(width/2, height/2).add(PVector.random2D().mult(random(0, 500)))));
  }
}



void draw() {
  background(50);
  
  for (Flyer f : flyers) {
    f.boid(flyers);
    f.show();
  }
  
}
