package com.remote.remote2d.gui.editor;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2D;

public class GuiCreateSpriteSheet extends GuiMenu {
	
	Animation animation;
	GuiTextField texID;
	GuiTextField startX;
	GuiTextField startY;
	GuiTextField dimX;
	GuiTextField dimY;
	GuiTextField framesX;
	GuiTextField framesY;
	GuiTextField paddingX;
	GuiTextField paddingY;
	GuiTextField frameLength;
	GuiTextField animSave;
	
	GuiButton createButton;
	
	Vector2D oldOffset;
	Vector2D offset;
	
	int scale = 1;
	
	public GuiCreateSpriteSheet()
	{
		backgroundColor = 0x7f9ddf;
		texID = new GuiTextField(new Vector2D(100,10), new Vector2D(200,40), 20);
		
		startX = new GuiTextField(new Vector2D(100,55), new Vector2D(100,40), 20);
		startX.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		startX.defaultText = "X";
		startY = new GuiTextField(new Vector2D(200,55), new Vector2D(100,40), 20);
		startY.defaultText = "Y";
		startY.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		
		dimX = new GuiTextField(new Vector2D(100,100), new Vector2D(100,40), 20);
		dimX.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		dimX.defaultText = "X";
		dimY = new GuiTextField(new Vector2D(200,100), new Vector2D(100,40), 20);
		dimY.defaultText = "Y";
		dimY.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		
		framesX = new GuiTextField(new Vector2D(100,145), new Vector2D(100,40), 20);
		framesX.defaultText = "X";
		framesX.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		framesY = new GuiTextField(new Vector2D(200,145), new Vector2D(100,40), 20);
		framesY.defaultText = "Y";
		framesY.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		
		paddingX = new GuiTextField(new Vector2D(100,190), new Vector2D(100,40), 20);
		paddingX.defaultText = "X";
		paddingX.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		paddingY = new GuiTextField(new Vector2D(200,190), new Vector2D(100,40), 20);
		paddingY.defaultText = "Y";
		paddingY.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		
		frameLength = new GuiTextField(new Vector2D(100,235), new Vector2D(200,40), 20);
		frameLength.defaultText = "(ms)";
		frameLength.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		
		animSave = new GuiTextField(new Vector2D(100,280), new Vector2D(200,40), 20);
		
		offset = new Vector2D(300,0);
		oldOffset = new Vector2D(300,0);
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		
		createButton = new GuiButton(0,new Vector2D(5,getHeight()-45),new Vector2D(100,40),"Create");
		buttonList.add(createButton);
		buttonList.add(new GuiButton(1,new Vector2D(110,getHeight()-45),new Vector2D(100,40),"Cancel"));
		buttonList.add(new GuiButton(2,new Vector2D(5,getHeight()-90),new Vector2D(205,40),"Regenerate"));
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		texID.render(interpolation);
		startX.render(interpolation);
		startY.render(interpolation);
		dimX.render(interpolation);
		dimY.render(interpolation);
		framesX.render(interpolation);
		framesY.render(interpolation);
		paddingX.render(interpolation);
		paddingY.render(interpolation);
		frameLength.render(interpolation);
		animSave.render(interpolation);
		
		Fonts.get("Arial").drawString("Tex. Path", 5, 20, 20, 0xffffff);
		Fonts.get("Arial").drawString("Start Pos", 5, 65, 20, 0xffffff);
		Fonts.get("Arial").drawString("Sprite Dim", 5, 110, 20, 0xffffff);
		Fonts.get("Arial").drawString("# Frames", 5, 155, 20, 0xffffff);
		Fonts.get("Arial").drawString("Padding", 5, 200, 20, 0xffffff);
		Fonts.get("Arial").drawString("Length", 5, 245, 20, 0xffffff);
		Fonts.get("Arial").drawString("Anim. Name", 5, 290, 20, 0xffffff);
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		Vector2D realOffset = Interpolator.linearInterpolate(oldOffset, offset, interpolation);
		
		drawBlueprintBackground();
		if(Remote2D.getInstance().artLoader.textureExists(texID.text))
		{
			Texture tex = Remote2D.getInstance().artLoader.getTexture(texID.text);
			tex.bind();			
			Vector2D dim = new Vector2D(tex.image.getWidth(),tex.image.getHeight());
			
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(300, 0, getWidth()-300, getHeight());
			
			GL11.glPushMatrix();
				GL11.glTranslatef(realOffset.x, realOffset.y, 0);
				GL11.glScalef(scale, scale, 1);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glTexCoord2f(0, 0);
					GL11.glVertex2f(0, 0);
					GL11.glTexCoord2f(1, 0);
					GL11.glVertex2f(dim.x, 0);
					GL11.glTexCoord2f(1, 1);
					GL11.glVertex2f(dim.x,dim.y);
					GL11.glTexCoord2f(0, 1);
					GL11.glVertex2f(0, dim.y);
				GL11.glEnd();
			GL11.glPopMatrix();
			if(animation != null)
			{
				GL11.glPushMatrix();
					GL11.glTranslatef(realOffset.x, realOffset.y, 0);
					GL11.glScalef(scale, scale, 1);
					animation.renderFrames();
				GL11.glPopMatrix();
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				
				Vector2D spriteDim = animation.getSpriteDim();
				animation.render(new Vector2D(10,350), spriteDim);
			}
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		texID.tick(i, j, k);
		startX.tick(i, j, k);
		startY.tick(i, j, k);
		dimX.tick(i, j, k);
		dimY.tick(i, j, k);
		framesX.tick(i, j, k);
		framesY.tick(i, j, k);
		paddingX.tick(i, j, k);
		paddingY.tick(i, j, k);
		frameLength.tick(i, j, k);
		animSave.tick(i, j, k);
		
		oldOffset = offset.copy();
		
		createButton.setDisabled(!isReady());
		
		if(Remote2D.getInstance().getKeyboardList().contains('[') && scale >= 2)
			scale /= 2;
		else if(Remote2D.getInstance().getKeyboardList().contains(']'))
			scale *= 2;

		if(Remote2D.getInstance().artLoader.textureExists(texID.text) )
		{
			Texture tex = Remote2D.getInstance().artLoader.getTexture(texID.text); 
			boolean up = Keyboard.isKeyDown(Keyboard.KEY_UP);
			boolean down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
			boolean left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
			boolean right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
			if(up)
				offset.y += 5;
			if(down)
				offset.y -= 5;
			if(left)
				offset.x += 5;
			if(right)
				offset.x -= 5;
			
			if(offset.x+tex.image.getWidth()*scale < getWidth())
				offset.x = getWidth()-tex.image.getWidth();
			if(offset.y+tex.image.getHeight()*scale < getHeight())
				offset.y = getHeight()-tex.image.getHeight();
			
			if(offset.x > 300)
				offset.x = 300;
			if(offset.y > 0)
				offset.y = 0;
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			R2DFileManager manager = new R2DFileManager("/res/anim/"+animSave.text+".r2d","R2D Animation: "+animSave.text,animation);
			manager.write();
			Remote2D.getInstance().guiList.pop();
		} else if(button.id == 1)
		{
			Remote2D.getInstance().guiList.pop();
		} else if(button.id == 2)
		{
			if(isReady())
			{
				animation = new Animation(
						texID.text,
						new Vector2D(Integer.parseInt(startX.text),Integer.parseInt(startY.text)),
						new Vector2D(Integer.parseInt(dimX.text),Integer.parseInt(dimY.text)),
						new Vector2D(Integer.parseInt(paddingX.text),Integer.parseInt(paddingY.text)),
						new Vector2D(Integer.parseInt(framesX.text),Integer.parseInt(framesY.text)),
						Integer.parseInt(frameLength.text)
				);
			}
		}
	}
	
	private boolean isReady()
	{
		boolean containsKey = Remote2D.getInstance().artLoader.textureExists(texID.text);
		boolean total = texID.hasText() && startX.hasText() && startY.hasText() 
				&& dimX.hasText() && dimY.hasText() && framesX.hasText() &&
				framesY.hasText() && paddingX.hasText() && paddingY.hasText()
				&& containsKey && frameLength.hasText() && animSave.hasText();
		return total;
	}

}
