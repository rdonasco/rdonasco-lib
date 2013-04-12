/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 11-Apr-2013
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
package com.rdonasco.datamanager.listeditor.controller;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.view.terminal.StreamResourceBuilder;
import com.rdonasco.datamanager.controller.DataDeleteStrategy;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.controller.DataRetrieveListStrategy;
import com.rdonasco.datamanager.controller.DataSaveStrategy;
import com.rdonasco.datamanager.controller.DataUpdateStrategy;
import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.datamanager.listeditor.view.ListEditorTheme;
import com.rdonasco.datamanager.listeditor.view.ListEditorView;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.datamanager.utils.TableHelper;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class ListEditorViewPanelController<VO extends ListEditorItem> implements
		ViewController<ListEditorView>
{

	private static final Logger LOG = Logger.getLogger(ListEditorViewPanelController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private ListEditorView editorViewPanel;
	private static final String TABLE_PROPERTY_ICON = "icon";
//	private static final String PROPERTY_NAME = "name";
	private DataManager<VO> dataManager;
	private DataManagerContainer<VO> dataContainer;
	@Inject
	private Instance<ApplicationExceptionPopupProvider> exceptionPopupProviderInstances;
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private Instance<ApplicationPopupProvider> popupProviderFactory;
	private ApplicationPopupProvider popupProvider;
	private Map<Object, Map<Object, TextField>> fieldMap = new HashMap<Object, Map<Object, TextField>>();
	private String itemListDescription = I18NResource.localize("List Editor Items");
	private String[] visibleColumns;
	private String[] columnHeaders;

	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			editorViewPanel.initWidget();
			setupEditorViewListeners();
			configureDataContainerStrategies();
			configureEditorTableBehavior();
		}
		catch (WidgetInitalizeException ex)
		{
			getExceptionPopupProvider().popUpErrorException(ex);
		}
	}

	@Override
	public ListEditorView getControlledView()
	{
		return editorViewPanel;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		try
		{
			dataContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			throw new WidgetException(ex);
		}
	}

	public String getItemListDescription()
	{
		return itemListDescription;
	}

	public void setItemListDescription(String itemListDescription)
	{
		this.itemListDescription = itemListDescription;
	}


	private void configureDataContainerStrategies()
	{
		dataContainer.setDataRetrieveListStrategy(new DataRetrieveListStrategy<VO>()
		{
			@Override
			public List<VO> retrieve() throws DataAccessException
			{
				List<VO> listItems;
				listItems = dataManager.retrieveAllData();
				Embedded icon;
				for (VO listItem : listItems)
				{
					icon = new Embedded(null, new StreamResourceBuilder()
							.setReferenceClass(this.getClass())
							.setRelativeResourcePath("images/delete.png")
							.setApplication(editorViewPanel.getApplication())
							.createStreamResource());
					icon.setDescription(I18NResource.localize("Delete Item"));
					listItem.setIcon(icon);
					setupDeleteIconClickListener(icon, listItem);
				}
				return listItems;
			}
		});
		dataContainer.setDataSaveStrategy(new DataSaveStrategy<VO>()
		{
			@Override
			public VO save(VO dataToSaveAndReturn)
					throws
					DataAccessException
			{
				return dataManager.saveData(dataToSaveAndReturn);
			}
		});
		dataContainer.setDataUpdateStrategy(new DataUpdateStrategy<VO>()
		{
			@Override
			public void update(VO dataToUpdate) throws
					DataAccessException
			{
				dataManager.updateData(dataToUpdate);
			}
		});
		dataContainer.setDataDeleteStrategy(new DataDeleteStrategy<VO>()
		{
			@Override
			public void delete(VO dataToDelete) throws
					DataAccessException
			{
				dataManager.deleteData(dataToDelete);
			}
		});
	}

	private void setupDeleteIconClickListener(Embedded icon,
			final VO itemVO)
	{
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					dataContainer.removeItem(itemVO);
				}
				catch (RuntimeException ex)
				{
					LOG.log(Level.WARNING, ex.getMessage(), ex);
					getPopupProvider().popUpError(I18NResource.localize("Error deleting an item"));
				}
			}
		});
	}

	private void setupEditorViewListeners()
	{
		editorViewPanel.addListener(new ComponentContainer.ComponentAttachListener()
		{
			@Override
			public void componentAttachedToContainer(
					ComponentContainer.ComponentAttachEvent event)
			{
				try
				{
					refreshView();
				}
				catch (WidgetException ex)
				{
					getExceptionPopupProvider().popUpErrorException(ex);
				}
			}
		});
	}

	private void configureEditorTableBehavior()
	{
		if (null == getVisibleColumns())
		{
			throw new IllegalStateException("visibleColumns not yet set");
		}
		if (null == getColumnHeaders())
		{
			throw new IllegalStateException("columnHeaders not yet set");
		}
		editorViewPanel.getEditorTable().setContainerDataSource(dataContainer);
		List<String> realVisibleColumns = new ArrayList<String>();
		realVisibleColumns.add(TABLE_PROPERTY_ICON);
		realVisibleColumns.addAll(Arrays.asList(getVisibleColumns()));
		editorViewPanel.getEditorTable().setVisibleColumns(realVisibleColumns.toArray(new String[0]));
		List<String> realColumnHeaders = new ArrayList<String>();
		realColumnHeaders.add(""); // empty header for the icon
		realColumnHeaders.addAll(Arrays.asList(getColumnHeaders()));
		editorViewPanel.getEditorTable().setColumnHeaders(realColumnHeaders.toArray(new String[0]));
		setupDefaultCellStyleGenerator();
		editorViewPanel.getEditorTable().setReadOnly(false);

		Table.ColumnGenerator columnGenerator = new Table.ColumnGenerator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source,
					final Object itemId,
					final Object columnId)
			{
				final TextField textField = new TextField();
				try
				{
					textField.setPropertyDataSource(source.getContainerProperty(itemId, columnId));
				}
				catch (Exception ex)
				{
					LOG.log(Level.SEVERE, ex.getMessage(), ex);
				}
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
							BeanItem<VO> itemToUpdate = (BeanItem) source.getItem(itemId);
							textField.commit();
							dataContainer.updateItem(itemToUpdate.getBean());
							textField.setReadOnly(true);
						}
						catch (DataAccessException ex)
						{
							getExceptionPopupProvider().popUpErrorException(ex);
						}
					}
				});
				return textField;
			}
		};
		for (String propertyName : getVisibleColumns())
		{
			editorViewPanel.getEditorTable().addGeneratedColumn(propertyName, columnGenerator);
		}
		editorViewPanel.getEditorTable().addListener(new ItemClickEvent.ItemClickListener()
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
		TableHelper.setupTable(editorViewPanel.getEditorTable());
		editorViewPanel.getEditorTable().setDragMode(Table.TableDragMode.ROW);
	}

	public String[] getColumnHeaders()
	{
		return columnHeaders;
	}

	public void setColumnHeaders(String[] columnHeaders)
	{
		this.columnHeaders = columnHeaders;
	}

	public String[] getVisibleColumns()
	{
		return visibleColumns;
	}

	public void setVisibleColumns(String[] visibleColumns)
	{
		this.visibleColumns = visibleColumns;
	}

	private void setupDefaultCellStyleGenerator()
	{
		editorViewPanel.getEditorTable()
				.setCellStyleGenerator(new Table.CellStyleGenerator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Object itemId, Object propertyId)
			{
				String style;
				if (TABLE_PROPERTY_ICON.equals(propertyId))
				{
					style = ListEditorTheme.CSS_ICON_IN_A_CELL;
				}
				else
				{
					style = ListEditorTheme.CSS_FULL_WIDTH;
				}
				return style;
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

	protected ApplicationExceptionPopupProvider getExceptionPopupProvider()
	{
		if (exceptionPopupProvider == null)
		{
			exceptionPopupProvider = exceptionPopupProviderInstances.get();
		}
		return exceptionPopupProvider;
	}

	protected ApplicationPopupProvider getPopupProvider()
	{
		if (popupProvider == null)
		{
			popupProvider = popupProviderFactory.get();
		}
		return popupProvider;
	}
}
