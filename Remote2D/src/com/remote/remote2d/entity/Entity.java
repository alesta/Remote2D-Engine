package com.remote.remote2d.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.entity.component.ComponentCollider;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.io.R2DTypeCollection;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.Collision;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;

/**
 * The basic class for all moving things in the game.  Things such as the player,
 * bullets, enemies, mechanized objects, etc. Anything that needs to tick().  Note,
 * that Entities themselves DO NOT tick, their components do.  Entities themselves
 * <i>are</i> the GameObject, and Components <i>control</i> the Entities.
 * 
 * @author Flafla2
 */
public class Entity extends EditorObject {
	
	public String name;
	public Vector2 pos;
	public Vector2 dim;
	public String resourcePath = "";
	public boolean repeatTex = false;
	public boolean linearScaling = false;
	public Color color = new Color(0xaaaaaa);
	public float alpha = 1.0f;
	
	private Vector2 oldPos;
	
	private static final String slashLoc = "/res/gui/slash.png";
	
	protected ArrayList<Entity> children;
	protected ArrayList<Component> components;
	protected Entity parent;
		
	public Entity(String name)
	{
		super(null);
		this.name = name;
		children = new ArrayList<Entity>();
		components = new ArrayList<Component>();
		
		pos = new Vector2(0,0);
		oldPos = new Vector2(0,0);
		dim = new Vector2(50,50);
	}
	
	public Entity(String name, String uuid)
	{
		super(uuid);
		this.name = name;
		children = new ArrayList<Entity>();
		components = new ArrayList<Component>();
		
		pos = new Vector2(0,0);
		oldPos = new Vector2(0,0);
		dim = new Vector2(50,50);
	}
	
	public Entity()
	{
		this("");
	}
	
	public void spawnEntityInWorld()
	{
		oldPos = pos.copy();
		
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
	
	public Vector2 getPos(float interpolation)
	{
		return Interpolator.linearInterpolate2f(oldPos, pos, interpolation);
	}
	
	public void addComponent(Component c)
	{
		Component cnew = c.clone();
		cnew.setEntity(this);
		components.add(cnew);
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
	
	public ArrayList<Collider> getColliders()
	{
		ArrayList<Collider> colliders = new ArrayList<Collider>();
		for(Component c : components)
		{
			if(c instanceof ComponentCollider)
				colliders.add(((ComponentCollider)c).getCollider());
		}
		return colliders;
	}
	
	/**
	 * Basically the broad phase of the collision detection algorithm.  Note that if
	 * this Entity is non-static it isn't counted in the algorithm (we don't know
	 * which collider to correct).
	 * @param coll The moving collider
	 * @param movement The movement vector of said collider
	 * @return If it passes, list of all colliders involved with this WorldElement.  Otherwise, null.
	 */
	public ArrayList<Collider> getPossibleColliders(Collider coll, Vector2 movement)
	{
		
		Collider mainCollider = getBroadPhaseCollider();
		if(mainCollider == null)
			return null;
		
		mainCollider = mainCollider.getTransformedCollider(pos);
		Collision mainColliderCollision = coll.getCollision(mainCollider, movement);
		if(!mainColliderCollision.collides)
			return null;
		
		ArrayList<Collider> colliders = getColliders();
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
	
	public boolean isPointColliding(Vector2 vec)
	{
		Collider mainCollider = getBroadPhaseCollider().getTransformedCollider(pos);
		if(mainCollider.isPointInside(vec))
		{
			ArrayList<Collider> colliders = getColliders();
			for(int x=0;x<colliders.size();x++)
				if(colliders.get(x).isPointInside(vec))
					return true;
			
			return false;
		} else
			return true;
	}
	
	public void renderColliders()
	{
		Collider mainCollider = getBroadPhaseCollider();
		ArrayList<Collider> colliders = getColliders();
		GL11.glPushMatrix();
			GL11.glTranslatef(pos.x,pos.y,0);
			if(mainCollider != null)
				mainCollider.drawCollider(0xffff00);
			for(int x=0;x<colliders.size();x++)
				colliders.get(x).drawCollider(0xffffff);
		GL11.glPopMatrix();
	}
	
	/**
	 * All of this Entity's colliders are guaranteed to be inside this broad collider.
	 * 
	 * @returns null if there are no colliders, otherwise the broad phase collider.
	 */
	public Collider getBroadPhaseCollider()
	{
		ArrayList<Collider> colliders = getColliders();
		if(colliders.size()==0)
			return null;
		Vector2 v1 = null;
		Vector2 v2 = null;
		
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
		return pos.getColliderWithDim(getDim());
	}
	
	public void reloadTextures()
	{
		
	}
	
	public Vector2 getDim()
	{
		return dim;
	}
	
	public void tick(int i, int j, int k)
	{
		oldPos = pos.copy();
		
		for(int x=0;x<components.size();x++)
			components.get(x).tick(i, j, k);
	}
	
	public void render(boolean editor, float interpolation)
	{
		Vector2 pos = Interpolator.linearInterpolate2f(oldPos, this.pos, interpolation);
		
		if(editor)
			oldPos = pos.copy();
		
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
			int color = 0xffffff;
			if(selected)
				color = 0xff0000;
			else
				color = 0xffaaaa;
			Renderer.drawRect(pos, dim, new Vector2(0,0), new Vector2(maxX, maxY), tex, color, 1);
		}
		
		if(Remote2D.getInstance().artLoader.textureExists(resourcePath))
		{
			Texture tex = Remote2D.getInstance().artLoader.getTexture(resourcePath,linearScaling,repeatTex);
			float maxX = 1;
			float maxY = 1;
			if(repeatTex)
			{
				maxX = ((float)dim.x)/((float)tex.image.getWidth());
				maxY = ((float)dim.y)/((float)tex.image.getHeight());
			}
			Renderer.drawRect(pos, dim, new Vector2(0,0), new Vector2(maxX, maxY), tex, color.getRGB(), 1);
		} else if(Remote2D.getInstance().artLoader.R2DExists(resourcePath))
			Remote2D.getInstance().artLoader.getAnimation(resourcePath).render(pos, dim);
		else
			Renderer.drawRect(pos, dim, color.getRGB(), alpha);
		
		if(editor && selected)
			Renderer.drawLineRect(pos, dim, 1, 0, 0, 1);
	}
	
	public Entity clone()
	{
		Entity e = new Entity(name,getUUID());
		e.alpha = this.alpha;
		e.parent = this.parent;
		e.color = new Color(color.getRGB());
		e.pos = this.pos.copy();
		e.dim = this.dim.copy();
		e.linearScaling = this.linearScaling;
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

	@Override
	public void apply() {
		
	}
	
	public void updatePos()
	{
		oldPos = pos.copy();
	}

	public void renderPreview(float interpolation) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-pos.x, -pos.y, 0);
		for(int x=0;x<getComponents().size();x++)
			getComponents().get(x).renderBefore(false,interpolation);
		render(false,interpolation);
		for(int x=0;x<getComponents().size();x++)
			getComponents().get(x).renderAfter(false,interpolation);
		GL11.glPopMatrix();
	}

	public static String getExtension() {
		return ".entity";
	}	
		
}
