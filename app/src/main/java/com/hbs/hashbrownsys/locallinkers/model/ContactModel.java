package com.hbs.hashbrownsys.locallinkers.model;
import java.io.Serializable;

public class ContactModel implements Serializable {

	private String contactNumber;
	private String ContactName ;


	public String getContactName()
	{
		return ContactName;
	}
	public void setContactName(String contactName)
	{
		ContactName = contactName;
	}
	public String getContactNumber()
	{
		return contactNumber;
	}
	public void setContactNumber(String contactNumber)
	{
		this.contactNumber = contactNumber;
	}
}