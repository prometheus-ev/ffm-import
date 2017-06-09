package de.prometheus.bildarchiv.model;

import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.openarchives.model.Entity;
import org.openarchives.model.OAIPMHtype;
import org.openarchives.model.RecordType;
import org.openarchives.model.ResumptionTokenType;

public class OAIPMHtypeWrapper {

	private JAXBElement<OAIPMHtype> oai;

	public OAIPMHtypeWrapper(JAXBElement<OAIPMHtype> oai) {
		this.oai = oai;
	}

	public int listSize() {
		BigInteger size = oai.getValue().getListRecords().getResumptionToken().getCompleteListSize();
		return size.intValue();
	}

	public ResumptionTokenType getResumptionToken() {
		return oai.getValue().getListRecords().getResumptionToken();
	}

	public List<RecordType> getRecords() {
		return oai.getValue().getListRecords().getRecord();
	}
	
	public Entity getEntity() {
		return oai.getValue().getGetRecord().getRecord().getMetadata().getEntity();
	}

	public boolean valid() {
		return false;
	}

	public boolean validEntity() {
		return oai != null && oai.getValue() != null && oai.getValue().getGetRecord() != null
				&& oai.getValue().getGetRecord().getRecord() != null
				&& oai.getValue().getGetRecord().getRecord().getMetadata() != null
				&& oai.getValue().getGetRecord().getRecord().getMetadata().getEntity() != null;
	}

}
