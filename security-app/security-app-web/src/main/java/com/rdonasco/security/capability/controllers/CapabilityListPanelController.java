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
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.utils.Constants;
import com.rdonasco.datamanager.utils.TableHelper;
import com.rdonasco.security.capability.views.CapabilityListPanel;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.CapabilityItemVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import de.steinwedel.vaadin.MessageBox;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class CapabilityListPanelController implements
		ViewController<CapabilityListPanel>
{

	private static final long serialVersionUID = 1L;
	protected static final long ADD_CAPABILITY_ID = -1L;
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
	private DataManagerContainer<CapabilityItemVO> capabilityItemTableContainer = new DataManagerContainer(CapabilityItemVO.class);
	private final Table capabilityListTable = new Table();

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
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
							MessageBox messageBox = new MessageBox(capabilityListPanel.getWindow(),
									I18NResource.localize("Are you sure?"),
									MessageBox.Icon.QUESTION,
									I18NResource.localize("Do you really want to delete this?"),
									new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, I18NResource.localize("Yes")),
									new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, I18NResource.localize("No")));
							messageBox.show(new MessageBox.EventListener()
							{
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClicked(
										MessageBox.ButtonType buttonType)
								{
									if (MessageBox.ButtonType.YES.equals(buttonType))
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
								}
							});
						}
					};
					return clickListener;

				}
			});
//			capabilityItemTableContainer.setDummyAddRecord(createDummyAddRecord());
			capabilityListTable.setContainerDataSource(capabilityItemTableContainer);
			capabilityListTable.setVisibleColumns(Constants.TABLE_VISIBLE_COLUMNS);
			capabilityListTable.setColumnHeaders(Constants.TABLE_VISIBLE_HEADERS);
			capabilityListPanel.setDataViewListTable(capabilityListTable);
			capabilityListPanel.initWidget();
			capabilityListPanel.getAddCapabilityButton().addListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					addNewCapability();
				}
			});
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

	private void addNewCapability()
	{
		CapabilityVO newCapabilityVO = new CapabilityVOBuilder()
				.setTitle("New Capability")
				.setDescription("New Capability")
				.createCapabilityVO();
		try
		{
			CapabilityItemVO newItemVO = new CapabilityItemVOBuilder()
					.setCapabilityVO(newCapabilityVO)
					.createCapabilityItemVO();
			BeanItem<CapabilityItemVO> newItemAdded = capabilityItemTableContainer.addItem(newItemVO);
			capabilityListTable.setCurrentPageFirstItemId(newItemAdded.getBean());
			capabilityListTable.select(newItemAdded.getBean());
		}
		catch (RuntimeException e)
		{
			getPopupProvider().popUpError(I18NResource.localizeWithParameter("Unable to add new capability", newCapabilityVO.getTitle()));
		}
	}

	public Table getCapabilityListTable()
	{
		return capabilityListTable;
	}
}
