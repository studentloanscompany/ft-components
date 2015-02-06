package uk.co.slc.sfd.cp.ft.jsf.dropdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;

@ManagedBean
@ApplicationScoped
public class DropDownBean {

	private static final String DEFAULT_VALUE = "pleaseSelect";	/**
	 * Get list of all countries and create list of SelectItem.
	 * 
	 * @link {@link NewAllCountriesEnum}
	 * @return
	 */
	public List<SelectItem> getCountries() {
		return getSelectItems(NewAllCountriesEnum.getAsList());
	}
	
	/**
	 * Get list of all countries and create list of SelectItem.
	 * remove UK from the list
	 * @return all countries but UK
	 */
	public List<SelectItem> getCountriesNoUK() {
		final int ukIndex = 0;
		List<SelectItem> allCoutryButUK = getSelectItems(NewAllCountriesEnum.getAsList());
		allCoutryButUK.remove(ukIndex);
		return  allCoutryButUK;
	}

	private List<SelectItem> getSelectItems(List<? extends Dropdownable> dropdownableList) {
		return getSelectItems(dropdownableList, false);
	}
	
	/**
	 * @param dropdownableList
	 * @param addDefaultItem
	 * @return
	 */
	private List<SelectItem> getSelectItems(List<? extends Dropdownable> dropdownableList, boolean addDefaultItem) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		
		if (addDefaultItem) {
			selectItems.add(new SelectItem(null, MessageProvider.getMessageValue(DEFAULT_VALUE)));
		}

		for (Dropdownable dropdownable : dropdownableList) {
			SelectItem item = new SelectItem(dropdownable.getKey(), dropdownable.getDropdownValueForLocale(locale));
			selectItems.add(item);
		}

		return selectItems;
	}	
}
