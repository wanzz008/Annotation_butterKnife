package com.wzz.annotation_butterknife.basic;

@Description(desc = "这是一个Child测试类",author = "wzz" ,age = 25)
public class Child {

    public String name ;
    public String sex ;

    @Description(desc = "这是一个Child中的study方法",author = "wzz" )
    public void study(){

    }

    @Description(desc = "这是一个Child中的eat方法",author = "wzz" ,age = 20 )
    public void eat(){

    }

    public Child( String name ){

    }


}
