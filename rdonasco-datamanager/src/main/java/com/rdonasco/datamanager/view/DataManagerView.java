/*
 * Copyright 2011 Roy F. Donasco.
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
package com.rdonasco.datamanager.view;

import com.rdonasco.datamanager.controller.DataManagerViewController;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.common.vaadin.view.ControlledView;

/**
 *
 * @author Roy F. Donasco
 */
@SuppressWarnings("serial")
public class DataManagerView<T> extends VerticalLayout implements ControlledView
{
    private DataManagerViewController dataManagerViewFactory;
    //@EJB
    private DataManager<T> dataManager;
    //@Inject
    private DataViewListPanel listView;
    //@Inject
    private DataForm<T> dataForm;

    public DataManagerViewController getDataManagerViewFactory()
    {
        return dataManagerViewFactory;
    }

    public void setDataManagerViewFactory(DataManagerViewController dataManagerViewFactory)
    {
        this.dataManagerViewFactory = dataManagerViewFactory;
    }
    
    

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager)
    {
        this.dataManager = dataManager;
    }        

    public DataForm<T> getDataForm()
    {
        return dataForm;
    }

    public void refreshData()
    {
        getListView().getTable().refreshData();
    }

    public void setDataForm(DataForm<T> dataForm)
    {
        this.dataForm = dataForm;
        dataForm.setView(this);
        if(getDataManager() != null)
        {
            dataForm.setDataManager(dataManager);
        }
    }

    public DataViewListPanel getListView()
    {
        return listView;
    }

    public void setListView(DataViewListPanel listView)
    {
        this.listView = listView;
        listView.setView(this);
    }

    @Override
    public void initWidget() throws WidgetInitalizeException
    {
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        splitPanel.setSplitPosition(300f, UNITS_PIXELS);

        getListView().getTable().setDataManager(dataManager);
        refreshData();
        getListView().setSizeFull();
        splitPanel.setFirstComponent(getListView());

        getDataForm().setSizeFull();
        getDataForm().setDataManager(dataManager);
        getDataForm().setView(this);
        splitPanel.setSecondComponent(getDataForm());

        splitPanel.setSizeFull();
        addComponent(splitPanel);
        setExpandRatio(splitPanel, 1.0f);

        setMargin(false);

        // setup listeners
        getListView().getTable().addListener(new ValueChangeListener()
        {

            @Override
            public void valueChange(ValueChangeEvent event)
            {
                displaySelectedRecordInTheForm();
            }
        });
    }

    public BeanItem displaySelectedRecordInTheForm()
    {
        BeanItem item = (BeanItem)getListView().getTable().getItem(getListView().getTable().getValue());
        if (null != item && item != getDataForm().getItemDataSource())
        {
            getDataForm().setCurrentRecord(item);
        }
        if(null != item)
        {
            getDataForm().changeModeToViewWithData();
        }
        else
        {
            getDataForm().changeModeToView();
        }
        return item;
    }
}
