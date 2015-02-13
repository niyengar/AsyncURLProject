/**
 * Utility class to sort the map in which values are being stored.
 * @author Navneet
 */

package com.self.asyncUrlProjectPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class URLUtility {
	
	public HashMap<String,Integer> sortMapByValue(HashMap<String,Integer>statisticMap)
	{
		List<Entry<String,Integer>> sortList = new ArrayList<Entry<String,Integer>>(statisticMap.entrySet());
		ListSorter ls = new ListSorter();
		Collections.sort(sortList, ls);
		HashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
		for(Entry<String,Integer> e:sortList)
		{
			sortedMap.put(e.getKey(), e.getValue());
		}
		statisticMap = sortedMap;
		System.out.println("Sort List Size: "+sortList.size());
		System.out.println("Results from statistics are -");
		if(sortList.size()>=10)
		{
			for(int i=sortList.size()-1;i>=sortList.size()-10;i--)
			{
				System.out.println("String: "+sortList.get(i).getKey()+" "+"count: "+statisticMap.get(sortList.get(i).getKey()));
			}
		}
		else
		{
			for(int i=sortList.size()-1;i>=0;i--)
			{
				System.out.println("String: "+sortList.get(i).getKey()+" "+"count: "+statisticMap.get(sortList.get(i).getKey()));
			}
		}
		
		return statisticMap;
	}

}
