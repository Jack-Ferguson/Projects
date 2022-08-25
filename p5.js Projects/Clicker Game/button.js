function Button(centerX, centerY, width, height, size, type) {

	this.type = type;

	if (this.type == "manual") {
		this.ID = clickButtons.length;
		if (this.ID == 0) {
			this.price = 0;
			this.label = "Click!";
		} else {
			this.price = clickPrice*Math.pow(clickPriceScaling, this.ID-1);
			this.label = "Manual Tier "+this.ID;
		}
		this.priceScaling = 1.1;
	} else if (this.type == "passive") {
		this.ID = cpsButtons.length;
		this.price = cpsPrice*Math.pow(cpsPriceScaling, this.ID);
		this.priceScaling = 2;
		this.label = "Passive Tier "+(this.ID+1);
	} else if (this.type == "arrow") {
		this.ID = arrowButtons.length;
	}

	this.centerX = centerX;
	this.centerY = centerY;
	this.width = width;
	this.height = height;
	this.size = size;
	this.active = false;
	this.cps = 1;

	this.onClick = function() {

		if (clicks[0] >= Math.round(this.price)) {
			clicks[0] -= Math.round(this.price);
			this.price *= this.priceScaling;
			if (this.type == "manual") {
				clicks[this.ID] += clicks[this.ID+1];
				if (this.ID == clickButtons.length-1 && this.price >= clickPrice*Math.pow(clickPriceScaling, this.ID)/2) {
					clickButtons.push(new Button(650, 250+(125*((clickButtons.length-1)%5)), 300, 100, 30, "manual"));
					clicks.push(1)
				}
			} else if (this.type == "passive") {
				cps[this.ID] += this.cps;
				if (this.ID == cpsButtons.length-1 && this.price >= cpsPrice*Math.pow(cpsPriceScaling, this.ID+1)/2) {
					cpsButtons.push(new Button(1000, 250+(125*(cpsButtons.length%5)), 300, 100, 30, "passive"));
					cps.push(0);
				}
			}
		}

		if (this.type == "arrow") {

			switch (this.ID) {
				case 0:
					if (clickPage > 0)
						clickPage--;
				break;

				case 1:
					if (clickPage < (clickButtons.length-1)/5 - 1)
						clickPage++;
				break;

				case 2:
					if (cpsPage > 0) {
						cpsPage--;
					}
				break;

				case 3:
					if (cpsPage < cpsButtons.length/5 - 1) {
						cpsPage++;
					}
				break;
			}
		}

		if (this.type == "start") {
			screen = "playScreen";
		}

	}

	this.show = function() {
		if (this.active == true) {
			if (Math.abs(this.centerX-mouseX) <= this.width/2 && Math.abs(this.centerY-mouseY) <= this.height/2) {
				fill(230);
				if (mouseIsPressed)
					fill(200);
			} else {
				fill(255);
			}
			noStroke();
			rectMode(CENTER);
			rect(this.centerX, this.centerY, this.width, this.height);

			fill(0);
			textAlign(LEFT, CENTER);
			textSize(this.size);
			text(this.label, this.centerX-this.width/2 + 10, this.centerY);
			textSize(this.size/2);
			let num = 0;
			let message = "";
			if ((this.ID != 0 || this.type == "passive") && this.type != "arrow") {
				// if (num > 1000000000000)
				// 	num = num.toExponential(4);
				// else
				// 	num = num.toLocaleString();
				text("Price: "+numberToWord(Math.round(this.price)), this.centerX-this.width/2 + 10, this.centerY-this.height/3);
			}
			if (this.type == "manual") {
				num = Math.round(clicks[this.ID+1]);
				// if (num > 1000000000000)
				// 	num = num.toExponential(4);
				// else
				// 	num = num.toLocaleString();
				if (this.ID == 0)
					textSize(this.size/3);
				text("Power: "+numberToWord(num), this.centerX-this.width/2 + 10, this.centerY+this.height/3);
			} else if (this.type == "passive") {
				num = Math.round(cps[this.ID]);
				// if (num > 1000000000000)
				// 	num = num.toExponential(4);
				// else
				// 	num = num.toLocaleString();
				text("Power: "+numberToWord(num), this.centerX-this.width/2 + 10, this.centerY+this.height/3);
			} else {
				if (this.ID%2==0) {
					textAlign(CENTER)
					text("↑", this.centerX, this.centerY);
				} else {
					textAlign(CENTER)
					text("↓", this.centerX, this.centerY);
				}
			}
		}
	}
}

