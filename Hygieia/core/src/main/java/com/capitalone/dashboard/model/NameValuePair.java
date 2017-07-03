package com.capitalone.dashboard.model;



public class NameValuePair {

	private String name;
	private long value;

	
	public NameValuePair() {
	}
	
	public NameValuePair(String name, long value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 
	 * @return
	 *     The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *     The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 *     The value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *     The value
	 */
	public void setValue(long value) {
		this.value = value;
	}
}

