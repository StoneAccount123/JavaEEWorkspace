package edu.whu.pojo;

import edu.whu.annotation.InitMethod;

/**
 * @author myAdministrator
 */

public class MyClass {

    @InitMethod
    public void init(){
        System.out.println("Myclass的初始化操作");
    }
}