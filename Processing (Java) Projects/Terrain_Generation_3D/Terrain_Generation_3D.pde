int scale = 15;

float flying = 0;

float smoothness = 100;

float[][] terrainHeight;

void setup() {
  
  size(1600, 800, P3D);
  
  terrainHeight = new float[2*width/scale+1][2*height/scale+1];
}


void draw() {
  
  flying -= .25/smoothness;
  float yOff = flying;
  for (int y = 0; y < terrainHeight[0].length; y++) {
    float xOff = 0;
    for (int x = 0; x < terrainHeight.length; x++) {
      terrainHeight[x][y] = map(noise(xOff, yOff), 0, 1, -300, 300);
      xOff += 1/max(smoothness, 10);
    } 
    yOff += 1/max(smoothness, 10);
  }
  
  translate(width/2, height/2);
  rotateX(PI/3);
  translate(-terrainHeight.length*scale/2, -terrainHeight[0].length*scale/2);
  
  background(0);
  
  generateMesh();
  
}

void generateMesh() {
  
  for (int y = 0; y < terrainHeight[0].length-1; y++) {
    beginShape(TRIANGLE_STRIP);
    for (int x = 0; x < terrainHeight.length; x++) {
  
      stroke(255);
      fill(0, map(terrainHeight[x][y], -300, 300, 0, 255), map(terrainHeight[x][y], -300, 300, 100, 0));
      
      vertex(x*scale, y*scale, terrainHeight[x][y]);
      vertex(x*scale, (y+1)*scale, terrainHeight[x][y+1]);
      
    } 
    endShape();
  }
  
}
