package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.art.Fonts;

public class GuiEditorTopMenuSection extends Gui {
	
	public static final int subheight = 20;
	
	GuiEditorTopMenu menu;
	
	String[] values;
	String title;
	int x;
	int y;
	int width;
	int subWidth;
	int height;
	int[] titledim;
	private boolean isEnabled = true;
	
	boolean isHovered = false;
	boolean isSelected = false;
	private int hoveredBox = -1;	//the drop down subsection that is currently hovered over
	private int selectedBox = -1;  	//when a box is physically clicked on, this changes.
									//It is used to communicate between this class and the menu class
									//via popSelectedBox()
	
	public GuiEditorTopMenuSection(int x, int y, int h, String[] values, String title, GuiEditorTopMenu menu)
	{
		super();
		
		this.x = x;
		this.y = y;
		this.menu = menu;
		height = h;
		this.values = values;
		this.title = title;
		titledim = Fonts.get("Arial").getStringDim(title, 20);
		width = titledim[0]+20;
		
		subWidth = width;
		for(int i=0;i<values.length;i++)
		{
			int thisWidth = Fonts.get("Arial").getStringDim(values[i], 20)[0];
			if(thisWidth > subWidth)
				subWidth = thisWidth;
		}
		subWidth += 20;
	}
	
	public void setEnabled(boolean enabled)
	{
		if(isEnabled != enabled)
		{
			Log.debug("Setting enabled of: "+title+" : "+isEnabled + " : "+enabled);
			isEnabled = enabled;
			menu.initSections();
			
			//Log.debug("Setting enabled of: "+title+" : "+isEnabled + " : "+enabled);
		}
	}
	
	public boolean getEnabled()
	{
		return isEnabled;
	}

	@Override
	public void render() {
		if(!isEnabled)
			return;
		int xPos = x+width/2-titledim[0]/2;
		int yPos = y+height/2-titledim[1]/2;
		
		if(isSelected)
		{
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glColor4f(1, 0.6f, 0.6f, 1);
			GL11.glPushMatrix();
				GL11.glTranslatef(x, y, 0);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(0, 0);
					GL11.glVertex2f(width, 0);
					GL11.glVertex2f(width,height);
					GL11.glVertex2f(0,height);
				GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glColor4f(1, 1, 1, 1);
		}
		
		Fonts.get("Arial").drawString(title, xPos, yPos, 20, 0xffffff);
		
		if(!isSelected)
			return;
		int currentY = height;
		
		for(int i=0;i<values.length;i++)
		{
			boolean isBoxHovered = hoveredBox == i;
			GL11.glColor4f(1, isBoxHovered?0.6f:0.2f, isBoxHovered?0.6f:0.2f, 1);
			GL11.glPushMatrix();
			
				GL11.glTranslatef(x, currentY, 0);
				
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(0, 0);
					GL11.glVertex2f(subWidth, 0);
					GL11.glVertex2f(subWidth,height);
					GL11.glVertex2f(0,height);
				GL11.glEnd();
				
				GL11.glColor3f(0, 0, 0);
				GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex2f(0, 0);
					GL11.glVertex2f(subWidth, 0);
					GL11.glVertex2f(subWidth,height);
					GL11.glVertex2f(0,height);
					GL11.glVertex2f(0, 0);
				GL11.glEnd();
				
			GL11.glPopMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			
			Fonts.get("Arial").drawString(values[i],x+10,currentY, 20, 0xffffff);
			currentY += subheight;
		}
	}
	
	boolean isMouseDownAccountedFor = false;

	@Override
	public void tick(int i, int j, int k, double delta) {
		if(!isEnabled)
			return;
		isHovered = i > x && j > y && i < x+width && j < y+height;
		if (k==1 && !isMouseDownAccountedFor)
		{
			if(isHovered)
			{
				isSelected = !isSelected;
			}
			isMouseDownAccountedFor = true;
		} else if(k==0)
			isMouseDownAccountedFor = false;
		
		if(isSelected && !isHovered)
		{
			for(int x=0;x<menu.sections.size();x++)
			{
				if(menu.sections.get(x).isHovered)
				{
					menu.sections.get(x).isSelected = true;
					isSelected = false;
				}
			}
		}
		
		if(isSelected)
		{
			int currentY = height;
			hoveredBox = -1;
			for(int a=0;a<values.length;a++)
			{
				boolean isBoxHovered = i > x && j > currentY && i < x+subWidth && j < currentY+subheight;
				if(isBoxHovered)
				{
					hoveredBox = a;
					a=values.length;//stop the loop
				}
				
				currentY += subheight;
			}
			if(!isHovered && k != 0)
			{
				isSelected = false;
				if(hoveredBox != -1)
				{
					selectedBox = hoveredBox;
				}
			}
		}
	}
	
	public int popSelectedBox()
	{
		int box = selectedBox;
		selectedBox = -1;
		return box;
	}

}
