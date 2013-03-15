/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.config.view;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.config.data.ConfigData;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.common.vaadin.view.NotificationFactory;
import com.rdonasco.datamanager.utils.CommonConstants;
import com.rdonasco.common.i18.I18NResource;

/**
 *
 * @author rdonasco
 */
public class ConfigDataView extends VerticalLayout implements ControlledView
{
	private static final long serialVersionUID = 1L;

    private TreeTable configTreeTable;
    @Inject
    private ConfigDataContainer configDataContainer;
    private Field currentlyEditedField;
    private static final Action ACTION_ADD_ELEMENT = new Action(I18NResource.
            localize("Add new element"));
    private static final Action ACTION_ADD_SUB_ELEMENT = new Action(I18NResource.
            localize("Add new sub-element"));
    private static final Action ACTION_ADD_ATTRIBUTE = new Action(I18NResource.
            localize("Add new attribute"));
    private static final Action ACTION_DELETE_ITEM = new Action(I18NResource.
            localize("Delete"));
    private static final Action ACTION_EXPAND_ALL = new Action(I18NResource.
            localize("Expand all"));
    private static final Action ACTION_COLLAPSE_ALL = new Action(I18NResource.
            localize("Collapse all"));
    private static final Action[] ACTIONS_ON_ROOT_ELEMENT = new Action[]
    {
        ACTION_EXPAND_ALL, ACTION_COLLAPSE_ALL, ACTION_ADD_ELEMENT,
        ACTION_ADD_SUB_ELEMENT, ACTION_ADD_ATTRIBUTE,
        ACTION_DELETE_ITEM
    };
    private static final Action[] ACTIONS_ON_ELEMENT = new Action[]
    {
        ACTION_EXPAND_ALL, ACTION_COLLAPSE_ALL, ACTION_ADD_SUB_ELEMENT,
        ACTION_ADD_ATTRIBUTE, ACTION_DELETE_ITEM
    };
    private static final Action[] ACTIONS_ON_ATTRIBUTE = new Action[]
    {
        ACTION_DELETE_ITEM
    };
    private static final Action[] ACTIONS_ON_TABLE = new Action[]
    {
        ACTION_ADD_ELEMENT
    };

    @Override
    public void initWidget() throws WidgetInitalizeException
    {
        setConfigTreeTable(new TreeTable());
        getConfigTreeTable().setSizeFull();
        addComponent(getConfigTreeTable());
        //getConfigDataContainer().initWidget();
        configureTreeTableAttributes();
        configureTreeTableEditableFields();

        configureTreeTableListener();
        configureTreeTableActionHandlers();
    }

    private void configureTreeTableEditableFields()
    {
        DefaultFieldFactory fieldFactory = prepareFieldFactory();
        getConfigTreeTable().setTableFieldFactory(fieldFactory);
        getConfigTreeTable().setEditable(true);
        getConfigTreeTable().setSelectable(true);
    }

    private void configureTreeTableAttributes()
    {
        getConfigTreeTable().setContainerDataSource(getConfigDataContainer());
        getConfigTreeTable().setVisibleColumns(
                new String[]
                {
                    ConfigDataContainer.PROPERTY_CONFIG_NAME,
                    ConfigDataContainer.PROPERTY_CONFIG_VALUE
                });
        getConfigTreeTable().setColumnHeaders(
                new String[]
                {
                    I18NResource.localize(
                    ConfigDataContainer.PROPERTY_CONFIG_NAME), I18NResource.
                    localize(ConfigDataContainer.PROPERTY_CONFIG_VALUE)
                });
    }

    private void configureTreeTableActionHandlers()
    {
        getConfigTreeTable().addActionHandler(new Action.Handler()
        {

            @Override
            public Action[] getActions(Object target, Object sender)
            {
                Action[] actions = null;
                if (target instanceof ConfigElement)
                {
                    ConfigElement configElement = (ConfigElement) target;
                    if (configElement.isRoot())
                    {
                        actions = ConfigDataView.ACTIONS_ON_ROOT_ELEMENT;
                    }
                    else
                    {
                        actions = ConfigDataView.ACTIONS_ON_ELEMENT;
                    }
                }
                else if (target instanceof ConfigAttribute)
                {
                    actions = ConfigDataView.ACTIONS_ON_ATTRIBUTE;
                }
                else
                {
                    actions = ConfigDataView.ACTIONS_ON_TABLE;
                }
                return actions;
            }

            @Override
            public void handleAction(Action action, Object sender, Object target)
            {
                try
                {

                    if (action == ACTION_ADD_ELEMENT)
                    {
                        ConfigElement element = (ConfigElement) target;

                        ConfigElement newElement = getConfigDataContainer().
                                createNewConfigElement();
                        getConfigTreeTable().select(newElement);
                    }
                    else if (action == ACTION_ADD_SUB_ELEMENT && target instanceof ConfigElement)
                    {
                        ConfigElement parentElement = (ConfigElement) target;
                        ConfigElement newSubElement = getConfigDataContainer().
                                createNewSubConfigElement(parentElement);
                        getConfigTreeTable().setCollapsed(parentElement, false);
                        getConfigTreeTable().select(newSubElement);
                    }
                    else if (action == ACTION_ADD_ATTRIBUTE && target instanceof ConfigElement)
                    {
                        ConfigElement parentElement = (ConfigElement) target;
                        ConfigAttribute newAttribute = getConfigDataContainer().
                                createNewAttribute(parentElement);
                        getConfigTreeTable().setCollapsed(parentElement, false);
                        getConfigTreeTable().select(newAttribute);
                    }
                    else if (action == ACTION_DELETE_ITEM)
                    {
                        getConfigDataContainer().removeConfigData((ConfigData)target);
                    }
                    else if (action == ACTION_EXPAND_ALL && target instanceof ConfigElement)
                    {
                        getConfigTreeTable().setCollapsed(target, false);
                    }
                    else if (action == ACTION_COLLAPSE_ALL && target instanceof ConfigElement)
                    {
                        getConfigTreeTable().setCollapsed(target, true);
                    }

                }
                catch (Exception ex)
                {
                    Logger.getLogger(ConfigDataView.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);
                    getApplication().getMainWindow().showNotification(
                            NotificationFactory.createHumanErrorNotification(
                            I18NResource.localize(CommonConstants.SYSTEM_ERROR),ex.getMessage()));
                }
            }
        });
    }

