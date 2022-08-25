int mapScale = 4;
float scale = 20;
float time = 0;
float density = .5;
float xTrans, yTrans, zTrans;

float radius = 50;

float[][] points;

void setup() {
  size(1600, 800, P3D);
  
  xTrans = -(mapScale*width)/4;
  yTrans = -(mapScale*height)/4;
  zTrans = 0;
  points = generateNoise();
}

void draw() {
  
  translate(xTrans, yTrans, zTrans);
  
  background(5, 161, 252);
  //background(0);
  
  //drawPoints();
  
  march(true);
}

void mouseWheel(MouseEvent e) {
  zTrans -= 10*e.getCount();
  if (zTrans >= 620)
    zTrans = 620;
  else if (zTrans <= -620)
    zTrans = -620;
}

void mouseDragged(MouseEvent e) {
  xTrans += (mouseX-pmouseX)*map(-zTrans, -620, 620, .1, 1.75);
  yTrans += (mouseY-pmouseY)*map(-zTrans, -620, 620, .1, 1.75);
}

float[][] generateNoise() {
  
  float[][] points = new float[mapScale*(int)(width/scale)][mapScale*(int)(height/scale)];
  
  float yOff = 0;
  for (int i = 1; i < points.length-1; i++) {
    float xOff = 0;
    for (int j = 1; j < points[0].length-1; j++) {
      
      float noiseValue = noise(xOff, yOff);
      
      points[i][j] = noiseValue;

      xOff += .1;
    }
    yOff += .1;
  }
  return points;
}

void drawPoints() {
  
  for (int i = 0; i < points.length; i++) {
    for (int j = 0; j < points[0].length; j++) {
  
      stroke(points[i][j]*255);
      strokeWeight(5);
      
      if (points[i][j] >= 1-density) /*(dist(i, j, points.length/2, points[0].length/2) <= radius)*/
        point(i*scale, j*scale);
    }
  } 
}

void march(boolean withFill) {
  ellipseMode(CENTER);
  for (int i = 0; i < points.length-1; i++) {
    for (int j = 0; j < points[0].length-1; j++) {
      
      if (withFill && points[i][j] >= 1-density) {
        fill(0, points[i][j]*255, 0);
        noStroke();
        
        if (points[i][j] - (1 - density) <= .05) {
          colorMode(HSB, 360, 100, 100);
          fill(38, points[i][j]*100, 99);
          colorMode(RGB, 255, 255, 255);
        }
        
        ellipse(i*scale, j*scale, sqrt(2)*scale, sqrt(2)*scale);
      }
        
      
      int index = 0;
      
      if (points[i][j] >= 1-density)
        index += 8;
      if (points[i+1][j] >= 1-density)
        index += 4;
      if (points[i+1][j+1] >= 1-density)
        index += 2;
      if (points[i][j+1] >= 1-density)
        index += 1;
        
      drawSquare(i, j, index);
    }
  }
}

void drawSquare(int i, int j, int index) {
  
  stroke(255);
  strokeWeight(3);
  
  for (int k = 0; k <= cellTable[index].length-4; k+= 4) {

    line(scale*(i+cellTable[index][k]), scale*(j+cellTable[index][k+1]), scale*(i+cellTable[index][k+2]), scale*(j+cellTable[index][k+3]));
    
  }
  
}
