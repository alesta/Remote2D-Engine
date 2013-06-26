package com.remote.remote2d.logic;

import org.lwjgl.opengl.GL11;

public class ColliderBox extends Collider{
	
	/**
	 * A simple Axis-Aligned Box Collider
	 */
	
	public Vector2D pos;
	public Vector2D dim;
	
	public ColliderBox(Vector2D pos, Vector2D size)
	{
		this.pos = pos;
		this.dim = size;
		updateVerts();
	}
	
	public Vector2D getPos()
	{
		Vector2D pos = new Vector2D(this.pos.x,this.pos.y);
		
		if(this.dim.x < 0)
		{
			pos.x += dim.x;
		}
		
		if(this.dim.y < 0)
		{
			pos.y += dim.y;
		}
		
		return pos;
	}
	
	public Vector2D getDim()
	{
		Vector2D dim = new Vector2D(this.dim.x,this.dim.y);
		
		if(this.dim.x < 0)
		{
			dim.x = Math.abs(this.dim.x);
		}
		
		if(this.dim.y < 0)
		{
			dim.y = Math.abs(this.dim.y);
		}
		
		return dim;
	}

	@Override
	public boolean isPointInside(Vector2D vec) {
		
		Vector2D pos = getPos();
		Vector2D dim = getDim();
		
		return vec.x > pos.x && vec.y > pos.y && vec.x < pos.x+dim.x && vec.y < pos.y+dim.y;
		
	}

	@Override
	public void drawCollider() {
		Vector2D pos = getPos();
		Vector2D dim = getDim();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		{
			GL11.glVertex2f(pos.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
			GL11.glVertex2f(pos.x, pos.y+dim.y);
			GL11.glVertex2f(pos.x, pos.y);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public Collider getTransformedCollider(Vector2D trans) {
		ColliderBox box = new ColliderBox(pos.add(trans),dim);
		return box;
	}

	@Override
	public void updateVerts() {
		verts = new Vector2D[4];
		verts[0] = new Vector2D(pos.x,pos.y);
		verts[1] = new Vector2D(pos.x+dim.x,pos.y);
		verts[2] = new Vector2D(pos.x+dim.x,pos.y+dim.y);
		verts[3] = new Vector2D(pos.x,pos.y+dim.y);
	}
}
