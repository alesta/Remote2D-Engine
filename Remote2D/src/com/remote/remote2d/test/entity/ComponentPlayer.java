package com.remote.remote2d.test.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.logic.Vector2D;
import com.remote.remote2d.logic.Vector2DF;

public class ComponentPlayer extends Component {
	
	public String idleAnimation = "";
	public String walkAnimation = "";
	public String jumpAnimation = "";
	public String fallAnimation = "";
	public String landAnimation = "";
	public boolean spriteFacesRight = true;
	
	private PlayerState state = PlayerState.IDLE;
	private FacingState facing = FacingState.RIGHT;
	private Animation currentAnimation;
	private int stateTimer = 0;
	
	private Vector2DF velocity = new Vector2DF(0,0);
	private Vector2DF acceleration = new Vector2DF(0,1);
	private Vector2D maxVelocity = new Vector2D(5,-1);
		
	public ComponentPlayer(Entity entity)
	{
		super(entity);
	}

	@Override
	public void tick(int i, int j, int k, double delta) {
		PlayerState oldState = state;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			if(state == PlayerState.IDLE)
				state = PlayerState.WALK;
			facing = FacingState.LEFT;
			acceleration.x = -1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			if(state == PlayerState.IDLE)
				state = PlayerState.WALK;
			facing = FacingState.RIGHT;
			acceleration.x = 1;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			acceleration.x = 0;
			velocity.x = 0;
			if(state == PlayerState.WALK)
				state = PlayerState.IDLE;
		}
		velocity = velocity.add(acceleration.multiplyVec(new Vector2DF((float)delta,(float)delta)));
		
		if(Math.abs(velocity.x) > maxVelocity.x && maxVelocity.x != -1)
		{
			if(velocity.x > 0)
				velocity.x = maxVelocity.x;
			else
				velocity.x = -maxVelocity.x;
		}
		if(Math.abs(velocity.y) > maxVelocity.y && maxVelocity.y != -1)
		{
			if(velocity.y > 0)
				velocity.y = maxVelocity.y;
			else
				velocity.y = -maxVelocity.y;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && (state == PlayerState.WALK || state == PlayerState.IDLE))
			velocity.y -= 20;
		
		Vector2D correction = Remote2D.getInstance().map.getCorrection(entity.pos.getColliderWithDim(entity.getDim()),new Vector2D(velocity));
		velocity = new Vector2DF(new Vector2D(velocity.add(correction)).getElements());
		
		//velocity = velocity.multiply(new Vector2DF(friction,friction));
		
		entity.pos = entity.pos.add(velocity);
		
		if(velocity.y > 0)
			state = PlayerState.FALL;
		else if(velocity.y < 0)
			state = PlayerState.JUMP;
		else if(state == PlayerState.FALL)
			state = PlayerState.LAND;
		//velocity.print();
		
		int right = Remote2D.getInstance().map.camera.x+Remote2D.getInstance().displayHandler.width;
		int left = Remote2D.getInstance().map.camera.x;
		if(entity.pos.x+entity.getDim().x > right)
			Remote2D.getInstance().map.camera.x += (entity.pos.x+entity.getDim().x)-right;
		if(entity.pos.x < left)
			Remote2D.getInstance().map.camera.x -= left-entity.pos.x;
		
		if(state == PlayerState.LAND)
		{
			if(stateTimer <= 0)
			{
				state = PlayerState.IDLE;
				stateTimer = 0;
			} else
				stateTimer--;
		}
		
		if(state != oldState)
		{
			currentAnimation = Remote2D.getInstance().artLoader.getAnimation(getPath());
			if(state == PlayerState.LAND && currentAnimation != null)
				stateTimer = currentAnimation.getFramelength()*currentAnimation.getFrames().x*currentAnimation.getFrames().y;
		}
		
		if(currentAnimation != null)
			currentAnimation.flippedX = spriteFacesRight ? (facing == FacingState.LEFT) : (facing == FacingState.RIGHT);
			
	}
	
	public void updateAnimation()
	{
		currentAnimation = Remote2D.getInstance().artLoader.getAnimation(getPath());
	}

	@Override
	public void renderBefore(boolean editor) {
		if(facing == (spriteFacesRight?FacingState.LEFT:FacingState.RIGHT)){
			GL11.glPushMatrix();
			GL11.glScalef(1, 1, 1);
			//GL11.glNormal3f(entity.pos.x+entity.getDim().x/2, entity.pos.y+entity.getDim().y/2, 1);
		}
		
		if(currentAnimation != null)
		{
			Vector2D posVec = new Vector2D(0,0);
			posVec.x = entity.pos.x+entity.getDim().x/2-currentAnimation.getSpriteDim().x/2;
			posVec.y = entity.pos.y+entity.getDim().y/2-currentAnimation.getSpriteDim().y/2;
			currentAnimation.render(posVec, currentAnimation.getSpriteDim());
		}
		
		if(facing == (spriteFacesRight?FacingState.LEFT:FacingState.RIGHT))
			GL11.glPopMatrix();
	}

	@Override
	public void onEntitySpawn() {
		updateAnimation();
	}
	
	public String getPath()
	{
		switch(state)
		{
		case IDLE:
			return idleAnimation;
		case WALK:
			return walkAnimation;
		case JUMP:
			return jumpAnimation;
		case FALL:
			return fallAnimation;
		case LAND:
			return landAnimation;
		}
		return "";
	}

	@Override
	public void renderAfter(boolean editor) {
		
	}

	@Override
	public void apply() {
		currentAnimation = Remote2D.getInstance().artLoader.getAnimation(idleAnimation);
	}

	@Override
	public Component clone() {
		ComponentPlayer player = new ComponentPlayer(entity);
		player.idleAnimation = idleAnimation;
		player.walkAnimation = walkAnimation;
		player.jumpAnimation = jumpAnimation;
		player.fallAnimation = jumpAnimation;
		player.landAnimation = landAnimation;
		player.updateAnimation();
		return player;
	}
	
	enum PlayerState
	{
		IDLE, WALK, JUMP, FALL, LAND;
	}
	
	enum FacingState
	{
		LEFT, RIGHT;
	}

}
