/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.common.vaadin.view;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class VaadinBeanUtils
{

    private static final String GET = "get";

    public static void copyToVaadinBeanItem(Object source, BeanItem beanItem)
    {
        if (source != null)
        {
            for (Method method : source.getClass().getMethods())
            {
                if (method.getName().startsWith(GET) && method.getName().length() > 3)
                {
                    String firstChar = method.getName().substring(3, 4).toLowerCase();
                    String propertyId = new StringBuilder().append(firstChar).append(method.getName().substring(4)).toString();
                    if (null != propertyId)
                    {
                        copyToBeanItemProperty(propertyId, method, beanItem, source);
                    }
                }
            }
            Logger.getLogger(VaadinBeanUtils.class.getName()).log(Level.FINE, source.toString());
        }
        
    }

    private static void copyToBeanItemProperty(String propertyId, Method method, BeanItem beanItem, Object source)
    {
        Property property = beanItem.getItemProperty(propertyId);
        if (null != property)
        {
            try
            {
                Object[] params = new Object[0];
                Object returnedValue = method.invoke(source, params);
                property.setValue(returnedValue);
            }
            catch(InvocationTargetException ex)
            {
                Logger.getLogger(VaadinBeanUtils.class.getName()).log(Level.FINEST, ex.getMessage(), ex);
            }
            catch(Property.ReadOnlyException ex)
            {
                Logger.getLogger(VaadinBeanUtils.class.getName()).log(Level.FINEST, ex.getMessage(), ex);
            }
            catch (Exception ex)
            {
                Logger.getLogger(VaadinBeanUtils.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
