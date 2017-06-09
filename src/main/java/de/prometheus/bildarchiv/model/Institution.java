package de.prometheus.bildarchiv.model;

import org.openarchives.model.Entity;

public class Institution extends Basic {
	
	private static final long serialVersionUID = 1881478228895488369L;
	
	private Place location;
	
	public Institution() { 
		super();
	}

	public Institution(Entity entity) {
		super(entity);
	}

	public Place getLocation() {
		return location;
	}
	
	public void setLocation(Place location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Institution [location=" + location + "]" + super.toString();
	}
	
}
