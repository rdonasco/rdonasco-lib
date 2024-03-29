/*
 * Copyright 2012 Roy F. Donasco <rdonasco2@yahoo.com>.
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
package com.rdonasco.config.data;

import com.rdonasco.config.data.XPathAttributeHolder;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco <rdonasco2@yahoo.com>
 */
public interface ConfigData extends Serializable, XPathAttributeHolder
{
    public void setName(String name);
    public String getName();
    public void setValue(String value);
    public String getValue();    
    public void setId(Long id);
    public Long getId();
    public ConfigData getParentConfig();
    public void setParentConfig(ConfigData config);
}
