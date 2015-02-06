/**
 * Script to add events to Google analytics
 * 
 * @author slc
 */

var CONFIG = (function(){
	var private = {
		'EXITPATH_NAME'        : 'ExitPath',
		'EXITPATH_DESCRIPTION' : ' Exit Page',
		'NAVBARURL'            : 'Nav Bar',
		'FILE_EXT'             : '.xhtml'
	};

	return {
		get : function(name) {
			return private[name];
		}
	};
})();

/**
 * On every page load it scan elements with class 'help' and search all anchors
 * to attach event 'onclick' to track help link.
 */
jQuery(document).ready(function($) {

	// Get all elements whose className contains 'help'.
	$("[class*=help]").children().each(function() {
		var childNode = $(this);
		childNode.find('a').each(function() {
			$(this).click(function() {
				trackEventSLC($(this).text(), null);
			});

		});
	});
	

	// Get all elements whose className contains 'pdf'.
	$("[class*=pdf]").each(function() {
		$(this).click(function() {
			trackEventSLC($(this).text(), null);
		});

	});
});

/**
 * Get invoked after the ajax response is arrived to get source that raised event
 *  and track in case of condition full filled.
 * 
 * @param data
 * @param value
 * @param condition
 */ 
var callBackComponentEventforTrack = function(data, value, condition) {
	if (data.status == 'complete') {
		if(condition){
			trackEventSLC(value, null);
		}
	}
};

/**
 * Get invoked after the ajax response is arrived to get source that raised event. 
 * 
 * @param data
 */ 
var callBackEventforTrack = function(data) {
	if (data.status == 'complete') {
		trackEventSLC(data.source, null);
	}
};

/**
 * Create URI using component text and add to google analytics track event.
 * 'source' is HTML element and its contains all attributes of element.
 * 
 * @param source
 */
var trackEventSLC = function(source, url) {
	if(url == null) {
		//In case if URL is not provided
		if(getBaseURL() != null && getBaseURL() != 'undefined'){
			category = filter(getBaseURL());
		} 
	} else{
		category = url;
	}
	
	if(source != null){
		//Get target component value.
		if (source.value != null && source.value != 'undefined') {
			action = source.value;
		} else if (source.text != null && source.text != 'undefined') {
			action = source.text;
		} else if (source.title != null && source.title != 'undefined') {
			action = source.title;
		} else if (source.id != null && source.id != 'undefined') {
			action = source.id;
		} else {
			action = source;
		}
		
		label = category + "/" + action;
		//Google analytics tracking event.
		if(trackEvent != 'undefined' && typeof(trackEvent) == 'function'){		
			trackEvent(category, action, label);
		}
	}
};

//  Wrapper function to call new trackEvent for universal analytics 
function trackEventPT(category, action, opt_label){
	trackEvent(category, action, opt_label);
}

/**
 * Return page url and in case if there is 
 * querystring return Exit Path page.
 * 
 * @return baseURL
 */
var getBaseURL = function() {
	baseURL = window.location.pathname;
	//If not Exit path page and querystring
	if ( window.location.href.indexOf(CONFIG.get('EXITPATH_NAME')) < 0) {
		baseURL = getPageName();
	} else {
		// Case of Exit Path page.
		// Get value from QueryString.
		queryString = window.location.search.substring(window.location.search.indexOf('=') + 1);
		baseURL = queryString + CONFIG.get('EXITPATH_DESCRIPTION');
	}
	
	/** 
	 * Remove JSESSIONID from page URI.
	 * 'g' modifier used for global match; continuing mathing after first match. 
	 */
	baseURL = baseURL.replace(new RegExp(";jsessionid=.*?(?=\\?|$)",'g'), "");
	
	return baseURL;
};

/**
 * @return pageName
 */
var getPageName = function(){
	fullURI = window.location.pathname;
	pageNameStr = fullURI.substring(fullURI.lastIndexOf('/') + 1, fullURI.length);
	
	return pageNameStr;
};

/**
 * Remove extensions
 * @param strToFilter
 */
var filter = function(strToFilter){
	return strToFilter.substring(0, strToFilter.indexOf(CONFIG.get('FILE_EXT')));
};