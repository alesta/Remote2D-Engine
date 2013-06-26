package com.remote.remote2d.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class InsertableEntityList {
	
private HashMap<String,Entity> insertableEntities;
	
	public InsertableEntityList()
	{
		insertableEntities = new HashMap<String,Entity>();
	}
	
	public void addInsertableComponent(String s, Entity e)
	{
		insertableEntities.put(s, e);
	}
	
	public Entity getEntity(String s)
	{
		return insertableEntities.get(s);
	}
	
	public boolean containsEntity(Entity s)
	{
		return insertableEntities.containsKey(s);
	}
	
	public String getEntityID(Entity e)
	{
		Iterator<Entry<String, Entity>> iterator = insertableEntities.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<String, Entity> entry = iterator.next();
			if(entry.getValue().getClass().isInstance(e))
				return entry.getKey();
		}
		return null;
	}
	
}