/*******************************************************************************************************/

function menuButton(x, y, width, height, txt, ref) {
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
  textAlign(CENTER, CENTER);
  text(txt, x, y);
}

function buttonRef(ref) {
  if (ref == "startGame") {
		if (clicks[0] == 0 && cps[0] == 0) {
			clicks = [];
			cps = [];
			clicks.push(0);
			clickButtons.push(new Button(250, 400-50, 400, 300, 100, "manual"));
			clicks.push(1);
			clickButtons.push(new Button(650, 250+(125*((clickButtons.length-1)%5)), 300, 100, 30, "manual"));
			clicks.push(1);
			cpsButtons.push(new Button(1000, 250+(125*(cpsButtons.length%5)), 300, 100, 30, "passive"));
			cps.push(0);
		} else {
			console.log("clicks: "+clicks+" | cps: "+cps);
			let clickFlag = false;
			let cpsFlag = false;
			let priceFlag = false;
			let gap = 0;
			clickButtons.push(new Button(250, 400-50, 400, 300, 100, "manual"));
			for (let i = 1; i < localStorage.getItem("clickerGame."+usernameInput.value()).split(",").length; i++) {
				let current = localStorage.getItem("clickerGame."+usernameInput.value()).split(",")[i];

				if (clickFlag) {
					if (current != "|||") {
						clickButtons.push(new Button(650, 250+(125*((clickButtons.length-1)%5)), 300, 100, 30, "manual"));
						clickButtons[clickButtons.length-1].active = true;
						clickButtons[clickButtons.length-1].price = current;
					}
				}
				if (cpsFlag) {
					if (current != "||||") {
						cpsButtons.push(new Button(1000, 250+(125*((cpsButtons.length)%5)), 300, 100, 30, "passive"));
						cpsButtons[cpsButtons.length-1].active = true;
						cpsButtons[cpsButtons.length-1].price = current;
					} else {
						playerClicks = localStorage.getItem("clickerGame."+usernameInput.value()).split(",")[i+1];
					}
				}

				if (current == "||") {
					clickFlag = true;
					i++;
				}
				if (current == "|||") {
					cpsFlag = true;
					clickFlag = false;
				}
				if (current == "||||") {
					cpsFlag = false;
				}
			}
		}
    usernameInput.position(-1000, -1000);
    screen = "playScreen";
  } else if (ref == "saves") {

    usernameInput.position(-1000, -1000);
  } else if (ref == "menu") {
    screen = "menuScreen";
    usernameInput.position(canvasWidth/2 - 100, canvasHeight/1.2);
  } else if (ref == "saveGame") {
		let saveArray = [];
		for (let i = 0; i < clicks.length; i++) {
			saveArray.push(Math.round(clicks[i]));
		}
		saveArray.push("|");
		for (let i = 0; i < cps.length; i++) {
			saveArray.push(Math.round(cps[i]));
		}
		saveArray.push("||");
		for (let i = 0; i < clickButtons.length; i++) {
			saveArray.push(Math.round(clickButtons[i].price));
		}
		saveArray.push("|||");
		for (let i = 0; i < cpsButtons.length; i++) {
			saveArray.push(Math.round(cpsButtons[i].price));
		}
		saveArray.push("||||");
		saveArray.push(playerClicks);
		localStorage.setItem("clickerGame."+usernameInput.value(), saveArray);
		console.log(saveArray);
		screen = "menuScreen";
		clicks = [];
		cps = [];
		clickButtons = [];
		cpsButtons = [];
		playerClicks = 0;
		clickPage = 0;
		cpsPage = 0;
	}
}
