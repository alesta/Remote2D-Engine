package com.remote.remote2d.entity;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.Collision;
import com.remote.remote2d.logic.Vector2D;

/**
 * The basic class for all moving things in the game.  Things such as the player,
 * bullets, enemies, mechanized objects, etc. Anything that needs to tick().  Note,
 * that Entities themselves DO NOT tick, their components do.  Entities themselves
 * <i>are</i> the GameObject, and Components <i>control</i> the Entities.
 * 
 * @author Flafla2
 */
public class Entity extends EditorObject implements Cloneable {
	
	/**
	 * If this is true, then this Entity doesn't do any logic - it only renders and
	 * holds child Entities.  Also, non-static entities can only use their main
	 * collider.
	 * TODO: Tweak the collision detection algorithm to allow for moving objects with more than one collider
	 */
	public boolean isStatic = true;
	public String name;
	public Vector2D pos;
	public Vector2D dim;
	public String resourcePath = "";
	public boolean repeatTex = false;
	public boolean linearScaling = false;
	public Color color = new Color(0xaaaaaa);
	public float alpha = 1.0f;
	
	private static final String slashLoc = "/res/gui/slash.png";
	
	protected ArrayList<Entity> children;
	protected ArrayList<Component> components;
	protected Entity parent;
	
	/**
	 * The Sub Colliders of the Entity - these dictate some of the finer details of the
	 * object that would otherwise lag the game if they were queried EVERY tick.
	 */
	protected ArrayList<Collider> colliders;
		
	public Entity(String name)
	{
		this.name = name;
		children = new ArrayList<Entity>();
		components = new ArrayList<Component>();
		colliders = new ArrayList<Collider>();
		
		pos = new Vector2D(0,0);
		dim = new Vector2D(50,50);
	}
	
	public Entity()
	{
		this("");
	}
	
	public void spawnEntityInWorld()
	{
		for(int x=0;x<components.size();x++)
			components.get(x).onEntitySpawn();
	}
	
	public void removeEntityFromWorld()
	{
		Remote2D.getInstance().map.getEntityList().removeEntityFromList(this);
	}
	
	public Entity getChild(int index)
	{
		return children.get(index);
	}
	
	public boolean hasChild(Entity e)
	{
		return children.contains(e);
	}
	
	public int getChildrenSize()
	{
		return children.size();
	}
	
	public void addCollider(Collider c)
	{
		colliders.add(c);
	}
	
	public void removeCollider(Collider c)
	{
		colliders.remove(c);
	}
	
	public void removeCollider(int c)
	{
		colliders.remove(c);
	}
	
	public Collider getCollider(int index)
	{
		return colliders.get(index);
	}
	
	public void addComponent(Component c)
	{
		Component cnew = c.clone();
		cnew.setEntity(this);
		components.add(cnew);
	}
	
	public int indexOfCollider(Collider c)
	{
		for(int x=0;x<colliders.size();x++)
		{
			if(c.isEqual(colliders.get(x)))
				return x;
		}
		return -1;
	}
	
	public void addChild(Entity e)
	{
		e.parent = this;
		children.add(e);
	}
	
	public void removeChild(Entity e)
	{
		if(children.contains(e))
		{
			children.remove(e);
			e.parent = null;
		}
	}
	
	public ArrayList<Component> getComponents()
	{
		return components;
	}
	
	/**
	 * Basically the broad phase of the collision detection algorithm.  Note that if
	 * this Entity is non-static it isn't counted in the algorithm (we don't know
	 * which collider to correct).
	 * @param coll The moving collider
	 * @param movement The movement vector of said collider
	 * @return If it passes, list of all colliders involved with this WorldElement.  Otherwise, null.
	 */
	public ArrayList<Collider> getPossibleColliders(Collider coll, Vector2D movement)
	{
		if(!isStatic)
			return null;
		
		Collider mainCollider = getMainCollider();
		if(mainCollider == null)
			return null;
		
		mainCollider = mainCollider.getTransformedCollider(pos);
		Collision mainColliderCollision = coll.getCollision(mainCollider, movement);
		if(!mainColliderCollision.collides)
			return null;
		
		ArrayList<Collider> retColliders = new ArrayList<Collider>();
		for(int x=0;x<colliders.size();x++)
		{
			retColliders.add(colliders.get(x).getTransformedCollider(pos));
		}
		
		return retColliders;
	}
	
	public Entity getParent()
	{
		return parent;
	}
	
	public boolean isPointColliding(Vector2D vec)
	{
		Collider mainCollider = getMainCollider().getTransformedCollider(pos);
		if(mainCollider.isPointInside(vec))
		{
			for(int x=0;x<colliders.size();x++)
				if(colliders.get(x).isPointInside(vec))
					return true;
			
			return false;
		} else
			return true;
	}
	
	public void renderColliders()
	{
		Collider mainCollider = getMainCollider();
		GL11.glPushMatrix();
			GL11.glTranslatef(pos.x,pos.y,0);
			GL11.glColor3f(1,1,0);
			if(mainCollider != null)
				mainCollider.drawCollider();
			GL11.glColor3f(1,1,1);
			for(int x=0;x<colliders.size();x++)
				colliders.get(x).drawCollider();
		GL11.glPopMatrix();
	}
	
