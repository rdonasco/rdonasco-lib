/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.listeditor.controller.ListEditorViewPanelController;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.views.CapabilityViewLayout;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.ResourceItemVO;
import com.rdonasco.security.capability.vo.ResourceItemVOBuilder;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
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
//	@Inject
//	private ResourcesEditorAndSelectorViewController resourceEditorController;
	@Inject
	private Instance<ResourceEditorController> listEditorViewControllerInstances;
	private ResourceEditorController resourceEditorController;
	@Inject
	private CapabilityDataManagerDecorator capabilityManager;
	@Inject
	private ActionEditorAndSelectorViewController actionEditorController;

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
			configureResourceEditor();
			capabilityViewLayout.addRightPanelContent(getResourceEditorController().getControlledView());
			capabilityViewLayout.addRightPanelContent(actionEditorController.getControlledView());

			// link the two controllers
			capabilityEditorViewController.setActionTableSource(actionEditorController.getControlledView().getActionEditorTable());
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

	private void configureResourceEditor()
	{
		DataManagerContainer resourcesDataContainer = new DataManagerContainer(ResourceItemVO.class);
		getResourceEditorController().setDataContainer(resourcesDataContainer);
		resourcesDataContainer.setDataManager(new DataManager<ResourceItemVO>()
		{
			@Override
			public void deleteData(ResourceItemVO data) throws
					DataAccessException
			{
				try
				{
					capabilityManager.removeResource(data.getResource());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}

			@Override
			public ResourceItemVO loadData(ResourceItemVO data) throws
					DataAccessException
			{
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public List<ResourceItemVO> retrieveAllData() throws
					DataAccessException
			{
				List<ResourceItemVO> resourceItems;
				try
				{
					List<ResourceVO> resources = capabilityManager.findAllResources();
					resourceItems = new ArrayList<ResourceItemVO>(resources.size());
					for (ResourceVO resource : resources)
					{
						resourceItems.add(new ResourceItemVOBuilder()
								.setResource(resource)
								.createResourceItemVO());
					}
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
				return resourceItems;
			}

			@Override
			public ResourceItemVO saveData(ResourceItemVO data) throws
					DataAccessException
			{
				try
				{
					data.setId(capabilityManager.addResource(data.getResource()).getId());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
				return data;
			}

			@Override
			public void updateData(ResourceItemVO data) throws
					DataAccessException
			{
				try
				{
					capabilityManager.updateResource(data.getResource());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}
		});

		getResourceEditorController().initializeControlledViewBehavior();
		getResourceEditorController().getControlledView().getEditorTable().setDragMode(Table.TableDragMode.ROW);
		capabilityEditorViewController.setResourceTableSource(getResourceEditorController().getControlledView().getEditorTable());

	}

	protected ListEditorViewPanelController getResourceEditorController()
	{
		if (null == resourceEditorController)
		{
			resourceEditorController = listEditorViewControllerInstances.get();
		}
		return resourceEditorController;
	}
}
