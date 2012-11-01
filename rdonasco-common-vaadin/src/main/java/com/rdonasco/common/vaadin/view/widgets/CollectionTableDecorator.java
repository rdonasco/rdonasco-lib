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

import com.vaadin.Application;
import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Container.PropertySetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ReadOnlyStatusChangeEvent;
import com.vaadin.data.Property.ReadOnlyStatusChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable.RepaintRequestListener;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractComponent.ComponentErrorEvent;
import com.vaadin.ui.AbstractComponent.ComponentErrorHandler;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.AbstractSelect.MultiSelectMode;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.FieldFactory;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnReorderListener;
import com.vaadin.ui.Table.ColumnResizeListener;
import com.vaadin.ui.Table.FooterClickListener;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.Table.RowGenerator;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.Table.TableTransferable;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.Window;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Roy F. Donasco
 */
public class CollectionTableDecorator
{
	private Table innerTable;
	public CollectionTableDecorator()
	{
		innerTable = new Table();
	}

	public CollectionTableDecorator(String caption)
	{
		innerTable = new Table(caption);
	}

	public CollectionTableDecorator(String caption, Container dataSource)
	{
		innerTable = new Table(caption,dataSource);
	}

	public Object[] getVisibleColumns()
	{
		return innerTable.getVisibleColumns();
	}

	public void setVisibleColumns(Object[] visibleColumns)
	{
		innerTable.setVisibleColumns(visibleColumns);
	}

	public String[] getColumnHeaders()
	{
		return innerTable.getColumnHeaders();
	}

	public void setColumnHeaders(String[] columnHeaders)
	{
		innerTable.setColumnHeaders(columnHeaders);
	}

	public Resource[] getColumnIcons()
	{
		return innerTable.getColumnIcons();
	}

	public void setColumnIcons(Resource[] columnIcons)
	{
		innerTable.setColumnIcons(columnIcons);
	}

	public String[] getColumnAlignments()
	{
		return innerTable.getColumnAlignments();
	}

	public void setColumnAlignments(String[] columnAlignments)
	{
		innerTable.setColumnAlignments(columnAlignments);
	}

	public void setColumnWidth(Object propertyId, int width)
	{
		innerTable.setColumnWidth(propertyId, width);
	}

	public void setColumnExpandRatio(Object propertyId, float expandRatio)
	{
		innerTable.setColumnExpandRatio(propertyId, expandRatio);
	}

	public float getColumnExpandRatio(Object propertyId)
	{
		return innerTable.getColumnExpandRatio(propertyId);
	}

	public int getColumnWidth(Object propertyId)
	{
		return innerTable.getColumnWidth(propertyId);
	}

	public int getPageLength()
	{
		return innerTable.getPageLength();
	}

	public void setPageLength(int pageLength)
	{
		innerTable.setPageLength(pageLength);
	}

	public void setCacheRate(double cacheRate)
	{
		innerTable.setCacheRate(cacheRate);
	}

	public double getCacheRate()
	{
		return innerTable.getCacheRate();
	}

	public Object getCurrentPageFirstItemId()
	{
		return innerTable.getCurrentPageFirstItemId();
	}

	public void setCurrentPageFirstItemId(Object currentPageFirstItemId)
	{
		innerTable.setCurrentPageFirstItemId(currentPageFirstItemId);
	}

	public Resource getColumnIcon(Object propertyId)
	{
		return innerTable.getColumnIcon(propertyId);
	}

	public void setColumnIcon(Object propertyId, Resource icon)
	{
		innerTable.setColumnIcon(propertyId, icon);
	}

	public String getColumnHeader(Object propertyId)
	{
		return innerTable.getColumnHeader(propertyId);
	}

	public void setColumnHeader(Object propertyId, String header)
	{
		innerTable.setColumnHeader(propertyId, header);
	}

	public String getColumnAlignment(Object propertyId)
	{
		return innerTable.getColumnAlignment(propertyId);
	}

	public void setColumnAlignment(Object propertyId, String alignment)
	{
		innerTable.setColumnAlignment(propertyId, alignment);
	}