	/**
	 * The Main Collider of the Entity - we only do logic on the sub colliders
	 * if the main Collider is touched.
	 */
	public Collider getMainCollider()
	{
		if(colliders.size()==0 || !isStatic)
			return null;
		Vector2D v1 = null;
		Vector2D v2 = null;
		
		for(int x=0;x<colliders.size();x++)
		{
			for(int y=0;y<colliders.get(x).verts.length;y++)
			{
				if(v1 == null)
					v1 = colliders.get(x).verts[y].copy();
				if(v2 == null)
					v2 = colliders.get(x).verts[y].copy();
				
				if(colliders.get(x).verts[y].x < v1.x)
					v1.x = colliders.get(x).verts[y].x;
				if(colliders.get(x).verts[y].x > v2.x)
					v2.x = colliders.get(x).verts[y].x;
				if(colliders.get(x).verts[y].y < v1.y)
					v1.y = colliders.get(x).verts[y].y;
				if(colliders.get(x).verts[y].y > v2.y)
					v2.y = colliders.get(x).verts[y].y;
			}
		}
		
		return v1.getColliderWithDim(v2.subtract(v1));
	}
	
	public Collider getGeneralCollider()
	{
		Collider mainCollider = getMainCollider();
		if(isStatic && mainCollider != null)
			return mainCollider.getTransformedCollider(pos);
		return pos.getColliderWithDim(getDim());
	}
	
	public void reloadTextures()
	{
		
	}
	
	public Vector2D getDim()
	{
		return dim;
	}
	
	public void render(boolean editor)
	{
		boolean selected = false;
		if(editor)
			if(Remote2D.getInstance().guiList.peek() instanceof GuiEditor)
				if(((GuiEditor)Remote2D.getInstance().guiList.peek()).getSelectedEntity() == this)
					selected = true;
		if(editor)
		{
			Texture tex = Remote2D.getInstance().artLoader.getTexture(slashLoc, false, true);
			float maxX = ((float)dim.x)/32f;
			float maxY = ((float)dim.y)/32f;
			tex.bind();
			if(selected)
				Gui.bindRGB(0xff0000);
			else
				Gui.bindRGB(0xffaaaa);
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
			Gui.bindRGB(0xffffff);
		}
		
		float[] colorFloats = Gui.getRGB(color.getRGB());
		GL11.glColor4f(colorFloats[0], colorFloats[1], colorFloats[2], alpha);
		if(Remote2D.getInstance().artLoader.textureExists(resourcePath))
		{
			Texture tex = Remote2D.getInstance().artLoader.getTexture(resourcePath,linearScaling,repeatTex);
			tex.bind();
			float maxX = 1;
			float maxY = 1;
			//Log.debug(color.getRGB()+"");
			if(repeatTex)
			{
				maxX = ((float)dim.x)/((float)tex.image.getWidth());
				maxY = ((float)dim.y)/((float)tex.image.getHeight());
			}
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
		} else if(Remote2D.getInstance().artLoader.R2DExists(resourcePath))
		{
			Remote2D.getInstance().artLoader.getAnimation(resourcePath).render(pos, dim);
		} else
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(pos.x,pos.y);
				GL11.glVertex2f(pos.x+dim.x, pos.y);
				GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
				GL11.glVertex2f(pos.x, pos.y+dim.y);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
		if(editor && selected)
		{
			Gui.bindRGB(0xff0000);
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
		
		Gui.bindRGB(0xffffff);
		
		
	}
	
	public Entity clone()
	{
		Entity e = new Entity();
		e.alpha = this.alpha;
		e.parent = this.parent;
		e.color = new Color(color.getRGB());
		e.pos = this.pos.copy();
		e.dim = this.dim.copy();
		e.isStatic = this.isStatic;
		e.linearScaling = this.linearScaling;
		e.name = this.name;
		e.repeatTex = this.repeatTex;
		e.resourcePath = this.resourcePath;
		
		for(int x=0;x<children.size();x++)
		{
			Entity c = children.get(x).clone();
			c.parent = e;
			e.children.add(e);
		}
		for(int x=0;x<components.size();x++)
		{
			Component c = components.get(x).clone();
			c.setEntity(e);
			e.components.add(c);
		}
		return e;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> ArrayList<T> getComponentsOfType(Class<T> type)
	{
		ArrayList<T> returnComponents = new ArrayList<T>();
		for(int x=0;x<components.size();x++)
			if(type.isInstance(components.get(x)))
				returnComponents.add((T) components.get(x));
		
		return returnComponents;
	}

	public int getColliderSize() {
		return colliders.size();
	}

	@Override
	public void apply() {
		
	}

	public void renderPreview() {
		GL11.glPushMatrix();
		GL11.glTranslatef(-pos.x, -pos.y, 0);
		for(int x=0;x<getComponents().size();x++)
			getComponents().get(x).renderBefore(false);
		render(false);
		for(int x=0;x<getComponents().size();x++)
			getComponents().get(x).renderAfter(false);
		GL11.glPopMatrix();
	}
		
}
