package com.nsdr.europeana.qa.hadoop;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.*;

public class JsonPathBasedCompletenessMapperTest {

	private final Text value = new Text("{\"edm:ProvidedCHO\":[{\"@about\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"edm:WebResource\":[{\"@about\":\"http:\\/\\/www.musei.uniroma1.it\\/erbario\\/catalogo\\/gestionedb\\/scheda.asp?inventario=2919\"},{\"@about\":\"http:\\/\\/www.musei.uniroma1.it\\/dbinfo\\/RMSMUS12\\/JPEG\\/2919.jpg\"}],\"ore:Aggregation\":[{\"@about\":\"http:\\/\\/data.europeana.eu\\/aggregation\\/provider\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\",\"edm:aggregatedCHO\":[{\"@resource\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"edm:dataProvider\":[\"Erbario - Museo di Botanica- Sapienza University Rome\"],\"edm:isShownAt\":[{\"@resource\":\"http:\\/\\/www.musei.uniroma1.it\\/erbario\\/catalogo\\/gestionedb\\/scheda.asp?inventario=2919\"}],\"edm:isShownBy\":[{\"@resource\":\"http:\\/\\/www.musei.uniroma1.it\\/dbinfo\\/RMSMUS12\\/JPEG\\/2919.jpg\"}],\"edm:object\":[{\"@resource\":\"http:\\/\\/www.musei.uniroma1.it\\/dbinfo\\/RMSMUS12\\/JPEG\\/2919.jpg\"}],\"edm:provider\":[\"Linked Heritage\"],\"edm:rights\":[{\"@resource\":\"http:\\/\\/www.europeana.eu\\/rights\\/rr-f\\/\"}]}],\"ore:Proxy\":[{\"@about\":\"http:\\/\\/data.europeana.eu\\/proxy\\/provider\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\",\"dc:description\":[\"Famiglia: Cruciferae; Genere: Nasturtium; Specie: officinale R. Br.\",\"Repository\\/Location: Erbario - Museo di Botanica- Sapienza University Rome\"],\"dc:identifier\":[\"inventory number 2919\"],\"dc:rights\":[\"Erbario - Museo di Botanica- Sapienza University Rome [Resource]\"],\"dc:source\":[\"Erbario - Museo di Botanica- Sapienza University Rome\"],\"dc:title\":[\"Nasturtium officinale\"],\"edm:europeanaProxy\":[\"false\"],\"ore:proxyFor\":[{\"@resource\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"ore:proxyIn\":[{\"@resource\":\"http:\\/\\/data.europeana.eu\\/aggregation\\/provider\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"edm:type\":[\"IMAGE\"]},{\"@about\":\"http:\\/\\/data.europeana.eu\\/proxy\\/europeana\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\",\"edm:europeanaProxy\":[\"true\"],\"ore:proxyFor\":[{\"@resource\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"ore:proxyIn\":[{\"@resource\":\"http:\\/\\/data.europeana.eu\\/aggregation\\/europeana\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"edm:type\":[\"IMAGE\"]}],\"edm:EuropeanaAggregation\":[{\"@about\":\"http:\\/\\/data.europeana.eu\\/aggregation\\/europeana\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\",\"edm:aggregatedCHO\":[{\"@resource\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\"}],\"edm:collectionName\":[\"2023829_AG-EU_LinkedHeritage_Herbarium\"],\"edm:country\":[\"Italy\"],\"edm:preview\":[{\"@resource\":\"http:\\/\\/europeanastatic.eu\\/api\\/image?uri=http:\\/\\/www.musei.uniroma1.it\\/dbinfo\\/RMSMUS12\\/JPEG\\/2919.jpg&size=LARGE&type=TEXT\"}],\"edm:landingPage\":[{\"@resource\":\"http:\\/\\/europeana.eu\\/portal\\/record\\/null.html\"}],\"edm:language\":[\"it\"]}],\"qIdentifier\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\",\"identifier\":\"http:\\/\\/data.europeana.eu\\/item\\/2023829\\/0003BD0E39A5AD674A1682C144699B279C7EF163\",\"sets\":[\"2023829_AG-EU_LinkedHeritage_Herbarium\"]}");

	@Test
	public void outputWithLabel() throws IOException, InterruptedException {

		new MapDriver<LongWritable, Text, Text, Text>()
			.withMapper(new JsonPathBasedCompletenessMapper(true))
			.withInput(new LongWritable(0), value)
			.withOutput(
				new Text("\"Erbario - Museo di Botanica...\",2023829/0003BD0E39A5AD674A1682C144699B279C7EF163"),
				new Text("\"TOTAL\":0.388889,\"MANDATORY\":0.692308,\"DESCRIPTIVENESS\":0.272727,\"SEARCHABILITY\":0.277778,\"CONTEXTUALIZATION\":0.090909,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.214286,\"VIEWING\":0.750000,\"REUSABILITY\":0.416667,\"MULTILINGUALITY\":0.400000"))
			.runTest();
	}

	@Test
	public void outputWithoutLabel() throws IOException, InterruptedException {

		new MapDriver<LongWritable, Text, Text, Text>()
			.withMapper(new JsonPathBasedCompletenessMapper(false))
			.withInput(new LongWritable(0), value)
			.withOutput(
				new Text("\"Erbario - Museo di Botanica...\",2023829/0003BD0E39A5AD674A1682C144699B279C7EF163"),
				new Text("0.388889,0.692308,0.272727,0.277778,0.090909,0.500000,0.214286,0.750000,0.416667,0.400000"))
			.runTest();
	}

}