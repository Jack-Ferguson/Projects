let canvasWidth, canvasHeight;
let bullets = [];
let bulletSpeed = 2;
let bulletSize = 50
let waveFrequency = 20;
let waveSize = 5;
let score = 0;
let highScore = 0;
let prevScore = 0;
let shipRight, shipLeft, shipUp, shipDown, shipNeutral;
let highScores, highScoresAvgTxt;

let screen = "menu";

const minBulletSize = 15;

let usernameInput;

function preload() {

  //load all images
  shipRight = loadImage('assets/spaceship_right.png');
  shipLeft = loadImage('assets/spaceship_left.png');
  shipUp = loadImage('assets/spaceship_up.png');
  shipDown = loadImage('assets/spaceship_down.png');
  shipNeutral = loadImage('assets/spaceship_neutral.png');
}

function setup() {

  //create canvas and initialize everything
  canvasWidth =1375;
  canvasHeight = 675;
  canvas = createCanvas(canvasWidth, canvasHeight);
  canvas.position(0, 0);
  background(240, 240, 240);
  player1 = new Player(768, 376.5);
  ellipseMode(CENTER);
  textSize(40);
  textAlign(LEFT);
  fill(204, 204, 0);
  text(text, 500, 500);
  rectMode(CENTER);
  imageMode(CENTER);
  usernameInput = createInput();
  usernameInput.position(canvasWidth/2 - 100, canvasHeight/1.2);
  usernameInput.size(200, 40);
}

function draw() {
  //handle which screen is showing
  if (screen == "play")
    playScreen();
  else if (screen == "menu")
    menuScreen();
  else if (screen == "scores")
    highScoreScreen();
}

function playScreen() {

  //what the user should see while in game
  background(50, 50, 50);
  textSize(40);
  textAlign(LEFT);
  fill(204, 204, 0);
  text(score, 30, 60);
  fill(255, 255, 255);

  //update player pos/display
  player1.show();
  player1.move();

  //handle bullet pos/display
  //checking bullet collisions with player and other bullets
  for (var i = 0; i < bullets.length; i++) {
    bullets[i].show();
    bullets[i].move();
    bullets[i].checkHit(player1);
    for (var j = 0; j < bullets.length; j++) {
      if (i != j) {
        bullets[i].checkCollide(bullets[j]);
      }
    }
  }

  //send waves on a frame based timer in case it does not run in real time
  if (frameCount % (Math.round(waveFrequency)*60) == 0) {
    sendWave(waveSize);
    progress();
  }
}

//what the player should see on the main menu
function menuScreen() {
  background(50);
  textSize(75);
  textAlign(CENTER);
  fill(255);
  text("PACIFIST ASTEROIDS", canvasWidth/2, canvasHeight/3);
  textSize(30);
  text("USERNAME", canvasWidth/2, canvasHeight/1.25);
  textSize(40);
  fill(204, 204, 0);
  text("HIGH SCORE: "+highScore, canvasWidth/2, canvasHeight/2.25);
  text("PREVIOUS SCORE: "+prevScore, canvasWidth/2, canvasHeight/1.8);

  //gotta make sure some people can never be beaten
  if (usernameInput.value()=="Dr. Bowling" || usernameInput.value()=="Jack" || usernameInput.value()=="KADY") {
    localStorage.setItem("pacifistAsteroids."+usernameInput.value(), Infinity);
  }
  //if the user already has a high score, update the text
  if (localStorage.getItem("pacifistAsteroids."+usernameInput.value())!=null && usernameInput.value()!="") {
    highScore = localStorage.getItem("pacifistAsteroids."+usernameInput.value());
  } else {
    highScore = 0;
  }
  button(canvasWidth/2-100, canvasHeight/1.5, 200, 100, "PLAY", "startGame");
  button(canvasWidth/2+100, canvasHeight/1.5, 200, 100, "SCORES", "highScores");
}

