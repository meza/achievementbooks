package com.stateshifterlabs.achievementbooks.data.compatibility.SA;

import java.util.HashMap;
import java.util.Map;

public class FormattingList {

	private Map<Integer, Formatting> formattings = new HashMap<Integer, Formatting>();


	public void put(int id, Formatting formatting) {
		formattings.put(id, formatting);
	}

	public Formatting formattingFor(int id) {
		if(!formattings.containsKey(id)) {
			return formattings.entrySet().iterator().next().getValue();
		}

		return formattings.get(id);
	}

}
