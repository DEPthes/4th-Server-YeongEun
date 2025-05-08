package org.example;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private static final Logger logger = Logger.getLogger("com.example");

    static {
        logger.setUseParentHandlers(false); // 기본 콘솔 핸들러 제거

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter()); // 기본 포맷 사용

        logger.addHandler(handler);
        logger.setLevel(Level.ALL); // 모든 로그 레벨 출력
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warning(msg);
    }

    public static void error(String msg) {
        logger.severe(msg);
    }

    public static void debug(String msg) {
        logger.fine(msg);
    }

    public void init() {
        logger.info("로그 초기화 완료");
    }
}