package com.remote.remote2d.art;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.logic.Vector2D;

public class CursorLoader {
	
	public static Texture tex;
	public static Vector2D hotspot;
	
	public static void setCursor(Texture tex, Vector2D hotspot)
	{
		CursorLoader.tex = tex;
		CursorLoader.hotspot = hotspot;
		
		if(tex != null)
		{
			Cursor emptyCursor;
			try {
				emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
				Mouse.setNativeCursor(emptyCursor);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		} else
		{
			try {
				Mouse.setNativeCursor(null);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void render()
	{
		if(tex != null)
		{
			Vector2D renderpos = new Vector2D(Remote2D.getInstance().getMouseCoords()).subtract(hotspot);
			tex.bind();
			
			GL11.glPushMatrix();
				GL11.glTranslatef(renderpos.x, renderpos.y, 0);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glTexCoord2f(0, 0);
					GL11.glVertex2f(0, 0);
					
					GL11.glTexCoord2f(1, 0);
					GL11.glVertex2f(tex.image.getWidth(), 0);
					
					GL11.glTexCoord2f(1, 1);
					GL11.glVertex2f(tex.image.getWidth(), tex.image.getHeight());
					
					GL11.glTexCoord2f(0, 1);
					GL11.glVertex2f(0, tex.image.getHeight());
				GL11.glEnd();
			GL11.glPopMatrix();
			
		}
	}
	
}
