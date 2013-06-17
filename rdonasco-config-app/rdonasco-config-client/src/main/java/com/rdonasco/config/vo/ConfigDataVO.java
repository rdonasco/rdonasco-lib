/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 06-Apr-2013
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
package com.rdonasco.config.vo;

import com.rdonasco.config.data.XPathAttributeHolder;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public interface ConfigDataVO extends Serializable, XPathAttributeHolder
{

	public void setName(String name);

	public String getName();

	public void setValue(String value);

	public String getValue();

	public void setId(Long id);

	public Long getId();

	public ConfigDataVO getParentConfig();

	public void setParentConfig(ConfigDataVO config);
}
