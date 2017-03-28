package de.prometheus.bildarchiv.beans;

import java.util.List;

import org.openarchives.beans.Entity;

// Medium
public class Medium extends Basic {
	
	private static final long serialVersionUID = 500834900196543618L;
	
	private List<Person> photographers;
	private Institution exploitationRight;
	private String imagePath;
	
	public Medium() { }
	
//	public Medium(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
//			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt, String imagePath) {
//		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
//		this.imagePath = imagePath;
//	}
	
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
