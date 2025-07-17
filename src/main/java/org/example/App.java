package org.example;

import org.bean.Cotton;
import org.bean.Doggy;
import org.bean.Giraffe;

public class App {

    public static void main(String[] args) {
        Cotton cotton = new Cotton();
        cotton.setId(1L);
        cotton.setType("长绒棉");
        cotton.setPrice(100.0);

        Giraffe giraffe = new Giraffe();
        giraffe.setId(1L);
        giraffe.setName("长颈鹿");
        System.out.println("giraffe = " + giraffe);
        System.out.println("giraffe: " + giraffe);

        Doggy doggy = new Doggy();
        doggy.setId(1L);
        doggy.setName("小狗");
        System.out.println("doggy: " + doggy);
    }

}