//what the player should see when looking at high score screen
function highScoreScreen() {
  background(50);
  textAlign(CENTER);
  textSize(40);
  fill(204, 204, 0);
  text("HIGH SCORES", canvasWidth/2, canvasHeight/5);
  textAlign(LEFT);
  textSize(30);
  fill(255);
  let sum = 0;
  for (let j = 0; j < highScores.length && j < 10; j++) {
    txt = j+1+". " + highScores[j][1]+" - "+highScores[j][0];
    sum += txt.length;
  }
  highScoresAvgTxt = sum/10;
  for (let j = 0; j < highScores.length && j < 10; j++) {
    txt = j+1+". " + highScores[j][1]+" - "+highScores[j][0];
    text(txt, canvasWidth/2-sum, canvasHeight/3.5+j*50);
  }
  button(100, 25, 200, 50, "BACK", "menu");
}

function button(x, y, width, height, txt, ref) {
  if (mouseX < x+width/2 && mouseX > x-width/2 && mouseY > y-height/2 && mouseY < y+height/2) {
    fill(200);
    if (mouseIsPressed) {
      buttonRef(ref);
    }
  } else {
      fill(255);
  }
  rectMode(CENTER);
  rect(x, y, width, height);
  fill(50);
  textSize(40);
  textAlign(CENTER);
  text(txt, x, y+12);
}

function buttonRef(ref) {
  if (ref == "startGame") {
    usernameInput.position(-1000, -1000);
    bullets = [];
    bulletSpeed = 2;
    bulletSize = 50;
    waveFrequency = 20;
    waveSize = 5;
    score = 0;
    player1.alive = true;
    player1.x = canvasWidth/2;
    player1.y = canvasHeight/2;
    frameCount = 0;
    localStorage.setItem("pacifistAsteroids."+usernameInput.value(), highScore);
    sendWave(3);
    screen = "play";
  } else if (ref == "highScores") {
    highScores = [];
    for (let i = 0; i < localStorage.length; i++) {
      if (localStorage.key(i).length > 18)
        highScores.push([localStorage.getItem(localStorage.key(i)), localStorage.key(i).substring(18)]);
    }
    for (let i = 0; i < highScores.length; i++) {
      for (let j = 0; j < highScores.length-1; j++) {
        if (int(highScores[j][0]) < int(highScores[j+1][0]) || highScores[j+1][0]=="Infinity") {
          let temp = highScores[j];
          highScores[j] = highScores[j+1];
          highScores[j+1] = temp;
        }
      }
    }
    screen = "scores";
    usernameInput.position(-1000, -1000);
  } else if (ref == "menu") {
    screen = "menu";
    usernameInput.position(canvasWidth/2 - 100, canvasHeight/1.2);
  }
}

function Player(x, y) {
  this.x = x;
  this.y = y;
  this.xVel = 0;
  this.yVel = 0;
  this.acc = .1;
  this.speed = 4;
  this.size = 10;
  this.alive = false;
  this.left = false;
  this.right = false;
  this.up = false;
  this.down = false;

  this.show = function() {
    if (this.left)
      image(shipLeft, this.x, this.y, this.size*10, this.size*10);
    if (this.right)
      image(shipRight, this.x, this.y, this.size*10, this.size*10);
    if (this.up)
      image(shipUp, this.x, this.y, this.size*10, this.size*10);
    if (this.down)
      image(shipDown, this.x, this.y, this.size*10, this.size*10);
    image(shipNeutral, this.x, this.y, this.size*10, this.size*10);
    ellipse(this.x, this.y, this.size);
    this.left = false;
    this.right = false;
    this.up = false;
    this.down = false;
  }

  this.move = function() {
    if (keyIsDown(37)) {
      //this.xVel -= this.acc;
      if (this.x > 0) {
        this.x -= this.speed
        this.left = true;
      }
    }
    if (keyIsDown(39)) {
      //this.xVel += this.acc;
      if (this.x < canvasWidth) {
        this.x += this.speed;
        this.right = true;
      }
    }
    if (keyIsDown(38)) {
      //this.yVel -= this.acc;
      if (this.y > 0) {
        this.y -= this.speed;
        this.up = true;
      }
    }
    if (keyIsDown(40)) {
      //this.yVel += this.acc;
      if (this.y < canvasHeight) {
        this.y += this.speed;
        this.down = true;
      }
    }
    // this.x += this.xVel;
    // this.y += this.yVel;
  }
}

