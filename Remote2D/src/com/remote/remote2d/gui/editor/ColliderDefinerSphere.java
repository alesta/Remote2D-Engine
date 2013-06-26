package com.remote.remote2d.gui.editor;

import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.ColliderSphere;
import com.remote.remote2d.logic.Vector2D;

public class ColliderDefinerSphere extends ColliderDefiner {
	
	Vector2D origin;
	int radius = -1;

	@Override
	public void click() {
		if(origin == null)
			origin = hover.copy();
		else if(radius == -1)
		{
			int a = Math.abs(hover.x-origin.x);
			int b = Math.abs(hover.y-origin.y);
			radius = (int)Math.sqrt(a*a+b*b);
		}
	}

	@Override
	public boolean isDefined() {
		return origin != null && radius != -1;
	}

	@Override
	public Collider getCollider() {
		if(hover == null || origin == null)
			return null;
		int a = Math.abs(hover.x-origin.x);
		int b = Math.abs(hover.y-origin.y);
		return isDefined() ? new ColliderSphere(origin,radius) : new ColliderSphere(origin,(int)Math.sqrt(a*a+b*b));
	}

}