	public boolean isColumnCollapsed(Object propertyId)
	{
		return innerTable.isColumnCollapsed(propertyId);
	}

	public void setColumnCollapsed(Object propertyId, boolean collapsed) throws IllegalStateException
	{
		innerTable.setColumnCollapsed(propertyId, collapsed);
	}

	public boolean isColumnCollapsingAllowed()
	{
		return innerTable.isColumnCollapsingAllowed();
	}

	public void setColumnCollapsingAllowed(boolean collapsingAllowed)
	{
		innerTable.setColumnCollapsingAllowed(collapsingAllowed);
	}

	public void setColumnCollapsible(Object propertyId, boolean collapsible)
	{
		innerTable.setColumnCollapsible(propertyId, collapsible);
	}

	public boolean isColumnCollapsible(Object propertyId)
	{
		return innerTable.isColumnCollapsible(propertyId);
	}

	public boolean isColumnReorderingAllowed()
	{
		return innerTable.isColumnReorderingAllowed();
	}

	public void setColumnReorderingAllowed(boolean columnReorderingAllowed)
	{
		innerTable.setColumnReorderingAllowed(columnReorderingAllowed);
	}

	public int getCurrentPageFirstItemIndex()
	{
		return innerTable.getCurrentPageFirstItemIndex();
	}

	public void setCurrentPageFirstItemIndex(int newIndex)
	{
		innerTable.setCurrentPageFirstItemIndex(newIndex);
	}

	public boolean isPageBufferingEnabled()
	{
		return innerTable.isPageBufferingEnabled();
	}

	public void setPageBufferingEnabled(boolean pageBuffering)
	{
		innerTable.setPageBufferingEnabled(pageBuffering);
	}

	public boolean isSelectable()
	{
		return innerTable.isSelectable();
	}

	public void setSelectable(boolean selectable)
	{
		innerTable.setSelectable(selectable);
	}

	public int getColumnHeaderMode()
	{
		return innerTable.getColumnHeaderMode();
	}

	public void setColumnHeaderMode(int columnHeaderMode)
	{
		innerTable.setColumnHeaderMode(columnHeaderMode);
	}

	public void requestRepaint()
	{
		innerTable.requestRepaint();
	}

	public void refreshCurrentPage()
	{
		innerTable.refreshCurrentPage();
	}

	public void setRowHeaderMode(int mode)
	{
		innerTable.setRowHeaderMode(mode);
	}

	public int getRowHeaderMode()
	{
		return innerTable.getRowHeaderMode();
	}

	public Object addItem(Object[] cells, Object itemId) throws UnsupportedOperationException
	{
		return innerTable.addItem(cells, itemId);
	}

	public void refreshRowCache()
	{
		innerTable.refreshRowCache();
	}

	public void setContainerDataSource(Container newDataSource)
	{
		innerTable.setContainerDataSource(newDataSource);
	}

	public void changeVariables(Object source,
			Map<String, Object> variables)
	{
		innerTable.changeVariables(source, variables);
	}

	public void paintContent(PaintTarget target) throws PaintException
	{
		innerTable.paintContent(target);
	}

	public void addActionHandler(Handler actionHandler)
	{
		innerTable.addActionHandler(actionHandler);
	}

	public void removeActionHandler(Handler actionHandler)
	{
		innerTable.removeActionHandler(actionHandler);
	}

	public void removeAllActionHandlers()
	{
		innerTable.removeAllActionHandlers();
	}

	public void valueChange(ValueChangeEvent event)
	{
		innerTable.valueChange(event);
	}

	public void attach()
	{
		innerTable.attach();
	}

	public void detach()
	{
		innerTable.detach();
	}

	public boolean removeAllItems()
	{
		return innerTable.removeAllItems();
	}

	public boolean removeItem(Object itemId)
	{
		return innerTable.removeItem(itemId);
	}

	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException
	{
		return innerTable.removeContainerProperty(propertyId);
	}