function Bullet(x, y, speed, radius, cd) {
  this.index = bullets.length;
  this.x = x;
  this.y = y;
  this.speed = speed;
  this.r = radius;
  this.hit = false;
  this.bounces = 5;
  if (cd != null) {
    this.cd = cd
  } else {
    this.cd = 0;
  }

  this.walls = [
    {
      changeDir: function(dir) {
        return dir*-1;
      },
      hit:false,
      x:[0,canvasWidth],
      y:[0,0]
    },
    {
      changeDir: function(dir) {
        return Math.PI-dir;
      },
      hit:false,
      x:[canvasWidth,canvasWidth],
      y:[0,canvasHeight]
    },
    {
      changeDir: function(dir) {
        return dir*-1;
      },
      hit:false,
      x:[0,canvasWidth],
      y:[canvasHeight,canvasHeight]
    },
    {
      changeDir: function(dir) {
        return Math.PI-dir;
      },
      hit:false,
      x:[0,0],
      y:[0,canvasHeight]
    }
  ];

    this.findDir = function(player) {
      if (!this.hit) {
        var dx = player.x-this.x;
        var dy = this.y-player.y;
        this.dir = atan2(dy,dx);
      }
    }

    this.move = function() {
      if (!this.hit) {
        this.x += this.speed*Math.cos(this.dir);
        this.y -= this.speed*Math.sin(this.dir);
        if (this.cd > 0) {
          this.cd--;
        }
        if (this.bounces <= 0 || (this.x < -500 || this.y < -500 || this.x > canvasWidth+500 || this.y > canvasHeight+500)) {
          this.hit = true;
          score++;
          if (score > highScore) {
            highScore = score;
            localStorage.setItem("pacifistAsteroids."+usernameInput.value(), highScore);
          }
        }
      }
    }

    this.checkHit = function(player) {
      if (Math.sqrt(Math.pow(this.x-player.x, 2)+Math.pow(this.y-player.y, 2)) < this.r/2 + player.size && !this.hit) {
        player.alive = false;
        prevScore = score;
        if (score > highScore) {
          highScore = score;
          localStorage.setItem("pacifistAsteroids."+usernameInput.value(), highScore);
        }
        this.hit = true;
        screen = "menu";
        usernameInput.position(canvasWidth/2 - 100, canvasHeight/1.2);
      }
    }

    this.checkCollide = function(bullet) {
      if (Math.sqrt(Math.pow(this.x-bullet.x, 2)+Math.pow(this.y-bullet.y, 2)) < this.r/2 + bullet.r/2 && !this.hit && this.cd <= 0) {
        this.split();
        bullet.split();
        score += 2;
        if (score > highScore) {
          highScore = score;
          localStorage.setItem("pacifistAsteroids."+usernameInput.value(), highScore);
        }
      } else {
        if (this.cd <= 0) {
          for (var i = 0; i < this.walls.length; i++) {
            /*
            0:top
            1:left
            2:bottom
            3:right
            */
            if (!this.walls[i].hit) {

              if (this.x + this.r/2 > this.walls[i].x[0]-5 && this.x-this.r/2 < this.walls[i].x[1]+5 && this.y+this.r/2 > this.walls[i].y[0]-5 && this.y-this.r/2 < this.walls[i].y[1]-5) {
                this.dir = this.walls[i].changeDir(this.dir);
                this.resetWalls();
                this.walls[i].hit = true;
                this.bounces--;
              }
              // if (this.x - this.r/2 < 0 || this.x + this.r/2 > canvasWidth) {
              //   // horizontal bounces
              //   // this.dir = Math.PI - this.dir;
              //   // this.cd = 75;
              //   // this.bounces--;
              // }
              // if (this.y -this.r/2 < 0 || this.y + this.r/2 > canvasHeight) {
              //   // vetical bounces
              //   // this.dir *= -1;
              //   // this.cd = 75;
              //   // this.bounces--;
              // }
            }
          }
        }
      }
    }

    this.resetWalls = function() {
      for (var i = 0; i < this.walls.length; i++)
        this.walls[i].hit = false;
    }

    this.show = function() {
      fill(255, 255, 255);
      if (!this.hit) {
        ellipse(this.x, this.y, this.r);
      }
    }

    this.respawn = function(x, y, dir, r, cd) {
      if (this.hit) {
        let coords = findCoords();
        if (x != null) {
          this.x = x;
        } else {
          this.x = coords.x;
        }
        if (y != null) {
          this.y = y;
        } else {
          this.y = coords.y;
        }
        this.speed = bulletSpeed;
        if (r != null) {
          this.r = r;
        } else {
          this.r = bulletSize;
        }
        this.hit = false;
        if (cd != null) {
          this.cd = cd;
        } else {
          this.cd = 75;
        }
        this.bounces = 5;
        if (dir != null) {
          this.dir = dir;
        } else {
          this.findDir(player1);
        }
        console.log("Bullet " + this.index + " respawned at x: " + this.x + ", y: "+this.y + " with speed " + this.speed + " and size " + this.r + " and direction " + this.dir);
      }
    }

    this.split = function() {
      this.hit = true;
      var childCount = Math.round(random(2, 4));
      var done = false;
      while (childCount > 2 && !done) {
        if (this.r/childCount < minBulletSize) {
          childCount--;
        } else {
          done = true
        }
      }
      if (this.r/childCount > minBulletSize) {
        let numBullets = 0;
        let hitBullets = [];
        for (var j = 0; j < bullets.length; j++) {
          if (bullets[j].hit) {
            numBullets++;
            hitBullets.push(j);
          }
        }
        if (childCount > numBullets) {
          for (var i = 0; i < childCount; i++) {
            let newDir = this.dir + random(-Math.PI/2, Math.PI/2);
            bullets.push(new Bullet(this.x, this.y, bulletSpeed, this.r/childCount, 75));
            bullets[bullets.length-1].dir = newDir;
          }
        } else {
          for (var i = 0; i < childCount; i++) {
            let newDir = this.dir + random(-Math.PI/2, Math.PI/2);
            bullets[hitBullets[i]].respawn(this.x, this.y, newDir, this.r/childCount);
          }
        }
      }
    }
}

