package com.coconut.mylottery.bean.test;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class Father {
    public Person person;

    public Father(Person person) {
        this.person = person;
    }
    public void showPerson(){

        System.out.println("father的showPerson方法调用person===="+ getPerson().getName());
    }

    public Person getPerson() {
        System.out.print("父类的getPerson方法===");

        return person;
    }

    public void sayPerson(){
        System.out.print("父类的方法调用sayPerson()===");
        showPerson();
    }
}
