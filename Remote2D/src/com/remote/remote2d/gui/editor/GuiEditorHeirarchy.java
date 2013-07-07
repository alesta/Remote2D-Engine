package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorHeirarchy extends GuiMenu {
	
	public Vector2 pos = new Vector2(0,20);
	public Vector2 dim;
	private GuiEditor editor;
	
	public GuiEditorHeirarchy(Vector2 pos, Vector2 dim, GuiEditor editor)
	{
		this.editor = editor;
		this.pos = pos;
		this.dim = dim;
	}
	
	@Override
	public void initGui()
	{
		
	}

	@Override
	public void tick(int i, int j, int k) {
		if(editor.getMap() == null)
			return;
		if(Remote2D.getInstance().hasMouseBeenPressed() && pos.getColliderWithDim(dim).isPointInside(new Vector2(i,j)))
		{
			float currentYPos = pos.y;
			int index = -1;
			for(int x=0;x<editor.getMap().getEntityList().size();x++)
			{
				Vector2 currentPos = new Vector2(pos.x,currentYPos);
				if(currentPos.getColliderWithDim(new Vector2(dim.x,20)).isPointInside(new Vector2(i,j)))
					index = x;
				currentYPos += 20;
			}
			editor.setSelectedEntity(index);
		}
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
		GL11.glColor4f(1, 1, 1, 1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if(editor.getMap() == null)
			return;
		
		float currentYPos = pos.y;
		for(int x=0;x<editor.getMap().getEntityList().size();x++)
		{
			int textColor = 0xffffff;
			if(editor.getMap().getEntityList().get(x).equals(editor.getSelectedEntity()))
			{
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor4f(1, 1, 1, 0.5f);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(pos.x,currentYPos);
					GL11.glVertex2f(pos.x+dim.x,currentYPos);
					GL11.glVertex2f(pos.x+dim.x,currentYPos+20);
					GL11.glVertex2f(pos.x,currentYPos+20);
				GL11.glEnd();
				GL11.glColor4f(1, 1, 1, 1);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				//textColor = 0x000000;
			}
			GL11.glBegin(GL11.GL_LINES);
				GL11.glVertex2f(pos.x,currentYPos+20);
				GL11.glVertex2f(pos.x+dim.x,currentYPos+20);
			GL11.glEnd();
			
			String name = editor.getMap().getEntityList().get(x).name;
			if(name.trim().equals(""))
				name = "Untitled";
			Fonts.get("Arial").drawString(name, pos.x, currentYPos, 20, textColor);
			currentYPos += 20;
		}
		
		GL11.glColor4f(1, 1, 1, 1);
		
	}
	
}
