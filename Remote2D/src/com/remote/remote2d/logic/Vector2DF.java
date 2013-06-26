package com.remote.remote2d.logic;

import com.esotericsoftware.minlog.Log;

public class Vector2DF extends Vector {
	
	public float x;
	public float y;
	
	public Vector2DF(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2DF(float[] x)
	{
		this(x[0],x[1]);
	}

	public Vector2DF(Vector3DF dim) {
		this.x = (int)dim.x;
		this.y = (int)dim.y;
	}
	
	@Override
	public float[] getElements() {
		float[] elements = {x,y};
		return elements;
	}

	@Override
	public Vector convertElementsToVector(float[] elements) {
		elements = removeExcessElements(elements);
		return new Vector2DF(elements[0],elements[1]);
	}
	
	@Override
	public int getVectorLength() {
		return 2;
	}
	
	public Vector2DF add(Vector vec)
	{
		return (Vector2DF)convertElementsToVector(super.addVec(vec).getElements());
	}
	
	public Vector2DF multiply(Vector vec)
	{
		return (Vector2DF)convertElementsToVector(super.multiplyVec(vec).getElements());
	}
	
	public Vector2DF divide(Vector vec)
	{
		return (Vector2DF)convertElementsToVector(super.divideVec(vec).getElements());
	}
	
	public Vector2DF subtract(Vector vec)
	{
		return (Vector2DF)convertElementsToVector(super.subtractVec(vec).getElements());
	}
	
	public Vector2DF abs()
	{
		return new Vector2DF(Math.abs(x),Math.abs(y));
	}
	
	public Vector2DF print()
	{
		Log.info("("+x+","+y+")");
		return this;
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
	
	public Vector2DF copy()
	{
		return new Vector2DF(x,y);
	}
	
}
