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
package com.rdonasco.common.exceptions;

public class MultipleEntityFoundException extends Exception
{

    private Object entity;

    public MultipleEntityFoundException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    public MultipleEntityFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MultipleEntityFoundException(String message, Throwable cause, Object entity)
    {
        super(message, cause);
        setEntity(entity);
    }

    public MultipleEntityFoundException(String message)
    {
        super(message);
    }

    /**
     * @return the entity
     */
    public Object getEntity()
    {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    private void setEntity(Object entity)
    {
        this.entity = entity;
    }
}