	public boolean addContainerProperty(Object propertyId,
			Class<?> type, Object defaultValue) throws UnsupportedOperationException
	{
		return innerTable.addContainerProperty(propertyId, type, defaultValue);
	}

	public boolean addContainerProperty(Object propertyId,
			Class<?> type, Object defaultValue, String columnHeader,
			Resource columnIcon, String columnAlignment) throws UnsupportedOperationException
	{
		return innerTable.addContainerProperty(propertyId, type, defaultValue, columnHeader, columnIcon, columnAlignment);
	}

	public void addGeneratedColumn(Object id, ColumnGenerator generatedColumn)
	{
		innerTable.addGeneratedColumn(id, generatedColumn);
	}

	public ColumnGenerator getColumnGenerator(Object columnId) throws IllegalArgumentException
	{
		return innerTable.getColumnGenerator(columnId);
	}

	public boolean removeGeneratedColumn(Object columnId)
	{
		return innerTable.removeGeneratedColumn(columnId);
	}

	public Collection<?> getVisibleItemIds()
	{
		return innerTable.getVisibleItemIds();
	}

	public void containerItemSetChange(ItemSetChangeEvent event)
	{
		innerTable.containerItemSetChange(event);
	}

	public void containerPropertySetChange(PropertySetChangeEvent event)
	{
		innerTable.containerPropertySetChange(event);
	}

	public void setNewItemsAllowed(boolean allowNewOptions) throws UnsupportedOperationException
	{
		innerTable.setNewItemsAllowed(allowNewOptions);
	}

	public Object nextItemId(Object itemId)
	{
		return innerTable.nextItemId(itemId);
	}

	public Object prevItemId(Object itemId)
	{
		return innerTable.prevItemId(itemId);
	}

	public Object firstItemId()
	{
		return innerTable.firstItemId();
	}

	public Object lastItemId()
	{
		return innerTable.lastItemId();
	}

	public boolean isFirstId(Object itemId)
	{
		return innerTable.isFirstId(itemId);
	}

	public boolean isLastId(Object itemId)
	{
		return innerTable.isLastId(itemId);
	}

	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException
	{
		return innerTable.addItemAfter(previousItemId);
	}

	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException
	{
		return innerTable.addItemAfter(previousItemId, newItemId);
	}

	void setTableFieldFactory(TableFieldFactory fieldFactory)
	{
		innerTable.setTableFieldFactory(fieldFactory);
	}

	public TableFieldFactory getTableFieldFactory()
	{
		return innerTable.getTableFieldFactory();
	}

	public FieldFactory getFieldFactory()
	{
		return innerTable.getFieldFactory();
	}

	public void setFieldFactory(FieldFactory fieldFactory)
	{
		innerTable.setFieldFactory(fieldFactory);
	}

	public boolean isEditable()
	{
		return innerTable.isEditable();
	}

	public void setEditable(boolean editable)
	{
		innerTable.setEditable(editable);
	}

	public void sort(Object[] propertyId, boolean[] ascending) throws UnsupportedOperationException
	{
		innerTable.sort(propertyId, ascending);
	}

	public void sort()
	{
		innerTable.sort();
	}

	public Collection<?> getSortableContainerPropertyIds()
	{
		return innerTable.getSortableContainerPropertyIds();
	}

	public Object getSortContainerPropertyId()
	{
		return innerTable.getSortContainerPropertyId();
	}

	public void setSortContainerPropertyId(Object propertyId)
	{
		innerTable.setSortContainerPropertyId(propertyId);
	}

	public boolean isSortAscending()
	{
		return innerTable.isSortAscending();
	}

	public void setSortAscending(boolean ascending)
	{
		innerTable.setSortAscending(ascending);
	}

	public boolean isSortDisabled()
	{
		return innerTable.isSortDisabled();
	}

	public void setSortDisabled(boolean sortDisabled)
	{
		innerTable.setSortDisabled(sortDisabled);
	}

	public void setLazyLoading(boolean useLazyLoading)
	{
		innerTable.setLazyLoading(useLazyLoading);
	}

