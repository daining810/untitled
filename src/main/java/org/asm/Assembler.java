package org.asm;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Assembler {

    private static final Map<String, Integer> symbol = new HashMap<>();
    private static boolean first = true;
    private static int ramNo = 16;
    private static final Map<String, Integer> freeRegister = new HashMap<>();

    public static void main(String[] args) throws IOException {
        readFile("src/main/resources/test.asm");
        readFile("src/main/resources/test.tmp");
    }

    public static void readFile(String fileName) throws IOException {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fileName))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                String analyze = analyze(line, lineNum);
                if (StrUtil.isNotBlank(analyze)) {
                    if (first) {
                        writeFile("src/main/resources/test.tmp", analyze);
                    } else {
                        writeFile("src/main/resources/test.hack", analyze);
                    }
                }
                lineNum++;
            }
        }
        System.out.println("assemble successfully, filename: " + fileName);
        first = false;
    }

    public static void writeFile(String fileName, String content) throws IOException {
        java.io.FileWriter writer = new java.io.FileWriter(fileName, true);
        writer.write(content + "\n");
        writer.close();
    }

    public static String analyze(String line, int lineNum) {
        InstructionType instructionType = getInstructionType(line);
        return analyzeInstruction(instructionType, line, lineNum);
    }

    public static InstructionType getInstructionType(String line) {
        if (StrUtil.isNotBlank(line)) {
            if (StrUtil.startWith(line, "@")) {
                return InstructionType.A;
            }
            if (StrUtil.startWith(line, "(")) {
                return InstructionType.L;
            }
            return InstructionType.C;
        } else {
            throw new IllegalArgumentException("line is blank");
        }
    }

    public static String analyzeInstruction(InstructionType type, String insruction, int lineNum) {
        switch (type) {
            case A:
                return first ? insruction : analyzeInstructionA(insruction);
            case C:
                return first ? insruction : analyzeInstructionC(insruction);
            case L:
                writeSymbol(insruction, lineNum);
                return "";
            default:
                throw new IllegalArgumentException("type is not supported");
        }
    }

    private static void writeSymbol(String insruction, int lineNum) {
        insruction = insruction.substring(1, insruction.length() - 1);
        if (!symbol.containsKey(insruction)) {
            symbol.put(insruction, lineNum - symbol.size());
        }
    }

    public static String analyzeInstructionA(String instruction) {
        String a = instruction.substring(1);
        switch (a) {
            case "SP":
                a = "0";
                break;
            case "LCL":
                a = "1";
                break;
            case "ARG":
                a = "2";
                break;
            case "THIS":
                a = "3";
                break;
            case "THAT":
                a = "4";
                break;
            case "R0":
                a = "0";
                break;
            case "R1":
                a = "1";
                break;
            case "R2":
                a = "2";
                break;
            case "R3":
                a = "3";
                break;
            case "R4":
                a = "4";
                break;
            case "R5":
                a = "5";
                break;
            case "R6":
                a = "6";
                break;
            case "R7":
                a = "7";
                break;
            case "R8":
                a = "8";
                break;
            case "R9":
                a = "9";
                break;
            case "R10":
                a = "10";
                break;
            case "R11":
                a = "11";
                break;
            case "R12":
                a = "12";
                break;
            case "R13":
                a = "13";
                break;
            case "R14":
                a = "14";
                break;
            case "R15":
                a = "15";
                break;
            case "SCREEN":
                a = "16384";
                break;
            case "KBD":
                a = "24576";
                break;
            default:
                if (symbol.containsKey(a)) {
                    a = symbol.get(a).toString();
                } else {
                    if (!NumberUtil.isNumber(a)) {
                        if (freeRegister.containsKey(a)) {
                            a = freeRegister.get(a).toString();
                        } else {
                            symbol.put(a, ramNo);
                            a = String.valueOf(ramNo);
                            ramNo++;
                        }
                    }
                }
                break;
        }

        // 将a转换成16位的二进制数
        String binary = Integer.toBinaryString(Integer.parseInt(a));
        return StrUtil.padPre(binary, 16, "0");
    }

    public static String analyzeInstructionC(String instruction) {
        String dest = "";
        String cmp = "";
        String jmp = "";
        int i = StrUtil.indexOf(instruction, '=');
        if (i != -1) {
            dest = instruction.substring(0, i);
            cmp = instruction.substring(i + 1);
            instruction = instruction.substring(i + 1);
        }
        i = StrUtil.indexOf(instruction, ';');
        if (i != -1) {
            cmp = instruction.substring(0, i);
            jmp = instruction.substring(i + 1);
        }
        return "111" + getCmp(cmp) + getDest(dest) + getJmp(jmp);
    }

    private static String getJmp(String jmp) {
        switch (jmp) {
            case "":
                return "000";
            case "JGT":
                return "001";
            case "JEQ":
                return "010";
            case "JGE":
                return "011";
            case "JLT":
                return "100";
            case "JNE":
                return "101";
            case "JLE":
                return "110";
            case "JMP":
                return "111";
            default:
                throw new IllegalArgumentException("jmp is not supported");
        }
    }

    private static String getDest(String dest) {
        switch (dest) {
            case "":
                return "000";
            case "M":
                return "001";
            case "D":
                return "010";
            case "MD":
                return "011";
            case "A":
                return "100";
            case "AM":
                return "101";
            case "AD":
                return "110";
            case "AMD":
                return "111";
            default:
                throw new IllegalArgumentException("dest is not supported");
        }
    }

    private static String getCmp(String cmp) {
        switch (cmp) {
            case "0":
                return "0101010";
            case "1":
                return "0111111";
            case "-1":
                return "0111010";
            case "D":
                return "0001100";
            case "A":
                return "0110000";
            case "!D":
                return "0001101";
            case "!A":
                return "0110001";
            case "-D":
                return "0001111";
            case "-A":
                return "0110011";
            case "D+1":
                return "0011111";
            case "A+1":
                return "0110111";
            case "D-1":
                return "0001110";
            case "A-1":
                return "0110010";
            case "D+A":
                return "0000010";
            case "D-A":
                return "0010011";
            case "A-D":
                return "0000111";
            case "D&A":
                return "0000000";
            case "D|A":
                return "0010101";
            case "M":
                return "1110000";
            case "!M":
                return "1110001";
            case "-M":
                return "1110011";
            case "M+1":
                return "1110111";
            case "M-1":
                return "1110010";
            case "D+M":
                return "1000010";
            case "D-M":
                return "1010011";
            case "M-D":
                return "1000111";
            case "D&M":
                return "1000000";
            case "D|M":
                return "1010101";
            default:
                throw new IllegalArgumentException("cmp is not supported");
        }
    }

}
