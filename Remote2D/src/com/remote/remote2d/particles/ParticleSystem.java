package com.remote.remote2d.particles;

import java.awt.Color;
import java.util.ArrayList;

import com.remote.remote2d.entity.EditorObject;

public class ParticleSystem extends EditorObject {
	
	public ArrayList<Particle> particles;
	public int maxParticles = 100;
	public int spawnRate = 10;
	public int systemLength = 1000;
	public Color color;
	

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

}
