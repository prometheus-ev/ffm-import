package de.prometheus.bildarchiv.model;

import java.util.List;
import java.util.Set;

import org.openarchives.model.Entity;

public class Part extends Basic {

	private static final long serialVersionUID = 9092233902468098037L;

	private List<String> creators;

	private Set<String> locations;

	public Part(Entity entity) {
		super(entity);
	}

	public List<String> getCreators() {
		return creators;
	}

	public void setCreators(List<String> creators) {
		this.creators = creators;
	}

	public void setLocation(Set<String> locations) {
		this.locations = locations;
	}
	
	public Set<String> getLocations() {
		return locations;
	}
	

	@Override
	public String toString() {
		return "PartOf [creators=" + creators + ", location=" + locations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((creators == null) ? 0 : creators.hashCode());
		result = prime * result + ((locations == null) ? 0 : locations.hashCode());
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
		Part other = (Part) obj;
		if (creators == null) {
			if (other.creators != null)
				return false;
		} else if (!creators.equals(other.creators))
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		return true;
	}


}
