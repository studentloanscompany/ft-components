/***
 * Implement all generic functions here to use by all other pages.
 */

function triggerButtonAction(elementId){
	document.getElementById(elementId).click();
}

/**
 * Prevent selecting values in elements on mouseup event.
 * @param event
 */
function preventMouseEvent(event) {
	if(BrowserDetect.browser == "Explorer"){
		event.returnValue = false;
	} else {
		event.preventDefault();
	}
}

/**
 * @param e
 * @param fieldId
 * @returns {Boolean}
 */
function moveNextField(e, fieldId) {
	var key = -1;
	if (e.keyCode)
		key = e.keyCode;
	else if (e.which)
		key = e.which;
	//Detect number key pads.
	if (key > 95 && key < 106) {
		key = key - 48;
	}
	if (key == 8) {
		return true;
	}
	if (/[^A-Za-z0-9 ]/.test(String.fromCharCode(key))) {
		return false;
	} else {
		var input = document.getElementById(fieldId);
		if (input.maxLength == 1 || input.value.length == input.maxLength) {
			input.focus();
			input.select();
		}
	}
}