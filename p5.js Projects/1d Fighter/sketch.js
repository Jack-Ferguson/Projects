var canvasWidth, canvasHeight;

function setup() {
  canvasWidth = 1536;
  canvasHeight = 753;
  canvas = createCanvas(canvasWidth, canvasHeight);
  canvas.position(0, 0);
  background(50, 50, 50);
  player1 = new Player(300, 1);
  player2 = new Player(1236, 2);
  noStroke();
  textAlign(CENTER);
}

function draw() {
  background(50, 50, 50);
  fill(255);
  rect(0 , canvasHeight/2 - 5, 1536, 10);
  if (player1.hp > 0 && player2.hp <= 0) {
    fill(255, 0, 144);
    textSize(16);
    text(".--. .-.. .- -.-- . .-. / .---- / .-- .. -. ...", canvasWidth/2, canvasHeight/2 + 5);
    if (keyIsDown(32)) {
      player1.hp = 50;
      player2.hp = 50;
      player1.x = 300;
      player2.x = 1236;
      player1.lastShot = -Infinity;
      player2.lastShot = -Infinity;
      player1.lastPhased = -Infinity;
      player2.lastPhased = -Infinity;
      player1.bullet.x = -1000;
      player2.bullet.x = -1000;
      player1.bullet.dir = -1;
      player2.bullet.dir = -1;
      player1.phased = false;
      player2.phased = false;
    }
  } else if (player1.hp <= 0 && player2.hp > 0) {
    fill(255, 0, 144);
    textSize(14);
    text(".--. .-.. .- -.-- . .-. / ..--- / .-- .. -. ...", canvasWidth/2, canvasHeight/2 + 5);
    if (keyIsDown(32)) {
      player1.hp = 50;
      player2.hp = 50;
      player1.x = 300;
      player2.x = 1236;
      player1.lastShot = -Infinity;
      player2.lastShot = -Infinity;
      player1.lastPhased = -Infinity;
      player2.lastPhased = -Infinity;
      player1.bullet.x = -1000;
      player2.bullet.x = -1000;
      player1.bullet.dir = -1;
      player2.bullet.dir = -1;
      player1.phased = false;
      player2.phased = false;
    }
  } else {
    player1.show();
    player2.show();
    player1.move();
    player2.move();
  }
}

function Player(x, id) {
  this.x = x;
  this.hp = 50;
  this.bullet = new Bullet(-1000, -1, id);
  this.lastShot = -Infinity;
  this.lastPhased = -Infinity;
  this.phased = false;

  this.show = function() {
    if (!this.phased) {
      fill(0);
    } else {
      fill(0, 191, 255);
    }
    rect(this.x, canvasHeight/2 - 5, 10, 10);
    fill(255, 0, 0);
    if (id == 2) {
      rect(0, canvasHeight/2 - 5, this.hp*4, 10);
    } else {
      rect(1536-(this.hp*4), canvasHeight/2 - 5, this.hp*4, 10);
    }
    this.bullet.show();
  }

  this.move = function() {
    if (id == 2) {
      if (keyIsDown(RIGHT_ARROW)) {
        if (this.x <= 1323) {
          this.x+=3;
        }
      }
      if (keyIsDown(LEFT_ARROW)) {
        if (this.x >= 200) {
          this.x-=3;
        }
      }
      if (keyIsDown(UP_ARROW)) {
        if (millis() - this.lastShot >= 3000) {
          this.shoot((player1.x-this.x)/Math.abs(player1.x-this.x));
          this.lastShot = millis();
        }
      }
      if (keyIsDown(DOWN_ARROW)) {
        if (millis() - this.lastPhased >= 5000) {
          this.phased = true;
          this.lastPhased = millis();
        }
      }
      if (millis() - this.lastPhased >= 750) {
        this.phased = false;
      }
    } else {
      if (keyIsDown(68)) {
        if (this.x <= 1323) {
          this.x+=3;
        }
      }
      if (keyIsDown(65)) {
        if (this.x >= 200) {
          this.x-=3;
        }
      }
      if (keyIsDown(87)) {
        if (millis() - this.lastShot >= 3000) {
          this.shoot((player2.x-this.x)/Math.abs(player2.x-this.x));
          this.lastShot = millis();
        }
      }
      if (keyIsDown(83)) {
        if (millis() - this.lastPhased >= 5000) {
          this.phased = true;
          this.lastPhased = millis();
        }
      }
      if (millis() - this.lastPhased >= 750) {
        this.phased = false;
      }
    }
  }

  this.shoot = function(dir) {
    this.bullet.x = this.x;
    this.bullet.dir = dir;
  }
}

function Bullet(x, dir, id) {
  this.x = x;
  this.dir = dir;

  this.show = function() {
    fill(255, 0, 144);
    rect(this.x, canvasHeight/2 - 5, 5, 10);
    this.cancel();
    this.move();
    this.hit();
  }

  this.cancel = function() {
    if (id==1) {
      if (this.x + 5 >= player2.bullet.x && this.x <= player2.bullet.x + 5) {
        this.x = -1000;
        this.dir = -1;
        player2.bullet.x = -1000;
        player2.bullet.dir = -1;
      }
    } else {
      if (this.x + 5 >= player1.bullet.x && this.x <= player1.bullet.x + 5) {
        this.x = -1000;
        this.dir = -1;
        player1.bullet.x = -1000;
        player1.bullet.dir = -1;
      }
    }
  }

  this.hit = function() {
    if (id == 1) {
      if (this.x + 5 >= player2.x && this.x <= player2.x + 10 && !player2.phased) {
        this.x = -1000;
        this.dir = -1000;
        player1.hp-= 5;
      }
    } else {
      if (this.x + 5 >= player1.x && this.x <= player1.x + 10 && !player1.phased) {
        this.x = -1000;
        this.dir = -1000;
        player2.hp-= 5;
      }
    }
  }

  this.move = function() {
    this.x += 5*this.dir;
  }
}
