/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.vo;


public class ResourceVOBuilder 
{
	private Long id;
	private String name;

	public ResourceVOBuilder()
	{
	}

	public ResourceVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceVOBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public ResourceVO createResourceVO()
	{
		return new ResourceVO(id, name);
	}

}
