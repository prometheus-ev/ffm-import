package de.prometheus.bildarchiv.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This class represents the bibliographic source of an illustration 
 */
public class Source extends Basic {

	private static final long serialVersionUID = 6360779168686812511L;
	
	private Literature literature;
	@XmlElementWrapper(name="inner_references")
	@XmlElement(name = "inner_reference")
	private List<String> innerReferences;
	
	public Source(Literature literature){
		this.literature = literature;
	}
	
	public Literature getLiterature() {
		return literature;
	}
	public void setLiterature(Literature literature) {
		this.literature = literature;
	}
	/*public List<String> getInnerReferences() {
		return innerReferences;
	}*/
	public void setInnerReferences(List<String> innerReferences) {
		this.innerReferences = innerReferences;
	}
	
	public void addInnerReference(String innerReference) {
		if (innerReferences == null) {
			innerReferences = new ArrayList<String>();
		}
		innerReferences.add(innerReference);
	}
	
	@Override
	public String toString() {
		return "Source [literature=" + literature + ", innerReferences=" + innerReferences + "]";
	}
	
}
