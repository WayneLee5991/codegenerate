package com.yuyi.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yuyi.service.CodeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodegenerateApplicationTests {
	
	@Autowired
	@Qualifier("codeService")
	private CodeService codeService;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	

	@Test
	public void get() throws SQLException{
		Connection conn = sqlSessionFactory.openSession().getConnection();
		PreparedStatement stm=conn.prepareStatement("select * from invoice_table");
		ResultSet rs=stm.executeQuery("show full columns from invoice_table");
		//ResultSet rs=stm.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString("Comment"));
		}
		
		//ResultSetMetaData rsmd = rs.getMetaData();
		//int columnCount=rsmd.getColumnCount();
	   /* System.out.println(columnCount);
	    for (int i = 1; i < columnCount; i++) {
	    	String columnname=rsmd.getColumnName(i);
			String columntype=rsmd.getColumnTypeName(i);
			System.out.println("列名"+columnname+"类型"+columntype);
		}*/
		
	}
	
	

}
