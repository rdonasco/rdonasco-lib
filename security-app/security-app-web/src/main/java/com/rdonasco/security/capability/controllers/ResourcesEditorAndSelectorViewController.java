/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataDeleteStrategy;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.controller.DataRetrieveListStrategy;
import com.rdonasco.datamanager.controller.DataSaveStrategy;
import com.rdonasco.datamanager.controller.DataUpdateStrategy;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.utils.TableHelper;
import com.rdonasco.security.capability.views.ResourcesEditorAndSelectorView;
import com.rdonasco.security.capability.vo.ResourceItemVO;
import com.rdonasco.security.capability.vo.ResourceItemVOBuilder;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class ResourcesEditorAndSelectorViewController implements
		ViewController<ResourcesEditorAndSelectorView>
{

	private static final long serialVersionUID = 1L;
	private static final String TABLE_PROPERTY_ICON = "icon";
	private static final String PROPERTY_NAME = "name";
	@Inject
	private ResourcesEditorAndSelectorView resourcesEditorAndSelectorView;
	@Inject
	private CapabilityDataManagerDecorator capabilityManager;
	private DataManagerContainer<ResourceItemVO> resourcesDataContainer = new DataManagerContainer<ResourceItemVO>(ResourceItemVO.class);
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopProvider;
	@Inject
	private ApplicationPopupProvider popupProvider;
	private Map<Object, Map<Object, TextField>> fieldMap = new HashMap<Object, Map<Object, TextField>>();

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			resourcesEditorAndSelectorView.initWidget();
			configureDataContainerStrategies();
			configureEditorTableBehavior();
			configureButtonBehavior();
			resourcesDataContainer.refresh();

		}
		catch (Exception ex)
		{
			exceptionPopProvider.popUpErrorException(ex);
		}
	}

	@Override
	public ResourcesEditorAndSelectorView getControlledView()
	{
		return resourcesEditorAndSelectorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void configureDataContainerStrategies()
	{
		resourcesDataContainer.setDataRetrieveListStrategy(new DataRetrieveListStrategy<ResourceItemVO>()
		{
			@Override
			public List<ResourceItemVO> retrieve() throws DataAccessException
			{
				List<ResourceVO> resources;
				List<ResourceItemVO> resourceItems;
				try
				{
					resources = capabilityManager.findAllResources();
					resourceItems = new ArrayList<ResourceItemVO>(resources.size());
					Embedded icon;
					for (ResourceVO resource : resources)
					{
						icon = IconHelper.createDeleteIcon("Delete");
						final ResourceItemVO resourceItemVO = new ResourceItemVOBuilder()
								.setIcon(icon)
								.setResource(resource)
								.createResourceItemVO();
						setupDeleteIconClickListener(icon, resourceItemVO);
						resourceItems.add(resourceItemVO);
					}
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
				return resourceItems;
			}
		});
		resourcesDataContainer.setDataSaveStrategy(new DataSaveStrategy<ResourceItemVO>()
		{
			@Override
			public ResourceItemVO save(ResourceItemVO dataToSaveAndReturn)
					throws
					DataAccessException
			{
				ResourceVO savedData;
				try
				{
					savedData = capabilityManager.addResource(dataToSaveAndReturn.getResource());
					dataToSaveAndReturn.setId(savedData.getId());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
				return dataToSaveAndReturn;
			}
		});
		resourcesDataContainer.setDataUpdateStrategy(new DataUpdateStrategy<ResourceItemVO>()
		{
			@Override
			public void update(ResourceItemVO dataToUpdate) throws
					DataAccessException
			{
				try
				{
					capabilityManager.updateResource(dataToUpdate.getResource());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}
		});
		resourcesDataContainer.setDataDeleteStrategy(new DataDeleteStrategy<ResourceItemVO>()
		{
			@Override
			public void delete(ResourceItemVO dataToDelete) throws
					DataAccessException
			{
				try
				{
					capabilityManager.removeResource(dataToDelete.getResource());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}
		});
	}

	private void addFieldToFieldCache(Object itemId, Object columnId,
			TextField textField)
	{
		Map<Object, TextField> subFieldMap = fieldMap.get(itemId);
		if (null == subFieldMap)
		{
			subFieldMap = new HashMap<Object, TextField>();
			fieldMap.put(itemId, subFieldMap);
		}
		subFieldMap.put(columnId, textField);
	}

	private TextField getFieldFromCache(Object itemId, Object propertyId)
	{
		Map<Object, TextField> subFieldMap = fieldMap.get(itemId);
		TextField textField = subFieldMap.get(propertyId);
		return textField;
	}

	private void setupDeleteIconClickListener(Embedded icon,
			final ResourceItemVO resourceItemVO)
	{
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					resourcesDataContainer.removeItem(resourceItemVO);
				}
				catch (RuntimeException e)
				{
					popupProvider.popUpError(I18NResource.localize("Error deleting an item"));
				}
			}
		});
	}

	private void addNewResourceItemVO()
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.setName(I18NResource.localize("New Resource"))
				.setDescription(I18NResource.localize("New Resource"))
				.createResourceVO();
		Embedded icon = IconHelper.createDeleteIcon("Delete");
		ResourceItemVO resourceItemVO = new ResourceItemVOBuilder()
				.setIcon(icon)
				.setResource(resourceVO)
				.createResourceItemVO();
		setupDeleteIconClickListener(icon, resourceItemVO);
		BeanItem<ResourceItemVO> beanItem = resourcesDataContainer.addItem(resourceItemVO);
		resourcesEditorAndSelectorView.getResourceEditorTable().setCurrentPageFirstItemId(beanItem.getBean());
		resourcesEditorAndSelectorView.getResourceEditorTable().select(beanItem.getBean());
		TextField field = getFieldFromCache(beanItem.getBean(), PROPERTY_NAME);
		field.setReadOnly(false);
		field.focus();
		field.selectAll();
	}

	private void configureEditorTableBehavior()
	{
		resourcesEditorAndSelectorView.getResourceEditorTable().setContainerDataSource(resourcesDataContainer);
		resourcesEditorAndSelectorView.getResourceEditorTable().setVisibleColumns(new String[]
		{
			TABLE_PROPERTY_ICON, PROPERTY_NAME
		});
		resourcesEditorAndSelectorView.getResourceEditorTable().setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Name")
		});
		resourcesEditorAndSelectorView.getResourceEditorTable()
				.setCellStyleGenerator(new Table.CellStyleGenerator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Object itemId, Object propertyId)
			{
				String style = null;
				if ("icon".equals(propertyId))
				{
					style = SecurityDefaultTheme.CSS_ICON_IN_A_CELL;
				}
				else if (PROPERTY_NAME.equals(propertyId))
				{
					style = SecurityDefaultTheme.CSS_FULL_WIDTH;
				}
				return style;
			}
		});
		resourcesEditorAndSelectorView.getResourceEditorTable().setReadOnly(false);

		Table.ColumnGenerator columnGenerator = new Table.ColumnGenerator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source,
					final Object itemId,
					final Object columnId)
			{
				final TextField textField = new TextField();
				ResourceItemVO resourceItem = (ResourceItemVO) itemId;
				textField.setValue(resourceItem.getName());
				textField.setReadOnly(true);
				textField.setWriteThrough(true);
				addFieldToFieldCache(itemId, columnId, textField);
				textField.addListener(new FieldEvents.BlurListener()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(FieldEvents.BlurEvent event)
					{
						try
						{
							BeanItem<ResourceItemVO> itemToUpdate = (BeanItem) source.getItem(itemId);
							itemToUpdate.getBean().setName((String) textField.getValue());
							resourcesDataContainer.updateItem(itemToUpdate.getBean());
							textField.setReadOnly(true);
						}
						catch (DataAccessException ex)
						{
							exceptionPopProvider.popUpErrorException(ex);
						}
					}
				});
				return textField;
			}
		};
		resourcesEditorAndSelectorView.getResourceEditorTable().addGeneratedColumn(PROPERTY_NAME, columnGenerator);
		resourcesEditorAndSelectorView.getResourceEditorTable().addListener(new ItemClickEvent.ItemClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event)
			{
				if (event.isDoubleClick())
				{
					TextField textField = getFieldFromCache(event.getItemId(), event.getPropertyId());
					textField.setReadOnly(false);
					textField.focus();
				}
			}
		});
		TableHelper.setupTable(resourcesEditorAndSelectorView.getResourceEditorTable());
		resourcesEditorAndSelectorView.getResourceEditorTable().setDragMode(Table.TableDragMode.ROW);
	}

	private void configureButtonBehavior()
	{
		resourcesEditorAndSelectorView.getAddResourceButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				addNewResourceItemVO();
			}
		});
	}
}
