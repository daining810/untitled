package org.example;

import org.bean.Cotton;
import org.bean.Doggy;
import org.bean.Giraffe;
import org.bean.Grass;

public class App {

    public static void main(String[] args) {
        Cotton cotton = new Cotton();
        cotton.setId(1L);
        cotton.setType("长绒棉");
        cotton.setPrice(100.0);

        Grass grass = new Grass();
        grass.setId(1L);
        grass.setType("草");

        Giraffe giraffe = new Giraffe();
        giraffe.setId(1L);
        giraffe.setName("好看的长颈鹿");
        System.out.println("giraffe = " + giraffe);
        System.out.println("giraffe: " + giraffe);
        giraffe.eat(grass);

        Doggy doggy = new Doggy();
        doggy.setId(1L);
        doggy.setName("小狗:旺财");
        System.out.println("doggy = " + doggy);
        System.out.println("doggy: " + doggy);

        System.out.println("doggy.getName() = " + doggy.getName());
        String foo = "foo";
        System.out.println("foo = " + foo);
        String boo = "boo";
        System.out.println("boo = " + boo);
    }

}
