package ex12Q2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a living being with preferences (e.g. a man, a woman, a dog).
 * @author Yaniv Tzur
 */
public abstract class Individual 
{	
	protected HashMap<String, List<Individual>> _preferenceLists; // The lists of the individual's 
																  // preferences about being matched 
																  // with other types of individuals.
																  // The variable maps between each
																  // other Individual subtype's name
																  // and the respective list.
	protected Individual[] _partners; // The individual's partners.
	
	/**
	 * Initializes the Individual object's preferences' lists and partners' list.
	 */
	public Individual()
	{
		_partners = new Individual[2];
		_preferenceLists = new HashMap<String, List<Individual>>();
	}
	
	/**
	 * Returns whether there is at least one empty preference list for another different type of
	 * individual.
	 * @return true if there is at least one empty preference list for another different type of
	 * 		   individual. Return false otherwise.
	 */
	public boolean arePreferenceListsEmpty()
	{
		List<Individual> preferenceList = null;
		if (_preferenceLists != null)
		{
			if (_preferenceLists.isEmpty())
				return true;
			for (String key : _preferenceLists.keySet())
			{
				preferenceList = _preferenceLists.get(key);
				if (preferenceList == null || preferenceList.isEmpty())
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Iterates over all existing preference lists and removes all of their elements.
	 */
	public void clearPreferenceLists()
	{
		List<Individual> preferenceList = null;
		if(_preferenceLists != null)
			for (String key : _preferenceLists.keySet())
			{
				preferenceList = _preferenceLists.get(key);
				if (preferenceList != null)
					preferenceList.clear();
			}
	}
	
	/**
	 * Adds the input individual to the preference list corresponding to that individual's subtype.
	 * Precondition: Expects the input string key to be the name of the input individual's concrete
	 * 				 subtype.
	 * @param key Expected to be the name of concrete subtype of the input individual.
	 * @param value The individual to be added to the appropriate preference list.
	 */
	public void addToPreferenceList(String key,
									Individual value)
	{
		List<Individual> preferenceList = null;
		if (_preferenceLists != null)
		{
			if (_preferenceLists.containsKey(key) == false)
				_preferenceLists.put(key, new ArrayList<Individual>());
			preferenceList = _preferenceLists.get(key);
			if (preferenceList.contains(value) == false)
				preferenceList.add(value);
		}
	}
	
	/**
	 * Returns the sum of all distances between an existing partner and the most highly preferred partner.
	 * @return The sum of all distances between an existing partner and the most highly preferred partner.
	 */
	public int getDistance()
	{
		int distance = 0;
		List<Individual> preferenceList = null;
		String className = null;
		if (_preferenceLists != null && _partners != null)
			for(Individual partner : _partners)
			{
				className = partner.getClass().getName();
				preferenceList = _preferenceLists.get(className);
				distance += preferenceList.indexOf(partner) + 1;
			}
		return distance;
	}
	
	/**
	 * Sets the input individuals to be the current individual's partners.
	 * @param individual1 The first individual partner.
	 * @param individual2 The second individual partner.
	 */
	public void setPartners(Individual individual1, Individual individual2)
	{
		if(_partners != null)
		{
			if (_partners.length > 0)
			{
				_partners[0] = individual1;
				if (_partners.length > 1)
				{
					_partners[1] = individual2;
				}
			}
		}
	}
}
