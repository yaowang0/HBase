Distributed Model install:<br>

查看Zookeeper集群状态：<br>
bin/zkServer.sh status<br>

启动Hadoop:<br>
start-all.sh<br>

cd conf<br>
vim hbase-env.sh<br>
export JAVA_HOME=<br>
export HBASE_MANAGERS_ZK=false<br>

vim hbase.site.xml<br>
<configuration>
	<property>
		<name>hbase.rootdir</name>
		<value>hdfs://namenode.example.org:8020/hbase</value> <!-- hdfs的地址 hdfs://namenode.example.org:8020 为hadoop core-site.xml中 fs.defaultFS的value值-->
	</property>
	<property>
		<name>hbase.cluster.distributed</name>
		<value>true</value>
	</property>
	<property>
		<name>hbase.zookeeper.quorum</name>
		<value>node-a.example.com,node-b.example.com,node-c.example.com</value> <!-- hostname-->
	</property>
</confguration>

vim regionservers<br>
hostname1<br>
hostname2<br>
hostname3<br>

将Hadoop配置文件的地址to HBASE_CLASSPATH<br>
vim hbase-env.sh<br>
export HBASE_CLASSPATH=/opt/hadoop-2.7.1/etc/hadoop<br>


将hbase-1.1.3分发到其他节点：<br>
scp -r hbase-1.1.3 root@slave1:/opt/<br>
scp -r hbase-1.1.3 root@slave2:/opt/<br>

启动：<br>
start-hbase.sh<br>
停止：<br>
stop-hbase.sh<br>
