package com.remote.remote2d.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.remote.remote2d.entity.component.Component;

public class EntityList {
	
	/**
	 * The actual entity list.
	 */
	private ArrayList<Entity> entityList;
	
	/**
	 * A big list of all of the Entities that are available.  When spawning an Entity into
	 * the world, you need to add it to the entity list if you expect it to get tick()ed
	 * or render()ed.
	 */
	
	public EntityList()
	{
		 entityList = new ArrayList<Entity>();
	}
	
	public void addEntityToList(Entity e)
	{
		entityList.add(e);
		spawn();
	}
	
	public void addEntityToList(Entity e,int i)
	{
		entityList.add(i,e);
		spawn();
	}
	
	public void spawn()
	{
		for(int i=0;i<entityList.size();i++)
		{
			entityList.get(i).spawnEntityInWorld();
		}
	}
	
	public void removeEntityFromList(Entity e)
	{
		for(int i=0;i<entityList.size();i++)
			if(entityList.get(i).equals(e))
				entityList.remove(i);
	}
	
	public void removeEntityFromList(int i)
	{
		entityList.remove(i);
	}
	
	public void reloadTextures()
	{
		for(int i=0;i<entityList.size();i++)
		{
			entityList.get(i).reloadTextures();
		}
	}
	
	public void render(boolean editor, float interpolation)
	{
		for(int i=0;i<entityList.size();i++)
		{
			ArrayList<Component> components = entityList.get(i).getComponents();
			for(int x=0;x<components.size();x++)
				components.get(x).renderBefore(editor, interpolation);
			entityList.get(i).render(editor,interpolation);
			for(int x=components.size()-1;x>=0;x--)
				components.get(x).renderAfter(editor, interpolation);
		}
	}
	
	public void tick(int i, int j, int k)
	{
		for(int x=0;x<entityList.size();x++)
		{
			entityList.get(x).tick(i, j, k);
		}
	}
	
	public void set(int x, Entity e)
	{
		entityList.set(x, e);
	}
	
	public int size()
	{
		return entityList.size();
	}
	
	public int indexOf(Entity e)
	{
		return entityList.indexOf(e);
	}
	
	public Entity get(int index)
	{
		return entityList.get(index);
	}
	
	public Entity getEntityWithUUID(String uuid)
	{
		for(Entity e : entityList)
		{
			if(e.getUUID().equals(uuid))
				return e;
		}
		return null;
	}

	public void clear() {
		entityList.clear();
	}
	
}
