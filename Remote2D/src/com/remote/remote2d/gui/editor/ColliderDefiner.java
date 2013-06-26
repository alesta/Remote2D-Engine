package com.remote.remote2d.gui.editor;

import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.Vector2D;

public abstract class ColliderDefiner {
	
	protected Vector2D hover;
	
	public abstract void click();
	public abstract boolean isDefined();
	public abstract Collider getCollider();
	
	public void hover(int i, int j)
	{
		hover = new Vector2D(i,j);
	}
}
