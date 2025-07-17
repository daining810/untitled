package org.bean;

import lombok.Data;

@Data
public class Giraffe {

    private Long id;

    private String name;

    public void eat(Grass grass) {
        System.out.println("Giraffe " + name + " is eating " + grass.getType() +
                " grass with id " + grass.getId() + ".");
    }

}
