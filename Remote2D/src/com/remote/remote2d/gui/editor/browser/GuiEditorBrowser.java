package com.remote.remote2d.gui.editor.browser;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorBrowser extends Gui {
	
	private ArrayList<Folder> folderStack;
	private int selectedFile = -1;
	private int doubleClickTimer = 50;
	
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
		
		Fonts.get("Arial").drawString("//..", 10, 20, 20, 0xffffff);
		
		int yOffset = 40;
		for(int x=0;x<currentFolder.folders.size();x++)
		{
			if(selectedFile-1 == x)
				GL11.glColor4f(1,1,1,0.5f);
			
			Fonts.get("Arial").drawString(new File(currentFolder.folders.get(x).getPath()).getName(), 10, yOffset, 20, 0xffffff);
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(pos.x, yOffset+20);
			GL11.glVertex2f(pos.x+dim.x, yOffset+20);
			GL11.glEnd();
			
			bindRGB(0xffffff);
			
			yOffset += 20;
		}
		
		for(int x=0;x<currentFolder.files.size();x++)
		{
			Fonts.get("Arial").drawString(currentFolder.files.get(x).getName(), 10, yOffset, 20, 0xffffff);
			
			if(selectedFile-1-currentFolder.folders.size() == x)
				GL11.glColor4f(1,1,1,0.5f);
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(pos.x, yOffset+20);
			GL11.glVertex2f(pos.x+dim.x, yOffset+20);
			GL11.glEnd();
			
			bindRGB(0xffffff);
			
			yOffset += 20;
		}
	}
	
	
	
}
