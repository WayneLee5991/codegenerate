/**
 * 
 */
package com.yuyi.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yuyi.model.ResultBean;
import com.yuyi.service.CodeService;

/**
* <p>Title:CodeController </p>
* <p>Description: </p>
* <p>Company: </p> 
* @author liwenq
* @date 2018年4月4日下午10:26:03
*/
@RestController
@RequestMapping("/code")
public class CodeController {
	
	@Autowired
	@Qualifier("codeService")
	private CodeService codeService;
	
	/**
	 * 获取数据库里的表
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/tables")
	public ResultBean<Collection<String>> getTables()throws Exception{
		return new ResultBean<>(codeService.listDatabaseTables());
	}
	
	/**
	 * 生成代码
	 * @param panName
	 * @param author
	 * @param poName
	 * @param daoName
	 * @param serviceName
	 * @param serviceImplName
	 * @param controllerName
	 * @param tablelist
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/generate")
	public ResultBean<String> generate(@RequestParam String panName,
									   @RequestParam String author,
									   @RequestParam String poName,
									   @RequestParam String daoName,
									   @RequestParam String serviceName,
									   @RequestParam String serviceImplName,
									   @RequestParam String controllerName,
									   @RequestParam String tablelist,
									   @RequestParam(defaultValue="false")Boolean model,
									   @RequestParam(defaultValue="false")Boolean dao,
									   @RequestParam(defaultValue="false")Boolean service,
									   @RequestParam(defaultValue="false")Boolean controller,
									   @RequestParam(defaultValue="1")Integer primaryKey)throws Exception{
		
		List<String> tables=JSON.parseArray(tablelist, String.class);
		for (String tbName : tables) {
			if (dao) {
				codeService.createProvider(author,primaryKey,tbName, poName,daoName, panName);
				codeService.createDAO(author,tbName, poName, daoName, panName);
			}
			if (model) {
				codeService.createModel(author,tbName,poName, panName);
			}
			if (service) {
				codeService.createService(author,tbName, serviceName, panName, poName);
				codeService.createServiceImpl(author,tbName, serviceName, serviceImplName,panName,poName,daoName);
			}
			if (controller) {
				codeService.createController(author, tbName, controllerName, serviceName, serviceImplName, poName, panName);
			}
		}
		return new ResultBean<String>();
	}
}
