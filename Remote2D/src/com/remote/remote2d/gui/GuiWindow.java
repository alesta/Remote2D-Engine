package com.remote.remote2d.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.logic.Vector2;

public abstract class GuiWindow extends Gui {
	
	public Vector2 pos;
	public Vector2 dim;
	public ColliderBox allowedBounds;
	public String title;
	
	protected ArrayList<GuiButton> buttonList;
	protected WindowHolder holder;
	protected boolean isSelected;
	
	private Vector2 dragOffset = new Vector2(-1,-1);
	private Vector2 oldPos;
	private boolean isDragging = false;
	private boolean dontTick = false;
	
	private final int windowTopColor = 0xff5555;
	private final int windowMainColor = 0xffbbbb;
	
	public GuiWindow(WindowHolder holder, Vector2 pos, Vector2 dim, ColliderBox allowedBounds, String title)
	{
		this.holder = holder;
		this.pos = pos;
		this.oldPos = pos.copy();
		this.dim = dim;
		this.title = title;
		this.allowedBounds = allowedBounds;
		this.isSelected = false;
		
		buttonList = new ArrayList<GuiButton>();
		initGui();
	}
	
	public void initGui()
	{
		
	}

	@Override
	public void tick(int i, int j, int k) {
		if(dontTick)
		{
			dontTick  = false;
			return;
		}
		
		oldPos = pos.copy();
		
		boolean buttonOverride = false;
		
		if(!pos.getColliderWithDim(dim.add(new Vector2(20,20))).isPointInside(new Vector2(i,j)) && Remote2D.getInstance().hasMouseBeenPressed())
		{
			isSelected = false;
			buttonOverride = true;
		}
		if(!isSelected)
		{
			if(pos.getColliderWithDim(dim.add(new Vector2(20,20))).isPointInside(new Vector2(i,j)) && Remote2D.getInstance().hasMouseBeenPressed())
			{
				holder.attemptToPutWindowOnTop(this);
				buttonOverride = true;
			}
			if(!isSelected)
			{
				for(int x=0;x<buttonList.size();x++)
				{
					if(!buttonList.get(x).getDisabled())
						buttonList.get(x).selectState = 1;
				}
				return;
			}
		}
		
		if(pos.getColliderWithDim(new Vector2(dim.x,20)).isPointInside(new Vector2(i,j)) && k == 1)
		{
			isDragging = true;
		} else if(k == 0)
			isDragging = false;
		
		if(isDragging)
		{
			if(dragOffset.x == -1 || dragOffset.y == -1)
				dragOffset = new Vector2(i-pos.x,j-pos.y);
			else
			{
				float x = i-dragOffset.x;
				float y = j-dragOffset.y;
				if(y < allowedBounds.pos.y)
					y = allowedBounds.pos.y;
				
				pos = new Vector2(x,y);
			}
		} else
			dragOffset = new Vector2(-1,-1);
		
		for(int x=0;x<buttonList.size();x++)
		{
			buttonList.get(x).tick((int)getMouseInWindow(i,j).x, (int)getMouseInWindow(i,j).y, k);
			if(buttonList.get(x).selectState == 2 || buttonList.get(x).selectState == 3)
			{
				if(Remote2D.getInstance().hasMouseBeenPressed() && !buttonOverride)
				{
					actionPerformed(buttonList.get(x));
				}
			}
		}
	}
	
	public abstract void renderContents(float interpolation);
	
	public Vector2 getMouseInWindow(int i, int j)
	{
		return new Vector2(i-pos.x,j-pos.y-20);
	}

	@Override
	public void render(float interpolation) {
		
		Vector2 pos = Interpolator.linearInterpolate2f(oldPos, this.pos, interpolation);
		
		Renderer.drawRect(pos, new Vector2(dim.x,20), isSelected ? windowTopColor : windowMainColor, 1.0f);
		Renderer.drawRect(pos.add(new Vector2(0,20)), dim, windowMainColor, 1.0f);
		
		Fonts.get("Arial").drawString(title, pos.x+10, pos.y+1, 20, 0xffffff);
		Renderer.startScissor(new Vector2(pos.x,pos.y+20), dim);
		
		GL11.glPushMatrix();
			GL11.glTranslatef(pos.x,pos.y+20,0);
			renderContents(interpolation);
			for(int x=0;x<buttonList.size();x++)
				buttonList.get(x).render(interpolation);
			GL11.glTranslatef(-pos.x,-pos.y-20,0);
		GL11.glPopMatrix();
		
		Renderer.endScissor();
		
		
		Renderer.drawLineRect(pos, dim.add(new Vector2(0,20)), 0x000000, 1.0f);
		
		GL11.glColor3f(0.4f, 0.4f, 0.4f);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glBegin(GL11.GL_LINES);
		
			GL11.glVertex2f(pos.x, pos.y+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+20);
		
		GL11.glEnd();
		
		GL11.glColor3f(1, 1, 1);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public GuiWindow setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		return this;
	}
	
	public void dontTick()
	{
		dontTick = true;
	}

	public void actionPerformed(GuiButton button)
	{
		
	}

}
