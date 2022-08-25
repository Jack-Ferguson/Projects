let specials = ["Million", "Billion", "Trillion", "Quadrillion", "Quintillion", "Sextillion", "Septillion", "Octillion", "Nonillion"];
let tens = ["", "Decillion", "Vigintillion", "Trigintillion", "Quadragintillion", "Quinquagintillion", "Sexagintillion", "Octogintillion", "Nonagintillion"];
let hundreds = ["", "Centillion", "Ducentillion", "Trecentillion", "Quadringentillion", "Quingentillion", "Sescentillion", "Septingentillion", "Octingentillion", "Nongentillion", "Millinillion"];
let ones = ["", "Un", "Duo", "Tre", "Quattor", "Quin", "Sex", "Septem", "Octo", "Novem"];
let prefixes = ["", "Deci", "Viginti", "Triginti", "Quadraginti", "Quinquaginti", "Sexaginti", "Septuaginti", "Octoginti", "Nonaginti"];

function numberToWord(num) {

	if (num < 1000000)
		return num.toLocaleString();
	else if (num == Infinity) {
		return "Bruh";
	}

	let decimals = 3;

	let scientific = num.toExponential(decimals+3);
	let exponent = scientific.substring(scientific.indexOf("+")+1);

	let	baseIllion = Math.floor(exponent/3-1);

	let extraDigitCount = Math.round(((exponent/3-1)-Math.floor(exponent/3-1))/(1/3));

	let left = Math.floor(scientific.substring(0, decimals+3+2)*Math.pow(10, extraDigitCount));
	let right = scientific.substring(extraDigitCount+2, extraDigitCount+2+decimals);


	let follower = "";

	if (baseIllion < 10)
		follower = specials[baseIllion-1];
	else {
		onesDigit = baseIllion%10;
		tensDigit = Math.floor(baseIllion/10)%10;
		hundredsDigit = Math.floor(baseIllion/100)%10;

		follower = ones[onesDigit] + tens[tensDigit] + hundreds[hundredsDigit];
	}

		return (left+"."+right + " " + follower);
		// return baseIllion;
		// return extraDigitCount;
}
