/**
 * The SimpleBackHandler object for back button handling among others
 * 
 * @author slc
 */
var SimpleBackHandler = {

	/**
	 * 
	 * Gets exactly preceding question to the current question
	 * 
	 * This function gets list of questions dynamically by analysing the
	 * content, current question is determined and previous question is found
	 * 
	 * Time complexity for finding previous question is BigO(index of
	 * currentQuestion)) Space complexity (memory) is 2 x size(questions), size
	 * is the memory allocated for the questions array
	 * 
	 * In order to keep existing functionality which requires linear backwards
	 * navigation regardless of user's actual route, this method returns exact
	 * previous question in the list.
	 * 
	 * replacing linear approach to basic stack implementation (push/pop) would
	 * allow us to navigate based on user's click
	 * 
	 * @author slc
	 * 
	 */
	onBackEvent : function() {
		var questions = this.getQuestions();
		for (var i = 1; i < questions.length; i++) {
			// if it is 0, the question has not been presented and ignored
			if ($(questions[i]).children().length > 0) {
				// check if it is a candidate for current question
				if (this.findAnswers(questions[i]).length == 0) { // current
					// question
					// which is
					// displayed
					// click on the previous answer, we're done!
					this.navigateQuestion(this.getFirstAnswer(questions,
							(i - 1)));
					return;
				}
			}
		}
		this.onPreviousQuestionNotFound();

	},

	/** there are no questions or we reached the first question* */
	onPreviousQuestionNotFound : function() {
		// if the page is allowed to navigate back
		if (canNavigateToPreviousPage()) {
			// override the custom back handling stuff back to normal
			// I assume it is history.back();
			window.onpopstate = function() {
				// restored default function
				window.history.back();

			};
			// now calling back manually to propagate the event again
			window.history.back();
		}

		// Following is the previous behaviour, cycling question navigation
		// nothing found show last answer
		// if we are on first question we back to last one again
		// this.navigateQuestion(this.getFirstAnswer(questions,
		// (questions.length - 1)));
	},

	getQuestions : function() {
		return $('*[id$=\\:questionContainer]');
	},

	findAnswers : function(container) {
		return $(container).find('a[id$=\\:change_answer]');
	},

	/** finds the first answer link resides in the question container */
	getFirstAnswer : function(questions, index) {
		for (var i = index; i >= 0; i--) {
			var answers = this.findAnswers(questions[i]);
			if (answers.length > 0) {
				return answers[0];
			}
		}
		return null;
	},

	/***/
	navigateQuestion : function(changeAnswerLink) {
		if (null != changeAnswerLink) {
			changeAnswerLink.click();
		}
	},

	/** Legacy init function, this needs to be tidied up */
	init : function() {

		if (typeof history.pushState === 'undefined') {
			/**
			 * non HTML5 IE browsers (less than IE 10) do not support the back
			 * operation via html5 push / pop operations, so we show a generic
			 * pop message indicating the same
			 */
			jQuery(document)
					.ready(
							function($) {
								if (BrowserDetect.browser == "Explorer") {
									// For IE different events handled
									window.onbeforeunload = function onBack(evt) {
										evt = window.event || evt;
										if (evt.clientY && evt.clientX > 600) {
											return "";
										} else if ((evt.clientX < 0)
												|| (evt.clientY < 0)
												|| (evt.clientX > document.body.clientWidth)
												|| (evt.clientY > document.body.clientHeight)) {
											return ieIncompatibilityMessage;
										}
										return undefined;
									};
								}
							});
			document.onkeydown = function(event) {
				event = window.event || event;
				if (event.keyCode == 8) {
					if (BrowserDetect.browser == "Explorer"
							&& event.srcElement == document.body) {
						alert(ieIncompatibilityMessage);
						event.returnValue = false;
					}
				}
			};
		}
		if (typeof history.pushState === "function") {
			history.pushState("preamble", null, null);
			window.onpopstate = function() {
				if (isAppPopStateActive || BrowserDetect.browser == "Firefox") {
					history.pushState("newPreamble", null, null);
					SimpleBackHandler.onBackEvent();
				}
				isAppPopStateActive = true;
			};
		}
	}
};

var isAppPopStateActive = false;
window.onload = function() {
	SimpleBackHandler.init();
};

/**
 * Method to avoid displaying pop up if we are navigating away from a page
 * (currently used to display IE incompatibility message
 */
function setNavigateAwayMessageNotRqd() {
	window.onbeforeunload = null;
}

/**
 * Used for back button navigation When the page is reached at first question or
 * there is no more question to navigate back, following default behaviour will
 * decide the rest of the navigation
 * 
 * Override this method in your page or if it is global for behaviour override
 * this method in your template.xhtml
 * 
 */
function canNavigateToPreviousPage() {
	return true;
}

/**
 * Method added setNavigateAwayMessageNotRqd and optional tracking event for
 * Google Analytics for tracking NavBar navigation events.
 * 
 * Add new methods in here or write new implementation in here.
 * 
 * @param source
 * @param track
 */
function preNavigateEvent(source, track) {
	setNavigateAwayMessageNotRqd();
	if (track) {
		trackEventSLC(source, CONFIG.get('NAVBARURL'));
	}
}

/**
 * Method added setNavigateAwayMessageNotRqd and optional tracking event for
 * Google Analytics.
 * 
 * @param source
 * @param track
 */
function preSaveAndExitEvent(source, track) {
	setNavigateAwayMessageNotRqd();
	if (track) {
		trackEventSLC(source, null);
	}
}
