package de.prometheus.bildarchiv.beans;

import java.util.List;

import org.openarchives.beans.Entity;

public class PartOf extends Basic {

	private static final long serialVersionUID = 9092233902468098037L;

	private List<String> creators;
	private String location;

	public PartOf(Entity entity) {
		super(entity);
	}

	public List<String> getCreators() {
		return creators;
	}

	public void setCreators(List<String> creators) {
		this.creators = creators;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	

	@Override
	public String toString() {
		return "PartOf [creators=" + creators + ", location=" + location + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((creators == null) ? 0 : creators.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartOf other = (PartOf) obj;
		if (creators == null) {
			if (other.creators != null)
				return false;
		} else if (!creators.equals(other.creators))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}
	
	

}
