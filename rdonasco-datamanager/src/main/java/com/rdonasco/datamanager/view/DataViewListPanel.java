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

import com.vaadin.data.Item;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import com.rdonasco.common.vaadin.view.BaligyaWidget;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.theme.DataManagerTheme;


/**
 *
 * @author Roy F. Donasco
 */
public class DataViewListPanel extends VerticalLayout implements BaligyaWidget
{

    private Button refreshButton = new Button(I18NResource.localize("Refresh List"), new Button.ClickListener()
    {

        @Override
        public void buttonClick(ClickEvent event)
        {
            getTable().refreshData();
        }
    });
    //@Inject
    private DataViewListTable table;

    public DataViewListTable getTable()
    {
        return table;
    }

    public void setTable(DataViewListTable table)
    {
        this.table = table;
    }

    @PostConstruct
    @Override
    public void initWidget()
    {
        addStyleName(DataManagerTheme.STYLE_LIST_VIEW);
//        refreshButton.setSizeFull();
        getTable().setSizeFull();
        addComponent(getTable());
        
        refreshButton.setWidth("100%");
        // setup icons       
        refreshButton.setIcon(new ThemeResource(DataManagerTheme.Resources.ICON_32_REFRESH));
        addComponent(refreshButton);        
        
        // allocate all remaining space to the table;
        setExpandRatio(getTable(), 1);

    }
    private DataManagerView view;

    public DataManagerView getView()
    {
        return view;
    }

    public void setView(DataManagerView view)
    {
        this.view = view;
    }

    public Item getSelectedRecord()
    {
        return getTable().getItem(getTable().getValue());
    }
}
