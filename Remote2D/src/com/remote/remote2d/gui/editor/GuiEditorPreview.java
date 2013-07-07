package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.editor.inspector.GuiEditorInspector;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorPreview extends Gui {
	
	private GuiEditorInspector inspector;
	
	public Vector2 pos;
	public Vector2 dim;
	
	public GuiEditorPreview(GuiEditorInspector inspector, Vector2 pos, Vector2 dim)
	{
		this.pos = pos;
		this.dim = dim;
		this.inspector = inspector;
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
		bindRGB(0xffffff);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Fonts.get("Arial").drawString("Preview", pos.x, pos.y, 20, 0xffffff);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(pos.x, pos.y+20);
		GL11.glVertex2f(pos.x+dim.x, pos.y+20);
		GL11.glEnd();
		
		GL11.glScissor((int)pos.x, (int)(Remote2D.getInstance().displayHandler.height-pos.y-dim.y), (int)dim.x, (int)dim.y-20);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		if(inspector.currentEntity != null)
		{
			GL11.glPushMatrix();
			if(inspector.currentEntity instanceof Entity)
			{
				GL11.glTranslatef(pos.x+dim.x/2-((Entity)inspector.currentEntity).getDim().x/2, pos.y+dim.y/2-((Entity)inspector.currentEntity).getDim().y/2, 0);
				((Entity)inspector.currentEntity).renderPreview(interpolation);
			}
			GL11.glPopMatrix();
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
	}

}
