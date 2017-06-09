package de.prometheus.bildarchiv.model;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FFMConedakorSource {

	@XmlElementWrapper(name="works")
	@XmlElement(name = "work")
	protected Set<Work> works;
	
	public FFMConedakorSource() { }
	
	public FFMConedakorSource(Set<Work> works) {
		this.works = works;
	}

	public void setWorks(Set<Work> works) {
		this.works = works;
	}
	
	public Set<Work> getUnmodifiableWorks() {
		return Collections.unmodifiableSet(works);
	}

	@Override
	public String toString() {
		return "FFMConedakorSource [works=" + works.size() + "]";
	}
	
}