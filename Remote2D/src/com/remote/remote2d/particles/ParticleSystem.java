package com.remote.remote2d.particles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.remote.remote2d.entity.EditorObject;
import com.remote.remote2d.logic.Vector2D;
import com.remote.remote2d.logic.Vector2DF;
import com.remote.remote2d.world.Map;

public class ParticleSystem extends EditorObject {
	
	public Vector2D pos;
	
	public ArrayList<Particle> particles;
	public int maxParticles = 100;
	public int maxSpawnRate = 10;
	public int spawnRateDeviation = 0;
	public int systemLength = 1000;
	
	public Vector2D startPosDeviation = new Vector2D(0,0);
	
	public Color startColor = new Color(0xffffff);
	public Color endColor = new Color(0xff0000);
	public int colorDeviation = 0;
	
	public Vector2D baseVelocity = new Vector2D(0,0);
	public Vector2D velocityDeviation = new Vector2D(10,10);
	public Vector2D environment = new Vector2D(0,0);
	
	public float startAlpha = 1.0f;
	public float endAlpha = 0.5f;
	public float startAlphaDeviation = 0;
	public float endAlphaDeviation = 0;
	
	public int startSize = 20;
	public int endSize = 10;
	public int startSizeDeviation = 0;
	public int endSizeDeviation = 0;
	
	public long particleLife = 100l;
	public long particleLifeDeviation = 10l;
	
	private Map map;
	private Random random;
	
	public ParticleSystem(Map map)
	{
		this.map = map;
		particles = new ArrayList<Particle>();
		random = new Random();
	}
	
	public boolean spawnParticle()
	{
		if(particles.size() >= maxParticles)
			return false;
		
		Vector2DF randomVec = new Vector2DF(random.nextFloat(),random.nextFloat());
		
		Vector2D startPos = pos.add(startPosDeviation.multiplyVec(randomVec));
		randomVec = new Vector2DF(random.nextFloat(),random.nextFloat());
		Vector2D velocity = baseVelocity.add(velocityDeviation.multiplyVec(randomVec));
		Color trueStartColor = new Color(startColor.getRed()+colorDeviation*random.nextFloat(),startColor.getGreen()+colorDeviation*random.nextFloat(),startColor.getBlue()+colorDeviation*random.nextFloat());
		Color trueEndColor = new Color(endColor.getRed()+colorDeviation*random.nextFloat(),endColor.getGreen()+colorDeviation*random.nextFloat(),endColor.getBlue()+colorDeviation*random.nextFloat());
		int trueStartSize = (int) (startSize+startSizeDeviation*random.nextFloat());
		int trueEndSize = (int) (endSize+endSizeDeviation*random.nextFloat());
		float trueStartAlpha = startAlpha+startAlphaDeviation*random.nextFloat();
		float trueEndAlpha = endAlpha+endAlphaDeviation*random.nextFloat();
		long lifeLength = (long)(particleLife+particleLifeDeviation*random.nextFloat());
		
		Particle particle = new Particle(startPos,velocity,environment,trueStartColor,trueEndColor,trueStartSize,trueEndSize,trueStartAlpha,trueEndAlpha,lifeLength);
		particles.add(particle);
		return true;
	}
	
	public void tick(double delta)
	{
		for(int x=0;x<maxSpawnRate;x++)
			spawnParticle();
		
		for(int x=0;x<particles.size();x++)
			particles.get(x).tick(map, delta);
	}
	
	public void render()
	{
		for(int x=0;x<particles.size();x++)
			particles.get(x).render();
	}

	@Override
	public void apply() {
		
	}

}
