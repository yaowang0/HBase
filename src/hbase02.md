Standalone Model install：<br>
tar -zxvf hbase-1.1.3-bin.tat.gz<br>
cd  hbase-1.1.2<br>
cd conf<br>
vim hbase-env.sh<br>
export JAVA_HOME=<br>

vim hbase-site.xml<br>
```
<configuration>
<property>
	<name>hbase.rootdir</name>
	<value>file:///opt/data/hbase</value>
</property>
<property>
	<name>hbase.zookeeper.property.dataDir</name>
	<value>/opte/data/zookeeper</value>
</property>
</configuration>
```
bin/start-hbase.sh<br>
jps<br>
只包含：<br>
HMaster<br>
