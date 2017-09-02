package com.yao.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class TestHbase {
   public static Connection conn = null;
    public static TableName test = TableName.valueOf("t_phone");
    public static Random ra = new Random();
    @Test
    public void create() throws IOException {

        Admin admin = conn.getAdmin();
        if(admin.tableExists(test)){
            admin.disableTable(test);
            admin.deleteTable(test);
        }
        HTableDescriptor ht = new HTableDescriptor(test);
        HColumnDescriptor hc = new HColumnDescriptor("cf1".getBytes());
        hc.setMaxVersions(5);
        hc.setBlockCacheEnabled(true);
        hc.setBlocksize(1800000);
        ht.addFamily(hc);
        admin.createTable(ht);
    }

    @Before
    public void setup() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.57.4,192.168.57.5,192.168.57.6");
        conn = ConnectionFactory.createConnection(conf);
    }
    @After
    public void after() throws IOException {
        if(conn!=null){
            conn.close();
        }
    }

    @Test
    public void insert() throws IOException {
        Table table = conn.getTable(test);
        List<Put> list = new ArrayList<Put>();
        for(int i=0;i<1000;i++){
            Put put = new Put(getRowKey("138").getBytes());
            put.addColumn("cf1".getBytes(),"address".getBytes(),"北京".getBytes());
            put.addColumn("cf1".getBytes(),"type".getBytes(),String.valueOf(ra.nextInt(2)).getBytes());
            list.add(put);
        }
        table.put(list);
    }
    @Test
    public void find() throws IOException {
        Table table = conn.getTable(test);
        Scan scan = new Scan("13899459154_2016106133221".getBytes(),"13899950496_20167720852".getBytes());
        ResultScanner scanner = table.getScanner(scan);
        Iterator<Result> it = scanner.iterator();
        while(it.hasNext()){
            Result next = it.next();
            byte[] value = next.getValue("cf1".getBytes(), "type".getBytes());
            System.out.println(new String(value,"GBK"));
        }


    }
    //某个手机号，某段时间，主叫电话
    //查询首字母1389，type=1
    @Test
    public void find1() throws IOException {
        Table table = conn.getTable(test);
        Scan scan = new Scan();
        FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        PrefixFilter pf = new PrefixFilter("1389".getBytes());
        SingleColumnValueFilter sf = new SingleColumnValueFilter("cf1".getBytes(),"type".getBytes(), CompareFilter.CompareOp.EQUAL,"1".getBytes());
        //过滤器的顺序影响效率
        fl.addFilter(pf);
        fl.addFilter(sf);
        scan.setFilter(fl);
        ResultScanner scanner = table.getScanner(scan);
        Iterator<Result> it = scanner.iterator();
        while(it.hasNext()){
            Result next = it.next();
            byte[] value = next.getValue("cf1".getBytes(), "address".getBytes());
            System.out.println(new String(value,"utf8"));
        }
    }

    @Test
    public void search() throws IOException {
        Table table = conn.getTable(test);
        Get get = new Get("RK123".getBytes());
//        get.addColumn("cf1".getBytes(),"name".getBytes());
        Result result = table.get(get);
        Cell cell = result.getColumnLatestCell("cf1".getBytes(), "age".getBytes());
        System.out.printf(new String(cell.getValueArray()));
    }

    public String getRowKey(String pre){
        return pre+ ra.nextInt(99999999)+"_2016"+ra.nextInt(12)+ra.nextInt(30)+ra.nextInt(24)+ra.nextInt(60)+ra.nextInt(60);
    }

}
