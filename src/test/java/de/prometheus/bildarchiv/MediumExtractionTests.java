package de.prometheus.bildarchiv;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openarchives.beans.ExtendedRelationship;
import org.openarchives.beans.Prometheus;

public class MediumExtractionTests {

	private static Set<ExtendedRelationship> bilddateiZuWerk;

	@BeforeClass
	public static void init() throws FileNotFoundException, JAXBException {
		Prometheus prom = GentleDataExtractor.load("/Users/matana/Desktop/conedaKor/28-03-2017_ffm_export.xml");
		bilddateiZuWerk = GentleDataExtractor.filterRelationships(prom.getRelations(), Relations.bilddateiZuWerk);
	}

	@Test
	public void getAllMediaFromWork() {

		// Medium -> Work
		Assert.assertNotNull(bilddateiZuWerk);

		Set<String> workIds = new HashSet<>();
		Set<String> mediumIds = new HashSet<>();
		Set<String> nullPointer = new HashSet<>();
		Map<String, Set<String>> collections = new HashMap<>();

		for (ExtendedRelationship extendedRelationship : bilddateiZuWerk) {

			if (extendedRelationship.getFrom().getImagePath() == null) {
				nullPointer.add(extendedRelationship.getFrom().getId());
				continue;
			}

			if (extendedRelationship.getTo().getTitle() == null) {
				nullPointer.add(extendedRelationship.getTo().getId());
				continue;
			}

			String workId = extendedRelationship.getTo().getId();
			String imagePath = extendedRelationship.getFrom().getImagePath().getValue();
			Set<String> collection = collections.get(workId);
			if (collection == null)
				collection = new HashSet<>();
			collection.add(imagePath);
			collections.put(workId, collection);

			mediumIds.add(extendedRelationship.getFrom().getId());
			workIds.add(extendedRelationship.getTo().getId());

		}

		System.out.println(String.format("work_ids=%s\n", workIds.size()));
		System.out.print(String.format("medium_ids=%s\n", mediumIds.size()));
		System.out.println(String.format("null_pointers=%s\n", nullPointer.size()));

	}
	
	@Test
	public void zickZackHausenTest() {
		String workId = "8a761bad-eee4-4870-9e93-e678eea2358b";
		Set<ExtendedRelationship> mediumSet = bilddateiZuWerk.stream().filter(x -> x.getTo().getId().equals(workId)).collect(Collectors.toSet());
		Assert.assertNotEquals(null, mediumSet);
		Assert.assertNotEquals(0, mediumSet.size());
		Assert.assertEquals(31, mediumSet.size());
	}
	
}