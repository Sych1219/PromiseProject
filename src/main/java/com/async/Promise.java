package com.async;



public abstract class Promise<T extends  Object>{

    //status: completed, failure, ....

    //has then promise to register

    public void then(){}
}
