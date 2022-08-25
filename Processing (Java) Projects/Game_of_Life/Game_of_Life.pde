int cellSize = 15;
int cellCountX, cellCountY;
int[][] cells;
boolean paused = false;


void setup() {
  frameRate(30);
  size(1920, 1080);
  
  cellCountX = width/cellSize;
  cellCountY = height/cellSize;
  
  cells = new int[cellCountX][cellCountY];
  
  for (int i = 0; i < cellCountX; i++) {
    for (int j = 0; j < cellCountY; j++) {
      cells[i][j] = (int) random(0, 2);
    }
  }
  
  System.out.println("x: " + cellCountX + "y: " + cellCountY);
}

void draw() {
  drawGrid();
  
  if (!paused)
    cells = doRules(cells);
}

void mousePressed() {
  if (mouseButton == LEFT)
    cells[mouseX/cellSize][mouseY/cellSize] = 1;
  else if (mouseButton == RIGHT)
    cells[mouseX/cellSize][mouseY/cellSize] = 0;
  else if (mouseButton == CENTER)
    cells = new int[cellCountX][cellCountY];
}

void keyPressed() {
  paused = !paused;
}

void drawGrid() {
  
  for (int i = 0; i < cellCountX; i++) {
    for (int j = 0; j < cellCountY; j++) {
      fill(cells[i][j]*255);
      rect(i*cellSize, j*cellSize, cellSize, cellSize);
    }
  }
}

int[][] doRules(int[][] input) {
  int[][] output = new int[input.length][input[0].length];
  
  for (int i = 0; i < input.length; i++) {
    for (int j = 0; j < input[0].length; j++) {
      
      //int neighbors = wrappedNeighborCount(input, i, j);
      int neighbors = neighborCount(input, i, j);
      
      //any live cell with two or three live neighbors survives
      if (cells[i][j] == 1 && neighbors == 2 || neighbors == 3) {
        output[i][j] = 1;
      }
      
      //Any dead cell with three live neighbors becomes a live cell
      if (cells[i][j] == 0 && neighbors == 3) {
        output[i][j] = 1;
      }
      
      //All other live cells die in the next generation. Similarly, all other dead cells stay dead.
      //This step requires no action because the copy array starts filled with dead cells
    }
  }
  
  
  return output;
}

int neighborCount(int[][] arr, int row, int col) {
  int neighbors = 0;
  
  for (int i = 0; i < arr.length; i++) {
    for (int j = 0; j < arr[0].length; j++) {
      
       if (abs(row-i) <= 1 && abs(col-j) <= 1 && !(row == i && col == j)) {
         neighbors += cells[i][j];
       }
       
    }
  }
  
  return neighbors;
}

int wrappedNeighborCount(int[][] arr, int row, int col) {
 int neighbors = 0;
 
 neighbors += arr[wrap(row-1, 0, arr.length-1)][wrap(col-1, 0, arr[0].length-1)];
 neighbors += arr[wrap(row-1, 0, arr.length-1)][wrap(col, 0, arr[0].length-1)];
 neighbors += arr[wrap(row-1, 0, arr.length-1)][wrap(col+1, 0, arr[0].length-1)];
 neighbors += arr[wrap(row, 0, arr.length-1)][wrap(col-1, 0, arr[0].length-1)];
 neighbors += arr[wrap(row, 0, arr.length-1)][wrap(col+1, 0, arr[0].length-1)];
 neighbors += arr[wrap(row+1, 0, arr.length-1)][wrap(col-1, 0, arr[0].length-1)];
 neighbors += arr[wrap(row+1, 0, arr.length-1)][wrap(col, 0, arr[0].length-1)];
 neighbors += arr[wrap(row+1, 0, arr.length-1)][wrap(col+1, 0, arr[0].length-1)];
 
 return neighbors;
}

int wrap(int num, int min, int max) {
  if (num < min)
    return max;
  if (num > max)
    return min;
  return num;
}
