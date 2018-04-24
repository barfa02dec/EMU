package com.capitalone.dashboard.request;

public class CustomerRequest {

	private String customer_name;
	private String customerCode;
	private String deactivated;
	
	public String getCustomerName() {
		return customer_name;
	}
	public void setCustomerName(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getDeactivate() {
		return deactivated;
	}
	public void setDeactivate(String deactivated) {
		this.deactivated = deactivated;
	}
	
}
