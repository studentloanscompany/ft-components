
if(_alxPageName != null){
    //Remove JSESSIONID from page URI.
    pageURL = window.location.pathname.replace(new RegExp(";jsessionid=.*?(?=\\?|$)",'g'), "");
    pageURL = pageURL + '/'+_alxPageName;
    _gaq.push(['_trackPageview', '\'' + pageURL +'\'']);
   }else{
    pageURL = window.location.href.replace(new RegExp(";jsessionid=.*?(?=\\?|$)",'g'), "");
    _gaq.push(['_trackPageview', pageURL]);
   }

(function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
  
function trackEvent(opt_label){
	_gaq.push(['_trackPageview', opt_label]);
}

//This lines to be appended
function trackEventPT(category, action, opt_label){
	_gaq.push(['_trackEvent', category, action, opt_label]);
}