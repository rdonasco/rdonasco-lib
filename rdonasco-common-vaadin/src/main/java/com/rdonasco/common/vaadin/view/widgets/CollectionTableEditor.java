/*
 * Copyright 2012 Roy F. Donasco.
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
package com.rdonasco.common.vaadin.view.widgets;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.addon.customfield.CustomField;

/**
 *
 * @author Roy F. Donasco
 */
@SuppressWarnings("serial")
public class CollectionTableEditor<T> extends CustomField
{

	private static final Logger LOG = Logger.getLogger(CollectionTableEditor.class.getName());
	private Class<T> dataElementType;
	private Class<Collection<T>> collectionType;
	private Table table = new Table();
	private BeanItemContainer<T> containerDataSource;
	private Button addRowButton = new Button("+");
	private Button deleteRowButton = new Button("x");
	private Property propertyDataSource;

	public CollectionTableEditor(Class<T> dataElementType,
			Class<Collection<T>> collectionType)
	{
		this.collectionType = collectionType;
		this.dataElementType = dataElementType;
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		addRowButton.setDescription(I18NResource.localize("Add new row to table"));
		deleteRowButton.setDescription(I18NResource.localize("delete row from table"));
		addRowButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				T newData = instantiateDataElement();
				containerDataSource.addItem(newData);
				
			}
		});
		deleteRowButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event)
			{
				List keys = new ArrayList(table.getItemIds());
				for(Object object : keys)
				{
					if(table.isSelected(object))
					{
						containerDataSource.removeItem(object);
					}
				}
			}
		});
		buttonLayout.addComponent(addRowButton);
		buttonLayout.addComponent(deleteRowButton);
		layout.addComponent(buttonLayout);
		layout.addComponent(table);
		table.setHeight(150f, UNITS_PIXELS);
		containerDataSource = new BeanItemContainer<T>(dataElementType);
		configureTableFieldFactory();
		setCompositionRoot(layout);
	}

	@Override
	public Class<?> getType()
	{
		return table.getType();
	}

	public Table getTable()
	{
		return table;
	}

	@Override
	public void setValue(Object newValue) throws ReadOnlyException,
			ConversionException
	{
		table.setValue(newValue);
	}

	@Override
	public Property getPropertyDataSource()
	{
		return table.getPropertyDataSource();
	}

	@Override
	public void setPropertyDataSource(Property newDataSource)
	{
		this.propertyDataSource = newDataSource;
		configureContainerAttributes();
		resetValues();
	}

	@Override
	public void commit()
	{
		Collection<T> values = instantiateCollection();
		values.addAll(containerDataSource.getItemIds());
		propertyDataSource.setValue(values);
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		table.setReadOnly(readOnly);
		table.setEditable(!readOnly);
		table.setSelectable(!readOnly);
		if(readOnly)
		{
			ButtonUtil.disableButtons(addRowButton,deleteRowButton);
		}
		else
		{
			ButtonUtil.enableButtons(addRowButton,deleteRowButton);
		}
	}

	private Collection instantiateCollection()
	{
		Collection collection = null;
		try
		{
			collection = collectionType.newInstance();
		}
		catch (InstantiationException ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
		catch (IllegalAccessException ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return collection;
	}

	private void configureContainerAttributes()
	{
		String fieldProperties[];
		BeanItem<T> beanItem = new BeanItem<T>(instantiateDataElement());
		Collection propertyCollection = beanItem.getItemPropertyIds();
		fieldProperties = new String[propertyCollection.size()];
		Object properties[] = propertyCollection.toArray();
		for (int i = 0; i < properties.length; i++)
		{
			fieldProperties[i] = properties[i].toString();
		}
		table.setContainerDataSource(containerDataSource);
		table.setVisibleColumns(fieldProperties);
	}

	private void resetValues()
	{
		Collection<T> values = (Collection<T>) propertyDataSource.getValue();
		if (values == null)
		{
			values = new ArrayList<T>();
		}
		containerDataSource.removeAllItems();
		containerDataSource.addAll(values);
	}

	private T instantiateDataElement()
	{
		T dataElement = null;
		try
		{
			dataElement = dataElementType.newInstance();
		}
		catch (InstantiationException ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
		catch (IllegalAccessException ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return dataElement;
	}

	private void configureTableFieldFactory()
	{
		table.setTableFieldFactory(new DefaultFieldFactory()
		{
			@Override
			public Field createField(final Item item, Object propertyId,
					Component uiContext)
			{
				Field field = super.createField(item, propertyId, uiContext);
				if (TextField.class.isAssignableFrom(field.getClass()))
				{
					((TextField) field).addListener(new FieldEvents.FocusListener()
					{
						@Override
						public void focus(FocusEvent event)
						{
							table.select(item);
						}
					});
				}
				return field;
			}

			@Override
			public Field createField(Container container, final Object itemId,
					Object propertyId, Component uiContext)
			{
				Field field = super.createField(container, itemId, propertyId, uiContext);
				if (TextField.class.isAssignableFrom(field.getClass()))
				{
					((TextField) field).addListener(new FieldEvents.FocusListener()
					{
						@Override
						public void focus(FocusEvent event)
						{
							table.select(itemId);
						}
					});
				}
				return field;
			}
		});
	}
}
