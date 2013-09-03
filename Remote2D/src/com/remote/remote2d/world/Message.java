package com.remote.remote2d.world;

import java.util.ArrayList;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.logic.Vector2;

public class Message {
	
	public String prefix;
	public String message;
	public int color;

	public Message(String prefix, String message, int color) {
		this.prefix = prefix;
		this.message = message;
		this.color = color;
	}
	
	/**
	 * Renders the message using the given font.  It also clips the message to
	 * the given width if desired.
	 * @param pos Position to render
	 * @param font Name of the font
	 * @param size Size of the font
	 * @param width Width of the clipping size, this is ignored if <= 0
	 */
	public void render(Vector2 pos, String font, int size, int width)
	{
		ArrayList<String> set;
		if(width > 0)
			set = Fonts.get("Arial").getStringSet(toString(), size, width);
		else
		{
			set = new ArrayList<String>();
			set.add(toString());
		}
		
		int currentY = 0;
		for(String s : set)
		{
			Fonts.get(font).drawString(s, pos.x, pos.y+currentY, size, color);
			currentY += size;
		}
	}
	
	public void render(Vector2 pos, String font, int size)
	{
		render(pos,font,size,-1);
	}
	
	public void print()
	{
		Log.info("CONSOLE", toString());
	}
	
	public String toString()
	{
		return "["+prefix+"]"+" "+message;
	}

}
