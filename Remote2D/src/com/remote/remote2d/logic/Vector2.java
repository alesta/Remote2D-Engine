package com.remote.remote2d.logic;

import com.esotericsoftware.minlog.Log;

public class Vector2 extends Vector {
	
	public float x;
	public float y;
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(float[] x)
	{
		this(x[0],x[1]);
	}

	public Vector2(Vector3 dim) {
		this.x = (int)dim.x;
		this.y = (int)dim.y;
	}
	
	public Vector2(int[] x) {
		this(x[0],x[1]);
	}

	public Vector2(float x) {
		this(x,x);
	}

	@Override
	public float[] getElements() {
		float[] elements = {x,y};
		return elements;
	}

	@Override
	public Vector convertElementsToVector(float[] elements) {
		elements = removeExcessElements(elements);
		return new Vector2(elements[0],elements[1]);
	}
	
	@Override
	public int getVectorLength() {
		return 2;
	}
	
	public Vector2 add(Vector vec)
	{
		return (Vector2)convertElementsToVector(super.addVec(vec).getElements());
	}
	
	public Vector2 multiply(Vector vec)
	{
		return (Vector2)convertElementsToVector(super.multiplyVec(vec).getElements());
	}
	
	public Vector2 divide(Vector vec)
	{
		return (Vector2)convertElementsToVector(super.divideVec(vec).getElements());
	}
	
	public Vector2 subtract(Vector vec)
	{
		return (Vector2)convertElementsToVector(super.subtractVec(vec).getElements());
	}
	
	public Vector2 abs()
	{
		return new Vector2(Math.abs(x),Math.abs(y));
	}
	
	public Vector2 print()
	{
		Log.info("("+x+","+y+")");
		return this;
	}
	
	public ColliderBox getColliderWithDim(Vector2 dim)
	{
		return new ColliderBox(new Vector2(x,y), new Vector2(dim.x,dim.y));
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
	
	public Vector2 normalize() {
		float distance = ColliderLogic.getDistBetweenPoints(0, 0, x, y);
		if(distance == 0)
			return new Vector2(0,0);
		return new Vector2(x/distance,y/distance);
	}
	
	public Vector2 copy()
	{
		return new Vector2(x,y);
	}
	
	public Vector2 perp()
	{
		return new Vector2(-y,x);
	}
	
}