function sendWave(count) {
  let numBullets = 0;
  let hitBullets = [];
  for (var j = 0; j < bullets.length; j++) {
    if (bullets[j].hit) {
      numBullets++;
      hitBullets.push(j);
    }
  }
  if (count > numBullets) {
    for (var i = 0; i < count; i++) {
      let coords = findCoords();
      bullets.push(new Bullet(coords.x, coords.y, bulletSpeed, bulletSize, 75));
      bullets[bullets.length-1].findDir(player1);
    }
  } else {
    for (var i = 0; i < count; i++) {
      bullets[hitBullets[i]].respawn(null, null, null, null, 75);
    }
  }
}

function progress() {
  if (bulletSpeed < 6) {
    bulletSpeed+=.2;
  }
  if (waveFrequency > 5) {
      waveFrequency*=.8;
  }
  if (waveSize < 20) {
    waveSize += 2;
  }
}

function findCoords() {
  var chance = Math.floor(random(1, 4.99));
  var coords = {};
  if (chance == 1) {
    coords.x = -bulletSize;
    coords.y = random(-bulletSize, canvasHeight+bulletSize);
  } else if (chance == 2) {
    coords.x = canvasWidth + bulletSize;
    coords.y = random(-bulletSize, canvasHeight+bulletSize);
  } else if (chance == 3) {
    coords.x = random(-bulletSize, canvasWidth+bulletSize);
    coords.y = -bulletSize;
  } else if (chance == 4) {
    coords.x = random(-bulletSize, canvasWidth+bulletSize);
    coords.y = canvasHeight + bulletSize;
  }

  return coords;
}
