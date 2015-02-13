/**
 * Utility class written for only this project - Not generic.
 * @author Navneet
 */

package com.self.asyncUrlProjectPackage;

import java.util.Comparator;
import java.util.Map.Entry;

class ListSorter implements Comparator<Entry<String,Integer>>
{

	@Override
	public int compare(Entry<String, Integer> entryOne,
			Entry<String, Integer> entryTwo) {
		return (entryOne.getValue().compareTo(entryTwo.getValue()));
	}
	
}
