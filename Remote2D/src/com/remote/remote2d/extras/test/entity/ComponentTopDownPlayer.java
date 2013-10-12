package com.remote.remote2d.extras.test.entity; //Package may be different obviously

import org.lwjgl.input.Keyboard;

import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentTopDownPlayer extends Component {
	
	//PUBLIC VARIABLES: Are editable within Remote2D's editor.
	public Animation northAnimation;
	public Animation southAnimation;
	public Animation westAnimation;
	
	@Override
	public void tick(int i, int j, int k) {
		//Use LWJGL's Keyboard.class to accept input
		boolean north = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean south = Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean east = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean west = Keyboard.isKeyDown(Keyboard.KEY_A);
		
		//Set velocity based on input
		Vector2 velocity = new Vector2(0,0);//velocity in pixels per tick
		if(north)
			velocity.y -= 5;
		if(south)
			velocity.y += 5;
		if(east)
			velocity.x += 5;
		if(west)
			velocity.x -= 5;
		
		entity.pos = entity.pos.add(velocity);
		
		if(westAnimation != null && !west && east)
			westAnimation.flippedX = true;
		else if(westAnimation != null)
			westAnimation.flippedX = false;
		
		Animation replaceAnimation = null;
		
		if(north)
			replaceAnimation = northAnimation;
		if(south)
			replaceAnimation = southAnimation;
		if(east)
			replaceAnimation = westAnimation;
		if(west)
			replaceAnimation = westAnimation;
		
		if(replaceAnimation != null && !entity.material.getAnimation().equals(replaceAnimation))
			entity.material.setAnimation(replaceAnimation);
		
		boolean keyDown = north || south || east || west;
		
		if(entity.material.getAnimation() != null)
		{
			if(keyDown)
				entity.material.getAnimation().animate = true;
			else if(entity.material.getAnimation().animate)
			{
				entity.material.getAnimation().animate = false;
				entity.material.getAnimation().setCurrentFrame(0);
			}
		}
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void apply() {
		
	}

}
