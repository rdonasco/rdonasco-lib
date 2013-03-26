/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.datamanager.view.DataRetrievalStrategy;
import com.rdonasco.datamanager.view.DataViewListTable;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.views.CapabilityListContainer;
import com.rdonasco.security.capability.views.CapabilityListPanel;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityListPanelController implements
		ViewController<CapabilityListPanel>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private CapabilityListPanel capabilityListPanel;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			DataRetrievalStrategy<CapabilityItemVO> dataRetrievalStrategy = new DataRetrievalStrategy<CapabilityItemVO>()
			{
				private List<CapabilityItemVO> listCache;
				@Override
				public List<CapabilityItemVO> retrieveDataUsing(
						DataManager<CapabilityItemVO> dataManager) throws
						DataAccessException
				{
					if (null == listCache)
					{
						listCache = createDummyCapabilities();
					}
					return listCache;
				}

				private List<CapabilityItemVO> createDummyCapabilities()
				{
					List<CapabilityItemVO> capabilities = new ArrayList<CapabilityItemVO>();
					for (long i = 0; i < 10; i++)
					{
						CapabilityVO capabilityVO = new CapabilityVOBuilder()
								.setId(i)
								.setTitle("DUMMY" + i)
								.createCapabilityVO();
						CapabilityItemVO itemVO = new CapabilityItemVO();
						itemVO.setCapabilityVO(capabilityVO);
						itemVO.setEmbeddedIcon(new Embedded("", new ThemeResource(SecurityDefaultTheme.ICONS_16x16_DELETE)));
						capabilities.add(itemVO);
					}
					return capabilities;
				}
			};
			DataViewListTable dataViewListTable = new DataViewListTable();
			dataViewListTable.setDataViewListTableRefreshStrategy(dataRetrievalStrategy);
			dataViewListTable.setContainerDataSource(new CapabilityListContainer());
			dataViewListTable.setVisibleColumns(CapabilityListContainer.VISIBLE_COLUMNS);
			dataViewListTable.setColumnHeaders(CapabilityListContainer.VISIBLE_HEADERS);
			dataViewListTable.initWidget();
			dataViewListTable.refreshData();
			dataViewListTable.addListener(new ItemClickEvent.ItemClickListener()
			{
				@Override
				public void itemClick(ItemClickEvent event)
				{
				}
			});
			capabilityListPanel.setDataViewListTable(dataViewListTable);
			capabilityListPanel.initWidget();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public CapabilityListPanel getControlledView()
	{
		return capabilityListPanel;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
