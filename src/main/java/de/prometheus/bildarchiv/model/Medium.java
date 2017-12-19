package de.prometheus.bildarchiv.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.openarchives.model.Entity;

/**
 * This class represents an image with his associated attributes. 
 */
public class Medium extends Basic {
	
	private static final long serialVersionUID = 500834900196543618L;
	
	private List<Person> photographers;
	private Institution exploitationRight;
	@XmlElementWrapper(name="credits")
	@XmlElement(name="credit")
	private List<Credit> credits;
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
	
	public void addCredit(Credit credit) {
		if (this.credits == null) {
			this.credits = new ArrayList<Credit>();
		}
		this.credits.add(credit);
	}

	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	public String toString() {
		return "Medium [photographers=" + photographers + ", exploitationRight=" + exploitationRight + ", credits="
				+ credits + ", imagePath=" + imagePath + "]" + super.toString();
	}

}
