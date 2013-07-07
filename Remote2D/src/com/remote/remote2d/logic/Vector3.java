package com.remote.remote2d.logic;

public class Vector3 extends Vector {
	
	public float x;
	public float y;
	public float z;
	
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		z = 1;
	}
	
	@Override
	public float[] getElements() {
		float[] elements = {x,y,z};
		return elements;
	}

	@Override
	public Vector convertElementsToVector(float[] elements) {
		elements = removeExcessElements(elements);
		return new Vector3(elements[0],elements[1],elements[2]);
	}
	
	@Override
	public int getVectorLength() {
		return 3;
	}
	
	public float dot(Vector3 vec)
	{
		return x*vec.x+y*vec.y+z*vec.z;
	}
	
	public Vector3 add(Vector3 vec)
	{
		return new Vector3(x+vec.x,y+vec.y,z+vec.z);
	}
	
	public String toString()
	{
		return "("+x+","+y+","+z+")";
	}
}
