let currentClicks = [];
let currentCps = [];

function menuScreen() {
  textSize(75);
  textAlign(CENTER);
  fill(255);
  text("Clicker Game", canvasWidth/2, canvasHeight/4);
  textSize(30);
  text("USERNAME", canvasWidth/2, canvasHeight/1.25);
  usernameInput.position(canvasWidth/2 - 100, canvasHeight/1.2);
  textSize(40);
  fill(204, 204, 0);
  if (usernameInput.value()!="" && localStorage.getItem("clickerGame."+usernameInput.value())!=null) {
    let flag = false;
    for (let i = 0; i < localStorage.getItem("clickerGame."+usernameInput.value()).split(",").length; i++) {
      let current = localStorage.getItem("clickerGame."+usernameInput.value()).split(",")[i];
      if (current == "||")
        break;
      if (!flag) {
        if (current == "|")
          flag = true;
        else {
          currentClicks.push(float(current));
        }
      } else {
        currentCps.push(float(current));
      }
    }
  } else {
    currentClicks.push(0);
    currentCps.push(0);
  }
  text("CURRENT CLICKS: "+numberToWord(currentClicks[0]), canvasWidth/2, canvasHeight/2.5);
  text("CURRENT CPS: "+numberToWord(currentCps[0]), canvasWidth/2, canvasHeight/2);
  clicks = currentClicks;
  cps = currentCps;
  currentClicks = [];
  currentCps = [];
  // if (localStorage.getItem(usernameInput.value())!=null && usernameInput.value()!="") {
  //   highScore = localStorage.getItem(usernameInput.value());
  // } else {
  //   highScore = 0;
  // }
  menuButton(canvasWidth/2-100, canvasHeight/1.5, 200, 100, "PLAY", "startGame");
  menuButton(canvasWidth/2+100, canvasHeight/1.5, 200, 100, "SAVES", "saves");
}
