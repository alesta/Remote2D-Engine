package com.remote.remote2d.logic;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.art.Renderer;

public class ColliderSphere extends Collider {
	
	public Vector2 pos;
	public float radius;
	
	/**
	 * A simple Sphere Collider
	 * @param x The x-position of the center of the sphere
	 * @param y The y-position of the center of the sphere
	 * @param radius The radius of the sphere
	 */
	
	public ColliderSphere(Vector2 pos, float radius)
	{
		this.pos = pos;
		this.radius = radius;
		updateVerts();
	}

	@Override
	public boolean isPointInside(Vector2 vec) {
		float distFromCenter = (float) Math.sqrt(Math.abs(pos.x-vec.x)+Math.abs(pos.y-vec.y));
		return distFromCenter < radius;
	}
	
	@Override
	public void drawCollider(int color) {
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		Vector2[] verts = new Vector2[360];
		for (int i=0; i <= 360; i++)
		{
			float degInRad = i*(3.14159f/180f);
			double x = Math.cos(degInRad)*radius+pos.x;
			double y = Math.sin(degInRad)*radius+pos.y;
			GL11.glVertex2d(x,y);
		}
		
		Renderer.drawLinePoly(verts, color, 1.0f);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		
		Renderer.drawRect(new Vector2(pos.x-3,pos.y-3), new Vector2(6), color, 1.0f);
	}

	@Override
	public Collider getTransformedCollider(Vector2 trans) {
		return new ColliderSphere(pos.add(trans),radius);
	}

	@Override
	public void updateVerts() {
		verts = new Vector2[360];
		for (int i=0; i < 360; i++)
		{
			float degInRad = i*(3.14159f/180f);
			double x = Math.cos(degInRad)*radius+pos.x;
			double y = Math.sin(degInRad)*radius+pos.y;
			verts[i] = new Vector2((int)x,(int)y);
		}
	}

}
