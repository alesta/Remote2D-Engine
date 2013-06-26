package com.remote.remote2d.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Texture;

public class R2DTypeAnimation extends R2DType {
	
	public Animation data;

	public R2DTypeAnimation(String id) {
		super(id);
	}
	
	public R2DTypeAnimation(String id, Animation data) {
		this(id);
		this.data = data;
	}

	@Override
	public void read(DataInput d) throws IOException {
		data = Remote2D.getInstance().artLoader.getAnimation(d.readUTF());
	}

	@Override
	public void write(DataOutput d) throws IOException {
		d.writeUTF(data.getPath());
	}

	@Override
	public byte getId() {
		return 13;
	}
	
	@Override
	public String toString()
	{
		return data.getPath();
	}

}
