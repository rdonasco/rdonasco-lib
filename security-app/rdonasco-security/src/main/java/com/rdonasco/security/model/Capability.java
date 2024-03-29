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

import com.rdonasco.security.utils.SecurityConstants;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "capability")
@NamedQueries(
		{
	@NamedQuery(name = Capability.NAMED_QUERY_FIND_BY_TITLE,
			query = "SELECT c FROM Capability c WHERE c.title = :title"),
	@NamedQuery(name = Capability.NAMED_QUERY_FIND_BY_RESOURCE_NAME,
			query = "SELECT c FROM Capability c WHERE c.resource.name = :resource_name")
})
public class Capability implements Serializable
{

	public static final String NAMED_QUERY_FIND_BY_RESOURCE_NAME = "findCapabilityByResourceName";

	public static final String NAMED_QUERY_FIND_BY_TITLE = "findCapabilityByTitle";

	public static final String QUERY_PARAM_RESOURCE_NAME = "resource_name";

	public static final String QUERY_PARAM_ACTION = "action";

	public static final String QUERY_PARAM_TITLE = "title";

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "CAPABILITY_IDGEN";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@Basic(optional = false)
	@Column(name = "title", nullable = false, length = 256, unique = true)
	private String title;

	@Basic(optional = true)
	@Column(name = "description", nullable = false, length = 256)
	private String description;

	@JoinColumn(name = "resource_id", referencedColumnName = "id", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Resource resource;
	
	@JoinColumn(name = "application_id", referencedColumnName = "id", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	 
	private Application application;
	// TODO: implement orphan removal to true
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "capability", fetch = FetchType.EAGER)
	private Collection<CapabilityAction> actions;

	// TODO: implement orphan removal to true
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "capability", fetch = FetchType.LAZY)
	private Collection<RoleCapability> rolesWithThisCapability;
	
	// TODO: implement orphan removal to true
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "capability", fetch = FetchType.LAZY)
	private Collection<UserCapability> usersWithThisCapability;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Resource getResource()
	{
		return resource;
	}

	public void setResource(Resource resource)
	{
		this.resource = resource;
	}

	public Collection<CapabilityAction> getActions()
	{
		return actions;
	}

	public Application getApplication()
	{
		return application;
	}

	public void setApplication(Application application)
	{
		this.application = application;
	}	

	public void setActions(Collection<CapabilityAction> actions)
	{
		this.actions = actions;
		for (CapabilityAction action : actions)
		{
			action.setCapability(this);
		}
	}

	public Collection<RoleCapability> getRolesWithThisCapability()
	{
		return rolesWithThisCapability;
	}

	public void setRolesWithThisCapability(
			Collection<RoleCapability> rolesWithThisCapability)
	{
		this.rolesWithThisCapability = rolesWithThisCapability;
	}

	public Collection<UserCapability> getUsersWithThisCapability()
	{
		return usersWithThisCapability;
	}

	public void setUsersWithThisCapability(
			Collection<UserCapability> usersWithThisCapability)
	{
		this.usersWithThisCapability = usersWithThisCapability;
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
		boolean isEqual = true;
		if (!(object instanceof Capability))
		{
			isEqual = false;
		}
		else
		{
			Capability other = (Capability) object;
			if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return "Capability{" + "id=" + id + ", title=" + title + ", description=" + description + '}';
	}
}