    private void configureTreeTableListener()
    {
        getConfigTreeTable().addListener(new ItemClickEvent.ItemClickListener()
        {

            @Override
            public void itemClick(ItemClickEvent event)
            {

                setClickedItemForEditMode(event);

            }
        });
    }

    public Field getCurrentlyEditedField()
    {
        return currentlyEditedField;
    }

    public void setCurrentlyEditedField(Field currentlyEditedField)
    {
        this.currentlyEditedField = currentlyEditedField;
    }

    private void disableCurrentlyEditedField()
    {
        if (getCurrentlyEditedField() != null)
        {
            getCurrentlyEditedField().setReadOnly(true);
        }
    }

    private void setClickedItemForEditMode(ItemClickEvent event)
    {
        disableCurrentlyEditedField();
        Map<Object, Field> fieldMap = (Map) event.getItem().
                getItemProperty(
                ConfigDataContainer.PROPERTY_CONFIG_VALUE_FIELD).
                getValue();
        Field field = fieldMap.get(event.getPropertyId());
        if (ConfigDataContainer.PROPERTY_CONFIG_VALUE.equals(
                event.getPropertyId()) && event.getItemId() instanceof ConfigAttribute)
        {
            field.setReadOnly(false);
        }
        else if (ConfigDataContainer.PROPERTY_CONFIG_NAME.equals(event.
                getPropertyId()))
        {
            field.setReadOnly(false);
        }
        setCurrentlyEditedField(field);
    }

    private DefaultFieldFactory prepareFieldFactory()
    {
        DefaultFieldFactory fieldFactory = new DefaultFieldFactory()
        {

            @Override
            public Field createField(Item item, Object propertyId, Component uiContext)
            {
                Property beanProperty = item.getItemProperty(propertyId);
                BeanItem beanItem = (BeanItem) item;
                Field field = new TextField(beanProperty);
                if (beanItem.getBean() instanceof ConfigElement)
                {

                    field.setReadOnly(ConfigDataContainer.PROPERTY_CONFIG_VALUE.
                            equals(propertyId));
                }
                return field;
            }

            @Override
            public Field createField(Container container, final Object itemId, final Object propertyId, Component uiContext)
            {
                final TextField field = new TextField(container.
                        getContainerProperty(
                        itemId,
                        propertyId));
                field.setReadOnly(true);
                field.setNullRepresentation("");
                final Item item = container.getItem(itemId);

                Map<Object, Field> fieldMap = (Map) item.getItemProperty(
                        ConfigDataContainer.PROPERTY_CONFIG_VALUE_FIELD).
                        getValue();
                if (fieldMap == null)
                {
                    fieldMap = new HashMap<Object, Field>();
                    item.getItemProperty(
                            ConfigDataContainer.PROPERTY_CONFIG_VALUE_FIELD).
                            setValue(fieldMap);
                }
                fieldMap.put(propertyId, field);

                field.addListener(new FieldEvents.BlurListener()
                {

                    @Override
                    public void blur(BlurEvent event)
                    {
                        field.setReadOnly(true);
                    }
                });
                field.addListener(new Property.ValueChangeListener()
                {

                    @Override
                    public void valueChange(ValueChangeEvent event)
                    {
                        try
                        {
                            ConfigValueChangeApplicator changeApplicator =
                                    (ConfigValueChangeApplicator) item.
                                    getItemProperty(
                                    ConfigDataContainer.PROPERTY_CONFIG_CHANGE_APPLICATOR).
                                    getValue();
                            if (changeApplicator != null)
                            {
                                changeApplicator.applyChanges();
                            }
                        }
                        catch (Exception ex)
                        {
                            Logger.getLogger(
                                    ConfigDataView.class.getName()).log(
                                    Level.SEVERE, ex.getMessage(), ex);
                            getApplication().getMainWindow().showNotification(
                                    NotificationFactory.
                                    createHumanErrorNotification(
                                    I18NResource.localize(
                                    CommonConstants.SYSTEM_ERROR),
                                    I18NResource.localizeWithParameter(
                                    CommonConstants.UNABLE_TO_UPDATE_RECORD__,event.getProperty().getValue())));
                        }
                    }
                });

                return field;
            }
        };
        return fieldFactory;

    }

    /**
     * @return the configDataContainer
     */
    public ConfigDataContainer getConfigDataContainer()
    {
        return configDataContainer;
    }

    /**
     * @param configDataContainer the configDataContainer to set
     */
    public void setConfigDataContainer(ConfigDataContainer configDataContainer)
    {
        this.configDataContainer = configDataContainer;
    }

    /**
     * @return the configTreeTable
     */
    public TreeTable getConfigTreeTable()
    {
        return configTreeTable;
    }

    /**
     * @param configTreeTable the configTreeTable to set
     */
    public void setConfigTreeTable(TreeTable configTreeTable)
    {
        this.configTreeTable = configTreeTable;
    }
}
