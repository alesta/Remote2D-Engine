package com.remote.remote2d.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2D;

public abstract class GuiWindow extends Gui {
	
	public Vector2D pos;
	public Vector2D dim;
	public ColliderBox allowedBounds;
	public String title;
	
	protected ArrayList<GuiButton> buttonList;
	protected WindowHolder holder;
	protected boolean isSelected;
	
	private Vector2D dragOffset = new Vector2D(-1,-1);
	private boolean isDragging = false;
	
	private final int windowTopColor = 0xff5555;
	private final int windowMainColor = 0xffbbbb;
	
	public GuiWindow(WindowHolder holder, Vector2D pos, Vector2D dim, ColliderBox allowedBounds, String title)
	{
		this.holder = holder;
		this.pos = pos;
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
	public void tick(int i, int j, int k, double delta) {
		boolean buttonOverride = false;
		
		if(!pos.getColliderWithDim(dim.add(new Vector2D(20,20))).isPointInside(new Vector2D(i,j)) && Remote2D.getInstance().hasMouseBeenPressed())
		{
			isSelected = false;
			buttonOverride = true;
		}
		if(!isSelected)
		{
			if(pos.getColliderWithDim(dim.add(new Vector2D(20,20))).isPointInside(new Vector2D(i,j)) && Remote2D.getInstance().hasMouseBeenPressed())
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
		
		if(pos.getColliderWithDim(new Vector2D(dim.x,20)).isPointInside(new Vector2D(i,j)) && k == 1)
		{
			isDragging = true;
		} else if(k == 0)
			isDragging = false;
		
		if(isDragging)
		{
			if(dragOffset.x == -1 || dragOffset.y == -1)
				dragOffset = new Vector2D(i-pos.x,j-pos.y);
			else
			{
				int x = i-dragOffset.x;
				int y = j-dragOffset.y;
				if(y < allowedBounds.pos.y)
					y = allowedBounds.pos.y;
				
				pos = new Vector2D(x,y);
			}
		} else
			dragOffset = new Vector2D(-1,-1);
		
		for(int x=0;x<buttonList.size();x++)
		{
			buttonList.get(x).tick(getMouseInWindow(i,j).x, getMouseInWindow(i,j).y, k, delta);
			if(buttonList.get(x).selectState == 2 || buttonList.get(x).selectState == 3)
			{
				if(Remote2D.getInstance().hasMouseBeenPressed() && !buttonOverride)
				{
					actionPerformed(buttonList.get(x));
				}
			}
		}
	}
	
	public abstract void renderContents();
	
	public Vector2D getMouseInWindow(int i, int j)
	{
		return new Vector2D(i-pos.x,j-pos.y-20);
	}

	@Override
	public void render() {
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float[] rgbtop = getRGB(isSelected ? windowTopColor : windowMainColor);
		float[] rgbbot = getRGB(windowMainColor);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(rgbtop[0], rgbtop[1], rgbtop[2]);
			GL11.glVertex2f(pos.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y+20);
			GL11.glVertex2f(pos.x, pos.y+20);
			
			GL11.glColor3f(rgbbot[0], rgbbot[1], rgbbot[2]);
			GL11.glVertex2f(pos.x, pos.y+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+20+dim.y);
			GL11.glVertex2f(pos.x, pos.y+20+dim.y);
		GL11.glEnd();
		GL11.glColor3f(1, 1, 1);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Fonts.get("Arial").drawString(title, pos.x+10, pos.y+1, 20, 0xffffff);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(pos.x, Remote2D.getInstance().displayHandler.height-(pos.y+dim.y+20), dim.x, dim.y);
		
		GL11.glPushMatrix();
			GL11.glTranslatef(pos.x,pos.y+20,0);
			renderContents();
			for(int x=0;x<buttonList.size();x++)
				buttonList.get(x).render();
			GL11.glTranslatef(-pos.x,-pos.y-20,0);
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glColor3f(0, 0, 0);
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
			
			GL11.glVertex2f(pos.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y+dim.y+20);
			GL11.glVertex2f(pos.x,pos.y+dim.y+20);
			GL11.glVertex2f(pos.x,pos.y);
			
		GL11.glEnd();
		
		GL11.glColor3f(0.4f, 0.4f, 0.4f);
		
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

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void actionPerformed(GuiButton button)
	{
		
	}

}
