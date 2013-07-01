package com.remote.remote2d.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.remote.remote2d.Remote2DException;
import com.remote.remote2d.entity.component.Component;

public class InsertableComponentList {
	
	private HashMap<String,Component> insertableComponents;
	
	public InsertableComponentList()
	{
		insertableComponents = new HashMap<String,Component>();
	}
	
	public void addInsertableComponent(String s, Component e)
	{
		insertableComponents.put(s, e);
	}
	
	public Component getComponent(String s)
	{
		return insertableComponents.get(s);
	}
	
	public boolean containsComponent(String s)
	{
		return insertableComponents.containsKey(s);
	}
	
	public String getComponentID(Component e)
	{
		Iterator<Entry<String, Component>> iterator = insertableComponents.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<String, Component> entry = iterator.next();
			if(entry.getValue().getClass().isInstance(e))
				return entry.getKey();
		}
		return null;
	}
	
	public Iterator<Entry<String,Component>> getIterator()
	{
		return insertableComponents.entrySet().iterator();
	}
}
