package com.remote.remote2d.logic;

import com.esotericsoftware.minlog.Log;

public class Vector2D extends Vector {
	
	public int x;
	public int y;
	
	public Vector2D(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(int[] x)
	{
		this(x[0],x[1]);
	}
	
	public Vector2D(Vector2DF dim) {
		this.x = (int)dim.x;
		this.y = (int)dim.y;
	}

	public Vector2D(Vector3DF dim) {
		this.x = (int)dim.x;
		this.y = (int)dim.y;
	}
	
	@Override
	public float[] getElements() {
		float[] elements = {x,y};
		return elements;
	}
	
	@Override
	public int getVectorLength() {
		return 2;
	}

	@Override
	public Vector convertElementsToVector(float[] elements) {
		elements = removeExcessElements(elements);
		return new Vector2D((int)elements[0],(int)elements[1]);
	}
	
	public Vector2D add(Vector vec)
	{
		return (Vector2D)convertElementsToVector(super.addVec(vec).getElements());
	}
	
	public Vector2D multiply(Vector vec)
	{
		return (Vector2D)convertElementsToVector(super.multiplyVec(vec).getElements());
	}
	
	public Vector2D divide(Vector vec)
	{
		return (Vector2D)convertElementsToVector(super.divideVec(vec).getElements());
	}
	
	public Vector2DF addSensitive(Vector2DF vec)
	{
		Vector2DF newThis = (Vector2DF) vec.convertElementsToVector(getElements());
		return (Vector2DF)vec.convertElementsToVector(newThis.addVec(vec).getElements());
	}
	
	public Vector2DF subtractSensitive(Vector2DF vec)
	{
		Vector2DF newThis = (Vector2DF) vec.convertElementsToVector(getElements());
		return (Vector2DF)vec.convertElementsToVector(newThis.subtractVec(vec).getElements());
	}
	
	public Vector2DF multiplySensitive(Vector2DF vec)
	{
		Vector2DF newThis = (Vector2DF) vec.convertElementsToVector(getElements());
		return (Vector2DF)vec.convertElementsToVector(newThis.multiplyVec(vec).getElements());
	}
	
	public Vector2DF divideSensitive(Vector2DF vec)
	{
		Vector2DF newThis = (Vector2DF) vec.convertElementsToVector(getElements());
		return (Vector2DF)vec.convertElementsToVector(newThis.divideVec(vec).getElements());
	}
	
	public Vector2D subtract(Vector vec)
	{
		return (Vector2D)convertElementsToVector(super.subtractVec(vec).getElements());
	}
	
	public Vector2DF normalize() {
		float distance = ColliderLogic.getDistBetweenPoints(0, 0, x, y);
		if(distance == 0)
			return new Vector2DF(0,0);
		return new Vector2DF(x/distance,y/distance);
	}
	
	public Vector2D abs()
	{
		return (Vector2D)absVec();
	}
	
	public ColliderBox getColliderWithDim(Vector2D dim)
	{
		return new ColliderBox(this, dim);
	}
	
	public Vector2D print()
	{
		Log.info("("+x+","+y+")");
		return this;
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
	
	public Vector2D copy()
	{
		return new Vector2D(x,y);
	}

	public Vector2D multiply(Vector2DF vec) {
		return new Vector2D((int)(x*vec.x),(int)(y*vec.y));
	}
	
	public Vector2D perp()
	{
		return new Vector2D(-y,x);
	}
	
}
