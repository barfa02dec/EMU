package com.capitalone.dashboard.client.story;

import java.util.List;

import com.capitalone.dashboard.model.Defect;
import com.capitalone.dashboard.model.Scope;

/**
 * Interface through which a story data collector object can be implemented.
 *
 * @author kfk884
 *
 */
public interface StoryDataClient {
	/**
	 * Explicitly updates queries for the source system, and initiates the
	 * update to MongoDB from those calls.
	 * @return the number of records updated
	 */
	int updateStoryInformation();
	
	void defectMetricsAggregation(List<Defect> defectsInDB, Scope scopeProject);
}
