package com.remote.remote2d.world;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.entity.EntityList;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.io.R2DFileSaver;
import com.remote.remote2d.io.R2DTypeCollection;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.Collision;
import com.remote.remote2d.logic.CollisionComparator;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;

public class Map implements R2DFileSaver {
	
	private EntityList entities;
	private Vector2 oldCamera = new Vector2(0,0);
	public Vector2 camera = new Vector2(0,0);
	public int backgroundColor = 0xffffff;
	public int gridSize = 16;
	public float scale = 1.0f;
	
	public boolean debug = false;
	
	public Map()
	{
		entities = new EntityList();
	}
	
	public Map(EntityList list)
	{
		entities = list;
	}
	
	public void render(boolean editor, float interpolation)
	{
		Vector2 iCamera = Interpolator.linearInterpolate2f(oldCamera, camera, interpolation);
		GL11.glPushMatrix();
		GL11.glTranslatef(-iCamera.x, -iCamera.y, 0);
		if(editor)
			GL11.glScalef(scale, scale, 1);
		entities.render(editor,interpolation);
		GL11.glPopMatrix();
	}
	
	public void drawGrid()
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(-camera.x, -camera.y, 0);
		GL11.glScalef(scale, scale, 1);
		Vector2 currentPos = new Vector2(0,0);
		currentPos.x = camera.x/scale-(camera.x/scale)%gridSize-gridSize;
		currentPos.y = camera.y/scale-(camera.y/scale)%gridSize-gridSize;
		GL11.glColor4f(0,0,0,0.25f);
		
		for(int x=0;x<Remote2D.getInstance().displayHandler.height/scale/gridSize+2;x++)
		{
			if(currentPos.y == 0)
			{
				GL11.glColor4f(1,0,0,0.5f);
				GL11.glLineWidth(3);
			}
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(currentPos.x,currentPos.y);
			GL11.glVertex2f((Remote2D.getInstance().displayHandler.width)/scale+gridSize*2+currentPos.x,currentPos.y);
			currentPos.y+=gridSize;
			GL11.glColor4f(0,0,0,0.25f);
			GL11.glEnd();
			GL11.glLineWidth(1);
		}
		
		currentPos.y = (int)(camera.y/scale-(camera.y/scale)%gridSize-gridSize);
		for(int y=0;y<Remote2D.getInstance().displayHandler.width/scale/gridSize+2;y++)
		{
			if(currentPos.x == 0)
			{
				GL11.glColor4f(0,1,0,0.5f);
				GL11.glLineWidth(3);
			}
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(currentPos.x,currentPos.y);
			GL11.glVertex2f(currentPos.x,(Remote2D.getInstance().displayHandler.height)/scale+gridSize*2+currentPos.y);
			
			currentPos.x+=gridSize;
			GL11.glColor4f(0,0,0,0.25f);
			
			GL11.glEnd();
			GL11.glLineWidth(1);
		}
		
		GL11.glPopMatrix();
		Gui.bindRGB(0xffffff);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void tick(int i, int j, int k, boolean editor)
	{
		oldCamera = camera.copy();
		if(!editor)
			entities.tick(i, j, k);
	}
	
	/**
	 * Gives you the correction if you have a moving collider somewhere in the map.
	 * @param coll The moving collider (before moving)
	 * @param vec Said collider's movement vector
	 * @return The "correction" - just add this to your movement vector and you won't collide with anything
	 */
	public Vector2 getCorrection(Collider coll,Vector2 vec)
	{
		ArrayList<Collider> allColliders = new ArrayList<Collider>();
		
		//Broad phase: check the main collider of each worldelement.
		for(int x=0;x<entities.size();x++)
		{
			ArrayList<Collider> elementColliders = entities.get(x).getPossibleColliders(coll, vec);
			if(elementColliders != null)
			{
				allColliders.addAll(elementColliders);
			}
		}
		
		//First collision pass: Test to see which element will be hit first, and if they are hit at all
		ArrayList<Collision> allCollision = new ArrayList<Collision>();
		for(int x=0;x<allColliders.size();x++)
		{
			Collider other = allColliders.get(x);
			Collision collision = coll.getCollision(other, vec);
			if(collision.collides)
				allCollision.add(collision);
		}
		
		Collections.sort(allCollision, new CollisionComparator());
		
		//Second collsion pass: Get the actual collision vectors of each collider and add it to the main return vector
		Vector2 correction = new Vector2(0,0);
		for(int x=0;x<allCollision.size();x++)
		{
			Collider other = allCollision.get(x).idle;
			Collision collision = coll.getCollision(other, vec.add(correction));
			if(collision.collides)
			{
				correction = correction.add(collision.correction);
			}
		}
		return correction;
	}
	
