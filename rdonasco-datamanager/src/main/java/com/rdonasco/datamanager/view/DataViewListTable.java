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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.vaadin.ui.Table;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import com.rdonasco.common.vaadin.view.ViewWidget;
import com.rdonasco.common.vaadin.view.NotificationFactory;
import com.rdonasco.datamanager.theme.DataManagerTheme;

/**
 *
 * @author Roy F. Donasco
 */
public class DataViewListTable extends Table implements ViewWidget
{

    private DataRetrievalStrategy dataViewListTableRefreshStrategy;

    public DataRetrievalStrategy getDataViewListTableRefreshStrategy()
    {
        if (null == dataViewListTableRefreshStrategy)
        {
            dataViewListTableRefreshStrategy = new DataRetrievalStrategy()
            {
                @Override
                public List retrieveDataUsing(DataManager dataManagerToUse) throws DataAccessException
                {
                    return dataManagerToUse.retrieveAllData();
                }
            };
        }
        return dataViewListTableRefreshStrategy;
    }

    public void setDataViewListTableRefreshStrategy(DataRetrievalStrategy dataViewListTableRefreshStrategy)
    {
        this.dataViewListTableRefreshStrategy = dataViewListTableRefreshStrategy;
    }

    @PostConstruct
    @Override
    public void initWidget()
    {
        addStyleName(DataManagerTheme.STYLE_RECORD_LIST);
        /*
         * Make table selectable, react immediatedly to user events, and pass events to the
         * controller (our main application)
         */
        setSelectable(true);
        setImmediate(true);

        /* We don't want to allow users to de-select a row */
        setNullSelectionAllowed(false);

        // set additional table features
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);

    }
    private DataManager dataManager;

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager)
    {
        this.dataManager = dataManager;
    }

    public void refreshData()
    {
        try
        {
            Object currentItem = getValue();
            this.removeAllItems();
            List list = getDataViewListTableRefreshStrategy().retrieveDataUsing(getDataManager());
            for (Object uom : list)
            {
                try
                {
                    addItem(uom);
                }
                catch (RuntimeException ex)
                {
                    Logger.getLogger(DataViewListTable.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            if (currentItem != null)
            {
                select(currentItem);
            }
        }
        catch (DataAccessException ex)
        {
			if(getApplication() != null && getApplication().getMainWindow() != null)
			{
				getApplication().getMainWindow().showNotification(
						NotificationFactory.createWarningNotification(
						I18NResource.localize("Warning"),
						I18NResource.localize("Unable to load records from database")));
			}
			Logger.getLogger(DataViewListTable.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
