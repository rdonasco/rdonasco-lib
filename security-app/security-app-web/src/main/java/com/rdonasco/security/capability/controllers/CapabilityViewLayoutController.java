/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.capability.views.CapabilityViewLayout;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;


/**
 *
 * @author Roy F. Donasco
 */
@Dependent
public class CapabilityViewLayoutController implements
		ViewController<CapabilityViewLayout>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private CapabilityViewLayout capabilityViewLayout;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private CapabilityListPanelController capabilityListPanelController;
	@Inject
	private CapabilityEditorViewController capabilityEditorViewController;
	@Inject
	private ResourcesEditorAndSelectorViewController resourceEditorController;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			// add other contents here
			capabilityViewLayout.initWidget();
			capabilityViewLayout.setLeftPanelContent(capabilityListPanelController.getControlledView());
			capabilityViewLayout.setCenterPanelContent(capabilityEditorViewController.getControlledView().getEditorForm());
			capabilityViewLayout.addRightPanelContent(resourceEditorController.getControlledView());

			// link the two controllers
			capabilityListPanelController.getControlledView().getDataViewListTable().addListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(Property.ValueChangeEvent event)
				{
					Table table = capabilityListPanelController.getControlledView().getDataViewListTable();
					BeanItem<CapabilityItemVO> item = (BeanItem) table.getItem(table.getValue());
					CapabilityItemVO capability = (CapabilityItemVO) item.getBean();
					if (!capability.getId().equals(CapabilityListPanelController.ADD_CAPABILITY_ID))
					{
						capabilityEditorViewController.setCurrentItem(item);
					}

				}
			});

		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public CapabilityViewLayout getControlledView()
	{
		return capabilityViewLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
