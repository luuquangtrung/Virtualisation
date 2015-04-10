package org.mario.RequestGenerator;

public class Customer_Profile {
	double budget;
	Size_Customer size;
	QoS_Importance qos_importance;
	
	public Customer_Profile(){
		initializeCustomerProfile();
	}
	
	public Customer_Profile(double budget, Size_Customer size, QoS_Importance qos_importance){
		this.budget = budget;
		this.qos_importance = qos_importance;
		this.size = size;
	}
	
	private void initializeCustomerProfile(){
		
	}
	
	public void generateRequest(){
		
	}
}
