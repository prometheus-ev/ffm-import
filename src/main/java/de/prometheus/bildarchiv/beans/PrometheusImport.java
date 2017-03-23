package de.prometheus.bildarchiv.beans;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PrometheusImport {

	@XmlElementWrapper(name="works")
	@XmlElement(name = "work")
	protected Set<Work> works;
	
	public PrometheusImport() { }
	
	public void setWorks(Set<Work> works) {
		this.works = works;
	}
	
	public Set<Work> getUnmodifiableWorks() {
		return Collections.unmodifiableSet(works);
	}

	@Override
	public String toString() {
		return "PrometheusImport [works=" + works.size() + "]";
	}
	
}