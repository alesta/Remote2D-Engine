package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.ColliderMatrixBox;
import com.remote.remote2d.logic.Vector2D;
import com.remote.remote2d.logic.Vector2DF;

public class GuiWindowCollisionTest extends GuiWindow {
	
	ColliderMatrixBox collider;
	boolean collides = false;

	public GuiWindowCollisionTest(WindowHolder holder, Vector2D pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2D(500,500), allowedBounds, "Matrix Collision Test");
		
		collider = new ColliderMatrixBox(new Vector2DF(150,200),new Vector2DF(200,100));
		
	}

	@Override
	public void renderContents() {
		GL11.glColor3f(collides ? 1 : 0, collides ? 0 : 1, 0);
		collider.drawCollider();
		GL11.glColor3f(1, 1, 1);
	}
	
	boolean scaleUp = true;
	@Override
	public void tick(int i, int j, int k, double delta)
	{
		super.tick(i, j, k, delta);
		
		collides = collider.isPointInside(getMouseInWindow(i,j));
		collider.setAngle(collider.getAngle()+0.01f*(float)delta);
		
		if(collider.getScale().x > 2.5f || collider.getScale().x < 0.5f)
		{
			scaleUp = !scaleUp;
			float y = Math.min(Math.max(collider.getScale().x,0.5f), 2.5f);
			collider.setScale(new Vector2DF(y,y));
		} else
		{
			float x = collider.getScale().x+(float)((scaleUp ? 0.05f : -0.05f)*delta);
			collider.setScale(new Vector2DF(x,x));
		}
			
		
	}

}
