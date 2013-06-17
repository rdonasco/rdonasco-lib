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
package com.rdonasco.config.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigXPathException extends Exception
{

    /**
     * Creates a new instance of
     * <code>ConfigXPathException</code> without detail message.
     */
    public ConfigXPathException()
    {
    }

    /**
     * Constructs an instance of
     * <code>ConfigXPathException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConfigXPathException(String msg)
    {
        super(msg);
    }
    
    public ConfigXPathException(Throwable cause)
    {
        super(cause);
    }
}
