package com.remote.remote2d.world;

import java.util.ArrayList;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.logic.Vector2;

public class Message {
	
	private String prefix;
	private String message;
	private int color;

	public Message(String prefix, String message, int color) {
		this.prefix = prefix;
		this.message = message;
		this.color = color;
	}
	
	/**
	 * Renders the message using the given font.  It also clips the message to
	 * the given width if desired.
	 * @param pos Position to render
	 * @param size Size of the font
	 * @param width Width of the given area (to clip to)
	 */
	public void render(Vector2 pos, int size, int width)
	{
		ArrayList<String> set = new ArrayList<String>();
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
			Fonts.get("Arial").drawString(s, pos.x, pos.y+currentY, size, color);
			currentY += size;
		}
	}
	
	public String toString()
	{
		return "["+prefix+"]"+" "+message;
	}

	public int getRenderHeight(int width, int size) {
		if(width > 0)
		{
			ArrayList<String> set = new ArrayList<String>();
			int h = 0;
			set = Fonts.get("Arial").getStringSet(toString(), size, width);
			for(String s : set)
				h += Fonts.get("Arial").getStringDim(s, size)[1];
			return h;
		}
		else
			return Fonts.get("Arial").getStringDim(toString(), size)[1];
	}

}
