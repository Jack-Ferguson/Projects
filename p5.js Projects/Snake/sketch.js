var canvasWidth, canvasHeight;

let grid;
let rows = 50;
let cols = 100;
let width;
let height;

let player;
let food;

function setup() {
  canvasWidth = 1536;
  canvasHeight = 753;
  canvas = createCanvas(canvasWidth, canvasHeight);
  canvas.position(0, 0);

  width = canvasWidth/cols;
  height = canvasHeight/rows;

  // grid = makeGrid(rows, cols);

  player = new Snake(canvasWidth/2/width, canvasHeight/2/height);
  food = new Food();
  food.respawn();
}

function draw() {
  frameRate(60);
  background(50);
  makeGrid(rows, cols);
  // for (let i = 0; i < grid.length; i++) {
  //   grid[i].show();
  // }
  player.show();
  food.show();

  if (frameCount%5==0) {
    player.check();
    player.move();
  }
}

function makeGrid(rows, cols) {
  stroke(100);
  // let grid = [];
  //
  // for (r = 0; r < rows; r++) {
  //   for (c = 0; c < cols; c++) {
  //     append(grid, new Square(c, r, width, height));
  //   }
  // }
  //
  // return grid;

  for (r = 0; r <= canvasHeight; r += height) {
    line(0, r, canvasWidth, r);
  }
  for (c = 0; c <= canvasWidth; c += width) {
    line(c, 0, c, canvasHeight);
  }

}

function Square(x, y) {
  this.x = x;
  this.y = y;

  this.show = function() {
    stroke(100);
    fill(0);
    rect(this.x*width, this.y*height, width, height);
  }
}

function Snake(startX, startY) {

  this.newLength = 2;

  this.positions = [{x: startX, y: startY}];
  this.direction = "up";
  this.alive = true;
  this.lastDir = "up";

  this.show = function() {
    stroke(0);
    fill(100, 255, 100);
    for (let i = 0; i < this.positions.length; i++) {
      rect(this.positions[i].x*width, this.positions[i].y*height, width, height);
    }
    if (!this.alive) {
      fill(100);
      rect(this.positions[0].x*width, this.positions[0].y*height, width, height);
    }
  }

  this.check = function() {
    if (keyIsDown(37)) {
      if (this.lastDir != "right") {
        this.direction = "left";
      }
    }
    if (keyIsDown(39)) {
      if (this.lastDir != "left") {
        this.direction = "right";
      }
    }
    if (keyIsDown(38)) {
      if (this.lastDir != "down") {
        this.direction = "up";
      }
    }
    if (keyIsDown(40)) {
      if (this.lastDir != "up") {
        this.direction = "down";
      }
    }
  }

  this.move = function() {
    if (this.alive) {
      if (this.direction == "left") {
        this.positions.unshift({x: this.positions[0].x-1, y: this.positions[0].y});
        this.lastDir = "left";
      } else if (this.direction == "right") {
        this.positions.unshift({x: this.positions[0].x+1, y: this.positions[0].y});
        this.lastDir = "right";
      } else if (this.direction == "up") {
        this.positions.unshift({x: this.positions[0].x, y: this.positions[0].y-1});
        this.lastDir = "up";
      } else if (this.direction == "down") {
        this.positions.unshift({x: this.positions[0].x, y: this.positions[0].y+1});
        this.lastDir = "down";
      }

      if (this.newLength > 0) {
        this.newLength--;
      } else {
        this.positions.pop();
      }
    }
    if (this.positions[0].x < 0 || this.positions[0].x*width >= canvasWidth || this.positions[0].y < 0 || this.positions[0].y*height >= canvasHeight) {
      this.alive = false;
    }
    if (this.positions[0].x == food.x && this.positions[0].y == food.y) {
      this.newLength += 5;
      food.respawn();
    }
    for (let i = 1; i < this.positions.length; i++) {
      if (this.positions[0].x == this.positions[i].x && this.positions[0].y == this.positions[i].y)
        this.alive = false;
    }
  }
}

function Food() {
  this.x;
  this.y;

  this.respawn = function() {
    this.x = Math.round(random(0, cols));
    this.y = Math.round(random(0, rows));
    for (let i = 1; i < player.positions.length; i++) {
      if (this.x == player.positions[i].x && this.y == player.positions[i].y)
        this.respawn();
    }
  }

  this.show = function() {
    fill(255, 0, 0);
    rect(this.x*width, this.y*height, width, height);
  }
}
