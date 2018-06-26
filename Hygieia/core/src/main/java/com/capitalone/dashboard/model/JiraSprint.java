package com.capitalone.dashboard.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class JiraSprint implements Comparable<JiraSprint> {

		private Long id;
	    private String start;
	    private String end;
	    private String name;
	    private String state;
	    private Boolean closed;
	    private Boolean editable;
	    private String viewBoardsUrl;
	    private String originalSprintData;
	    private SprintData sprintData;
	  
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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

		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}

		public Boolean getClosed() {
			if((closed != null && closed == true)  || (!StringUtils.isEmpty(this.state) && this.state.equalsIgnoreCase("CLOSED")))
				return true;
			else
				return false;
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
		
		public String getOriginalSprintData() {
			return originalSprintData;
		}
		public void setOriginalSprintData(String originalSprintData) {
			this.originalSprintData = originalSprintData;
		}
		
		public SprintData getSprintData() {
			return sprintData;
		}
		public void setSprintData(SprintData sprintData) {
			this.sprintData = sprintData;
		}	
	    
	@Override
	public int compareTo(JiraSprint js) {
		
		if(this.id==js.id){
			return 0;
		}else if (this.id<js.id){
			return 1;
		}else{
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		JiraSprint that = (JiraSprint) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(id, that.id).build();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).toHashCode();
	}
	
	
}
