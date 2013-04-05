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
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.utils.TableHelper;
import com.rdonasco.security.capability.views.ActionEditorAndSelectorView;
import com.rdonasco.security.capability.vo.ActionItemVOBuilder;
import com.rdonasco.security.capability.vo.ActionItemVO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.vo.ActionVO;
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
public class ActionEditorAndSelectorViewController implements
		ViewController<ActionEditorAndSelectorView>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private ActionEditorAndSelectorView editorAndSelectorView;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	private DataManagerContainer<ActionItemVO> actionDataContainer = new DataManagerContainer<ActionItemVO>(ActionItemVO.class);
	@Inject
	private CapabilityDataManagerDecorator capabilityManager;
	private Map<Object, Map<Object, TextField>> fieldMap = new HashMap<Object, Map<Object, TextField>>();
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			editorAndSelectorView.initWidget();
			configureDataContainerStrategies();
			configureEditorTableBehavior();
			configureButtonBehavior();
			actionDataContainer.refresh();
		}
		catch (Exception ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public ActionEditorAndSelectorView getControlledView()
	{
		return editorAndSelectorView;
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
		actionDataContainer.setDataRetrieveListStrategy(new DataRetrieveListStrategy<ActionItemVO>()
		{
			@Override
			public List<ActionItemVO> retrieve() throws DataAccessException
			{
				List<ActionVO> actions;
				List<ActionItemVO> actionItems;
				try
				{
					actions = capabilityManager.findAllActions();
					actionItems = new ArrayList<ActionItemVO>(actions.size());
					for (ActionVO action : actions)
					{
						Embedded icon = IconHelper.createDeleteIcon("Delete");
						ActionItemVO actionItem = new ActionItemVOBuilder()
								.setAction(action)
								.setIcon(icon)
								.createActionItemVO();
						setupDeleteIconClickListener(icon, actionItem);
						actionItems.add(actionItem);
					}
				}
				catch (CapabilityManagerException e)
				{
					throw new DataAccessException(e);
				}
				return actionItems;
			}
		});
		actionDataContainer.setDataSaveStrategy(new DataSaveStrategy<ActionItemVO>()
		{
			@Override
			public ActionItemVO save(ActionItemVO dataToSaveAndReturn) throws
					DataAccessException
			{
				ActionVO savedData;
				try
				{
					savedData = capabilityManager.createNewAction(dataToSaveAndReturn.getAction());
					dataToSaveAndReturn.setId(savedData.getId());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
				return dataToSaveAndReturn;
			}
		});
		actionDataContainer.setDataUpdateStrategy(new DataUpdateStrategy<ActionItemVO>()
		{
			@Override
			public void update(ActionItemVO dataToUpdate) throws
					DataAccessException
			{
				try
				{
					capabilityManager.updateAction(dataToUpdate.getAction());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}
		});
		actionDataContainer.setDataDeleteStrategy(new DataDeleteStrategy<ActionItemVO>()
		{
			@Override
			public void delete(ActionItemVO dataToDelete) throws
					DataAccessException
			{
				try
				{
					capabilityManager.removeAction(dataToDelete.getAction());
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}
		});
	}

	private void setupDeleteIconClickListener(Embedded icon,
			final ActionItemVO actionItem)
	{
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				actionDataContainer.removeItem(actionItem);
			}
		});
	}

	private void configureEditorTableBehavior()
	{
		Table editorTable = editorAndSelectorView.getActionEditorTable();
		editorTable.setContainerDataSource(actionDataContainer);
		editorTable.setVisibleColumns(new String[]
		{
			"icon", "name"
		});
		editorTable.setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Name")
		});
		editorTable.setCellStyleGenerator(new Table.CellStyleGenerator()
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
				else if ("name".equals(propertyId))
				{
					style = SecurityDefaultTheme.CSS_FULL_WIDTH;
				}
				return style;
			}
		});

		Table.ColumnGenerator columnGenerator = new Table.ColumnGenerator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source,
					final Object itemId,
					final Object columnId)
			{
				final TextField textField = new TextField();
				ActionItemVO resourceItem = (ActionItemVO) itemId;
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
							BeanItem<ActionItemVO> itemToUpdate = (BeanItem) source.getItem(itemId);
							itemToUpdate.getBean().setName((String) textField.getValue());
							actionDataContainer.updateItem(itemToUpdate.getBean());
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
		editorTable.addGeneratedColumn("name", columnGenerator);
		editorTable.addListener(new ItemClickEvent.ItemClickListener()
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
		TableHelper.setupTable(editorTable);
		editorTable.setDragMode(Table.TableDragMode.MULTIROW);
		editorTable.setMultiSelect(true);
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

	private void configureButtonBehavior()
	{
		editorAndSelectorView.getAddActionButton().addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				addNewActionItemVO();
			}
		});
	}

	private void addNewActionItemVO()
	{
		ActionVO resourceVO = ActionVO.createWithIdNameAndDescription(null, "New Action", "New Action");
		Embedded icon = IconHelper.createDeleteIcon("Delete");
		ActionItemVO resourceItemVO = new ActionItemVOBuilder()
				.setIcon(icon)
				.setAction(resourceVO)
				.createActionItemVO();
		setupDeleteIconClickListener(icon, resourceItemVO);
		BeanItem<ActionItemVO> beanItem = actionDataContainer.addItem(resourceItemVO);
		editorAndSelectorView.getActionEditorTable().setCurrentPageFirstItemId(beanItem.getBean());
		editorAndSelectorView.getActionEditorTable().select(beanItem.getBean());
		TextField field = getFieldFromCache(beanItem.getBean(), "name");
		field.setReadOnly(false);
		field.focus();
		field.selectAll();
	}
}
