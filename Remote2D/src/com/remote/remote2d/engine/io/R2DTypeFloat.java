package com.remote.remote2d.engine.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class R2DTypeFloat extends R2DType {
	
	public float data;

	public R2DTypeFloat(String id) {
		super(id);
	}
	
	public R2DTypeFloat(String id, float data) {
		this(id);
		this.data = data;
	}

	@Override
	public void read(DataInput d) throws IOException {
		data = d.readFloat();
	}

	@Override
	public void write(DataOutput d) throws IOException {
		d.writeFloat(data);
	}

	@Override
	public byte getId() {
		return 5;
	}
	
	@Override
	public String toString()
	{
		return data+"";
	}

}
