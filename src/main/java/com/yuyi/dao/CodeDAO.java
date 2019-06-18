/**
 * 
 */
package com.yuyi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* <p>Title:CodeDAO </p>
* <p>Description: </p>
* <p>Company: </p> 
* @author liwenq
* @date 2018年4月4日下午10:27:52
*/
@Mapper
public interface CodeDAO {
	
	/**查询数据库里的表
	 * @return
	 */
	@Select("show tables")
	List<String> listDataBaseTables();
	
	
}
