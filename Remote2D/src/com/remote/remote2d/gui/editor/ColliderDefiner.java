package com.remote.remote2d.gui.editor;

import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.Vector2;

public abstract class ColliderDefiner {
	
	protected Vector2 hover;
	
	public abstract void click();
	public abstract boolean isDefined();
	public abstract Collider getCollider();
	
	public void hover(float f, float g)
	{
		hover = new Vector2(f,g);
	}
}
