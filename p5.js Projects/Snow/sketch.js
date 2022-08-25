var canvasWidth, canvasHeight;
var particle = [];

function setup() {
  canvasWidth = displayWidth/2;
  canvasHeight = displayWidth/2;
  canvas = createCanvas(canvasWidth, canvasHeight);
  canvas.position(displayWidth/2 - canvasWidth/2, displayHeight/2-canvasHeight/2);

  for (var i = 0; i < 200; i++) {
    particle[i] = new Particle();
  }
}

function draw() {
  background(230, 230, 230);
  for (var i = 0; i < particle.length; i++) {
    particle[i].update();
    particle[i].show();
  }
}

function Particle() {
  this.z = random(0, 10);
  this.xPos = random(canvasWidth);
  this.yPos = random(-canvasHeight);
  this.yVel = map(this.z, 0, 10, 0, 1);
  this.yAcc = map(this.z, 0, 10, 0, .007);

  this.update = function() {
    this.yPos = this.yPos+this.yVel;
    this.yVel = this.yVel + this.yAcc;
    if (this.yPos>canvasHeight) {
      this.reset();
    }
  }

  this.reset = function() {
      this.yPos = random(-canvasHeight);
      this.xPos = random(canvasWidth);
      this.yVel = map(this.z, 0, 10, .01, 1);
  }

  this.show = function() {
    SnowFlake(this.xPos, this.yPos, this.z);
  }

}

function SnowFlake(x, y, size) {

  this.xPos = x;
  this.yPos = y
  this.size = size;
  this.mod = .7

  line(this.xPos, this.yPos, this.xPos+size, this.yPos);
  line(this.xPos, this.yPos, this.xPos-size, this.yPos);
  line(this.xPos, this.yPos, this.xPos, this.yPos+size);
  line(this.xPos, this.yPos, this.xPos, this.yPos-size);
  line(this.xPos, this.yPos, this.xPos+(size*this.mod), this.yPos+(size*this.mod));
  line(this.xPos, this.yPos, this.xPos+(size*this.mod), this.yPos-(size*this.mod));
  line(this.xPos, this.yPos, this.xPos-(size*this.mod), this.yPos+(size*this.mod));
  line(this.xPos, this.yPos, this.xPos-(size*this.mod), this.yPos-(size*this.mod));

}
