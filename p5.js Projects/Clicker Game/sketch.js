// let canvasWidth = 1200;
// let canvasHeight = 900;

let canvasWidth, canvasHeight;
let fps = 30;

let screen = "menuScreen";

let usernameInput;

let clicks = [0];
let cps = [];

let clickPrice = 10;
let clickPriceScaling = 100;
let cpsPrice = 20;
let cpsPriceScaling = 100;

let clickButtons = [];
let cpsButtons = [];
let arrowButtons = [];

let playerClicks = 0;

let clickPage = 0;
let cpsPage = 0;

function setup() {
	canvasWidth = 1375;
	canvasHeight = 675;
	frameRate(fps);
	createCanvas(canvasWidth, canvasHeight);
	background(50);



	arrowButtons[0] = new Button(600, 850, 50, 50, 60, "arrow");
	arrowButtons[0].active = true;
	arrowButtons[1] = new Button(700, 850, 50, 50, 60, "arrow");
	arrowButtons[1].active = true;
	arrowButtons[2] = new Button(950, 850, 50, 50, 60, "arrow");
	arrowButtons[2].active = true;
	arrowButtons[3] = new Button(1050, 850, 50, 50, 60, "arrow");
	arrowButtons[3].active = true;

	usernameInput = createInput();
	usernameInput.position(canvasWidth/2 - 100, canvasHeight/1.2);
	usernameInput.size(200, 40);
}

function draw() {
	background(50)

	if (screen == "menuScreen") {
		menuScreen();
	} else if (screen == "playScreen") {
		playScreen();
	}
}

function mouseClicked() {
	for (i = 0; i < clickButtons.length; i++) {
		if (clickButtons[i].active && Math.abs(clickButtons[i].centerX-mouseX) <= clickButtons[i].width/2 && Math.abs(clickButtons[i].centerY-mouseY) <= clickButtons[i].height/2) {
			clickButtons[i].onClick();
		if (i == 0)
			playerClicks++;
		}
	}
	for (i = 0; i < cpsButtons.length; i++) {
		if (cpsButtons[i].active && Math.abs(cpsButtons[i].centerX-mouseX) <= cpsButtons[i].width/2 && Math.abs(cpsButtons[i].centerY-mouseY) <= cpsButtons[i].height/2) {
			cpsButtons[i].onClick();
		}
	}
	for (i = 0; i < arrowButtons.length; i++) {
		if (arrowButtons[i].active && Math.abs(arrowButtons[i].centerX-mouseX) <= arrowButtons[i].width/2 && Math.abs(arrowButtons[i].centerY-mouseY) <= arrowButtons[i].height/2) {
			arrowButtons[i].onClick();
		}
	}
}
