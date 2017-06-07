# ffm-import
A gentle command line tool for harvesting OAI-PMH XML data provided by <a href="https://github.com/coneda/kor">ConedaKOR</a> (Frankfurt)  
  
Building the jar file (ffm.jar) with Maven:  
<code>$ cd /ffm-import/</code>  
<code>$ mvn clean install</code>
  
The generated jar file is located under <code>/ffm-import/target/ffm-import-jar-with-dependencies.jar</code>  
Rename the file from <code>ffm-import-jar-with-dependencies.jar</code> to <code>ffm.jar</code>.
  
Running the programm:  
<code>$ java -Xms1g -Xmx2g -c ./conf -jar ffm.jar -d ./data</code>

  
## Current data model  
<img src="https://github.com/matana/ffm-import/blob/master/20170511_154237.jpg" alt="The chaos graph" />
