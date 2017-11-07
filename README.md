# ffm-import
A gentle command line tool for harvesting OAI-PMH XML data provided by <a href="https://github.com/coneda/kor">ConedaKOR</a> (Frankfurt)  
  
Building the jar file (ffm.jar) with Maven:  
<code>$ cd /ffm-import/</code>  
<code>$ mvn clean install</code>
  
The generated jar file is located under <code>/ffm-import/target/ffm-import-jar-with-dependencies.jar</code>  
Rename the file from <code>ffm-import-jar-with-dependencies.jar</code> to <code>ffm.jar</code>.
  
Running the programm:  
<code>$ java -Xms1g -Xmx2g -jar ffm.jar -c ./conf -d ./data</code>

  
## Current data model  
<img src="https://github.com/matana/ffm-import/blob/master/20170511_154237.jpg" alt="The chaos graph" />

# separate harvesting and transforming
To generate separate harvesting and transforming applications change mainClass element value of plugin element in pom.xml from de.prometheus.bildarchiv.Application to de.prometheus.bildarchiv.Harvester or de.prometheus.bildarchiv.Transformer respectively

<plugin>
	<artifactId>maven-assembly-plugin</artifactId>
	<configuration>
		<finalName>ffm-import</finalName>
		<archive>
			<manifest>
				<mainClass>de.prometheus.bildarchiv.Application</mainClass>
			</manifest>
		</archive>
		<descriptorRefs>
			<descriptorRef>jar-with-dependencies</descriptorRef>
		</descriptorRefs>
	</configuration>
	<executions>
		<execution>
			<id>make-assembly</id> <!-- this is used for inheritance merges -->
			<phase>package</phase> <!-- bind to the packaging phase -->
			<goals>
				<goal>single</goal>
			</goals>
		</execution>
	</executions>
</plugin>

Rebuild and run with above commands. Make sure data directory specified is the same for Harvester and Transformer.