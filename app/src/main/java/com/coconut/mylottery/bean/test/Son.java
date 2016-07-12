package com.coconut.mylottery.bean.test;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class Son extends Father{

   //public Person person =new Person("huangyan");
    public Son(Person p) {
        super(p);
        //person.setName("huangyan");
        //this.person = p;
    }

    public void showPerson(){

        System.out.print("son的showPerson方法里调用getperson()====");
        System.out.println(getPerson().getName());
        System.out.println("son的showPerson方法直接调用属性person==="+person.getName());
    }

    //@Override
    //public Person getPerson() {
    //    System.out.print("子类的getPerson方法===");
    //    return person;
    //}

    @Override
    public void sayPerson() {
        System.out.print("子类的方法调用sayPerson()===");
        showPerson();
    }
}
