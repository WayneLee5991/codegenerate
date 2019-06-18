/**
 * 
 */
package com.yuyi.test;

/**
* <p>Title:EnumTest </p>
* <p>Company: 山东宇易</p> 
* @author 马呈邦
* @date 2018年4月8日下午12:42:56
* Description: 
*/

	
public enum EnumTest{
    MON, TUE, WED, THU, FRI, SAT, SUN;

		
	public static void main(String[] args) {
	    for (EnumTest e : EnumTest.values()) {
	        System.out.println(e.toString());
	    }
	     
	    System.out.println("----------------我是分隔线------------------");
	 
	    EnumTest test = EnumTest.TUE;
	    
	    switch (test) {
	    
		    	case MON:
		    		System.out.println("今天是星期一");
		    break;
		    	case TUE:
		    		System.out.println("今天是星期二");
		    break;
		    // ... ...
		    	default:
		        	System.out.println(test);
		    break;
	    }
	}
}
	

