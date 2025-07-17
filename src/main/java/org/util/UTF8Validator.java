package org.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class UTF8Validator {

    public static void main(String[] args) throws Exception {
        String filePath = "path/to/your/file.txt";
        Map<Long, String> errors = detectInvalidUTF8Sequences(filePath);

        if (errors.isEmpty()) {
            System.out.println("文件是有效的 UTF-8 编码，没有发现无效序列");
        } else {
            System.out.println("发现 " + errors.size() + " 个无效 UTF-8 序列:");
            errors.forEach((position, description) ->
                    System.out.printf("位置: %d - 问题: %s%n", position, description));
        }
    }

    /**
     * 检测文件中的无效 UTF-8 序列
     *
     * @param filePath 文件路径
     * @return 包含错误位置和描述的 Map（键：字节偏移量，值：问题描述）
     * @throws IOException 如果读取文件时发生错误
     */
    public static Map<Long, String> detectInvalidUTF8Sequences(String filePath) throws IOException {
        byte[] allBytes = Files.readAllBytes(Paths.get(filePath));
        return detectInvalidUTF8Sequences(allBytes);
    }

    /**
     * 检测字节数组中的无效 UTF-8 序列
     *
     * @param bytes 要检测的字节数组
     * @return 包含错误位置和描述的 Map（键：字节偏移量，值：问题描述）
     */
    public static Map<Long, String> detectInvalidUTF8Sequences(byte[] bytes) {
        Map<Long, String> errors = new TreeMap<>();
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer charBuffer = CharBuffer.allocate(bytes.length * 2); // 分配足够空间

        while (byteBuffer.hasRemaining()) {
            int startPosition = byteBuffer.position();
            CoderResult result = decoder.decode(byteBuffer, charBuffer, false);

            if (result.isError()) {
                // 获取无效序列的开始位置
                int errorLength = 0;

                try {
                    result.throwException();
                } catch (MalformedInputException e) {
                    errorLength = e.getInputLength();
                    errors.put((long) startPosition, "无效的 UTF-8 序列 (长度: " + errorLength + ")");
                } catch (UnmappableCharacterException e) {
                    errorLength = e.getInputLength();
                    errors.put((long) startPosition, "无法映射的字符 (代码: 0x" +
                            bytesToHex(Arrays.copyOfRange(bytes, startPosition, startPosition + errorLength)) + ")");
                } catch (CharacterCodingException e) {
                    throw new RuntimeException(e);
                }

                // 跳过无效序列，继续检查后续内容
                byteBuffer.position(startPosition + errorLength);
            }
        }

        // 检查文件结束时的解码状态
        if (decoder.decode(byteBuffer, charBuffer, true).isError()) {
            errors.put((long) byteBuffer.position(), "文件末尾存在无效序列");
        }

        return errors;
    }

    // 辅助方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}