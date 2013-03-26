/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.views;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.vo.CapabilityVO;
import com.vaadin.data.util.BeanItemContainer;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityListContainer extends BeanItemContainer<CapabilityVO>
{
	private static final long serialVersionUID = 1L;

	public CapabilityListContainer()
	{
		super(CapabilityVO.class);
	}
	public static final Object[] DATA_COLUMNS = new Object[]
	{
		"id", "title", "description", "resource", "actions"
	};
	public static final Object[] VISIBLE_COLUMNS = new Object[]
	{
		"title"
	};
	public static final String[] VISIBLE_HEADERS = new String[]
	{
		I18NResource.localize("Title")
	};
}
