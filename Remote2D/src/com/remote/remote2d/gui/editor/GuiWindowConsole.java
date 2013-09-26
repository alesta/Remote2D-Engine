package com.remote.remote2d.gui.editor;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.world.Console;
import com.remote.remote2d.world.Message;

public class GuiWindowConsole extends GuiWindow {
	
	public ArrayList<ConsoleMessage> messages;
	private long lastUpdateTime = -1;
	private float oldOffset = 0;
	private float offset = 0;

	public GuiWindowConsole(WindowHolder holder, Vector2 pos, Vector2 dim, ColliderBox allowedBounds) {
		super(holder, pos, dim, allowedBounds, "Console");
		messages = new ArrayList<ConsoleMessage>();
		updateEntries();
	}

	@Override
	public void renderContents(float interpolation) {
		float offset = (float) Interpolator.linearInterpolate(oldOffset, this.offset, interpolation);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -offset, 0);
		for(ConsoleMessage message : messages)
		{
			if((message.pos.y > offset && message.pos.y < offset+dim.y) || (message.pos.y+message.dim.y > offset && message.pos.y+message.dim.y < offset+dim.y))
			message.render(interpolation);
		}
		GL11.glPopMatrix();
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i,j,k);
		oldOffset = offset;
		if(pos.getColliderWithDim(dim).isPointInside(new Vector2(i,j)))
		{
			if(Remote2D.getInstance().getDeltaWheel() < 0)
				offset += 20;
			if(Remote2D.getInstance().getDeltaWheel() > 0)
				offset -= 20;
			
			if(offset > getTotalSectionHeight()-dim.y+20)
				offset = getTotalSectionHeight()-dim.y+20;
			if(offset < 0)
				offset = 0;
		}
		if(lastUpdateTime < Console.getLastMessageTime())
		{
			updateEntries();
			lastUpdateTime = System.currentTimeMillis();
		}
	}
	
	public void updateEntries()
	{
		messages.clear();
		int ypos = 0;
		for(int x=0;x<Console.size();x++)
		{
			ConsoleMessage mess = new ConsoleMessage(Console.getMessage(x),new Vector2(0,ypos),(int)dim.x);
			messages.add(mess);
			ypos += mess.getDim().y;
		}
	}
	
	public float getTotalSectionHeight()
	{
		float value = 0;
		for(ConsoleMessage message : messages)
			value += message.getDim().y;
		return value;
	}
	
	private class ConsoleMessage
	{
		private Message message;
		public Vector2 pos;
		private Vector2 dim;
		
		public ConsoleMessage(Message message, Vector2 pos, int width)
		{
			this.message = message;
			this.pos = pos.copy();
			this.dim = new Vector2(width,message.getRenderHeight(width,20));
		}
		
		public void render(float interpolation)
		{
			message.render(pos,20,(int)dim.x);
			Renderer.drawLine(new Vector2(pos.x,pos.y+dim.y), pos.add(dim), 0xffffff, 1.0f);
		}
		
		public Vector2 getDim()
		{
			return dim;
		}
	}

}
