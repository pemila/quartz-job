package com.pemila.job;

import org.springframework.stereotype.Component;

/**
 * @author 月在未央
 * @date 2019/4/1 14:34
 */
@Component
public class HelloWorld {

    public void sayHello(){
        String name = Thread.currentThread().getName();
        System.out.println(name+"- hello - "+System.currentTimeMillis()/1000);
    }
    public void sayHi(){
        String name = Thread.currentThread().getName();
        System.out.println(name+"- hi - "+System.currentTimeMillis()/1000);
    }

    public void sayWorld(){
        String name = Thread.currentThread().getName();
        System.out.println(name+"- world - "+System.currentTimeMillis()/1000);
    }

}
