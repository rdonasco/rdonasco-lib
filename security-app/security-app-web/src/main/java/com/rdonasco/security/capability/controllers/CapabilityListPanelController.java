/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.controllers.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.utils.Constants;
import com.rdonasco.security.capability.utils.TableHelper;
import com.rdonasco.security.capability.views.CapabilityListPanel;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.CapabilityItemVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
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
	CapabilityDataManagerDecorator dataManager;
	@Inject
	private Instance<ApplicationExceptionPopupProvider> exceptionPopupProviderFactory;
	@Inject
	private Instance<ApplicationPopupProvider> popupProviderFactory;
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	private ApplicationPopupProvider popupProvider;
	private DataManagerContainer capabilityItemTableContainer = new DataManagerContainer(CapabilityItemVO.class);

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			final Table capabilityListTable = new Table();
			capabilityListTable.addStyleName(SecurityDefaultTheme.CSS_CAPABILITY_TABLE);
			TableHelper.setupTable(capabilityListTable);
			capabilityItemTableContainer.setDataManager(dataManager);
			dataManager.setClickListenerProvider(new ClickListenerProvider<CapabilityItemVO>()
			{
				@Override
				public MouseEvents.ClickListener provideClickListenerFor(
						final CapabilityItemVO data)
				{
					MouseEvents.ClickListener clickListener = new MouseEvents.ClickListener()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void click(MouseEvents.ClickEvent event)
						{
							try
							{
								capabilityItemTableContainer.removeItem(data);
								getPopupProvider().popUpInfo("Capability deleted");
							}
							catch (Exception e)
							{
								getExceptionPopupProvider().popUpErrorException(e);
							}
						}
					};
					return clickListener;

				}
			});
			capabilityItemTableContainer.setDummyAddRecord(createDummyAddRecord());
			capabilityListTable.setContainerDataSource(capabilityItemTableContainer);
			capabilityListTable.setVisibleColumns(Constants.TABLE_VISIBLE_COLUMNS);
			capabilityListTable.setColumnHeaders(Constants.TABLE_VISIBLE_HEADERS);
			capabilityListPanel.setDataViewListTable(capabilityListTable);
			capabilityListPanel.initWidget();
			capabilityItemTableContainer.refresh();
		}
		catch (Exception ex)
		{
			getExceptionPopupProvider().popUpErrorException(ex);
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
		try
		{
			capabilityItemTableContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			getExceptionPopupProvider().popUpErrorException(ex);
		}
	}

	protected ApplicationExceptionPopupProvider getExceptionPopupProvider()
	{
		if (null == exceptionPopupProvider)
		{
			exceptionPopupProvider = exceptionPopupProviderFactory.get();
		}
		return exceptionPopupProvider;
	}

	public ApplicationPopupProvider getPopupProvider()
	{
		if (null == popupProvider)
		{
			popupProvider = popupProviderFactory.get();
		}
		return popupProvider;
	}

	private CapabilityItemVO createDummyAddRecord()
	{
		String caption = I18NResource.localize("Add new capability");
		CapabilityVO capability = new CapabilityVOBuilder()
				.setId(Long.MAX_VALUE)
				.setTitle(caption)
				.createCapabilityVO();
		CapabilityItemVO itemVO;
		Embedded icon = new Embedded(null, new ThemeResource(SecurityDefaultTheme.ICONS_16x16_ADD));
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				addNewCapability();
			}
		});
		icon.setDescription(caption);
		final CapabilityItemVO capabilityItem = new CapabilityItemVOBuilder()
				.setCapabilityVO(capability)
				.setEmbeddedIcon(icon)
				.createCapabilityItemVO();
		itemVO = capabilityItem;
		return itemVO;
	}

	private void addNewCapability()
	{
		CapabilityVO newCapabilityVO = new CapabilityVOBuilder()
				.setTitle("New capability")
				.setDescription("New capability")
				.createCapabilityVO();
		CapabilityItemVO newItemVO = new CapabilityItemVOBuilder()
				.setCapabilityVO(newCapabilityVO)
				.createCapabilityItemVO();
		capabilityItemTableContainer.addItem(newItemVO);
	}
}
