# Europeana Metadata Quality Assurance Toolkit

This toolkit is a Hadoop-based tool to measure different metadata quality metrics.

Usage

1) Install Hadoop (in Ubuntu 14.04 you can follow this guide: http://www.bogotobogo.com/Hadoop/BigData_hadoop_Install_on_ubuntu_single_node_cluster.php)

2) Start Hadoop

    start-dfs.sh
    start-yarn.sh

3) Create europeana.json (it is not part of the project, you can use Europeana API to create a file, which contains the full records, one record per line, without the API's payload information). More info comes soon.

4) Upload europeana.json to Hadoop

    hdfs dfs -mkdir europeana
    hdfs dfs -put path/to/europeana.json europeana

5) Build the project

    cd path/to/europeana-qa
    mvn clean install

6) Run Hadoop job

Right now we have three Hadoop jobs, as there are three different sources: the API's search results, the API's full record results, and the OAI-PMH result. The classes for analysis of these results are

* com.nsdr.europeana.qa.hadoop.CompletenessCountForFullRecord
* com.nsdr.europeana.qa.hadoop.CompletenessCountForSearchRecord
* com.nsdr.europeana.qa.hadoop.CompletenessCountForOaiRecord

To run Hadoop you should give the appropriate class, for example:

    hadoop jar europeana-qa-1.0-SNAPSHOT.jar \
      com.nsdr.europeana.qa.hadoop.CompletenessCountForFullRecord \
      /europeana/europeana.json /europeana/output

Where

- `haddop jar` is the main Hadoop command  
- `europeana-qa-1.0-SNAPSHOT.jar` is the Java jar file
- `com.nsdr.europeana.qa.hadoop.CompletenessCount` is the Java class which do the calculation
- `/europeana/europeana.json` is the input source containing the JSON records
- `/europeana/output` is the target directory, where the output will be landed as `part-r-00000` file (that's Hadoop default)

Note: if you run it several time, before the next run you should remove the output directory by

    hdfs dfs -rm -r -f europeana/output

7) Watch the result:

    hdfs dfs -cat europeana/output/part-r-00000

The result is something like that:

    /05812/B699370A_8F7C_4DBB_83E9_322192658499	0.33536586
    /08517/0043C1D03DF7D74846850A61FEC9002718ED17DE	0.6047904
    /08517/019A18E745BC0D259D6DB9CC1D4D42DB49A466FC	0.6047904
    /08517/029ED640821E0B503A1C865A336EB7D56F066CEA	0.6047904
    /08517/031533050922E81697C6BB049C400B3C94709318	0.6036036
    /08517/03158C73689983A03642F6C8F286A6C0EE13F744	0.6047904
    /08517/059685786EB66D46FA766A0AD990541ABC27BA6F	0.6047904
    /08517/0634E70E8EF52D0D371D24BD1007E422B5875A23	0.25786164

which is pair of Europeana ID and a double floating point result of the "completeness" metric in the range of 0 and 1.
