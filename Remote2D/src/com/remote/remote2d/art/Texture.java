package com.remote.remote2d.art;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

public class Texture {
	public boolean linearScaling = false;
	public boolean repeat = false;
	public String textureLocation;
	public BufferedImage image;
	public int glId;
	
	public Texture(String loc, boolean linearScaling, boolean repeat)
	{
		textureLocation = loc;
		this.linearScaling = linearScaling;
		this.repeat = repeat;
		image = TextureLoader.loadImage(textureLocation);
		glId = TextureLoader.loadTexture(image,linearScaling,repeat);
	}
	
	public Texture(BufferedImage image, boolean linearScaling,boolean repeat) {
		textureLocation = "";
		this.image = image;
		this.linearScaling = linearScaling;
		this.repeat = repeat;
		glId = TextureLoader.loadTexture(image,linearScaling,repeat);
	}

	public void bind()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
	}
	
	public void removeTexture()
	{
		GL11.glDeleteTextures(glId);
	}
	
	public String getTextureLocation() {
		return textureLocation;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public int getGlId() {
		return glId;
	}

	public void reload() {
		GL11.glDeleteTextures(glId);
		glId = TextureLoader.loadTexture(image,linearScaling,repeat);
	}
}
