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

jps:<br>
HRegionServer<br>
HMaster<br>

版本依赖：<br>
https://hbase.apache.org/book.html#basic.prerequisites<br>

UI:<br>
1.0版本之前端口60010：hostname://60010<br>
1.0版本之后端口16010：hostname://16010<br>

hdfsUI<br>
hostname:50070<br>
在/hbase可以看到HBase的各种数据。<br>
/data为数据<br>

create 't1','f1','f2','f3'<br>
desc 't1'<br>
put 't1','r1','f1:name','zs'<br>
put 't1','r1','f1:sex','1'<br>
get 't1','r1','f1'<br>
get 't1','r1','f1:sex'<br>
alter 't1',{NAME => 'f1', VERSION => 3}<br>
put 't1','r1','f1:sex','2'<br>
put 't1','r1','f1:sex','1'<br>
get 't1','1',{COLUMN => 'f1:sex', VERSION => 3}<br>

flush命令：<br>
将memory数据刷到磁盘<br>
flush 't1'<br>