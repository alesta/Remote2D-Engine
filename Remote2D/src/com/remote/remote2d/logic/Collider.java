package com.remote.remote2d.logic;

import com.esotericsoftware.minlog.Log;

public abstract class Collider {
	
	public boolean isIdle = true;
	public Vector2D[] verts;
	
	public Collider(Vector2D[] verts)
	{
		this.verts = verts;
	}
	
	public Collider()
	{
		
	}
	
	public Collision getCollision(Collider other, Vector2D movement)
	{
		Collider moved = this.getTransformedCollider(movement);
		Collision collision = new Collision();
		collision.idle = other;
		collision.moving = this;
		collision.movementVector = movement.normalize();
		updateVerts();
		for(int j = moved.verts.length-1, i = 0; i < moved.verts.length; j = i, i++)
		{
			Vector2D v0 = verts[j];
			Vector2D v1 = verts[i];
			Vector2D edge = new Vector2D(0,0);
			edge.x = v1.x - v0.x; // edge
			edge.y = v1.y - v0.y; // edge
		  
			Vector2D axis = edge.perp(); // Separate axis is perpendicular to the edge
			if(calculateCollisionInfo(axis, moved, other, collision))
			{
				if(!collision.collides)
				{
					collision.correction = new Vector2D(0,0);
					collision.lengthSquared = 0;
				}
				return collision;
			}
		}
		
		for(int j = other.verts.length-1, i = 0; i < other.verts.length; j = i, i++)
		{
			Vector2D v0 = other.verts[j];
			Vector2D v1 = other.verts[i];
			Vector2D edge2 = new Vector2D(0,0);
			edge2.x = v1.x - v0.x; // edge
			edge2.y = v1.y - v0.y; // edge
		  
			Vector2D axis = edge2.perp(); // Separate axis is perpendicular to the edge
			if(calculateCollisionInfo(axis, moved, other, collision))
			{
				if(!collision.collides)
				{
					collision.correction = new Vector2D(0,0);
					collision.lengthSquared = 0;
				}
				return collision;
			}
		}
		
		collision.collides = true;
		return collision;
	}
	
	protected boolean doesCollideOnAxis(Collider moved, Collider poly2, Vector2D axis)
	{
		int[] thisMinMax = moved.calculateInterval(axis);
		int mina = thisMinMax[0];
		int maxa = thisMinMax[1];
		int[] otherMinMax = poly2.calculateInterval(axis);
		int minb = otherMinMax[0];
		int maxb = otherMinMax[1];
		
		double d0 = maxb-mina;
		double d1 = minb-maxa;
		
		if(d0 < 0.0 || d1 > 0.0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param axis
	 * @param poly
	 * @param info
	 * @return If the collision info is "finished."  In other words, if more calculations
	 * 			are not needed.
	 */
	protected boolean calculateCollisionInfo(Vector2D axis, Collider moved, Collider poly2, Collision info) {
		int[] thisMinMax = moved.calculateInterval(axis);
		int mina = thisMinMax[0];
		int maxa = thisMinMax[1];
		int[] otherMinMax = poly2.calculateInterval(axis);
		int minb = otherMinMax[0];
		int maxb = otherMinMax[1];
		
		double d0 = maxb-mina;
		double d1 = minb-maxa;
		
		if(d0 < 0.0 || d1 > 0.0)
		{
			info.collides = false;
			return true;
		}	
		
		// find out if overlap on the right or left of the polygon.
		double overlap = (d0 < -d1)? d0 : d1;
		
		if(mina < maxb && (maxb-mina < info.max-info.min || info.max < 0 || info.min < 0))
		{
			info.min = mina;
			info.max = maxb;
		} else if(mina >= maxb && (maxa-minb < info.max-info.min || info.max < 0 || info.min < 0))
		{
			info.min = minb;
			info.max = maxa;
		}
			

		// the axis length squared
		double axis_length_squared = axis.dotVec(axis);
		assert(axis_length_squared > 0.00001);

		// the mtd vector for that axis
		Vector2D sep = new Vector2D(0,0); 
		sep.x = (int) (axis.x * (overlap / axis_length_squared));
		sep.y = (int) (axis.y * (overlap / axis_length_squared));
		

		// the mtd vector length squared.
		double sep_length_squared = sep.dotVec(sep);
		
		// if that vector is smaller than our computed MTD (or the mtd hasn't been computed yet)
		// use that vector as our current mtd.
		boolean favorable = sep_length_squared < info.lengthSquared;
		boolean lastPreferred = getOpposites(info.correction.x,info.movementVector.x) && getOpposites(info.correction.y,info.movementVector.y) && info.lengthSquared >= 0.0;
		boolean preferred = getOpposites(sep.x,info.movementVector.x) && getOpposites(sep.y,info.movementVector.y);
		if((favorable && preferred) || info.lengthSquared < 0.0 || (!lastPreferred && preferred))
		{
			info.lengthSquared = sep_length_squared;
			info.correction = sep;
		}
		return false;
		
	}
	
	protected boolean getOpposites(float x, float y)
	{
		return (x>0 && y<0) || (x<0 && y>0) || x==0 || y==0;
	}
	
	protected int[] calculateInterval(Vector2D axis)
	{
		return calculateInterval(verts,axis);
	}
		
	protected int[] calculateInterval(Vector2D[] verts, Vector2D axis) {
		int min;
		int max;
		min = max = (int)verts[0].dotVec(axis);
		for (int i = 1; i < verts.length; i++) {
			int d = (int)verts[i].dotVec(axis);
			if (d < min)
				min = d;
			else if (d > max)
				max = d;
		}
		int[] interval = {min,max};
		return interval;
	}
	
	protected boolean intervalsSeparated(float mina, float maxa, float minb, float maxb) {
		return (mina > maxb) || (minb > maxa);
	}
	
	public boolean isEqual(Collider c)
	{
		if(c == null)
			return false;
		c.updateVerts();
		updateVerts();
		if(c.verts.length != verts.length)
			return false;
		
		for(int x=0;x<verts.length;x++)
		{
			if(verts[x].x != c.verts[x].x || verts[x].y != c.verts[x].y)
				return false;
		}
		
		return true;
	}
	
	public abstract void updateVerts();
	public abstract boolean isPointInside(Vector2D vec);
	public abstract Collider getTransformedCollider(Vector2D trans);
	public abstract void drawCollider();
	
}
