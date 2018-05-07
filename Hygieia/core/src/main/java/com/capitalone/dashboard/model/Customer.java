package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
public class Customer extends BaseModel implements Comparable<Customer>{

	@Indexed(name="index_Customer_customerCode")
	private String customerCode;
	private String customerName;
	private boolean enabled;

	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerCode == null) ? 0 : customerCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (customerCode == null) {
			if (other.customerCode != null)
				return false;
		} else if (!customerCode.equals(other.customerCode))
			return false;
		return true;
	}
	@Override
	public int compareTo(Customer arg0) {
		if((arg0.customerCode).compareTo(this.customerCode) > 0){
			return 1;
		}else if ((arg0.customerCode).compareTo(this.customerCode) == 0){
			return 0;
		}
		return -1;
	}
}
