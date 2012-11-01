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
import com.vaadin.data.Buffered;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.addon.customfield.CustomField;

/**
 * This class can be used to edit a collection containing a pojo or bean. Sample
 * usage {code} Collection<Dummy> collection = new ArrayList<Dummy>(); final
 * CollectionTableEditor<Dummy> tableEditor = new
 * CollectionTableEditor(Dummy.class, collection.getClass());
 * tableEditor.setPropertyDataSource(item.getItemProperty(propertyId)); Field
 * field = tableEditor; // return this as your field {code}
 *
 * @author Roy F. Donasco
 */
@SuppressWarnings("serial")
public class CollectionTableEditor<T> extends CustomField implements
		InvocationHandler
{

	private static final Logger LOG = Logger.getLogger(CollectionTableEditor.class.getName());
	private static final String METHOD_CREATE_FIELD = "createField";
	private Class<T> dataElementType;
	private Class<Collection<T>> collectionType;
	private BeanItemContainer<T> containerDataSource;
	private Button addRowButton = new Button("+");
	private Button deleteRowButton = new Button("x");
	private Property propertyDataSource;
	private TableFieldFactory tableFieldFactory;
	private CollectionTableDecorator decoratedTable;

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
		deleteRowButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				List keys = new ArrayList(getDecoratedTable().getItemIds());
				for (Object object : keys)
				{
					if (getDecoratedTable().isSelected(object))
					{
						containerDataSource.removeItem(object);
					}
				}
			}
		});
		buttonLayout.addComponent(addRowButton);
		buttonLayout.addComponent(deleteRowButton);
		layout.addComponent(buttonLayout);
		layout.addComponent(getPrivateTable());
		containerDataSource = new BeanItemContainer<T>(dataElementType);
		getPrivateTable().setContainerDataSource(containerDataSource);
		configureDefaultFieldFactory();
		setCompositionRoot(layout);
		clearSizes();
	}

	private Table getPrivateTable()
	{
		return getDecoratedTable().getInnerTable();
	}

	private void clearSizes()
	{
		setSizeUndefined();
	}

	@Override
	public Class<?> getType()
	{
		return getDecoratedTable().getType();
	}

	@Override
	public void setValue(Object newValue) throws Property.ReadOnlyException,
			Property.ConversionException
	{
		getDecoratedTable().setValue(newValue);
	}

	@Override
	public Property getPropertyDataSource()
	{
		return getDecoratedTable().getPropertyDataSource();
	}

	@Override
	public void setPropertyDataSource(Property newDataSource)
	{
		this.propertyDataSource = newDataSource;
//		configureContainerAttributes();
		resetValues();
	}

	@Override
	public void commit()
	{
		getDecoratedTable().getInnerTable().commit();
		Collection<T> values = instantiateCollection();
		values.addAll(containerDataSource.getItemIds());
		propertyDataSource.setValue(values);
	}

	@Override
	public void discard() throws Buffered.SourceException
	{
		getDecoratedTable().getInnerTable().discard();
		resetValues();
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		getDecoratedTable().setReadOnly(readOnly);
		getDecoratedTable().setEditable(!readOnly);
		getDecoratedTable().setSelectable(!readOnly);
		if (readOnly)
		{
			ButtonUtil.disableButtons(addRowButton, deleteRowButton);
		}
		else
		{
			ButtonUtil.enableButtons(addRowButton, deleteRowButton);
		}
	}

	private Collection<T> instantiateCollection()
	{
		Collection<T> collection = null;
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
//		getTable().setContainerDataSource(containerDataSource);
//		getTable().setVisibleColumns(fieldProperties);
	}

	private void resetValues()
	{
		Collection<T> values = (Collection<T>) propertyDataSource.getValue();
		if (values == null)
		{
			values = instantiateCollection();
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

	public void setTableFieldFactory(TableFieldFactory fieldFactory)
	{
		this.tableFieldFactory = fieldFactory;
		TableFieldFactory proxiedFactory = (TableFieldFactory) Proxy.newProxyInstance(fieldFactory.getClass().getClassLoader(), fieldFactory.getClass().getInterfaces(), this);
		getDecoratedTable().setTableFieldFactory(proxiedFactory);
	}
	private static final int ARG_ITEM_ID = 1;

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws
			Throwable
	{
		Object result = method.invoke(tableFieldFactory, arguments);
		if (METHOD_CREATE_FIELD.equals(method.getName()) && arguments != null && arguments.length == 4)
		{
			final T item = (T) arguments[ARG_ITEM_ID];
			Field field = (Field) result;
			if (FocusNotifier.class.isAssignableFrom(field.getClass()))
			{
				((FocusNotifier) field).addListener(new FieldEvents.FocusListener()
				{
					@Override
					public void focus(FocusEvent event)
					{
						getDecoratedTable().select(item);
					}
				});
			}
		}

		return result;
	}

	public Button getAddRowButton()
	{
		return addRowButton;
	}

	public Button getDeleteRowButton()
	{
		return deleteRowButton;
	}

	private void configureDefaultFieldFactory()
	{
		setTableFieldFactory(DefaultFieldFactory.get());
	}

	public CollectionTableDecorator getDecoratedTable()
	{
		if(null == decoratedTable)
		{
			decoratedTable = new CollectionTableDecorator();
		}
		return decoratedTable;
		
	}
}
