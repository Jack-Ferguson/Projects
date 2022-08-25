function playScreen() {
	fill(0);
	textAlign(LEFT, CENTER);
	textSize(60);
	// text("Clicks: "+Math.round(clicks[0]).toLocaleString(), 50, 80);
	// text("Passive CPS: "+Math.round(cps[0]).toLocaleString(), 50, 140);
	text("Clicks: "+numberToWord(Math.round(clicks[0])), 50, 80);
	text("Passive CPS: "+numberToWord(Math.round(cps[0])), 50, 140);
	textSize(canvasWidth*canvasHeight/40000);
	// text("Player Clicks: "+playerClicks, 50, 550);
	text("Player Clicks: "+numberToWord(playerClicks), 50, 650);

	for (i = 0; i < clickButtons.length; i++) {
		if (i < 5*clickPage + 1 || i > 5*clickPage+5)
			clickButtons[i].active = false;
		else
			clickButtons[i].active = true;

		clickButtons[0].active = true;
		clickButtons[i].show();
	}

	for (i = 0; i < cpsButtons.length; i++) {
		if (i < 5*cpsPage || i >= 5*cpsPage+5)
			cpsButtons[i].active = false;
		else
			cpsButtons[i].active = true;
		cpsButtons[i].show();
	}

	for (i = 0; i < arrowButtons.length; i++) {
		arrowButtons[i].show();
	}

	if (frameCount%1==0) {
		for (i = cps.length-1; i > 0; i--) {
			cps[i-1] += cps[i]/fps;
		}
		clicks[0] += cps[0]/fps;
	}

	menuButton(250, 850, 300, 100, "Save and Exit", "saveGame");

}
