package com.remote.remote2d.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Texture;

public class R2DTypeTexture extends R2DType {
	
	public Texture data;

	public R2DTypeTexture(String id) {
		super(id);
	}
	
	public R2DTypeTexture(String id, Texture data) {
		this(id);
		this.data = data;
	}

	@Override
	public void read(DataInput d) throws IOException {
		data = Remote2D.getInstance().artLoader.getTexture(d.readUTF());
	}

	@Override
	public void write(DataOutput d) throws IOException {
		d.writeUTF(data.textureLocation);
	}

	@Override
	public byte getId() {
		return 12;
	}
	
	@Override
	public String toString()
	{
		return data.textureLocation;
	}

}
