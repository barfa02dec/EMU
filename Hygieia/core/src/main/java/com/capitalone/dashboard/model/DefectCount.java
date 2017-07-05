package com.capitalone.dashboard.model;


import java.util.ArrayList;
import java.util.List;

public class DefectCount
{
   private Long total =  0L;
   private List<NameValuePair> severity = new ArrayList<NameValuePair>();

    public Long getTotal ()
    {
        return total;
    }

    public void setTotal (Long total)
    {
        this.total = total;
    }

	public List<NameValuePair> getSeverity() {
		return severity;
	}

	public void setSeverity(List<NameValuePair> severity) {
		this.severity = severity;
	}

}