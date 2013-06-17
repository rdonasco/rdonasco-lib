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
package com.rdonasco.common.data.utils;

import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class CountryData implements Serializable, KeyValuePair
{

    private String countryCode;
    private String countryName;
    private String countryFlag;

    private CountryData()
    {
    }

    public static CountryData createCountryDataUsing(String code, String name, String flag)
    {
        CountryData newCountryData = new CountryData();
        newCountryData.setCountryCode(code);
        newCountryData.setCountryName(name);
        newCountryData.setCountryFlag(flag);
        return newCountryData;
    }

    public String getDisplayString()
    {
        return getCountryName();
    }

    public String getReplacementString()
    {
        return getCountryName();
    }

    public String getCountryFlag()
    {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag)
    {
        this.countryFlag = countryFlag;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public String getKey()
    {
        return this.countryCode;
    }

    public String getValue()
    {
        return this.countryName;
    }

    @Override
    public String toString()
    {
        return new StringBuilder("CountryData{").append("countryCode=").append(countryCode).append(",countryName=").append(countryName).append('}').toString();
    }
}
