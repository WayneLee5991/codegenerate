/**
 * 
 */
package com.yuyi.service.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyi.dao.CodeDAO;
import com.yuyi.service.CodeService;


/**
 *
 * @author 李文庆
 * 2018年4月24日 上午10:09:30
 */
@Service("codeService")
public class CodeServiceImpl implements CodeService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeDAO codeDAO;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**获取ResultSet
	 * @param tbName
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getResultSet(String tbName) throws SQLException{
		Connection conn = sqlSessionFactory.openSession().getConnection();
		PreparedStatement stm=conn.prepareStatement("select * from "+tbName);
		ResultSet rs=stm.executeQuery();
		return rs;
	}
	/**获取字段的注释 ResultSet
	 * @param tbName
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getCommentResultSet(String tbName) throws SQLException{
		Connection conn = sqlSessionFactory.openSession().getConnection();
		PreparedStatement stm=conn.prepareStatement("select * from "+tbName);
		ResultSet rs=stm.executeQuery("show full columns from "+tbName);
		return rs;
	}
	
	/**查询数据库里的表*/
	@Override
	public List<String> listDatabaseTables() {
		return codeDAO.listDataBaseTables();
	}
	
	/**生成文件
	 * @param panName
	 * @param paramName
	 */
	public String creatFile(String panName,String paramName){
		String []farr=paramName.split("\\.");
		String fpath=panName+":/codeGenerate/";
		for (String f:farr) {
			// E:/codeGenerate/com/ui/../..
			fpath+=f+"/";
		}
		File f=new File(fpath);
		f.mkdirs();
		return fpath;
	}
	
	/**处理表名，生成类名
	 * @param tbName
	 * @return
	 */
	public String dealClass(String tbName){
		String classname="";
		if (tbName.contains("_")) {
			if (tbName.contains("table")) {
				/**去掉_table后缀*/
				classname= tbName.substring(0,tbName.lastIndexOf("_"));
			}else{
				classname = tbName;
			}
			String a = null;
			if (classname.contains("_")){
				a = classname.substring(0, classname.lastIndexOf("_"));
			}
			/**去掉table后缀后还有"_" */
			if (classname.contains("_")) {
				/**有两个*/
				if (a.contains("_")) {
					classname = classname.substring(0,1).toUpperCase()+classname.substring(1, classname.indexOf("_"))//Logistics
					+classname.substring( classname.indexOf("_")+1,classname.indexOf("_")+2 ).toUpperCase()//S
					+classname.substring(classname.indexOf("_")+2,classname.lastIndexOf("_"))//ervice
					+classname.substring( classname.lastIndexOf("_")+1,classname.lastIndexOf("_")+2 ).toUpperCase()//O
					+classname.substring(classname.lastIndexOf("_")+2)
					+"DO";
				}else{//只有一个
					classname = classname.substring(0,1).toUpperCase()+classname.substring(1, classname.indexOf("_"))//Logistics
					+classname.substring(classname.indexOf("_")+1,classname.indexOf("_")+2).toUpperCase()//S
					+classname.substring(classname.lastIndexOf("_")+2)+"DO";
				}
			}else{
				classname=classname.substring(0,1).toUpperCase()+classname.substring(1)+"DO";
			}
		}else{
			classname=tbName.substring(0,1).toUpperCase()+tbName.substring(1)+"DO";
		}
		return classname;
	}
	
	/**处理列名，生成属性
	 * @param columnname
	 * @return
	 */
	public String dealProperty(String columnname){
		/**先把列名转为小写*/
		String propertyname=columnname.toLowerCase();
		if (propertyname.contains("_")) {
			if (!Objects.equals(propertyname.indexOf("_"),propertyname.lastIndexOf("_"))) {
				propertyname=propertyname.substring(0,propertyname.indexOf("_"))//第一个下划线前
						+propertyname.substring(propertyname.indexOf("_")+1,propertyname.indexOf("_")+2).toUpperCase()//第一个下划线后首字母大写
						+propertyname.substring(propertyname.indexOf("_")+2,propertyname.lastIndexOf("_"))
						+propertyname.substring(propertyname.lastIndexOf("_")+1,propertyname.lastIndexOf("_")+2).toUpperCase()//最后一个下划线后首字母大写
						+propertyname.substring(propertyname.lastIndexOf("_")+2);
			}else{
				propertyname=propertyname.substring(0,propertyname.lastIndexOf("_"))//下划线前
						+propertyname.substring(propertyname.lastIndexOf("_")+1,propertyname.lastIndexOf("_")+2).toUpperCase()//下划线后首字母大写
						+propertyname.substring(propertyname.lastIndexOf("_")+2);
				propertyname=propertyname.replace("_", "");
			}
		}
		return propertyname;
	}
	
	/**
	 * 处理列数据类型，生成属性的数据类型
	 * @param columntype
	 * @return
	 */
	public String dealType(String columntype){
		String propertytype="";
		if (columntype.equals("INT")) {
			propertytype="Integer";
		}else if(columntype.equals("VARCHAR")){
			propertytype="String";
		}else if (columntype.contains("CHAR")) {
			propertytype="String";
		}else if (columntype.contains("TEXT")) {
			propertytype="String";
		}else if(columntype.contains("ENUM")){
			propertytype="String";
		}else if(columntype.contains("BIGINT")){
			propertytype="Long";
		}else if(columntype.equals("DOUBLE")){
			propertytype="Double";
		}else if(columntype.equals("DATETIME")){
			propertytype="LocalDateTime";
		}else if(columntype.equals("DATE")){
			propertytype="LocalDate";
		}else if(columntype.equals("TIME")){
			propertytype="LocalTime";
		}else if(columntype.equals("DECIMAL")){
			propertytype="BigDecimal";
		}else if(columntype.equals("NUMERIC")){
			propertytype="BigDecimal";
		}
		return propertytype;
	}
	
	/**
	 * 处理表名，生成model类名
	 * @param tbName
	 * @return
	 */
	public String dealPo(String tbName){
		String po="";
		if (tbName.contains("_")) {
			/**去掉_table后缀*/
			if (tbName.contains("table")) {
				po= tbName.substring(0,tbName.lastIndexOf("_"));
			}else{
				po = tbName;
			}
			String a = "";
			if (po.contains("_")) {
				a = po.substring(0, po.lastIndexOf("_"));
				/**有两个*/
				if (a.contains("_")) {
					po = po.substring(0,1).toUpperCase()+po.substring(1, po.indexOf("_"))//Logistics
					+po.substring( po.indexOf("_")+1,po.indexOf("_")+2 ).toUpperCase()//S
					+po.substring(po.indexOf("_")+2,po.lastIndexOf("_"))//ervice
					+po.substring( po.lastIndexOf("_")+1,po.lastIndexOf("_")+2 ).toUpperCase()//O
					+po.substring(po.lastIndexOf("_")+2);
				}else{//只有一个
					po = po.substring(0,1).toUpperCase()+po.substring(1, po.indexOf("_"))//Logistics
					+po.substring(po.indexOf("_")+1,po.indexOf("_")+2).toUpperCase()//S
					+po.substring(po.lastIndexOf("_")+2);
				}
			}else{
				po=po.substring(0,1).toUpperCase()+po.substring(1);
			}
		}else{
			po=tbName.substring(0,1).toUpperCase()+tbName.substring(1);
		}
		return po;
	}
	
	//创建实体类
	@Override
	public void createModel(String author,String tbName, String poName, String panName) throws SQLException, IOException {
		//处理包名，创建对应的文件夹
		String fpath=this.creatFile(panName, poName);
		//将表名处理为对应的类名
		String classname=this.dealClass(tbName);
		//获取resultset
		ResultSet rs=this.getResultSet(tbName);
		ResultSet commentRs = this.getCommentResultSet(tbName);
		List<String> commentList = new ArrayList<String>();
		while (commentRs.next()) {
			String comment = commentRs.getString("Comment");
			commentList.add(comment);
		}
		
		ResultSetMetaData rsmd=rs.getMetaData();
		//列数
		int columncount=rsmd.getColumnCount();
		//开始写类
		String content="package "+poName+";	\r\n\r\n";//处理包名
		content+="import java.io.Serializable;\r\n";
		content+="import java.math.BigDecimal;\r\n";
		content+="import java.time.LocalDateTime;\r\n";
		content+="import java.time.LocalDate;\r\n";
		content+="import java.time.LocalTime;\r\n\r\n";
		content+="/**"+"\r\n";
		content+="* @author "+author+"\r\n";
		content+="* @date "+DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss").format(LocalDateTime.now())+"\r\n";
		content+="*/"+"\r\n";
		content+="public class "+classname+" implements Serializable {\r\n\r\n";
		content+="	private static final long serialVersionUID = 1L;\r\n\r\n";
		
		String toString="";
		//循环生成属性
		for (int i = 1; i <= columncount; i++) {
			String columnname=rsmd.getColumnName(i);
			String columntype=rsmd.getColumnTypeName(i);
			//处理属性名
			String propertyname=this.dealProperty(columnname);
			//处理属性的数据类型
			String propertytype=this.dealType(columntype);
			content+="	/** "+commentList.get(i-1)+"*/\r\n";
			content+="	private "+propertytype+" "+propertyname+";\r\n\r\n";
			//toString 方法
			if (i%4==0) {
				toString+="\", "+propertyname+"=\""+"+"+propertyname+"\r\n      			  +";
			}else{
				toString+="\", "+propertyname+"=\""+"+"+propertyname+"+";
			}
		}
		
		//循环生成方法
		for (int i = 1; i <= columncount; i++) {
			String columnname=rsmd.getColumnName(i);
			String columntype=rsmd.getColumnTypeName(i);
			//处理属性名
			String propertyname=this.dealProperty(columnname);
			//处理属性数据类型
			String propertytype=this.dealType(columntype);
			//get&set方法
			String getname="get"+propertyname.substring(0,1).toUpperCase()+propertyname.substring(1);
			content+="	/** 获取 "+commentList.get(i-1)+"*/\r\n";
			content+="	public "+propertytype+" "+getname+"(){\r\n		return this."+propertyname+";\r\n	}\r\n\r\n";
			String setname="set"+propertyname.substring(0,1).toUpperCase()+propertyname.substring(1);
			content+="	/** 设置"+commentList.get(i-1)+"*/\r\n";
			content+="	public void "+setname+"("+propertytype+" "+propertyname+"){\r\n		this."+propertyname+"="+propertyname+";\r\n	}\r\n\r\n";
		}
		content+="\r\n    @Override\r\n    public String toString(){\r\n        return \""+classname+" ["+toString.substring(2)+"\"]\";\r\n    }\r\n}";
		//流 写出对应的类和配置文件
		FileOutputStream out=new FileOutputStream(fpath+classname+".java");
		out.write(content.getBytes());
		out.flush();
		out.close();
		logger.info("generate model successfully!");
	}
	
	
	/**
	 * @param tbName
	 * @param poName
	 * @throws Exception
	 */
	static String providerName;
	@Override
	public void createProvider(String author,Integer primaryKeyMethod,String tbName, String poName,String daoName,String panName) throws Exception {
		providerName=daoName.substring(0,daoName.lastIndexOf(".")+1)+"provider";//包名
		String fpath=this.creatFile(panName, providerName);
		//获取resultset
		ResultSet rs=this.getResultSet(tbName);
		ResultSetMetaData rsmd=rs.getMetaData();
		//处理类名
		String poDO=this.dealClass(tbName);
		String po=this.dealPo(tbName);
		String pol=po.toLowerCase();
		String iProviderName=po+author.toUpperCase()+"Provider";//类名
		//列数
		int columncount=rsmd.getColumnCount();
		//获取数据库第一列(主键)
		String columnName=rsmd.getColumnName(1);
		String primaryKey=columnName.substring(0).toLowerCase();
		//生成属性的判断语句
		String insertValue="";
		String updateValue="";
		for (int j = 1; j < columncount+1; j++) {
			String columnname=rsmd.getColumnName(j);
			//处理属性名
			String propertyname=this.dealProperty(columnname);
			//处理属性的数据类型
			String getname="get"+propertyname.substring(0,1).toUpperCase()+propertyname.substring(1);
			
			if (primaryKeyMethod == 1) {
				
				if (j != 1) {
					insertValue+="				if("+pol+"."+getname+"()!=null){\r\n";
					insertValue+="					VALUES(\""+columnname+"\",\"#{"+pol+"."+propertyname+"}\");\r\n";
					insertValue+="				}\r\n";
					
					updateValue+="				if("+pol+"."+getname+"()!=null){\r\n";
					updateValue+="					SET(\""+columnname+" = #{"+pol+"."+propertyname+"}\");\r\n";
					updateValue+="				}\r\n";
				}
				
			} else{
				
				insertValue+="				if("+pol+"."+getname+"()!=null){\r\n";
				insertValue+="					VALUES(\""+columnname+"\",\"#{"+pol+"."+propertyname+"}\");\r\n";
				insertValue+="				}\r\n";
				
				if (j != 1) {
					updateValue+="				if("+pol+"."+getname+"()!=null){\r\n";
					updateValue+="					SET(\""+columnname+" = #{"+pol+"."+propertyname+"}\");\r\n";
					updateValue+="				}\r\n";
				}
			}
			
			
		}
		//开始写类
		String content="package "+providerName+";\r\n\r\n";
		content+="import org.apache.ibatis.annotations.Param;\r\n";
		content+="import org.apache.ibatis.jdbc.SQL;\r\n\r\n";
		content+="import "+poName+"."+poDO+";\r\n\r\n";		
		content+="/**\r\n";
		content+="* @author "+author+"\r\n";
		content+="* @date "+DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss").format(LocalDateTime.now())+"\r\n";
		content+="*/"+"\r\n";
		content+="public class "+iProviderName+"{\r\n\r\n";		
		content+="    public String insert"+po+"(@Param(\""+pol+"\") "+poDO+" "+pol+"){\r\n";
		content+="        return new SQL(){\r\n";
		content+="            {\r\n";
		content+="                INSERT_INTO(\""+tbName+"\");\r\n";
		content+=insertValue;
		content+="		    }\r\n";
		content+="	     }.toString();\r\n";
		content+="    }\r\n";
		content+="    public String update"+po+"(@Param(\""+pol+"\") "+poDO+" "+pol+"){\r\n";
		content+="        return new SQL(){\r\n";
		content+="            {\r\n";
		content+="                UPDATE(\""+tbName+"\");\r\n";
		content+=updateValue;
		content+="		        WHERE(\""+primaryKey+" = #{"+pol+"."+primaryKey+"}\");\r\n";
		content+="		    }\r\n";
		content+="	     }.toString();\r\n";
		content+="    }\r\n}\r\n";
		
		//流 写出对应的类和配置文件
		FileOutputStream out=new FileOutputStream(fpath+iProviderName+".java");
		out.write(content.getBytes());
		out.flush();
		out.close();
		logger.info("generate provider successfully!");
	}
	
	//创建dao 注解方式
	@Override
	public void createDAO(String author,String tbName, String poName, String daoName, String panName) throws Exception {

		String fpath=this.creatFile(panName, daoName);
		
		ResultSet rs=this.getResultSet(tbName);
		ResultSetMetaData rsmd=rs.getMetaData();
		String columnName=rsmd.getColumnName(1);
		//String columnType=rsmd.getColumnTypeName(1);
		//获取数据库第一列(主键)
		String primaryKey=columnName.substring(0).toLowerCase();
		//转换类型
		//String propertytype=this.dealType(columnType);
		//将表名处理为对应的实体类类名
		String poDO=this.dealClass(tbName);
		String po=this.dealPo(tbName);
		String poa=po.toLowerCase();
		//将表名处理为对应的dao类名
		String idaoname=po+author.toUpperCase()+"DAO";
		//查找列数
		int columncount=rsmd.getColumnCount();
		
		String propertynameall="";
		String propertynamevalue="";
		String propertynameBuild="";
		//循环生成属性
		for (int i = 1; i <= columncount; i++) {
			String columnname=rsmd.getColumnName(i);
			//String columntype=rsmd.getColumnTypeName(i);
			String propertyname=columnname.toLowerCase();
			//insert 方法
			propertynameall+=propertyname+",";
			propertynamevalue+="#{"+poa+"."+propertyname+"},";
		}
		
		for (int i = 2; i <= columncount; i++) {
			String columnname=rsmd.getColumnName(i);
			String propertyname=columnname.toLowerCase();
			propertynameBuild+=propertyname+"=#{"+poa+"."+propertyname+"},";
		}
		propertynameall=propertynameall.substring(0,propertynameall.length()-1);
		propertynamevalue=propertynamevalue.substring(0,propertynamevalue.length()-1);
		propertynameBuild=propertynameBuild.substring(0,propertynameBuild.length()-1);
		
		//开始写类
		String content="package "+daoName+";\r\n\r\n";
		
		content+="import java.util.List;\r\n\r\n";
		content+="import org.apache.ibatis.annotations.InsertProvider;\r\n";
		content+="import org.apache.ibatis.annotations.Mapper;\r\n";
		content+="import org.apache.ibatis.annotations.Options;\r\n";
		content+="import org.apache.ibatis.annotations.Param;\r\n";
		content+="import org.apache.ibatis.annotations.Select;\r\n";
		content+="import org.apache.ibatis.annotations.UpdateProvider;\r\n\r\n";
		content+="import "+poName+"."+poDO+";\r\n";
		content+="import "+providerName+"."+po+author.toUpperCase()+"Provider;\r\n\r\n";
		content+="/**"+"\r\n";
		content+="* @author "+author+"\r\n";
		content+="* @date "+DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss").format(LocalDateTime.now())+"\r\n";
		content+="*/"+"\r\n";
		content+="@Mapper\r\n";
		content+="public interface "+idaoname+"{\r\n\r\n";
		content+="    @InsertProvider(method = \"insert"+po+"\", type = "+po+author.toUpperCase()+"Provider.class)\r\n";
		content+="    @Options(useGeneratedKeys = true,keyProperty = \""+poa+"."+primaryKey+"\")\r\n";
		content+="	Integer insert(@Param(\""+poa+"\")"+poDO+" "+poa+")throws Exception;\r\n\r\n";
		
		/*content+="    @Delete(\"delete from "+tbName+" where "+primaryKey+" = #{"+poa+"."+primaryKey+"} \")\r\n";
		content+="	int deleteById(@Param(\""+poa+"\")"+po+" "+poa+")throws Exception;\r\n\r\n";
		*/
		content+="    @UpdateProvider(method = \"update"+po+"\", type = "+po+author.toUpperCase()+"Provider.class)\r\n";
		content+="	Integer updateById(@Param(\""+poa+"\")"+poDO+" "+poa+")throws Exception;\r\n\r\n";
		
		content+="    @Select(\"SELECT "+propertynameall+" FROM "+tbName+" WHERE "+primaryKey+" = #{"+primaryKey+"}\")\r\n";
		content+="	"+poDO+" getByPrimaryKey(Integer "+primaryKey+")throws Exception;\r\n\r\n";
		
		content+="    @Select(\"SELECT "+propertynameall+" FROM "+tbName+" \")\r\n";
		content+="	List<"+poDO+"> listAll"+poDO+"()throws Exception;\r\n\r\n}";
		
		//流 写出对应的类和配置文件
		FileOutputStream out=new FileOutputStream(fpath+idaoname+".java");
		out.write(content.getBytes());
		out.flush();
		out.close();
		logger.info("generate DAO successgully!");
		
	}
	
	//创建service接口
	@Override
	public void createService(String author,String tbName,String serviceName, String panName,String poName) throws Exception {
		String []farr=serviceName.split("\\.");
		String fpath=panName+":/codeGenerate/";
		for (String f:farr) {
			// I:/CreateSSM/com/ruide/service/
			fpath+=f+"/";
		}
		//System.out.println(fpath);
		File f=new File(fpath);
		f.mkdirs();
		
		ResultSet rs=this.getResultSet(tbName);
		ResultSetMetaData rsmd=rs.getMetaData();
		String columnName=rsmd.getColumnName(1);
		//System.out.println(columnname+columntype);
		//获取数据库第一列(主键)
		String primaryKey=columnName.substring(0).toLowerCase();
		
		//将表名处理为对应的实体类类名
		String poDO=this.dealClass(tbName);
		String po=this.dealPo(tbName);
		String poa=po.substring(0,1).toLowerCase()+po.substring(1);
		
		String iservicename=po+author.toUpperCase()+"Service";
		
		String content="package "+serviceName+";\r\n\r\n";
		content+="import java.util.List;\r\n\r\n";
		content+="import "+poName+"."+poDO+";\r\n\r\n";	
		content+="/**"+"\r\n";
		content+="* @author "+author+"\r\n";
		content+="* @date "+DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss").format(LocalDateTime.now())+"\r\n";
		content+="*/"+"\r\n";
		content+="public interface "+iservicename+"{\r\n\r\n";
		content+="    /**\r\n";
		content+="    * 添加"+tbName+"\r\n";
		content+="    */\r\n";
		content+="	"+poDO+" insert("+poDO+" "+poa+")throws Exception;\r\n\r\n";
		
		/*content+="	int deleteById("+po+" "+poa+");\r\n\r\n";*/
		content+="    /**\r\n";
		content+="    * 根据id修改"+tbName+"\r\n";
		content+="    */\r\n";
		content+="	int updateById("+poDO+" "+poa+")throws Exception;\r\n\r\n";
		
		content+="    /**\r\n";
		content+="    * 查询"+tbName+"所有信息\r\n";
		content+="    */\r\n";
		content+="	List<"+poDO+"> listAll"+poDO+"()throws Exception;\r\n\r\n";
		
		content+="    /**\r\n";
		content+="    * 根据主键查询\r\n";
		content+="    */\r\n";
		content+="	"+poDO+" getByPrimaryKey(Integer "+primaryKey+")throws Exception;\r\n\r\n}";
		
		//流 写出对应的类和配置文件
		FileOutputStream out=new FileOutputStream(fpath+iservicename+".java");
		out.write(content.getBytes());
		out.flush();
		out.close();
		logger.info("generate service interface successfully");
	}
	
	//创建service接口实现类ServiceImpl
	@Override
	public void createServiceImpl(String author,String tbName,String serviceName, String serviceImplName, String panName,String poName,String daoName) throws Exception {
		String []farr=serviceImplName.split("\\.");
		String fpath=panName+":/codeGenerate/";
		for (String f:farr) {
			// I:/CreateSSM/com/ruide/service/
			fpath+=f+"/";
		}
		//System.out.println(fpath);
		File f=new File(fpath);
		f.mkdirs();
		
		ResultSet rs=this.getResultSet(tbName);
		ResultSetMetaData rsmd=rs.getMetaData();
		String columnName=rsmd.getColumnName(1);
		//System.out.println(columnname+columntype);
		//获取数据库第一列(主键)
		String primaryKey=columnName.substring(0).toLowerCase();
		
		//将表名处理为对应的实体类类名
		String poDO=this.dealClass(tbName);
		String po=this.dealPo(tbName);
		String poa=po.substring(0,1).toLowerCase()+po.substring(1);
		
		String iserviceName=po+author.toUpperCase()+"Service";
		String iserviceImplName=po+author.toUpperCase()+"ServiceImpl";
		String idaoname=po+author.toUpperCase()+"DAO";
		String idaonamelower=idaoname.substring(0,1).toLowerCase()+idaoname.substring(1);
		
		String content="package "+serviceImplName+";\r\n\r\n";
		content+="import java.util.List;\r\n\r\n";
		content+="import org.springframework.stereotype.Service;\r\n";
		content+="import org.springframework.transaction.annotation.Transactional;\r\n";
		content+="import org.springframework.beans.factory.annotation.Autowired;\r\n\r\n";
		content+="import "+serviceName+"."+iserviceName+";\r\n";
		content+="import "+daoName+"."+idaoname+";\r\n";
		content+="import "+poName+"."+poDO+";\r\n\r\n";
		content+="/**"+"\r\n";
		content+="* @author "+author+"\r\n";
		content+="* @date "+DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss").format(LocalDateTime.now())+"\r\n";
		content+="*/"+"\r\n";
		content+="@Service(\""+iserviceName.substring(0,1).toLowerCase()+iserviceName.substring(1)+"\")\r\n";
		content+="public class "+iserviceImplName+" implements "+iserviceName+" {\r\n\r\n";
		content+="    @Autowired\r\n";
		content+="    private "+idaoname+" "+idaonamelower+";\r\n\r\n";
		content+="    @Override\r\n";
		content+="    @Transactional\r\n";
		content+="	public "+poDO+" insert("+poDO+" "+poa+")throws Exception{\r\n\r\n";
		content+="	    "+idaonamelower+".insert("+poa+");\r\n";
		content+="	    return "+poa+";\r\n\r\n";
		content+="    }\r\n\r\n";
		
		content+="    @Override\r\n";
		content+="    @Transactional\r\n";
		content+="	public int updateById("+poDO+" "+poa+")throws Exception{\r\n\r\n";
		content+="	    return "+idaonamelower+".updateById("+poa+");\r\n\r\n";
		content+="    }\r\n\r\n";
		
		content+="    @Override\r\n";
		content+="	public List<"+poDO+"> listAll"+poDO+"()throws Exception{\r\n\r\n";
		content+="	    return "+idaonamelower+".listAll"+poDO+"();\r\n\r\n";
		content+="    }\r\n\r\n";
		
		content+="    @Override\r\n";
		content+="	public "+poDO+" getByPrimaryKey(Integer "+primaryKey+")throws Exception{\r\n\r\n";
		content+="	    return "+idaonamelower+".getByPrimaryKey("+primaryKey+");\r\n\r\n";
		content+="    }\r\n\r\n}";
		
		//流 写出对应的类和配置文件
		FileOutputStream out=new FileOutputStream(fpath+iserviceImplName+".java");
		out.write(content.getBytes());
		out.flush();
		out.close();
		logger.info("generate service implements successfully!");
	}
	
	//创建controller类
	@Override
	public void createController(String author,String tbName, String controllerName,String serviceName,String serviceImplName,String poName,String panName) throws Exception {
		//处理包名，创建对应的文件夹
		String fpath=this.creatFile(panName, controllerName);
		String po=this.dealPo(tbName);
		
		String iserviceName=po+author.toUpperCase()+"Service";
		String iserviceNameLower=iserviceName.toLowerCase();
		String iserviceImplName=po+author.toUpperCase()+"ServiceImpl";
		String icontrollerName=po+"Controller";
		
		String content="package "+controllerName+";\r\n\r\n";
		content+="import java.util.List;\r\n\r\n";
		content+="import org.springframework.beans.factory.annotation.Autowired;"+"\r\n";
		content+="import org.springframework.beans.factory.annotation.Qualifier;"+"\r\n";
		content+="import org.springframework.boot.autoconfigure.SpringBootApplication;"+"\r\n";
		content+="import org.springframework.stereotype.Controller;"+"\r\n";
		content+="import org.springframework.web.bind.annotation.GetMapping;"+"\r\n";
		content+="import org.springframework.web.bind.annotation.ResponseBody;"+"\r\n\r\n";
		content+="import "+poName+"."+po+";\r\n";
		content+="import "+serviceName+"."+iserviceName+";"+"\r\n\r\n";
		
		content+="/**"+"\r\n";
		content+="* @author "+author+"\r\n";
		content+="* @date "+DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss").format(LocalDateTime.now())+"\r\n";
		content+="*/"+"\r\n";
		content+="@RestController\r\n";
		content+="public class "+icontrollerName+"{\r\n\r\n";
		content+="    @Autowired\r\n";
		content+="    @Qualifier(\""+iserviceImplName+"\")\r\n";
		content+="    private "+iserviceName+" "+iserviceNameLower+";\r\n\r\n";
		content+="	/**\r\n";
		content+="     *\r\n";
		content+="     * @return\r\n";
		content+="     */\r\n";
		content+="    @GetMapping(\"   \")\r\n";
		content+="    public String findAll() {\r\n";
		content+="          return \"success\";\r\n";
		content+="    }\r\n";
		content+="\r\n";
		content+="}\r\n";
		
		FileOutputStream out=new FileOutputStream(fpath+icontrollerName+".java");
		out.write(content.getBytes());
		out.flush();
		out.close();
		logger.info("generate controller successfilly");
	}


}
