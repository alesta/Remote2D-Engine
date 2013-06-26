package com.remote.remote2d.logic;

/**
 * A base Vector class.  All vector classes should extend this, because it makes
 * it easier to multiply, divide, etc. vectors of varying data types.  The default
 * vector classes (Vector2D, Vector2DF, Vector3DF) all have methods in them for
 * ease of use so that they convert values from all of the other 3 classes.
 * Worst case, you just have to cast the final vector to the vector of that
 * type.
 * @author Flafla2
 *
 */
public abstract class Vector {
	
	public abstract float[] getElements();
	public abstract int getVectorLength();
	public abstract Vector convertElementsToVector(float[] elements);
	
	public float[] removeExcessElements(float[] elements)
	{
		int sizeToUse = Math.min(elements.length, getVectorLength());
		float[] returnValue = new float[getVectorLength()];
		for(int x=0;x<sizeToUse;x++)
			returnValue[x] = elements[x];
		for(int x=sizeToUse;x<getVectorLength();x++)
			returnValue[x] = 0;
		return returnValue;
			
	}
	
	public Vector multiplyVec(Vector vec)
	{
		float[] elements = vec.getElements();
		float[] currentElements = getElements();
		int length = (elements.length<currentElements.length?elements.length:currentElements.length);
		float[] finalElements = new float[length];
		for(int x=0;x<length;x++)
		{
			finalElements[x] = elements[x]*currentElements[x];
		}
		
		return convertElementsToVector(finalElements);
	}
	
	public Vector divideVec(Vector vec)
	{
		float[] elements = vec.getElements();
		float[] currentElements = getElements();
		int length = (elements.length<currentElements.length?elements.length:currentElements.length);
		float[] finalElements = new float[length];
		for(int x=0;x<length;x++)
		{
			finalElements[x] = currentElements[x]/elements[x];
		}
		
		return convertElementsToVector(finalElements);
	}
	
	public Vector addVec(Vector vec)
	{
		float[] elements = vec.getElements();
		float[] currentElements = getElements();
		int length = (elements.length<currentElements.length?elements.length:currentElements.length);
		float[] finalElements = new float[length];
		for(int x=0;x<length;x++)
		{
			finalElements[x] = elements[x]+currentElements[x];
		}
		
		return convertElementsToVector(finalElements);
	}
	
	public Vector subtractVec(Vector vec)
	{
		float[] elements = vec.getElements();
		float[] currentElements = getElements();
		int length = (elements.length<currentElements.length?elements.length:currentElements.length);
		float[] finalElements = new float[length];
		for(int x=0;x<length;x++)
		{
			finalElements[x] = currentElements[x]-elements[x];
		}
		
		return convertElementsToVector(finalElements);
	}
	
	public Vector absVec(Vector vec)
	{
		float[] elements = getElements();
		for(int x=0;x<elements.length;x++)
		{
			elements[x] = Math.abs(elements[x]);
		}
		
		return convertElementsToVector(elements);
	}
	
	public Vector copyVec(Vector vec)
	{
		return convertElementsToVector(getElements());
	}
	
	public Vector absVec()
	{
		return absVec(this);
	}
	
	public float dotVec(Vector vec)
	{
		float[] elements = vec.getElements();
		float[] currentElements = getElements();
		int length = (elements.length<currentElements.length?elements.length:currentElements.length);
		float finalProduct = 0;
		for(int x=0;x<length;x++)
		{
			finalProduct += elements[x]*currentElements[x];
		}
		
		return finalProduct;
	}
	
}
