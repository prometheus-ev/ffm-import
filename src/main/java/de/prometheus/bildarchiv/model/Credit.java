package de.prometheus.bildarchiv.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.openarchives.model.Entity.Fields.Field;

public class Credit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1792189323952762792L;
	
	private String author;
	private String title;
	private String editor;
	private String volume;
	private String publisher;
	private String yearOfPublication;
	private String placeOfPublication;
	private String internalReference;
	private String comment;
		
	public Credit(Literature literature, List<String> properties) {
		super();
		if(literature != null) {
			String author = null;
			if (literature.getAuthor() != null && literature.getAuthor().getTitle() != null) {
				String[] authorNameParts = literature.getAuthor().getTitle().split(", ");
				ArrayUtils.reverse(authorNameParts);
				author = String.join(", ", authorNameParts);
			}
			this.author = author;

			String title = null;
			if (literature.getTitle() != null) {
				title = literature.getTitle() + (literature.getDistinction() == null ? "" : (" " + literature.getDistinction()));
			}
			this.title = title;

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
			if (literature.getPublisher() != null && literature.getPublisher().getTitle() != null) {
				String[] editorNameParts = literature.getPublisher().getTitle().split(", ");
				ArrayUtils.reverse(editorNameParts);
				editor = String.join(", ", editorNameParts);
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

			String internalReference = null;
			for(String property : properties) {
				if (internalReference == null) {
					internalReference = property;
				}
				else {
					internalReference = internalReference + ", " + property;
				}
			}
			this.internalReference = internalReference;
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
	public String getInternalReference() {
		return internalReference;
	}
	public void setInternalReference(String internalReference) {
		this.internalReference = internalReference;
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
	
	@Override
	public String toString() {
		return "Credit [author=" + author + ", title=" + title + ", editor=" + editor + ", volume=" + volume
				+ ", publisher=" + publisher + ", yearOfPublication=" + yearOfPublication + ", placeOfPublication="
				+ placeOfPublication + ", internalReference=" + internalReference + ", comment=" + comment + "]";
	}
	
}
