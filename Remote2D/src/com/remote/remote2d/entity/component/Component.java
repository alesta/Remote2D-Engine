package com.remote.remote2d.entity.component;

import com.remote.remote2d.entity.EditorObject;
import com.remote.remote2d.entity.Entity;

/**
 * A component is an "attachment" to an entity, which modifies its behavior.
 * Components are advantageous because they can be reused on entities, allowing
 * more to be done in-editor and less in code.
 * 
 * @author Flafla2
 */
public abstract class Component extends EditorObject{
	
	protected Entity entity;
	
	public Component(Entity e)
	{
		this.entity = e;
	}
	
	public Class<?> getComponentClass()
	{
		return this.getClass();
	}
	
	/**
	 * A normal tick method for this component IN GAME.  Not in the editor!  Note:
	 * if the entity is static then this isn't called.
	 */
	public abstract void tick(int i, int j, int k);
	/**
	 * A render method for before the entity renders (but not necessarily directly;
	 * other components may get in between).
	 */
	public abstract void renderBefore(boolean editor, float interpolation);
	/**
	 * Triggered when the entity spawns into the world.  Mostly used for components
	 * that set variables for the entity.
	 */
	public abstract void onEntitySpawn();
	/**
	 * A render method for after the entity renders (again, not necessarily directly
	 * after).
	 */
	public abstract void renderAfter(boolean editor, float interpolation);
	
	public abstract Component clone();
	
	public void setEntity(Entity e)
	{
		this.entity = e;
	}
	
}
