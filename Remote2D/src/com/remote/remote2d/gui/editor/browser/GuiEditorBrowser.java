package com.remote.remote2d.gui.editor.browser;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorBrowser extends Gui {
	
	private ArrayList<Folder> folderStack;
	private int selectedFile = -1;
	private int doubleClickTimer = 0;
	
	public Vector2D pos;
	public Vector2D dim;
	
	public GuiEditorBrowser(Vector2D pos, Vector2D dim)
	{
		folderStack = new ArrayList<Folder>();
		folderStack.add(new Folder(Remote2D.getJarPath().getPath()));
		
		this.pos = pos;
		this.dim = dim;
	}

	@Override
	public void tick(int i, int j, int k) {
		
		Folder currentFolder = folderStack.get(folderStack.size()-1);
		
		if(Remote2D.getInstance().hasMouseBeenPressed() && pos.getColliderWithDim(dim).isPointInside(new Vector2D(i,j)))
		{
			int yPos = j-pos.y-20;
			if(yPos < (currentFolder.files.size()+currentFolder.folders.size()+1)*20)
			{
				if(selectedFile != yPos/20)
				{
					selectedFile = yPos/20;
					doubleClickTimer = 50;
				} else if(doubleClickTimer > 0)
				{
					doubleClickTimer = 0;
					if(selectedFile == 0)
					{
						if(folderStack.size() > 1)
							folderStack.remove(folderStack.size()-1);
					}
					else if(selectedFile-1 < currentFolder.files.size())
						Log.info("Browser is opening file: "+currentFolder.files.get(selectedFile-1).getPath());
					else if(selectedFile-1-currentFolder.files.size() < currentFolder.folders.size())
						folderStack.add(currentFolder.folders.get(selectedFile-1-currentFolder.files.size()));
				}
			} else
			{
				selectedFile = -1;
				doubleClickTimer = 0;
			}
		} else if(Remote2D.getInstance().hasMouseBeenPressed())
		{
			selectedFile = -1;
			doubleClickTimer = 0;
		}
		
		doubleClickTimer--;
		
	}

	@Override
	public void render(float interpolation) {
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0,0,0,0.5f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y+dim.y);
			GL11.glVertex2f(pos.x,pos.y+dim.y);
		GL11.glEnd();
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(pos.x, pos.y+20);
		GL11.glVertex2f(pos.x+dim.x, pos.y+20);
		GL11.glVertex2f(pos.x, pos.y+40);
		GL11.glVertex2f(pos.x+dim.x, pos.y+40);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Fonts.get("Arial").drawString("Browser", pos.x+10, pos.y, 20, 0xffffff);
		
		Folder currentFolder = folderStack.get(folderStack.size()-1);
		
		Fonts.get("Arial").drawString("...", pos.x+10, pos.y+20, 20, folderStack.size() == 1 ? 0x999999 : 0xffffff);
		int yOffset = 20;
		if(selectedFile == 0)
		{
			GL11.glColor4f(1,1,1,0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x,  pos.y+yOffset);
			GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset);
			GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset+20);
			GL11.glVertex2f(pos.x,  pos.y+yOffset+20);
			GL11.glEnd();
			GL11.glColor4f(1,1,1,1);
		}
		yOffset = 40;
		
		for(int x=0;x<currentFolder.folders.size();x++)
		{
			if(selectedFile-1 == x)
			{
				GL11.glColor4f(1,1,1,0.5f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(pos.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset+20);
				GL11.glVertex2f(pos.x,  pos.y+yOffset+20);
				GL11.glEnd();
				GL11.glColor4f(1,1,1,1);
			}
			
			Fonts.get("Arial").drawString(new File(currentFolder.folders.get(x).getPath()).getName(), pos.x+10, pos.y+yOffset, 20, 0xffffff);
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(pos.x, pos.y+yOffset+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+yOffset+20);
			GL11.glEnd();
			
			bindRGB(0xffffff);
			
			yOffset += 20;
		}
		
		for(int x=0;x<currentFolder.files.size();x++)
		{
			Fonts.get("Arial").drawString(currentFolder.files.get(x).getName(), pos.x+10, pos.y+yOffset, 20, 0xffffff);
			
			if(selectedFile-1-currentFolder.folders.size() == x)
			{
				GL11.glColor4f(1,1,1,0.5f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(pos.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset+20);
				GL11.glVertex2f(pos.x,  pos.y+yOffset+20);
				GL11.glEnd();
				GL11.glColor4f(1,1,1,1);
			}
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(pos.x, pos.y+yOffset+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+yOffset+20);
			GL11.glEnd();
			
			bindRGB(0xffffff);
			
			yOffset += 20;
		}
	}
	
	
	
}
