package de.prometheus.bildarchiv;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.Relation;

public final class Relations {
	
	public static final String wurdeGezeigtIn = "221611b8-d627-42ee-baeb-4ba093a69a44";
	public static final String mitgliedVon = "39f259c4-eda7-4849-936b-acc974eb0a7a";
	public static final String hatSchwester = "4dd97871-c4c2-4008-a6ef-0ed8e97d2f02";
	public static final String wirdIllustruertDurch = "819b072c-49ad-4ad8-b83f-d9bb242737d0";
	public static final String verwertungsrechtAmFoto = "c10f4765-0bed-4c4f-b221-7072a4518ee2";
	public static final String bewohntVon = "fed5b585-525c-485a-84d5-35f9e5c2c2e8";
	public static final String hatVater = "34dc8583-4d9d-4d1f-ac4f-46d3cc1a1b9c";
	public static final String erschienenIn = "8c838b8e-da68-4fea-9fb1-2f1e4be845e0";
	public static final String autorInVon = "6518a10e-a54f-40ed-bfa3-2294973a8fc0";
	public static final String aequivalentZu = "fda2628d-da16-4a0d-8048-609a65c59824";
	public static final String leiterVon = "c262572e-9936-4234-9759-1952a123b7b3";
	public static final String organisiertVon = "b48ec12c-063a-432e-a731-a08dc092f3b0";
	public static final String kuratiertVon = "b75ddfae-075a-49e6-8c51-415d4f63a0f0";
	public static final String ehemalsIn = "37efc254-370a-47cb-b99e-79a35fd7a43b";
	public static final String befindetSichIn = "5bb80ec9-b270-49d3-bf28-1d34416aba2d";
	public static final String institutionInOrt = "e4137765-16cb-471b-9c1f-be91986f552f";
	public static final String sterbeOrt = "5aef873d-8447-4e16-acdc-72b4bd63c8d8";
	public static final String hatGeschaffen = "7e36ac16-f040-4510-9a10-f56c7b190c9d";
	public static final String ausstellungskatalogZu = "9d2a27f3-09db-4a42-9fdb-677775d4b7b6";
	public static final String stelltDar = "bc4421b3-899d-45a7-9fc3-3fbbc7e451cd";
	public static final String bilddateiZuWerk = "f1f2d2c9-8e11-4a1e-bcc5-c898be1a7c62";
	public static final String fandStattIn = "3078b4bf-2df4-4ecf-92d3-b6622b06a944";
	public static final String bezugspunktFuer = "1d99d12f-aa71-4d5e-8b41-4cd2fd59d23c";
	public static final String schuelerInVon = "721ec762-a90f-48e2-b07e-b1f9dc1b9a8f";
	public static final String zugehoerigePublikationen = "849c07ec-5169-4773-baeb-2f1ea6e358a2";
	public static final String ausgestellteWerke = "7931901e-f783-4873-8415-a439a1fe8bcf";
	public static final String standortIn = "977e5f29-2faf-403d-b541-92e6cb4cc479";
	public static final String istTeilVonA = "c4ac71ea-df47-4c8e-869b-f1d82c8cc07b";
	public static final String geburtsortVon = "57ec1a76-09b3-4480-a563-a4217189ec1e";
	public static final String hatDatei = "ce0cb47f-d72d-45ad-9bbd-4170474ceb85";
	public static final String sammlungskatalog = "ed70502e-ea02-4a00-989c-a59f7b47a7c6";
	public static final String literaturEnthaeltBilddatei = "5823b68b-a83a-4ff1-8dd3-e0fe90e4eb7c";
	public static final String istEhepartnerVon = "0b4a6d2a-e4f3-4fab-8630-536014109b53";
	public static final String hatMutter = "f110ac4a-6178-4f17-881e-c5d69a1f79b7";
	public static final String zugehoerigeInformation = "5e162aa9-9989-4089-849a-130384dc3a3c";
	public static final String zugehoerigeMedien = "0f7eb576-7e9b-41f9-ad68-14ae082fea58";
	public static final String istTeilVonB = "8554ee75-43ac-4c42-9d38-1b18d3e0c37a";
	public static final String herausgeberInVon = "360f76df-c2bc-42eb-b4ad-aaf40f39b56d";
	public static final String hatBruder = "6ee3536e-cf77-40a2-909a-7bd2b52753df";
	public static final String stehtInVerbindungZu = "5b9bcb28-9eb9-4f5e-80b8-b2ee34be69d1";
	public static final String fotografiertVon = "9d65a331-bcb7-40cf-8512-f630ad6890f6";
	public static final String basisdatenZumWerkAus = "d9907615-d4c9-405f-be39-83974a2c55a0";
	public static final String gehoertZurAusstellung = "9c8a6a15-cc3d-4a13-a77b-1faa3704ae56";
	public static final String auftraggeberVonWerk = "97b01978-e9f3-437a-851a-b37e3253ccec";

	private Relations() {
		
	}

	public static void main(String[] args) throws IOException, JAXBException {
		String listRecords = Endpoint.RELATIONS.listRecords();
		HttpURLConnection c = GentleUtils.getConnectionFor(listRecords);
		JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(c);
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		for (RecordType recordType : records) {
			System.out.println(recordType.getMetadata().getRelation());
		}
	}
	
	public static Set<Relation> getRelations() throws IOException, JAXBException {
		String listRecords = Endpoint.RELATIONS.listRecords();
		HttpURLConnection c = GentleUtils.getConnectionFor(listRecords);
		JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(c);
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		Set<Relation> toReturn = new HashSet<>();
		for (RecordType recordType : records) {
			toReturn.add(recordType.getMetadata().getRelation());
		}
		return toReturn;
	}

}
