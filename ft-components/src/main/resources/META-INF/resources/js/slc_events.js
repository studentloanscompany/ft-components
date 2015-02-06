/**
 * The slc_events.js
 * This context is stateless, i.e recreates cursor on each event
 * however it manipulates some of the elements on DOM tree
 * 
 * @author slc
 */

// main context for SLC namespace
var SLC = new SLCMain();
function SLCMain() {
	// Generic event handler for slc elements
	function SLCEventHandler() {
		this.handleEvent = function (element, callback, event) {
			if (!event) {
				event = window.event;
			}
			return callback.onKeyEvent(element, event);
		};
	};
	
	function SLCOnEnterCallback() {
		this.onKeyEvent = function(element, event) {
			var genericKeyCode = SLC.Utils.getGenericKeyCode(event);
			if (genericKeyCode == SLC.Constants.KEYS.KEY_ENTER) {
				SLC.getElement(element).onclick();
				event.returnValue = false;
				return false;
			}
			return true;
		};
	};
	
	// used for moving focus on next elements
	// based on maxlength attribute of an input field
	// next element is not configurable, basically moves to next input
	function SLCFocusCallback() {
		this.onKeyEvent = function(element, event) {
			var genericKeyCode = SLC.Utils.getGenericKeyCode(event);
			if (SLC.Constants.KEYS.isValidSearchKey(genericKeyCode)) {
				var source = event.target;
			    if (!source) {
				    source = event.srcElement;
				} 
				var focusableElements = element.getElementsByTagName(SLC.Constants.TAGS.INPUT);
				if (source.value.length == source.maxLength && source.maxLength == SLC.Utils.findCarretPosition(source)) {
				    for (var i = 0; i < focusableElements.length; i++) {
				    	if (focusableElements[i] == source) {
				    		var nextIndex = i + 1;
				    		for (var j = nextIndex; j < focusableElements.length; j++) {
				    			if (focusableElements[j].type == 'text') {
				    	    	    focusableElements[j].focus();
				    	    	    if (!document.selection){
										focusableElements[j].select();
									}
				    	    	    return false;
				    	    	 }
				    		}
				    		break;
				    	}  			       
				    }
				}
			}
		};
	}

	// this callback is implemented to handle search events
	function SLCSearchCallback() {
		this.onKeyEvent = function(element, event) {
			var genericKeyCode = SLC.Utils.getGenericKeyCode(event);
			if (SLC.Constants.EVENTS.KYPRS == event.type 
					|| SLC.Constants.EVENTS.KYDWN == event.type) {
				if (genericKeyCode == SLC.Constants.KEYS.KEY_ENTER) {
					this.select(element, event);
					event.returnValue = false;
					return false;
				}
				if (genericKeyCode == SLC.Constants.KEYS.KEY_ESC) {
					// do not propagate escape before key is up.
					// it overrides ie's default behaviour
					return false;
				}
			}
			if (SLC.Constants.EVENTS.KYDWN == event.type) {
				if (SLC.readyToClick) {
				    return this.navigate(element, event);
				}
				return true;
			} else if (SLC.Constants.EVENTS.KYUP == event.type) {
				return this.search(element, event);
			}
		};
		
		this.navigate = function(element, event) {
			var genericKeyCode = SLC.Utils.getGenericKeyCode(event);
			if (genericKeyCode == SLC.Constants.KEYS.KEY_DOWN_ARROW
					|| genericKeyCode == SLC.Constants.KEYS.KEY_UP_ARROW) {
				var currentCursor = SLC.Utils.findCursor(element);
				if (currentCursor) {
					var newCursor = null;
					if (currentCursor.NEW) {
						newCursor = currentCursor;
						currentCursor = null;
					} else {
						newCursor = SLC.Utils.createCursor(currentCursor, 
								genericKeyCode != SLC.Constants.KEYS.KEY_DOWN_ARROW);
					}
					this.onCursorChange(element, currentCursor, newCursor);	
				}
				
				return false;
			}
			
			return true;
		};
		
		this.search = function(element, event) {
			var genericKeyCode = SLC.Utils.getGenericKeyCode(event);
			var overrideQueryLength = false;
			if (genericKeyCode == SLC.Constants.KEYS.KEY_ESC) {
				if (element.value.length > 0) {
					overrideQueryLength = true;	
				}
				element.value = SLC.Constants.TAGS.EMPTY;
			}	
			
			if (SLC.Constants.KEYS.isValidSearchKey(genericKeyCode)) {
				if (!overrideQueryLength && element.initialQueryLength) {
					if (element.value.length > 0 && element.value.length <  element.initialQueryLength) {
						return;
					}
				}
				var actionId = element.id + SLC.Constants.CUSTOM_FOR_SEARCH.DEF_ACTION_SUFFIX;
				var actionElement = SLC.getElement(actionId);
				if (actionElement) {
					SLC.readyToClick = false;
				    var resultTable = SLC.getElement(actionElement.searchResultTable);
			        if(resultTable) {
			            resultTable.className = 'search-result-pending';
			        }
				    
					var clickRequest = function() {
						actionElement.onclick();	
					};
					
					if (SLC.searchTimeOut) {
						clearTimeout(SLC.searchTimeOut);
					}
					SLC.searchTimeOut = setTimeout(clickRequest, 500);
					return false;
				}
			} 
			
			return true;
		};
		
		this.select = function(element, event) {
			if (!SLC.readyToClick) {
				return false;
			}
			var cursor = SLC.Utils.findCursor(element);
			if (cursor && !cursor.NEW) {
				var link = SLC.Utils.findFirstElement(cursor.ROW,
						SLC.Constants.TAGS.ANCHOR);
				if (link) {
					link.onclick();
				}
			}
			// do not propagate arrow keys
			return false;
		};

		this.onCursorChange = function(element, preCursor, postCursor) {
			// cursor logic implemented here i.e changing style of the searched
			// elements
			if (preCursor && postCursor) {
				preCursor.ROW.className = postCursor.ROW.className;
			}
			if (postCursor) {
				postCursor.ROW.className = 'search-result-selected-false-oncursor';//SLC.Constants.CUSTOM_FOR_SEARCH.CURSOR_STYLE_CLASS;
				if (postCursor.ROW.selectedItem) {
					postCursor.ROW.className = 'search-result-selected-true-oncursor';//postCursor.ROW.className + '-selected';
				} 
				postCursor.ROW.scrollIntoView(false);
				if (element) {
					var valueElement = SLC.Utils.findFirstElement(postCursor.ROW, SLC.Constants.TAGS.INPUT);
					if (valueElement) {
						element.value = valueElement.value;
					}
				}
			}
		};
	};

	function SLCUtils() {
		
		this.createCursor = function(currentCursor, downwards) {
			var cursor = null;
			if (currentCursor) {
				var index = currentCursor.INDEX;
				if (downwards) {
					index--;
					if (index < 0) {
						index = currentCursor.ROW_SET.length - 1;
					}
				} else {
					index++;
					if (index == currentCursor.ROW_SET.length) {
						index = 0; // reached the end of set, reset to the
						// first row
					}
				}
				if (currentCursor.ROW_SET[index]) {
					cursor = SLC.CursorFactory.newInstance(currentCursor.ROW_SET, index);
				}
			}
			return cursor;
		};

		this.findCursor = function(element) {
			var rows = this.getTable(element);
			if (rows) {
				for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if (row.cursor) {
						var cursor = SLC.CursorFactory.newInstance(rows, i);
						return cursor;
					}
				}
				if (rows[0]) { // reset cursor to first row
					var cursor = SLC.CursorFactory.newInstance(rows, 0);
					cursor.NEW = true;
					return cursor;
				}
			}
		};
		
		this.findSelectedCursor = function(element) {
			var rows = this.getTable(element);
			var cursor = null;
			if (rows) {
				for (var i = 0; i < rows.length; i++) {
					if (rows[i]) {
						if (i == 0) {
							cursor = SLC.CursorFactory.newInstance(rows, 0);
						}
						if (rows[i].className == SLC.Constants.CUSTOM_FOR_SEARCH.SELECTED_STYLE_CLASS) {
							cursor = SLC.CursorFactory.newInstance(rows, i);
							rows[i].selectedItem = true;
							break;
						}
					}
				}
			}
			return cursor;
		};

		this.getTable = function(element) {
			var rows = null;
			if(element && element.searchResultList){
				var table = SLC.getElement(element.searchResultList);
				if (table) {
					if (table.getElementsByTagName(SLC.Constants.TAGS.LI)) {
						rows = table.getElementsByTagName(SLC.Constants.TAGS.LI);
					}
				    else if (table.getElementsByTagName(SLC.Constants.TAGS.TBODY) ) {
						rows = table.getElementsByTagName(SLC.Constants.TAGS.TBODY)[0].getElementsByTagName(SLC.Constants.TAGS.TR);
					} else if (table.getElementsByTagName(SLC.Constants.TAGS.TR)) {
						rows = table.getElementsByTagName(SLC.Constants.TAGS.TR);
					}	
				}
			}
			return rows;
		};
		
		this.findFirstElement = function(element, tagName) {
			if (element.getElementsByTagName(tagName)) {
				return element.getElementsByTagName(tagName)[0];
			}
			return null;
		};
		
		this.getGenericKeyCode = function(e) {
			return e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
		};
		this.findCarretPosition = function(input) {
			var caretPosition = 0;
			if (document.selection) {
				input.focus();
				var oSel = document.selection.createRange();
				oSel.moveStart('character', -input.value.length);
				caretPosition = oSel.text.length;
			} else if (input.selectionStart || input.selectionStart == '0') {
				caretPosition = input.selectionStart;
			}
			return (caretPosition);
		};
	};
	
	function CursorFactory() {
		
		this.newInstance = function(rowSet, index) {
			return new Cursor(rowSet, index);
		};

		function Cursor(rowSet, index) {
			this.ROW_SET = rowSet;
			this.INDEX = index;
			this.ROW = null;
			this.NEW = false;
			if (this.ROW_SET) {
				for ( var i = 0; i < this.ROW_SET.length; i++) {
					if (this.ROW_SET[i].cursor) {
						// remove previous flag from the row
						this.ROW_SET[i].cursor = false;
						break;
					}
				}

				this.ROW = this.ROW_SET[index];
				this.ROW.cursor = true;
			}
		}
	};
	
	

	function SLCConstants() {
		
		this.EVENTS = new EventType();
		this.TAGS = new Tag();
		this.KEYS = new KeyCode();
		
		this.CUSTOM_FOR_SEARCH = new CustomForSearch();
		
		function KeyCode() {
			// -- KEY CODES -- //
			this.KEY_UP_ARROW     = 38;
			this.KEY_DOWN_ARROW   = 40;
			this.KEY_ENTER        = 13;
			this.KEY_TAB          = 9;
			this.KEY_BACKSPACE    = 8;
			this.KEY_RIGHT_ARROW  = 39;
			this.KEY_LEFT_ARROW   = 37;
			this.KEY_PLUS         = 61;
			this.KEY_PLUS_ADD     = 107;
			this.KEY_MINUS        = 45;
			this.KEY_END          = 35;
			this.KEY_HOME         = 36;
			this.KEY_ESC          = 27;
			this.KEY_CTRL         = 17;
			this.KEY_SHIFT        = 16;
			this.KEY_SPACE        = 32;
			this.KEY_ANY_MATCHING = 49;
			this.KEY_ALL_MATCHING = 50;
			this.KEY_DETAILS      = 51;
			
			this.isValidSearchKey = function(genericKeyCode) {
				return !(genericKeyCode == this.KEY_UP_ARROW
						|| genericKeyCode == this.KEY_DOWN_ARROW
						|| genericKeyCode == this.KEY_LEFT_ARROW
						|| genericKeyCode == this.KEY_RIGHT_ARROW
						|| genericKeyCode == this.KEY_PLUS
						|| genericKeyCode == this.KEY_PLUS_ADD
						|| genericKeyCode == this.KEY_SHIFT
						|| genericKeyCode == this.KEY_CTRL
						|| genericKeyCode == this.KEY_HOME
						|| genericKeyCode == this.KEY_TAB
						|| genericKeyCode == this.KEY_END
						|| genericKeyCode == this.KEY_ENTER 
						|| genericKeyCode == this.KEY_SPACE);
			};
		};
		
		function EventType() {
			this.KYPRS = "keypress";
			this.KYDWN = "keydown";
			this.KYUP  = "keyup";
		};
		
		function Tag() {
			this.INPUT          = "input";
			this.TBODY          = "tbody";
			this.LI             = "li";
			this.TR             = "tr";
			this.TD             = "td";
			this.ANCHOR         = "a";
			this.EMPTY          = "";
			this.DISPLAY_INLINE = "inline";
			this.DISPLAY_NONE   = "none";
		};
		
		function CustomForSearch() {
			this.QUERYBOX             = "query";
			this.SEARCHTABLE          = "searchResultList";
			this.SEARCHTABLECONTAINER = "searchResultTable";
			this.WORKING              = "workingIndicator";
	        this.DEF_ACTION_SUFFIX    = "Action";
	        this.SELECTED_STYLE_CLASS = "search-result-selected-true";
			this.RESPONSE_BEGIN       = "begin";
			this.RESPONSE_SUCCESS     = "success";
		};
	}
	
	this.focus = function(id) {
		if (SLC && SLC.getElement(id)) {
			SLC.getElement(id).focus();	
		}
	};
	
	this.getElement = function(id) {
		if (id) {
			return document.getElementById(id);	
		} else {
			return null;
		}
	};
	
	this.showWorkingIndicator = function(data) {
		if (data.status == SLC.Constants.CUSTOM_FOR_SEARCH.RESPONSE_BEGIN) {
	    	if(SLC.getElement(SLC.Constants.CUSTOM_FOR_SEARCH.WORKING)){
	    		SLC.getElement(SLC.Constants.CUSTOM_FOR_SEARCH.WORKING).style.display = 
					SLC.Constants.TAGS.DISPLAY_INLINE;	
	    	}
			
		} else if (data.status == SLC.Constants.CUSTOM_FOR_SEARCH.RESPONSE_SUCCESS) {
			if(SLC.getElement(SLC.Constants.CUSTOM_FOR_SEARCH.WORKING)){
				SLC.getElement(SLC.Constants.CUSTOM_FOR_SEARCH.WORKING).style.display =  
					SLC.Constants.TAGS.DISPLAY_NONE;	
			} 
		
			SLC.selectDefaultCursor(data.source);
			var resultTable = SLC.getElement(data.source.searchResultTable);
			if(resultTable) {
			    resultTable.className = 'search-result';
			}
			SLC.readyToClick = true;
		} 
	};
	
	this.selectDefaultCursor = function(element) {
		var selectedCursor = SLC.Utils.findSelectedCursor(element);
		if (selectedCursor) {
			SLC.SearchCallback.onCursorChange(null,null, selectedCursor);
		}
	};
	
	this.overrideDocument = function() {
		if (document.addEventListener) {
			document.addEventListener(SLC.Constants.EVENTS.KYDWN, SLC.onKeyPressOverride, false);
			document.addEventListener(SLC.Constants.EVENTS.KYPRS, SLC.onKeyPressOverride, false);
			document.addEventListener(SLC.Constants.EVENTS.KYUP, SLC.onKeyPressOverride, false);

		} else if (document.attachEvent) {
		    document.attachEvent("on" + SLC.Constants.EVENTS.KYDWN, SLC.onKeyPressOverride);
			document.attachEvent("on" + SLC.Constants.EVENTS.KYPRS, SLC.onKeyPressOverride);
			document.attachEvent("on" + SLC.Constants.EVENTS.KYUP, SLC.onKeyPressOverride);
		}
	};
	
	this.onKeyPressOverride = function(e) {
		if (!e) {
	        e = window.event;
		}
		// override default behaviour 
		// while content is loaded with ajax, using backspace on 
		// search input causing page navigation on firefox
		// added following method to override this
		var genericKeyCode = SLC.Utils.getGenericKeyCode(e);
		// do not propagate back space
		if (genericKeyCode == SLC.Constants.KEYS.KEY_BACKSPACE) {
			source = e.target;
			if (!source) {
				source = e.srcElement;
			}
			if (source == document.body) {
				if (e.preventDefault) {
					e.preventDefault();
					e.stopPropagation();	
				}
			} 
	
		} else {
			// anything other than back space should propagate
			return true;
		}
	};
	
	this.prepareSearchBox = function(clientId, initialQueryLength) {
		if (!SLC || null == SLC) {
			SLC = new SLCMain();
		}
		var componentId = clientId + ":" + SLC.Constants.CUSTOM_FOR_SEARCH.QUERYBOX;
		var boxElement = SLC.getElement(componentId);
		var actionId = componentId + SLC.Constants.CUSTOM_FOR_SEARCH.DEF_ACTION_SUFFIX;
		var actionElement = SLC.getElement(actionId);
		boxElement.searchResultList = clientId + ":" + SLC.Constants.CUSTOM_FOR_SEARCH.SEARCHTABLE;
		boxElement.searchResultTable = clientId + ":" + SLC.Constants.CUSTOM_FOR_SEARCH.SEARCHTABLECONTAINER;
		if (actionElement){
        	actionElement.searchResultList = boxElement.searchResultList;
			actionElement.searchResultTable = boxElement.searchResultTable;			
		}
		if (boxElement) {
			boxElement.initialQueryLength = initialQueryLength;
			SLC.selectDefaultCursor(boxElement);
		}
		SLC.focus(componentId);
	};
	
	this.enableDisableContinueOnSelect = function (source, continueButton) {
		if (source) {
			if(SLC.getElement(continueButton)) {
				SLC.getElement(continueButton).disabled = source.selectedIndex == 0; 
			}
		}
	};
	this.FocusCallback = new SLCFocusCallback();
	this.OnEnterCallback = new SLCOnEnterCallback();
	this.SearchCallback = new SLCSearchCallback();
	this.Utils = new SLCUtils();
	this.Constants = new SLCConstants();
	this.Handler = new SLCEventHandler();
	this.CursorFactory = new CursorFactory();
	this.searchTimeOut = null;
	this.readyToClick = true;
}
SLC.overrideDocument();
