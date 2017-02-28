package com.stateshifterlabs.achievementbooks.SA;

import java.util.HashMap;
import java.util.Map;

public class FormattingList {

	private final Map<Integer, Formatting> formattings = new HashMap<Integer, Formatting>();


	public void put(int id, Formatting formatting) {
		formattings.put(id, formatting);
	}

	public Formatting formattingFor(int id) throws NoSuchFormattingException {
		if(!formattings.containsKey(id)) {
			throw new NoSuchFormattingException();
		}

		return formattings.get(id);
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof FormattingList)) {
			return false;
		}

		FormattingList that = (FormattingList) o;


		return formattings == null ? that.formattings == null : formattings.equals(that.formattings);
	}

	@Override
	public final int hashCode() {
		if (formattings == null) {
			return 0;
		}
		return formattings.hashCode();
	}
}
