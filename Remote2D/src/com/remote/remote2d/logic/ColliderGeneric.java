package com.remote.remote2d.logic;

import org.lwjgl.opengl.GL11;

public class ColliderGeneric extends Collider {

	public ColliderGeneric(Vector2[] newVerts) {
		this.verts = newVerts;
	}

	@Override
	public void updateVerts() {
	}

	@Override
	public boolean isPointInside(Vector2 vec) {
		return false;
	}

	@Override
	public Collider getTransformedCollider(Vector2 trans) {
		Vector2[] newVerts = new Vector2[verts.length];
		for(int x=0;x<verts.length;x++)
		{
			newVerts[x] = new Vector2(verts[x].x+trans.x,verts[x].y+trans.y);
		}
		return new ColliderGeneric(newVerts);
	}

	@Override
	public void drawCollider() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		{
			for(int x=0;x<verts.length;x++)
			{
				GL11.glVertex2f(verts[x].x, verts[x].y);
			}
			GL11.glVertex2f(verts[0].x, verts[0].y);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

}
