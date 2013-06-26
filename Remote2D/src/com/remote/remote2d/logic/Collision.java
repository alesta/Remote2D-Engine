package com.remote.remote2d.logic;

public class Collision {
	
	public double lengthSquared = -1.0d;
	public Vector2D correction = new Vector2D(0,0);
	public Vector2DF movementVector = new Vector2DF(0,0);
	public boolean collides = false;
	
	public int min = -1;
	public int max = -1;
	
	public Collider idle;
	public Collider moving;
	
}
