/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jan-2013
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
package com.rdonasco.security.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "capability_action", catalog = "", schema = "",
uniqueConstraints =
@UniqueConstraint(
		columnNames =
{
	"capability_id", "action_id"
}, name = "unique_capability_action"))
public class CapabilityAction implements Serializable
{

	public static final String NAMED_QUERY_FIND_ACTION_BY_NAME = "findActionByName";
	public static final String QUERY_PARAM_ACTION = "action";
	private static final long serialVersionUID = 1L;
	private static final String GENERATOR_KEY = "ACTION_IDGEN";
	private static final String GENERATOR_TABLE = "SEQUENCE";
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = GENERATOR_TABLE)
	@Column(name = "id", nullable = false)
	private Long id;
	@JoinColumn(name = "capability_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Capability capability;
	@JoinColumn(name = "action_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false, fetch= FetchType.EAGER)
	private Action action;
	

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Capability getCapability()
	{
		return capability;
	}

	public void setCapability(Capability capability)
	{
		this.capability = capability;
	}

	public Action getAction()
	{
		return action;
	}

	public void setAction(Action action)
	{
		this.action = action;
	}
	
	

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CapabilityAction))
		{
			return false;
		}
		CapabilityAction other = (CapabilityAction) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

}
