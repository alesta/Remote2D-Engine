package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.ColliderLogic;
import com.remote.remote2d.logic.ColliderSphere;
import com.remote.remote2d.logic.Collision;
import com.remote.remote2d.logic.Vector2D;


public class GuiWindowGeneralColliderTest extends GuiWindow {
	
	Collider mainCollider;
	Collider moveCollider;
	Collider altMoveCollider;
	Collider rawMoveCollider;
	Vector2D vec = new Vector2D(20,20);
	Collision collision = new Collision();

	public GuiWindowGeneralColliderTest(WindowHolder holder, Vector2D pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2D(500,500), allowedBounds, "Collider Test");
	}
		
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2D(10,10),new Vector2D(150,40),"AA Box"));
		buttonList.add(new GuiButton(1,new Vector2D(10,55),new Vector2D(150,40),"Sphere"));
		buttonList.add(new GuiButton(2,new Vector2D(340,10),new Vector2D(150,40),"AA Box"));
		buttonList.add(new GuiButton(3,new Vector2D(340,55),new Vector2D(150,40),"Sphere"));
		buttonList.add(new GuiButton(4,new Vector2D(175,450),new Vector2D(150,40),"Done"));
	}

	@Override
	public void renderContents() {
		if(mainCollider != null)
			mainCollider.drawCollider();
		if(moveCollider != null)
			moveCollider.drawCollider();
		
		if(altMoveCollider != null)
		{
			GL11.glColor3f(0, 1, 0);
			altMoveCollider.drawCollider();
			GL11.glColor3f(1, 1, 1);
		}
		
		if(rawMoveCollider != null)
		{
			GL11.glColor3f(0, 0, 1);
			rawMoveCollider.drawCollider();
			GL11.glColor3f(1, 1, 1);
		}
		
		if(collision.collides)
		{
			Fonts.get("Arial").drawString("Colliding!", 10, 200, 20, 0xffffff);
			Fonts.get("Arial").drawString(collision.correction.toString(), 10, 220, 20, 0xffffff);
		}
	}
	
	@Override
	public void tick(int i, int j, int k, double delta)
	{
		super.tick(i, j, k, delta);
		altMoveCollider = null;
		rawMoveCollider = null;
		if(mainCollider != null && moveCollider != null)
		{
			Vector2D m = getMouseInWindow(i,j);
			
			moveCollider = ColliderLogic.setColliderPos(moveCollider, m);
			
			collision = moveCollider.getCollision(mainCollider,vec);
			altMoveCollider = moveCollider.getTransformedCollider(vec.add(collision.correction));
			rawMoveCollider = moveCollider.getTransformedCollider(vec);
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			mainCollider = new ColliderBox(new Vector2D(150,175),new Vector2D(200,150));
		} else if(button.id == 1)
		{
			mainCollider = new ColliderSphere(new Vector2D(250,250),100);
		} else if(button.id == 2)
		{
			moveCollider = new ColliderBox(new Vector2D(150,175),new Vector2D(200,150));
			moveCollider.isIdle = false;
		} else if(button.id == 3)
		{
			moveCollider = new ColliderSphere(new Vector2D(250,250),100);
			moveCollider.isIdle = false;
		} else if(button.id == 4)
		{
			holder.closeWindow(this);
		}
	}

}
