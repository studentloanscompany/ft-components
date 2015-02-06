var SLCNavigationUtils = new SLCNavigation();

jQuery(function($) {
	// Load dialog on click
	$('.progress a').click(function(e) {
		var source = e.target;
		if (!source) {
			source = e.srcElement;
		}
		if (SLCNavigationUtils.stateChanged) {
			$('[id="navBar:continueNext"]').attr('href', source.href);
			$('#navbar-popup').css('width', '35em');
			$('#navbar-popup').modal({
				close : false
			});
			$('#simplemodal-container').css('height', 'auto');
			$('#navbar-popup').css('width', 'auto');
			return false;
		}
	});
});

function SLCNavigation() {
	this.stateChanged = false;
	this.setStateChanged = function(newState) {
		SLCNavigationUtils.stateChanged = newState;
	};
}