	/**
	 * Uses quicksort to sort a batch of collisions by distance traveled.
	 * @param collection
	 * @return the sorted batch
	 */
	public static ArrayList<Collision> sortColliders(ArrayList<Collision> collection)
	{
		if(collection.size() <= 1)
			return collection;
		Log.debug("sort colliders:"+collection.size());
		int pivot = collection.size()/2;
		boolean odd = collection.size()%2 != 0;
		ArrayList<Collision> less = new ArrayList<Collision>();
		ArrayList<Collision> more = new ArrayList<Collision>();
		for(int x=0;x<collection.size();x++)
		{
			int distanceThis = collection.get(x).max-collection.get(x).min;
			int distancePivot;
			if(odd)
				distancePivot = (collection.get(pivot).max+collection.get(pivot+1).max)/2-(collection.get(pivot).min+collection.get(pivot+1).min)/2;
			else
				distancePivot = collection.get(pivot).max-collection.get(pivot).min;
			Log.debug(x+" "+distancePivot+" "+distanceThis);
			if(distanceThis > distancePivot)//if the shortest correction is longer than the pivot(so shortest time to collision), we set it towards the front
				less.add(collection.get(x));
			else
				more.add(collection.get(x));
		}
		
		Log.debug("less:"+less.size()+" more:"+more.size());
		
		ArrayList<Collision> returnArray = sortColliders(less);
		returnArray.addAll(sortColliders(more));
		
		return returnArray;
	}
	
	public Entity getTopEntityAtPoint(Vector2 vec)
	{
		Entity top = null;
		for(int x=0;x<entities.size();x++)
		{
			if(entities.get(x).pos.getColliderWithDim(entities.get(x).getDim()).isPointInside(vec))
				top = entities.get(x);
		}
		return top;
	}
	
	@Override
	public void saveR2DFile(R2DTypeCollection collection) {
		collection.setInteger("entityCount", entities.size());
		for(int x=0;x<entities.size();x++)
		{
			R2DTypeCollection c = new R2DTypeCollection("entity_"+x);
			entities.get(x).saveR2DFile(c);
			ArrayList<Component> components = entities.get(x).getComponents();
			c.setInteger("componentCount", components.size());
			for(int y=0;y<components.size();y++)
			{
				R2DTypeCollection cComp = new R2DTypeCollection("component_"+y);
				cComp.setString("className", Remote2D.getInstance().componentList.getComponentID(components.get(y)));
				components.get(y).saveR2DFile(cComp);
				c.setCollection(cComp);
			}
			collection.setCollection(c);
		}
	}
	
	@Override
	public void loadR2DFile(R2DTypeCollection collection) {
		entities.clear();
		int entityCount = collection.getInteger("entityCount");
		for(int x=0;x<entityCount;x++)
		{
			R2DTypeCollection c = collection.getCollection("entity_"+x);
			Entity e = new Entity();
			e.loadR2DFile(c);
			int componentCount = c.getInteger("componentCount");
			for(int y=0;y<componentCount;y++)
			{
				R2DTypeCollection cComp = c.getCollection("component_"+y);
				Component comp = Remote2D.getInstance().componentList.getComponent(cComp.getString("className")).clone();
				comp.loadR2DFile(cComp);
				comp.apply();
				e.addComponent(comp);
			}
			entities.addEntityToList(e);
		}
		//collection.printContents();
	}
	
	public Map copy()
	{
		EntityList entityList = entities.clone();
		Map map = new Map(entityList);
		map.oldCamera = camera.copy();
		map.camera = camera.copy();
		return map;
	}
	
	public EntityList getEntityList() {
		return entities;
	}

	public static String getExtension() {
		return ".r2d";
	}
	
}
