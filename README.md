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

    hadoop jar target/europeana-qa-1.0-SNAPSHOT.jar com.nsdr.europeana.qa.hadoop.CompletenessCount \
      /europeana/europeana.json /europeana/output

  Note: if you run it several time, before the next run you should remove the output directory by

    hdfs dfs -rm -r -f europeana/output

7) Watch the result:

    hdfs dfs -cat europeana/output/part-r-00000

