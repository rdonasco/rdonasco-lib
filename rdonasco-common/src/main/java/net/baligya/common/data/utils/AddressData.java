/*
 *  Copyright 2010 Roy F. Donasco.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */


package net.baligya.common.data.utils;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 *
 * @author  rdonasco
 */
@Embeddable
public class AddressData implements Serializable
{
    
    /** Holds value of property street. */
	@Column(name="street",length=256)
    private String street;
    
    /** Holds value of property city. */
	@Column(name="city",length=128)
    private String city;
    
    /** Holds value of property state. */
	@Column(name="state",length=128)
    private String state;
    
    /** Holds value of property zipCode. */
	@Column(name="zip_code",length=128)
    private String zipCode;
    
    /** Holds value of property country. */
	@Column(name="country",length=2)
    private String country;
    
    /** Holds value of property fieldCount. */
	@Transient
    private int fieldCount = 5;
    
    /**
     * Creates a new instance of AddressData
     */
    public AddressData()
    {
    }
    
    /** Getter for property street.
     * @return Value of property street.
     */
    public String getStreet()
    {
        return this.street;
    }
    
    /** Setter for property street.
     * @param street New value of property street.
     */
    public void setStreet(String street)
    {
        this.street = street;
    }
    
    /** Getter for property city.
     * @return Value of property city.
     */
    public String getCity()
    {
        return this.city;
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(String city)
    {
        this.city = city;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     */
    public String getState()
    {
        return this.state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     */
    public void setState(String state)
    {
        this.state = state;
    }
    
    /** Getter for property zipCode.
     * @return Value of property zipCode.
     */
    public String getZipCode()
    {
        return this.zipCode;
    }
    
    /** Setter for property zipCode.
     * @param zipCode New value of property zipCode.
     */
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }
    
    /** Getter for property country.
     * @return Value of property country.
     */
    public String getCountry()
    {
        return this.country;
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     */
    public void setCountry(String country)
    {
        this.country = country;
    }
    
	@Override
    public String toString()
    {
        return new StringBuilder("[country=").append(getCountry())
			.append(",zipCode=").append(getZipCode())
			.append(",state=").append(getState())
			.append(",city=").append(getCity())
			.append(",street=").append(getStreet())
			.toString();
    }
            
    /** Getter for property fieldCount.
     * @return Value of property fieldCount.
     */
    public int getFieldCount()
    {
        return this.fieldCount;
    }
    
    /** Setter for property fieldCount.
     * @param fieldCount New value of property fieldCount.
     */
    public void setFieldCount(int fieldCount)
    {
        this.fieldCount = fieldCount;
    }
    
    public String getFormatted()
    {
        StringBuilder buffer = new StringBuilder();
        if (getStreet() != null && getStreet().trim().length() > 0)
            buffer.append(getStreet()).append(", ");
        if (getCity() != null && getCity().trim().length() > 0)
            buffer.append(getCity()).append(", ");
        if (getState() != null && getState().trim().length() > 0)
            buffer.append(getState()).append(", ");
        if (getZipCode() != null && getZipCode().trim().length() > 0)
            buffer.append(getZipCode()).append(" ");
        if (getCountry() != null && getCountry().trim().length() > 0)
            buffer.append(getCountry()).append(" ");
        return buffer.toString();
    }

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof AddressData))
		{
			return false;
		}
		AddressData other = (AddressData) object;
		return (hashCode() == other.hashCode());
	}
}
