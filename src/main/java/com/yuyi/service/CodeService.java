/**
 * 
 */
package com.yuyi.service;

import java.util.List;


/**
 *
 * @author 李文庆
 * 2018年4月24日 上午10:55:19
 */
public interface CodeService {
	
	/**查询数据库里的表
	 * @return
	 */
	List<String> listDatabaseTables();
	
	/**生成model类
	 * @param sql
	 * @param tname
	 * @param daoName
	 * @param panName
	 */
	void createModel(String author,String tbName,String poName,String panName)throws Exception;
	
	/**生成动态sql
	 * @param tbName
	 * @param poName
	 * @throws Exception
	 */
	void createProvider(String author,Integer primaryKey,String tbName,String poName,String daoName,String panName)throws Exception;
	
	/**生成DAO
	 * @param tbName
	 * @param poName
	 * @param daoName
	 * @param panName
	 * @throws Exception
	 */
	void createDAO(String author,Integer primaryKey,String tbName,String poName,String daoName,String panName)throws Exception;
	
	/**生成Service接口
	 * @param tbName
	 * @param serviceName
	 * @param panName
	 * @throws Exception
	 */
	void createService(String author,String tbName,String serviceName,String panName,String poName)throws Exception;
	
	/**生成service接口实现类
	 * @param tbName
	 * @param serviceName
	 * @param panName
	 * @throws Exception
	 */
	void createServiceImpl(String author,String tbName,String serviceName,String serviceImplName,String panName,String poName,String daoName)throws Exception;
	
	/**生成controller
	 * @param tbName
	 * @param controllerName
	 * @param panName
	 * @throws Exception
	 */
	void createController(String author,String tbName, String controllerName,String serviceName,String serviceImplName,String poName,String panName)throws Exception;
	
}
