package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
public class Customer extends BaseModel implements Comparable<Customer>{

	private String customer_name;
	@Indexed(name="index_customer_code")
	private String customerCode;
	private String deactivated;

	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getDeactivated() {
		return deactivated;
	}
	public void setDeactivated(String deactivated) {
		this.deactivated = deactivated;
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
