/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.views;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.vaadin.data.util.BeanItemContainer;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityListContainer extends BeanItemContainer<CapabilityItemVO>
{
	private static final long serialVersionUID = 1L;

	public CapabilityListContainer()
	{
		super(CapabilityItemVO.class);
	}
	public static final Object[] DATA_COLUMNS = new Object[]
	{
		"id", "title", "description", "resource", "actions", "embddedIcon"
	};
	public static final Object[] VISIBLE_COLUMNS = new Object[]
	{
		"embeddedIcon", "title"
	};
	public static final String[] VISIBLE_HEADERS = new String[]
	{
		"",
		I18NResource.localize("Title")
	};
}
