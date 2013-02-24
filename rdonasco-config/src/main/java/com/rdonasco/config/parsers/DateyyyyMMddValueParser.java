/*
 * Copyright 2012 Roy F. Donasco.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.config.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class DateyyyyMMddValueParser implements ValueParser<Date>
{
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date parse(Class<Date> outputClass, String value)
    {
        Date date = null;
        try
        {
           if(Date.class.equals(outputClass))
           {
               date = FORMAT.parse(value);
           }
            
        }
        catch (ParseException ex)
        {
            Logger.getLogger(DateyyyyMMddValueParser.class.getName()).
                    log(Level.WARNING, ex.getMessage(), ex);
        }
        return date;
    }
    
}
