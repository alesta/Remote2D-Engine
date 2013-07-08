package com.remote.remote2d.art;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.logic.Vector2;

public class CursorLoader {
	
	public static String tex;
	public static Vector2 hotspot;
	
	public static void setCursor(String tex, Vector2 hotspot)
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
	
	public static void render(float interpolation)
	{
		Texture tex = Remote2D.getInstance().artLoader.getTexture(CursorLoader.tex);
		if(tex != null)
		{
			Vector2 renderpos = new Vector2(Remote2D.getInstance().getMouseCoords()).subtract(hotspot);
			
			Vector2 dim = new Vector2(tex.image.getWidth(),tex.image.getHeight());
			Renderer.drawRect(renderpos, dim, tex, 0xffffff, 1);
			
		}
	}
	
}
