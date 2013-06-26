package com.remote.remote2d.logic;

/**
 * A utility class that detects and handles collisions with the Collider class.
 * @author Flafla2
 *
 */
public class ColliderLogic {
	
	public static int getDistBetweenPoints(int x1, int y1, int x2, int y2)
	{
		return (int)Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
	}
	
	public static Collider setColliderPos(Collider c, Vector2D pos)
	{
		if(c instanceof ColliderBox)
		{
			ColliderBox coll = (ColliderBox)c;
			return new ColliderBox(pos,coll.getDim());
		} else if(c instanceof ColliderSphere)
		{
			ColliderSphere coll = (ColliderSphere)c;
			return new ColliderSphere(pos,coll.radius);
		}
		return c;
	}
	
}
