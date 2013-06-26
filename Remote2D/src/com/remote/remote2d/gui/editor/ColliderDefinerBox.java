package com.remote.remote2d.gui.editor;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.Vector2D;

public class ColliderDefinerBox extends ColliderDefiner {
	
	private Vector2D point1 = new Vector2D(-1,-1);
	private Vector2D point2 = new Vector2D(-1,-1);

	@Override
	public void click() {
		if(point1.x == -1)
			point1 = new Vector2D(hover.x,hover.y);
		else if(point2.x == -1)
			point2 = new Vector2D(hover.x,hover.y);
	}

	@Override
	public boolean isDefined() {
		return point1.x != -1 && point2.x != -1;
	}

	@Override
	public Collider getCollider() {
		if(hover == null)
			hover = new Vector2D(0,0);
		Vector2D point1 = this.point1.x==-1 ? hover : this.point1;
		Vector2D point2 = this.point2.x==-1 ? hover : this.point2;
		return point1.getColliderWithDim(point2.subtract(point1));
	}

}
