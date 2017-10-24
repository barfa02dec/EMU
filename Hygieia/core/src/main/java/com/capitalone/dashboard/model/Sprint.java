/*************************DA-BOARD-LICENSE-START*********************************
 * Copyright 2014 CapitalOne, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************DA-BOARD-LICENSE-END*********************************/

package com.capitalone.dashboard.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 
 * @author munisekhar.k
 * 
 */
@Document(collection = "sprint")
public class Sprint extends BaseModel implements java.lang.Comparable<Sprint> {
		
	    private Long sprintId;
	    @Indexed(name="index_Sprint_projectId")
		private String projectId;
	    private String start;
	    private String end;
	    private String name;
	    private Boolean closed;
	    private Boolean editable;
	    private String viewBoardsUrl;
	    private SprintData sprintData;
	    private String projectName;
	    
	  
		
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		public String getProjectId() {
			return projectId;
		}
		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}
		
		public Long getSid() {
			return sprintId;
		}
		public void setSid(Long sid) {
			this.sprintId = sid;
		}
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Boolean getClosed() {
			return closed;
		}
		public void setClosed(Boolean closed) {
			this.closed = closed;
		}
		public Boolean getEditable() {
			return editable;
		}
		public void setEditable(Boolean editable) {
			this.editable = editable;
		}
		public String getViewBoardsUrl() {
			return viewBoardsUrl;
		}
		public void setViewBoardsUrl(String viewBoardsUrl) {
			this.viewBoardsUrl = viewBoardsUrl;
		}
		public SprintData getSprintData() {
			return sprintData;
		}
		public void setSprintData(SprintData sprintData) {
			this.sprintData = sprintData;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Sprint that = (Sprint) o;
			EqualsBuilder builder = new EqualsBuilder();
			return builder.append(sprintId, that.sprintId).build();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(sprintId).toHashCode();
		}
		@Override
		public int compareTo(Sprint arg0) {
			//sort by descending order
			if(arg0.sprintId<this.sprintId){
				return 1;
			}else if (arg0.sprintId==this.sprintId){
				return 0;
			}
			return -1;
		}
	
}
