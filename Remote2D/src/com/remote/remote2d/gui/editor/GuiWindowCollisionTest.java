package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.ColliderMatrixBox;
import com.remote.remote2d.logic.Vector2;

public class GuiWindowCollisionTest extends GuiWindow {
	
	ColliderMatrixBox collider;
	boolean collides = false;

	public GuiWindowCollisionTest(WindowHolder holder, Vector2 pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2(500,500), allowedBounds, "Matrix Collision Test");
		
		collider = new ColliderMatrixBox(new Vector2(150,200),new Vector2(200,100));
		
	}

	@Override
	public void renderContents(float interpolation) {
		collider.drawCollider(collides ? 0xff0000 : 0x00ff00);
	}
	
	boolean scaleUp = true;
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
		collides = collider.isPointInside(new Vector2(getMouseInWindow(i,j).getElements()));
		collider.setAngle(collider.getAngle()+0.01f);
		
		if(collider.getScale().x > 2.5f || collider.getScale().x < 0.5f)
		{
			scaleUp = !scaleUp;
			float y = Math.min(Math.max(collider.getScale().x,0.5f), 2.5f);
			collider.setScale(new Vector2(y,y));
		} else
		{
			float x = collider.getScale().x+(float)((scaleUp ? 0.05f : -0.05f));
			collider.setScale(new Vector2(x,x));
		}
			
		
	}

}
