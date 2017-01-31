package com.prodec.keel.model;

import java.util.List;

public interface FilterListener<T> {
	void setResults(List<T> results);
	void clear();
}
