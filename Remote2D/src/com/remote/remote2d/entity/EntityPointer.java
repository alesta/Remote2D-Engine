package com.remote.remote2d.entity;

import com.remote.remote2d.world.Map;

public class EntityPointer {
	
	private String uuid;
	private Map map;

	public EntityPointer(String uuid, Map map)
	{
		this.uuid = uuid;
		this.map = map;
	}
	
	public EntityPointer(Entity entity, Map map)
	{
		this(entity.getUUID(),map);
	}
	
	public Entity getEntity()
	{
		if(map != null)
			return map.getEntityList().getEntityWithUUID(uuid);
		else
			return null;
	}
	
	public String uuid()
	{
		return uuid;
	}

}
