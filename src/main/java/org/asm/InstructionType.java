package org.asm;

import lombok.Getter;

@Getter
public enum InstructionType {
    A("A"),
    C("C"),
    L("L");

    private final String type;

    InstructionType(String type) {
        this.type = type;
    }
}
