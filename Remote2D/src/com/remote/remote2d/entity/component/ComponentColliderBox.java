package com.remote.remote2d.entity.component;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2D;

public class ComponentColliderBox extends Component {
	
	public Vector2D pos;
	public Vector2D dim;
	
	private ColliderBox currentCollider;

	public ComponentColliderBox(Entity e) {
		super(e);
		pos = new Vector2D(0,0);
		dim = new Vector2D(10,10);
		currentCollider = pos.getColliderWithDim(dim);
	}

	@Override
	public void tick(int i, int j, int k) {
		
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}
	
	public void setCurrentCollider(ColliderBox c)
	{
		this.currentCollider = c;
	}

	@Override
	public void onEntitySpawn() {
		if(currentCollider != null)
			entity.addCollider(currentCollider);
 	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(currentCollider != null && editor)
		{
			GL11.glColor3f(1, 0, 0);
			currentCollider.getTransformedCollider(entity.pos).drawCollider();
			GL11.glColor3f(1, 1, 1);
		}
	}

	@Override
	public void apply() {
		currentCollider = pos.getColliderWithDim(dim);
	}

	@Override
	public Component clone() {
		ComponentColliderBox newColl = new ComponentColliderBox(entity);
		newColl.pos = this.pos;
		newColl.dim = this.dim;
		newColl.setCurrentCollider(currentCollider);
		return newColl;
	}
}