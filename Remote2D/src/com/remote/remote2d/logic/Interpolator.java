package com.remote.remote2d.logic;

public class Interpolator {
	
	public static double linearInterpolate(double y1, double y2, double mu)
	{
		return(y1*(1-mu)+y2*mu);
	}
	
	private static float[] linearInterpolate(Vector y1, Vector y2, double mu)
	{
		float[] interp = new float[y1.getVectorLength() > y2.getVectorLength() ? y1.getVectorLength() : y2.getVectorLength()];
		int max = y1.getVectorLength() > y2.getVectorLength() ? y2.getVectorLength() : y1.getVectorLength();
		for(int x=0;x<max;x++)
			interp[x] = (float)linearInterpolate(y1.getElements()[x],y2.getElements()[x],mu);
		
		for(int x=max;x<interp.length;x++)
			interp[x] = y1.getVectorLength() > y2.getVectorLength() ? y1.getElements()[x] : y2.getElements()[x];
		return interp;
	}
	
	public static Vector2 linearInterpolate2i(Vector y1, Vector y2, double mu)
	{
		float[] interp = linearInterpolate(y1,y2,mu);
		return (Vector2)new Vector2(0,0).convertElementsToVector(interp);
	}
	
	public static Vector2 linearInterpolate2f(Vector y1, Vector y2, double mu)
	{
		float[] interp = linearInterpolate(y1,y2,mu);
		return (Vector2)new Vector2(0,0).convertElementsToVector(interp);
	}
	
	public static double cosineInterpolate(double y1, double y2, double mu)
	{
		double mu2 = (1-Math.cos(mu*3.1415926535d))/2;
		return(y1*(1-mu2)+y2*mu2);
	}
	
	public static double cubicInterpolate(double y0, double y1, double y2, double y3, double mu)
	{
		double a0,a1,a2,a3,mu2;
		
		mu2 = mu*mu;
		a0 = y3 - y2 - y0 + y1;
		a1 = y0 - y1 - a0;
		a2 = y2 - y0;
		a3 = y1;
		return(a0*mu*mu2+a1*mu2+a2*mu+a3);
	}
}
