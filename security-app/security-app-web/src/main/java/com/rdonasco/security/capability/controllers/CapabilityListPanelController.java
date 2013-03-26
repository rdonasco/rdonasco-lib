/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.datamanager.view.DataRetrievalStrategy;
import com.rdonasco.datamanager.view.DataViewListTable;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.controllers.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.views.CapabilityListContainer;
import com.rdonasco.security.capability.views.CapabilityListPanel;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.CapabilityItemVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
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
	@Inject
	private ApplicationPopupProvider applicationPopupProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			final DataViewListTable dataViewListTable = new DataViewListTable();
			dataViewListTable.addStyleName(SecurityDefaultTheme.CSS_CAPABILITY_TABLE);
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
					for (long i = 1; i < 11; i++)
					{
						final CapabilityVO capabilityVO = new CapabilityVOBuilder()
								.setId(i)
								.setTitle("DUMMY" + i)
								.createCapabilityVO();
						Embedded icon = new Embedded(null, new ThemeResource(SecurityDefaultTheme.ICONS_16x16_DELETE));
						icon.setDescription(I18NResource.localize("Delete"));
						final CapabilityItemVO itemVO = new CapabilityItemVOBuilder()
								.setCapabilityVO(capabilityVO)
								.setEmbeddedIcon(icon)
								.createCapabilityItemVO();
						icon.addListener(new MouseEvents.ClickListener()
						{
							private static final long serialVersionUID = 1L;
							@Override
							public void click(MouseEvents.ClickEvent event)
							{
								dataViewListTable.removeItem(itemVO);
								applicationPopupProvider.popUpInfo("Image clicked for item " + capabilityVO.getId());
							}
						});
						itemVO.setEmbeddedIcon(icon);
						capabilities.add(itemVO);
					}
					CapabilityVO newCapabilityVO = new CapabilityVOBuilder()
							.setId(0L)
							.setTitle(I18NResource.localize("New Capability"))
							.createCapabilityVO();
					Embedded newIcon = new Embedded(null, new ThemeResource(SecurityDefaultTheme.ICONS_16x16_ADD));
					newIcon.setDescription(I18NResource.localize("Add"));
					newIcon.addListener(new MouseEvents.ClickListener()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void click(MouseEvents.ClickEvent event)
						{
							applicationPopupProvider.popUpInfo("Image to add item clicked.");
						}
					});
					CapabilityItemVO newCapabilityItemVO = new CapabilityItemVOBuilder()
							.setCapabilityVO(newCapabilityVO)
							.setEmbeddedIcon(newIcon)
							.createCapabilityItemVO();
					capabilities.add(newCapabilityItemVO);

					return capabilities;
				}
			};
			
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
