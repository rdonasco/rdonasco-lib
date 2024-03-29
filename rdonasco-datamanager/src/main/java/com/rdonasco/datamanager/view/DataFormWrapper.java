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
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.Notification;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.datamanager.utils.CommonConstants;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.vaadin.view.ViewWidget;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.rdonasco.common.vaadin.view.NotificationFactory;
import com.rdonasco.common.vaadin.view.VaadinBeanUtils;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.theme.DataManagerTheme;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import de.steinwedel.vaadin.MessageBox;
import de.steinwedel.vaadin.MessageBox.ButtonType;

/**
 *
 * @author Roy F. Donasco
 */
public abstract class DataFormWrapper<T> extends VerticalLayout implements
		ViewWidget
{
	private Form formDelegate = null;
	private DataManagerView view;
	private Button saveButton = new Button(I18NResource.localize(CommonConstants.SAVE),
			new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					BeanItem beanItem = (BeanItem) getFormDelegate().getItemDataSource();
					T data = (T) beanItem.getBean();
					try
					{
						getFormDelegate().commit();
						if (isItNewData(data))
						{
							persistNewData(data);
						}
						else
						{
							persistExistingData(data);
						}
						changeModeToViewWithData();

					}
					catch (Exception ex)
					{
						saveButton.setEnabled(true);
						getApplication().getMainWindow().showNotification(
								NotificationFactory.createFromException(
								Notification.TYPE_WARNING_MESSAGE, ex));
						Logger.getLogger(DataFormWrapper.class.getName()).log(
								Level.WARNING, ex.getMessage(), ex);
					}
				}

				private void persistNewData(T newData) throws Exception
				{

					newData = getNewDataCommitStrategy().commit(newData);
					if (null != getView())
					{
						Table table = getView().getListView().getTable();

						Item addedItem = (Item) table.getContainerDataSource().
								addItem(
								newData);
						table.select(newData);
						setCurrentRecord(addedItem);

					}
					getApplication().getMainWindow().showNotification(
							NotificationFactory.createHumanNotification(I18NResource.
							localize(CommonConstants.SUCCESS), I18NResource.localize(
							CommonConstants.NEW_RECORD_SAVED)));


				}

				private void persistExistingData(T existingData) throws
						Exception
				{

					getUpdatedDataCommitStrategy().commit(existingData);
					getApplication().getMainWindow().showNotification(
							NotificationFactory.createHumanNotification(I18NResource.
							localize(CommonConstants.SUCCESS), I18NResource.localize(
							CommonConstants.RECORD_UPDATED)));

				}
			});
	private Button cancelButton = new Button(I18NResource.localize("Cancel"),
			new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					getFormDelegate().discard();
					if (null != getView() && null != getView().getListView().
							getSelectedRecord())
					{
						getView().displaySelectedRecordInTheForm();
					}
					else
					{
						changeModeToView();
					}
				}
			});
	private Button editButton = new Button(I18NResource.localize("Edit"),
			new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					changeModeToEdit();
				}
			});
	private Button deleteButton = new Button(I18NResource.localize(CommonConstants.DELETE),
			new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					MessageBox messageBox = new MessageBox(getWindow(),
							I18NResource.localize("Are you sure?"),
							MessageBox.Icon.QUESTION,
							I18NResource.localize("Do you really want to delete this?"),
							new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, I18NResource.localize("Yes")),
							new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, I18NResource.localize("No")));
					messageBox.show(new MessageBox.EventListener()
					{
						public void buttonClicked(ButtonType buttonType)
						{
							if (ButtonType.YES.equals(buttonType))
							{
								deleteCurrentRecord();
								getApplication().getMainWindow().showNotification(
										NotificationFactory.createWarningNotification(I18NResource.
										localize(CommonConstants.DELETED),
										I18NResource.localize(CommonConstants.DATA_NO_LONGER_EXIST)));
								changeModeToView();
							}
						}
					});
				}

				public void deleteCurrentRecord()
				{
					try
					{
						BeanItem item = (BeanItem) getFormDelegate().getItemDataSource();
						getDeleteCommitStrategy().commit((T) item.getBean());
						Table table = getView().getListView().getTable();
						if (table != null)
						{
							table.removeItem(table.getValue());
						}
						getFormDelegate().setItemDataSource(null);
					}
					catch (Exception ex)
					{
						if (null != getView())
						{
							getView().refreshData();
						}
						Logger.getLogger(DataFormWrapper.class.getName()).log(
								Level.WARNING, ex.getMessage(), ex);
						String message = new StringBuilder(I18NResource.localize(
								"Unable to delete record at this moment")).
								append(" ").append(I18NResource.localize(
								"Please try again later")).toString();
						getApplication().getMainWindow().showNotification(NotificationFactory.createWarningNotification(I18NResource.localize("Warning"),
								message));
						throw new RuntimeException(message, ex);
					}
				}
			});
	private Button createNewButton = new Button(I18NResource.localize(
			"Create New"), new Button.ClickListener()
	{
		@Override
		public void buttonClick(ClickEvent event)
		{
			T newData = initializeNewData();
			BeanItem beanItem = new BeanItem(newData);
			getFormDelegate().setItemDataSource(beanItem);
			getFormDelegate().setVisibleItemProperties(getVisibleColumns());
			changeModeToCreateNew();
		}
	});
	private DataManager<T> dataManager;
	private DataFormCommitStrategy<T> deleteCommitStrategy = new DataFormCommitStrategy<T>()
	{
		@Override
		public T commit(T dataOnScreen) throws DataAccessException
		{
			T dataOnDb = getDataManager().loadData(dataOnScreen);
			getDataManager().deleteData(dataOnDb);
			return dataOnDb;
		}
	};
	private DataFormCommitStrategy<T> newDataCommitStrategy = new DataFormCommitStrategy<T>()
	{
		@Override
		public T commit(T newData) throws DataAccessException
		{
			if (getDataManager() != null)
			{
				newData = getDataManager().saveData(newData);
			}
			return newData;
		}
	};
	private DataFormCommitStrategy<T> updatedDataCommitStrategy = new DataFormCommitStrategy<T>()
	{
		@Override
		public T commit(T existingData) throws DataAccessException
		{
			if (getDataManager() != null)
			{
				getDataManager().updateData(existingData);
			}
			return existingData;
		}
	};

	public DataFormWrapper()
	{
	}

	public DataFormCommitStrategy<T> getDeleteCommitStrategy()
	{
		return deleteCommitStrategy;
	}

	public void setDeleteCommitStrategy(
			DataFormCommitStrategy<T> deleteCommitStrategy)
	{
		this.deleteCommitStrategy = deleteCommitStrategy;
	}

	public DataFormCommitStrategy<T> getNewDataCommitStrategy()
	{
		return newDataCommitStrategy;
	}

	public void setNewDataCommitStrategy(
			DataFormCommitStrategy<T> newDataCommitStrategy)
	{
		this.newDataCommitStrategy = newDataCommitStrategy;
	}

	public DataFormCommitStrategy<T> getUpdatedDataCommitStrategy()
	{
		return updatedDataCommitStrategy;
	}

	public void setUpdatedDataCommitStrategy(
			DataFormCommitStrategy<T> updatedDataCommitStrategy)
	{
		this.updatedDataCommitStrategy = updatedDataCommitStrategy;
	}

	protected void refreshBeanItemUsingValuesFromDatabase(BeanItem beanItem)
			throws DataAccessException
	{
		T beanData = (T) beanItem.getBean();
		T dataFromDb = getDataManager().loadData(beanData);
		VaadinBeanUtils.copyToVaadinBeanItem(dataFromDb, beanItem);
		changeCaptionUsingCurrentItem();
	}

	private void changeCaptionToNoItemSelected()
	{
		setCaption(I18NResource.localize(CommonConstants.NO_ITEM_SELECTED));
	}

	private void setupFooter()
	{
		HorizontalLayout footer = new HorizontalLayout();
		footer.setStyleName(DataManagerTheme.STYLE_DATA_ENTRY_FORM_TOOLBAR);
		footer.setSpacing(true);
		footer.addComponent(createNewButton);
		footer.addComponent(editButton);
		footer.addComponent(deleteButton);
		footer.addComponent(saveButton);
		footer.addComponent(cancelButton);

		getFormDelegate().setFooter(footer);
	}

	private void setupButtonIcons()
	{
		// setup icons       
		createNewButton.setIcon(new ThemeResource(
				DataManagerTheme.Resources.ICON_32_NEW_BUTTON));
		saveButton.setIcon(new ThemeResource(
				DataManagerTheme.Resources.ICON_32_SAVE_BUTTON));
		saveButton.setDisableOnClick(true);
		cancelButton.setIcon(new ThemeResource(
				DataManagerTheme.Resources.ICON_32_CANCEL_BUTTON));
		editButton.setIcon(new ThemeResource(
				DataManagerTheme.Resources.ICON_32_EDIT_BUTTON));
		deleteButton.setIcon(new ThemeResource(
				DataManagerTheme.Resources.ICON_32_DELETE_BUTTON));
	}

	public void changeModeToCreateNew()
	{
		setReadOnly(false);
		ButtonUtil.showButtons(saveButton, cancelButton);
		ButtonUtil.hideButtons(createNewButton, deleteButton, editButton);
		setCaption(I18NResource.localize(CommonConstants.NEW_RECORD));
		clearErrors();
		focus();
	}

	public void changeModeToEdit()
	{
		clearErrors();
		refreshScreenDataUsingValuesFromDatabase();

		setReadOnly(false);
		ButtonUtil.showButtons(saveButton, cancelButton);
		ButtonUtil.hideButtons(createNewButton, deleteButton, editButton);
		focus();
	}

	@Override
	public void focus()
	{
		formDelegate.focus();
	}

	public void changeModeToView()
	{
		clearErrors();
		ButtonUtil.hideButtons(saveButton, cancelButton, editButton,
				deleteButton);
		ButtonUtil.showButtons(createNewButton);
		getFormDelegate().setItemDataSource(null);
		setComponentError(null);
		setReadOnly(true);
		changeCaptionToNoItemSelected();
	}

	public void changeModeToViewWithData()
	{
		clearErrors();
		ButtonUtil.hideButtons(saveButton, cancelButton);
		ButtonUtil.showButtons(createNewButton, editButton, deleteButton);
		setComponentError(null);
		setReadOnly(true);
		changeCaptionUsingCurrentItem();
	}

	public void clearErrors()
	{
		setComponentError(null);
		saveButton.setComponentError(null);
		cancelButton.setComponentError(null);
		createNewButton.setComponentError(null);
		deleteButton.setComponentError(null);
		editButton.setComponentError(null);
	}

	@Override
	public void setComponentError(ErrorMessage componentError)
	{
		getFormDelegate().setComponentError(componentError);
	}


	public void setDataManager(DataManager<T> dataManager)
	{
		this.dataManager = dataManager;
	}

	public DataManager<T> getDataManager()
	{
		return dataManager;
	}

	public DataManagerView getView()
	{
		return view;
	}

	public Button getCancelButton()
	{
		return cancelButton;
	}

	public Button getCreateNewButton()
	{
		return createNewButton;
	}

	public Button getDeleteButton()
	{
		return deleteButton;
	}

	public Button getEditButton()
	{
		return editButton;
	}

	public Button getSaveButton()
	{
		return saveButton;
	}

	@Override
	public void initWidget()
	{
		setFormDelegate(instantiateForm());
		setStyleName(DataManagerTheme.STYLE_DATA_ENTRY_FORM);
		setupFormFieldsAndCaption();
		setupButtonIcons();
		setupFooter();
		// Enable buffering so that commit() must be called for the form
		// before input is written to the data. (Form input is not written
		// immediately through to the underlying object.)
		getFormDelegate().setWriteThrough(false);
		addComponent(getFormDelegate());
		changeModeToView();
	}

	public void setCurrentRecord(Item item)
	{
		try
		{
			BeanItem beanItem = (BeanItem) item;
			refreshBeanItemUsingValuesFromDatabase(beanItem);
			getFormDelegate().setItemDataSource(beanItem);
			getFormDelegate().setVisibleItemProperties(getVisibleColumns());
			changeCaptionUsingCurrentItem();
		}
		catch (DataAccessException ex)
		{
			Logger.getLogger(DataFormWrapper.class.getName()).log(Level.WARNING, ex.
					getMessage(), ex);
		}
	}

	protected void changeCaptionUsingCurrentItem()
	{
		BeanItem beanItem = (BeanItem) getFormDelegate().getItemDataSource();
		if (beanItem != null && beanItem.getBean() != null)
		{
			String itemCaption = beanItem.getBean().toString();
			if (itemCaption == null)
			{
				itemCaption = I18NResource.localize(
						CommonConstants.ITEM_TO_STRING_NOT_IMPLEMENTED);
			}
			setCaption(itemCaption);
		}
		else
		{
			changeCaptionToNoItemSelected();
		}
	}

	public void setView(DataManagerView view)
	{
		this.view = view;
	}

	public void refreshScreenDataUsingValuesFromDatabase()
	{
		try
		{
			BeanItem beanItem = (BeanItem) getFormDelegate().getItemDataSource();
			refreshBeanItemUsingValuesFromDatabase(beanItem);
		}
		catch (DataAccessException ex)
		{
			if (null != getView())
			{
				getView().refreshData();
			}
			Logger.getLogger(DataFormWrapper.class.getName()).log(Level.WARNING, ex.
					getMessage(), ex);
			getApplication().getMainWindow().showNotification(NotificationFactory.createWarningNotification(I18NResource.localize(CommonConstants.SYSTEM_WARNING),
					I18NResource.localize(CommonConstants.EDITING_OF_RECORD_FAILED)));
			throw new RuntimeException(ex);
		}
	}

	protected Form instantiateForm()
	{
		return new Form();
	}

	public void setFormFieldFactory(FormFieldFactory fieldFactory)
	{
		formDelegate.setFormFieldFactory(fieldFactory);
	}

	public FormFieldFactory getFormFieldFactory()
	{
		return formDelegate.getFormFieldFactory();
	}

	public void setLayout(Layout newLayout)
	{
		formDelegate.setLayout(newLayout);
	}

	public Item getItemDataSource()
	{
		return formDelegate.getItemDataSource();
	}

	public Layout getFooter()
	{
		return formDelegate.getFooter();
	}

	@Override
	public String getCaption()
	{
		return formDelegate.getCaption();
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		formDelegate.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly()
	{
		return formDelegate.isReadOnly();
	}

	@Override
	public void setCaption(String caption)
	{
		formDelegate.setCaption(caption);
	}

	protected abstract Object[] getVisibleColumns();

	protected abstract void setupFormFieldsAndCaption();

	protected abstract boolean isItNewData(T data);

	protected abstract T initializeNewData();

	private void setFormDelegate(Form formDelegate)
	{
		this.formDelegate = formDelegate;
	}

	public Form getFormDelegate()
	{
		if (null == formDelegate)
		{
			formDelegate = instantiateForm();
		}
		return formDelegate;
	}
}
