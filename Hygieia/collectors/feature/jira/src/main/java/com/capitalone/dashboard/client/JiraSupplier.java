package com.capitalone.dashboard.client;

import com.capitalone.dashboard.util.NewFeatureSettings;

public interface JiraSupplier<T> {
	
	T get(NewFeatureSettings featureSettings);

}
