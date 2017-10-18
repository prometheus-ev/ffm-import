package de.prometheus.bildarchiv.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.openarchives.model.OAIPMHtype;
import org.openarchives.model.RecordType;

import de.prometheus.bildarchiv.exception.HttpRequestException;
import de.prometheus.bildarchiv.exception.HttpURLConnectionException;

/**
 * @author matana
 */
public interface Relations {
	
	public static final String WURDE_GEZEIGT_IN = "221611b8-d627-42ee-baeb-4ba093a69a44";
	public static final String MITGLIED_VON = "39f259c4-eda7-4849-936b-acc974eb0a7a";
	public static final String HAT_SCHWESTER = "4dd97871-c4c2-4008-a6ef-0ed8e97d2f02";
	public static final String WIRD_ILLUSTRIERT_DURCH = "819b072c-49ad-4ad8-b83f-d9bb242737d0";
	public static final String VERWERTUNGSRECHT_AM_FOTO = "c10f4765-0bed-4c4f-b221-7072a4518ee2";
	public static final String BEWOHNT_VON = "fed5b585-525c-485a-84d5-35f9e5c2c2e8";
	public static final String HAT_VATER = "34dc8583-4d9d-4d1f-ac4f-46d3cc1a1b9c";
	public static final String ERSCHIENEN_IN = "8c838b8e-da68-4fea-9fb1-2f1e4be845e0";
	public static final String AUTORIN_VON = "6518a10e-a54f-40ed-bfa3-2294973a8fc0";
	public static final String AEQUIVALENT_ZU = "fda2628d-da16-4a0d-8048-609a65c59824";
	public static final String LEITER_VON = "c262572e-9936-4234-9759-1952a123b7b3";
	public static final String ORGANISIERT_VON = "b48ec12c-063a-432e-a731-a08dc092f3b0";
	public static final String KURATIERT_VON = "b75ddfae-075a-49e6-8c51-415d4f63a0f0";
	public static final String EHEMALS_IN = "37efc254-370a-47cb-b99e-79a35fd7a43b";
	public static final String BEFINDET_SICH_IN = "5bb80ec9-b270-49d3-bf28-1d34416aba2d";
	public static final String INSTITUTION_IN_ORT = "e4137765-16cb-471b-9c1f-be91986f552f";
	public static final String STERBE_ORT = "5aef873d-8447-4e16-acdc-72b4bd63c8d8";
	public static final String HAT_GESCHAFFEN = "7e36ac16-f040-4510-9a10-f56c7b190c9d";
	public static final String AUSSTELLUNGSKATALOG_ZU = "9d2a27f3-09db-4a42-9fdb-677775d4b7b6";
	public static final String STELLT_DAR = "bc4421b3-899d-45a7-9fc3-3fbbc7e451cd";
	public static final String BILDDATEI_ZU_WERK = "f1f2d2c9-8e11-4a1e-bcc5-c898be1a7c62";
	public static final String FAND_STATT_IN = "3078b4bf-2df4-4ecf-92d3-b6622b06a944";
	public static final String BEZUGSPUNKT_FUER = "1d99d12f-aa71-4d5e-8b41-4cd2fd59d23c";
	public static final String SCHUELERIN_VON = "721ec762-a90f-48e2-b07e-b1f9dc1b9a8f";
	public static final String ZUGEHOERIGE_PUBLIKATIONEN = "849c07ec-5169-4773-baeb-2f1ea6e358a2";
	public static final String AUSGESTELLTE_WERKE = "7931901e-f783-4873-8415-a439a1fe8bcf";
	public static final String STANDORT_IN = "977e5f29-2faf-403d-b541-92e6cb4cc479";
	public static final String IST_TEIL_VON = "8554ee75-43ac-4c42-9d38-1b18d3e0c37a";
	public static final String GEBURTSORT_VON = "57ec1a76-09b3-4480-a563-a4217189ec1e";
	public static final String HAT_DATEI = "ce0cb47f-d72d-45ad-9bbd-4170474ceb85";
	public static final String SAMMLUNGSKATALOG = "ed70502e-ea02-4a00-989c-a59f7b47a7c6";
	public static final String LITERATUR_ENTHAELT_BILDDATEI = "5823b68b-a83a-4ff1-8dd3-e0fe90e4eb7c";
	public static final String IST_EHEPARTNER_VON = "0b4a6d2a-e4f3-4fab-8630-536014109b53";
	public static final String HAT_MUTTER = "f110ac4a-6178-4f17-881e-c5d69a1f79b7";
	public static final String ZUGEHOERIGE_INFORMATION = "5e162aa9-9989-4089-849a-130384dc3a3c";
	public static final String ZUGEHOERIGE_MEDIEN = "0f7eb576-7e9b-41f9-ad68-14ae082fea58";
	public static final String HERAUSGEBERIN_VON = "360f76df-c2bc-42eb-b4ad-aaf40f39b56d";
	public static final String HAT_BRUDER = "6ee3536e-cf77-40a2-909a-7bd2b52753df";
	public static final String STEHT_IN_VERBINDUNG_ZU = "5b9bcb28-9eb9-4f5e-80b8-b2ee34be69d1";
	public static final String FOTOGRAFIERT_VON = "9d65a331-bcb7-40cf-8512-f630ad6890f6";
	public static final String BASISDATEN_ZUM_WERK_AUS = "d9907615-d4c9-405f-be39-83974a2c55a0";
	public static final String GEHOERT_ZUR_AUSSTELLUNG = "9c8a6a15-cc3d-4a13-a77b-1faa3704ae56";
	public static final String AUFTRAGGEBER_VON_WERK = "97b01978-e9f3-437a-851a-b37e3253ccec";

	/**
	 * Prints all triple relations between all supported kinds (see {@link  Endpoint#KINDS}) <br>
	 * <code>subject -> predicate -> object</code>
	 * @throws IOException
	 * @throws JAXBException
	 * @throws HttpRequestException 
	 */
	public default void printRelations() throws IOException, JAXBException, HttpRequestException, HttpURLConnectionException {
		String url = Endpoint.RELATIONS.listRecords(null);
		HttpURLConnection c = GentleUtils.getHttpURLConnection(url);
		JAXBElement<OAIPMHtype> oai = GentleUtils.unmarshalOAIPMHtype(c, null);
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		for (RecordType recordType : records) {
			System.out.println(recordType.getMetadata().getRelation());
		}
	}
}
