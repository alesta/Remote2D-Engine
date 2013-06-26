package com.remote.remote2d.entity;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.logic.Vector2D;

public class EntityEmpty extends Entity {
	
	private static final String slashLoc = "/res/gui/slash.png";
	private static final int color = 0xffff00;
	
	public Vector2D dim = new Vector2D(50,50);

	@Override
	public void reloadTextures() {
		
	}

	@Override
	public Vector2D getDim() {
		return dim;
	}

	@Override
	public void render(boolean editor) {
		if(editor)
		{
			Texture tex = Remote2D.getInstance().artLoader.getTexture(slashLoc, false, true);
			float maxX = ((float)dim.x)/32f;
			float maxY = ((float)dim.y)/32f;
			Log.debug(maxX+" "+maxY);
			tex.bind();
			Gui.bindRGB(color);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(pos.x,pos.y);
				GL11.glTexCoord2f(maxX, 0);
				GL11.glVertex2f(pos.x+dim.x, pos.y);
				GL11.glTexCoord2f(maxX, maxY);
				GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
				GL11.glTexCoord2f(0, maxY);
				GL11.glVertex2f(pos.x, pos.y+dim.y);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex2f(pos.x,pos.y);
				GL11.glVertex2f(pos.x+dim.x, pos.y);
				GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
				GL11.glVertex2f(pos.x, pos.y+dim.y);
				GL11.glVertex2f(pos.x,pos.y);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			Gui.bindRGB(0xffffff);
		}
	}

	@Override
	public Entity clone() {
		EntityEmpty next = new EntityEmpty();
		next.pos = this.pos;
		next.name = this.name;
		next.isStatic = this.isStatic;
		next.dim = this.dim.copy();
		for(int x=0;x<colliders.size();x++)
			next.addCollider(colliders.get(x));
		for(int x=0;x<children.size();x++)
			next.addChild(children.get(x));
		for(int x=0;x<getComponents().size();x++)
			next.addComponent(getComponents().get(x).clone());
		return next;
	}

	@Override
	public void apply() {
		
	}

}
