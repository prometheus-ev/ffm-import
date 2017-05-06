package de.prometheus.bildarchiv.beans;

import java.util.List;

import org.openarchives.beans.Entity;

/**
 * This class represents an image with his associated attributes. 
 */
public class Medium extends Basic {
	
	private static final long serialVersionUID = 500834900196543618L;
	
	private List<Person> photographers;
	private Institution exploitationRight;
	private String imagePath;
	
	public Medium() { }
	
	public Medium(Entity entity) {
		super(entity);
		this.imagePath = entity.getImagePath() == null ? "unknown" : entity.getImagePath().getValue();
	}

	public List<Person> getPhotographers() {
		return photographers;
	}
	
	public void setPhotographers(List<Person> photographers) {
		this.photographers = photographers;
	}
	
	public Institution getExploitationRight() {
		return exploitationRight;
	}
	
	public void setExploitationRight(Institution exploitationRight) {
		this.exploitationRight = exploitationRight;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return "Medium [photographers=" + photographers + ", exploitationRight=" + exploitationRight + ", imagePath="
				+ imagePath + "]" + super.toString();
	}
	
	

}
