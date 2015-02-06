/**
 * Handle paging on client side. All events in this class triggered from
 * 
 * @link quickAddressSearch.xhtml.
 * 
 * @author slc
 */

var PagedResult = new PagedResult();

function PagedResult() {
	var currentPage = 0;
	var pageSize = 0;
	var totalRecords = 0;
	var parentNodeString = null;

	/**
	 * UI event (@link quickAddressSearch.xhtml#findAddress#onevent). Get
	 * invoked ('Find' button onevent on postcode lookup page) after the ajax
	 * response is arrived and results loaded.
	 * 
	 * @param data,
	 *            defaultPageSize
	 */
	this.init = function(data, defaultPageSize) {
		this.busy(data);
		// Track search
		if (typeof (callBackEventforTrack) == 'function'
				&& callBackEventforTrack != 'undefined') {
			callBackEventforTrack(data);
		}

		currentPage = 1;
		pageSize = defaultPageSize;
		totalRecords = $('#parentPanel ul').children().length;
		parentNodeString = data.source.id.substring(0, data.source.id
				.lastIndexOf(':'));

		if (totalRecords != 0) {
			this.setPagedResultNumberLabel(0, pageSize - 1);
		}
	};

	/**
	 * Show busy cursor until response arrived.
	 * 
	 * @param data
	 */
	this.busy = function(data) {
		if (data.status == "begin") {
			data.source.disabled = true;
			data.source.style.cursor = 'wait';

		} else if (data.status == "complete") {
			data.source.disabled = false;
			data.source.style.cursor = 'default';
		}
	};

	/**
	 * UI event (@link quickAddressSearch.xhtml#nextButton#onclick).
	 * 
	 * Triggered when next click to next set of results.
	 */
	this.next = function() {
		currentPage++;
		if (this.getTotalPages() >= currentPage) {
			this.togglePage(currentPage);
		}
		this.setCurrentPage();
		return false;
	};

	/**
	 * UI event (@link quickAddressSearch.xhtml#prevButton#onclick).
	 * 
	 * Triggered when prev click to previous set of results.
	 */
	this.previous = function() {
		currentPage--;
		if (currentPage > 0) {
			this.togglePage(currentPage);
		}
		this.setCurrentPage();
		return false;
	};

	/**
	 * Calculate start and end elements range and show next set of elements and
	 * hide previous set of elements.
	 * 
	 * @param currentPage
	 */
	this.togglePage = function(currentPage) {
		var first = (currentPage - 1) * pageSize;
		var last = (currentPage * pageSize) - 1;
		$('#parentPanel').show();
		$('#parentPanel ul li').each(function(i, e) {
			if (i >= first && i <= last) {
				$(e).show();
			} else {
				$(e).hide();
			}
		});
		this.setPagedResultNumberLabel(first, last);
	};

	/**
	 * @return total number of pages.
	 */
	this.getTotalPages = function() {
		return Math.ceil(totalRecords / pageSize);
	};

	/**
	 * Reset current page to 1 or total number of pages in case iff counter
	 * exceeds.
	 */
	this.setCurrentPage = function() {
		if (currentPage == 0) {
			currentPage = 1;
		} else if (currentPage > this.getTotalPages()) {
			currentPage = this.getTotalPages();
		}
	};

	/**
	 * Show start and end records of total records in label.
	 * 
	 * @param start
	 *            and end
	 */
	this.setPagedResultNumberLabel = function(start, end) {
		start++;
		end++;

		if (end > totalRecords) {
			end = totalRecords;
		}
		document.getElementById(this.getParentNode()
				+ ":pagedResultNumberLabel").innerHTML = "(" + start + " to "
				+ end + ") of " + totalRecords + " Possible Address shown";
	};

	/**
	 * @return parentNodeString
	 */
	this.getParentNode = function() {
		return parentNodeString;
	};
}