	public void setCellStyleGenerator(CellStyleGenerator cellStyleGenerator)
	{
		innerTable.setCellStyleGenerator(cellStyleGenerator);
	}

	public CellStyleGenerator getCellStyleGenerator()
	{
		return innerTable.getCellStyleGenerator();
	}

	public void addListener(ItemClickListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(ItemClickListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void setEnabled(boolean enabled)
	{
		innerTable.setEnabled(enabled);
	}

	public void requestRepaintAll()
	{
		innerTable.requestRepaintAll();
	}

	public void setDragMode(TableDragMode newDragMode)
	{
		innerTable.setDragMode(newDragMode);
	}

	public TableDragMode getDragMode()
	{
		return innerTable.getDragMode();
	}

	public TableTransferable getTransferable(Map<String, Object> rawVariables)
	{
		return innerTable.getTransferable(rawVariables);
	}

	public DropHandler getDropHandler()
	{
		return innerTable.getDropHandler();
	}

	public void setDropHandler(DropHandler dropHandler)
	{
		innerTable.setDropHandler(dropHandler);
	}

	public AbstractSelectTargetDetails translateDropTargetDetails(Map<String, Object> clientVariables)
	{
		return innerTable.translateDropTargetDetails(clientVariables);
	}

	public void setMultiSelectMode(MultiSelectMode mode)
	{
		innerTable.setMultiSelectMode(mode);
	}

	public MultiSelectMode getMultiSelectMode()
	{
		return innerTable.getMultiSelectMode();
	}

	public void addListener(HeaderClickListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(HeaderClickListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void addListener(FooterClickListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(FooterClickListener listener)
	{
		innerTable.removeListener(listener);
	}

	public String getColumnFooter(Object propertyId)
	{
		return innerTable.getColumnFooter(propertyId);
	}

	public void setColumnFooter(Object propertyId, String footer)
	{
		innerTable.setColumnFooter(propertyId, footer);
	}

	public void setFooterVisible(boolean visible)
	{
		innerTable.setFooterVisible(visible);
	}

	public boolean isFooterVisible()
	{
		return innerTable.isFooterVisible();
	}

	public void addListener(ColumnResizeListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(ColumnResizeListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void addListener(ColumnReorderListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(ColumnReorderListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void setItemDescriptionGenerator(ItemDescriptionGenerator generator)
	{
		innerTable.setItemDescriptionGenerator(generator);
	}

	public ItemDescriptionGenerator getItemDescriptionGenerator()
	{
		return innerTable.getItemDescriptionGenerator();
	}

	public void setRowGenerator(RowGenerator generator)
	{
		innerTable.setRowGenerator(generator);
	}

	public RowGenerator getRowGenerator()
	{
		return innerTable.getRowGenerator();
	}

	public void setVisible(boolean visible)
	{
		innerTable.setVisible(visible);
	}

	public void setNewItemHandler(NewItemHandler newItemHandler)
	{
		innerTable.setNewItemHandler(newItemHandler);
	}

	public NewItemHandler getNewItemHandler()
	{
		return innerTable.getNewItemHandler();
	}

	public Class<?> getType()
	{
		return innerTable.getType();
	}

	public Object getValue()
	{
		return innerTable.getValue();
	}

	public void setValue(Object newValue) throws ReadOnlyException,
			ConversionException
	{
		innerTable.setValue(newValue);
	}

	public Item getItem(Object itemId)
	{
		return innerTable.getItem(itemId);
	}

	public Collection<?> getItemIds()
	{
		return innerTable.getItemIds();
	}

	public Collection<?> getContainerPropertyIds()
	{
		return innerTable.getContainerPropertyIds();
	}

	public Class<?> getType(Object propertyId)
	{
		return innerTable.getType(propertyId);
	}

	public int size()
	{
		return innerTable.size();
	}

	public boolean containsId(Object itemId)
	{
		return innerTable.containsId(itemId);
	}

	public Property getContainerProperty(Object itemId, Object propertyId)
	{
		return innerTable.getContainerProperty(itemId, propertyId);
	}

	public Object addItem() throws UnsupportedOperationException
	{
		return innerTable.addItem();
	}

	public Item addItem(Object itemId) throws UnsupportedOperationException
	{
		return innerTable.addItem(itemId);
	}

	public Container getContainerDataSource()
	{
		return innerTable.getContainerDataSource();
	}

	public boolean isMultiSelect()
	{
		return innerTable.isMultiSelect();
	}

	public void setMultiSelect(boolean multiSelect)
	{
		innerTable.setMultiSelect(multiSelect);
	}

	public boolean isNewItemsAllowed()
	{
		return innerTable.isNewItemsAllowed();
	}

	public void setItemCaption(Object itemId, String caption)
	{
		innerTable.setItemCaption(itemId, caption);
	}

	public String getItemCaption(Object itemId)
	{
		return innerTable.getItemCaption(itemId);
	}

	public void setItemIcon(Object itemId, Resource icon)
	{
		innerTable.setItemIcon(itemId, icon);
	}

	public Resource getItemIcon(Object itemId)
	{
		return innerTable.getItemIcon(itemId);
	}

	public void setItemCaptionMode(int mode)
	{
		innerTable.setItemCaptionMode(mode);
	}

	public int getItemCaptionMode()
	{
		return innerTable.getItemCaptionMode();
	}

	public void setItemCaptionPropertyId(Object propertyId)
	{
		innerTable.setItemCaptionPropertyId(propertyId);
	}

	public Object getItemCaptionPropertyId()
	{
		return innerTable.getItemCaptionPropertyId();
	}

	public void setItemIconPropertyId(Object propertyId) throws IllegalArgumentException
	{
		innerTable.setItemIconPropertyId(propertyId);
	}

	public Object getItemIconPropertyId()
	{
		return innerTable.getItemIconPropertyId();
	}

	public boolean isSelected(Object itemId)
	{
		return innerTable.isSelected(itemId);
	}

	public void select(Object itemId)
	{
		innerTable.select(itemId);
	}

	public void unselect(Object itemId)
	{
		innerTable.unselect(itemId);
	}

	public void addListener(PropertySetChangeListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(PropertySetChangeListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void addListener(ItemSetChangeListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(ItemSetChangeListener listener)
	{
		innerTable.removeListener(listener);
	}

	public Collection<?> getListeners(Class<?> eventType)
	{
		return innerTable.getListeners(eventType);
	}

	public void setNullSelectionAllowed(boolean nullSelectionAllowed)
	{
		innerTable.setNullSelectionAllowed(nullSelectionAllowed);
	}

	public boolean isNullSelectionAllowed()
	{
		return innerTable.isNullSelectionAllowed();
	}

	public Object getNullSelectionItemId()
	{
		return innerTable.getNullSelectionItemId();
	}

	public void setNullSelectionItemId(Object nullSelectionItemId)
	{
		innerTable.setNullSelectionItemId(nullSelectionItemId);
	}

	public boolean isReadOnly()
	{
		return innerTable.isReadOnly();
	}

	public void setReadOnly(boolean readOnly)
	{
		innerTable.setReadOnly(readOnly);
	}

	public boolean isInvalidCommitted()
	{
		return innerTable.isInvalidCommitted();
	}

	public void setInvalidCommitted(boolean isCommitted)
	{
		innerTable.setInvalidCommitted(isCommitted);
	}

	public void commit() throws SourceException, InvalidValueException
	{
		innerTable.commit();
	}

	public void discard() throws SourceException
	{
		innerTable.discard();
	}

	public boolean isModified()
	{
		return innerTable.isModified();
	}

	public boolean isWriteThrough()
	{
		return innerTable.isWriteThrough();
	}

	public void setWriteThrough(boolean writeThrough) throws SourceException,
			InvalidValueException
	{
		innerTable.setWriteThrough(writeThrough);
	}

	public boolean isReadThrough()
	{
		return innerTable.isReadThrough();
	}

	public void setReadThrough(boolean readThrough) throws SourceException
	{
		innerTable.setReadThrough(readThrough);
	}

	public String toString()
	{
		return innerTable.toString();
	}

	public Property getPropertyDataSource()
	{
		return innerTable.getPropertyDataSource();
	}

	public void setPropertyDataSource(Property newDataSource)
	{
		innerTable.setPropertyDataSource(newDataSource);
	}

	public void addValidator(Validator validator)
	{
		innerTable.addValidator(validator);
	}

	public Collection<Validator> getValidators()
	{
		return innerTable.getValidators();
	}

	public void removeValidator(Validator validator)
	{
		innerTable.removeValidator(validator);
	}

	public void removeAllValidators()
	{
		innerTable.removeAllValidators();
	}

	public boolean isValid()
	{
		return innerTable.isValid();
	}

	public void validate() throws InvalidValueException
	{
		innerTable.validate();
	}

	public boolean isInvalidAllowed()
	{
		return innerTable.isInvalidAllowed();
	}

	public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException
	{
		innerTable.setInvalidAllowed(invalidAllowed);
	}

	public ErrorMessage getErrorMessage()
	{
		return innerTable.getErrorMessage();
	}

	public void addListener(ValueChangeListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(ValueChangeListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void readOnlyStatusChange(ReadOnlyStatusChangeEvent event)
	{
		innerTable.readOnlyStatusChange(event);
	}

	public void addListener(ReadOnlyStatusChangeListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(ReadOnlyStatusChangeListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void focus()
	{
		innerTable.focus();
	}

	public int getTabIndex()
	{
		return innerTable.getTabIndex();
	}

	public void setTabIndex(int tabIndex)
	{
		innerTable.setTabIndex(tabIndex);
	}

	public boolean isRequired()
	{
		return innerTable.isRequired();
	}

	public void setRequired(boolean required)
	{
		innerTable.setRequired(required);
	}

	public void setRequiredError(String requiredMessage)
	{
		innerTable.setRequiredError(requiredMessage);
	}

	public String getRequiredError()
	{
		return innerTable.getRequiredError();
	}

	public boolean isValidationVisible()
	{
		return innerTable.isValidationVisible();
	}

	public void setValidationVisible(boolean validateAutomatically)
	{
		innerTable.setValidationVisible(validateAutomatically);
	}

	public void setCurrentBufferedSourceException(SourceException currentBufferedSourceException)
	{
		innerTable.setCurrentBufferedSourceException(currentBufferedSourceException);
	}

	public void addShortcutListener(ShortcutListener shortcut)
	{
		innerTable.addShortcutListener(shortcut);
	}

	public void removeShortcutListener(ShortcutListener shortcut)
	{
		innerTable.removeShortcutListener(shortcut);
	}

	public void setDebugId(String id)
	{
		innerTable.setDebugId(id);
	}

	public String getDebugId()
	{
		return innerTable.getDebugId();
	}

	public String getStyle()
	{
		return innerTable.getStyle();
	}

	public void setStyle(String style)
	{
		innerTable.setStyle(style);
	}

	public String getStyleName()
	{
		return innerTable.getStyleName();
	}

	public void setStyleName(String style)
	{
		innerTable.setStyleName(style);
	}

	public void addStyleName(String style)
	{
		innerTable.addStyleName(style);
	}

	public void removeStyleName(String style)
	{
		innerTable.removeStyleName(style);
	}

	public String getCaption()
	{
		return innerTable.getCaption();
	}

	public void setCaption(String caption)
	{
		innerTable.setCaption(caption);
	}

	public Locale getLocale()
	{
		return innerTable.getLocale();
	}

	public void setLocale(Locale locale)
	{
		innerTable.setLocale(locale);
	}

	public Resource getIcon()
	{
		return innerTable.getIcon();
	}

	public void setIcon(Resource icon)
	{
		innerTable.setIcon(icon);
	}

	public boolean isEnabled()
	{
		return innerTable.isEnabled();
	}

	public boolean isImmediate()
	{
		return innerTable.isImmediate();
	}

	public void setImmediate(boolean immediate)
	{
		innerTable.setImmediate(immediate);
	}

	public boolean isVisible()
	{
		return innerTable.isVisible();
	}

	public String getDescription()
	{
		return innerTable.getDescription();
	}

	public void setDescription(String description)
	{
		innerTable.setDescription(description);
	}

	public Component getParent()
	{
		return innerTable.getParent();
	}

	public void setParent(Component parent)
	{
		innerTable.setParent(parent);
	}

	public ErrorMessage getComponentError()
	{
		return innerTable.getComponentError();
	}

	public void setComponentError(ErrorMessage componentError)
	{
		innerTable.setComponentError(componentError);
	}

	public Window getWindow()
	{
		return innerTable.getWindow();
	}

	public Application getApplication()
	{
		return innerTable.getApplication();
	}

	public void requestRepaintRequests()
	{
		innerTable.requestRepaintRequests();
	}

	public void paint(PaintTarget target) throws PaintException
	{
		innerTable.paint(target);
	}

	public void childRequestedRepaint(Collection<RepaintRequestListener> alreadyNotified)
	{
		innerTable.childRequestedRepaint(alreadyNotified);
	}

	public void addListener(RepaintRequestListener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(RepaintRequestListener listener)
	{
		innerTable.removeListener(listener);
	}

	public void addListener(Class<?> eventType, Object target, Method method)
	{
		innerTable.addListener(eventType, target, method);
	}

	public void addListener(Class<?> eventType, Object target, String methodName)
	{
		innerTable.addListener(eventType, target, methodName);
	}

	public void removeListener(Class<?> eventType, Object target)
	{
		innerTable.removeListener(eventType, target);
	}

	public void removeListener(Class<?> eventType, Object target, Method method)
	{
		innerTable.removeListener(eventType, target, method);
	}

	public void removeListener(Class<?> eventType, Object target,
			String methodName)
	{
		innerTable.removeListener(eventType, target, methodName);
	}

	public void addListener(Listener listener)
	{
		innerTable.addListener(listener);
	}

	public void removeListener(Listener listener)
	{
		innerTable.removeListener(listener);
	}

	public void setData(Object data)
	{
		innerTable.setData(data);
	}

	public Object getData()
	{
		return innerTable.getData();
	}

	public float getHeight()
	{
		return innerTable.getHeight();
	}

	public int getHeightUnits()
	{
		return innerTable.getHeightUnits();
	}

	public float getWidth()
	{
		return innerTable.getWidth();
	}

	public int getWidthUnits()
	{
		return innerTable.getWidthUnits();
	}

	public void setHeight(float height)
	{
		innerTable.setHeight(height);
	}

	public void setHeightUnits(int unit)
	{
		innerTable.setHeightUnits(unit);
	}

	public void setHeight(float height, int unit)
	{
		innerTable.setHeight(height, unit);
	}

	public void setSizeFull()
	{
		innerTable.setSizeFull();
	}

	public void setSizeUndefined()
	{
		innerTable.setSizeUndefined();
	}

	public void setWidth(float width)
	{
		innerTable.setWidth(width);
	}

	public void setWidthUnits(int unit)
	{
		innerTable.setWidthUnits(unit);
	}

	public void setWidth(float width, int unit)
	{
		innerTable.setWidth(width, unit);
	}

	public void setWidth(String width)
	{
		innerTable.setWidth(width);
	}

	public void setHeight(String height)
	{
		innerTable.setHeight(height);
	}

	public ComponentErrorHandler getErrorHandler()
	{
		return innerTable.getErrorHandler();
	}

	public void setErrorHandler(ComponentErrorHandler errorHandler)
	{
		innerTable.setErrorHandler(errorHandler);
	}

	public boolean handleError(ComponentErrorEvent error)
	{
		return innerTable.handleError(error);
	}

	public int hashCode()
	{
		return innerTable.hashCode();
	}

	public boolean equals(Object o)
	{
		return innerTable.equals(o);
	}
	
	Table getInnerTable()
	{
		return innerTable;
	}
}
