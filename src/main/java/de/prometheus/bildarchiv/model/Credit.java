package de.prometheus.bildarchiv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.openarchives.model.Entity.Fields.Field;

public class Credit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1792189323952762792L;
	
	private String author;
	private String title;
	private String distinction;
	private String editor;
	private String volume;
	private String publisher;
	private String yearOfPublication;
	private String placeOfPublication;
	@XmlElementWrapper(name="internal_references")
	@XmlElement(name="internal_reference")
	private List<String> internalReferences;
	private String comment;
		
	public Credit(Literature literature, List<String> properties) {
		super();
		if(literature != null) {
			String author = null;
			if (literature.getAuthor() != null) {
				author = literature.getAuthor().getTitle();
			}
			this.author = author;

			String title = null;
			title = literature.getTitle();
			this.title = title;
			
			String distinction = null;
			distinction = literature.getDistinction();
			this.distinction = distinction;
			

			String volume = null;
			String publisher = null;
			String yearOfPublication = null;
			if(literature.getFields() != null) {
				for(Field field: literature.getFields().getField()) {
					if(field.getName() != null) {
						switch(field.getName()) {
						case "Band": 
							volume = field.getValue();
							break;
						case "publisher":
							publisher = field.getValue();
							break;
						case "year_of_publication":
							yearOfPublication = field.getValue();
							break;
						}
					}
				}
			}
			this.volume = volume;
			this.publisher = publisher;
			this.yearOfPublication = yearOfPublication;

			String editor = null;
			if (literature.getPublisher() != null) {
				editor = literature.getPublisher().getTitle();
			}
			this.editor = editor;

			String placeOfPublication = null;
			if(literature.getPublishedIn() != null) {
				placeOfPublication = literature.getPublishedIn().getTitle();
			}
			this.placeOfPublication = placeOfPublication; 

			String comment = null;
			comment = literature.getComment();
			this.comment = comment;

			this.internalReferences = properties;
		}
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getPlaceOfPublication() {
		return placeOfPublication;
	}
	public void setPlaceOfPublication(String placeOfPublication) {
		this.placeOfPublication = placeOfPublication;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getYearOfPublication() {
		return yearOfPublication;
	}
	public void setYearOfPublication(String yearOfPublication) {
		this.yearOfPublication = yearOfPublication;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getDistinction() {
		return distinction;
	}
	public void setDistinction(String distinction) {
		this.distinction = distinction;
	}
	public void addInternalRefernce(String internalReference) {
		if (this.internalReferences == null) {
			this.internalReferences = new ArrayList<String>();
		}
		this.internalReferences.add(internalReference);
	}

	@Override
	public String toString() {
		return "Credit [author=" + author + ", title=" + title + ", distinction=" + distinction + ", editor=" + editor
				+ ", volume=" + volume + ", publisher=" + publisher + ", yearOfPublication=" + yearOfPublication
				+ ", placeOfPublication=" + placeOfPublication + ", internalReferences=" + internalReferences
				+ ", comment=" + comment + "]";
	}

}
