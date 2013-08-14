package com.remote.remote2d.entity.component;

import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.logic.Collider;

public abstract class ComponentCollider extends Component {

	public ComponentCollider(Entity e) {
		super(e);
	}
	
	public abstract Collider getCollider();

}
