package com.example.admin_project.userlog.service;

import com.example.admin_project.userlog.entity.UserLog;
import com.example.admin_project.userlog.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLogService {

    private final UserLogRepository userLogRepository;

    private static final String LOG_FILE_PATH = "C:/log/user-action.log";


    private long lastProcessedLine = 0;

    @Scheduled(fixedDelay = 60000) // 매 1분마다 실행
    public void processLogFile() {
        try {
            log.info(">>> [스케줄러 실행] 로그 파일 파싱 시작");
            Path path = Paths.get(LOG_FILE_PATH);
            if (!Files.exists(path)) {
                log.warn("로그 파일이 존재하지 않음: {}", LOG_FILE_PATH);
                return;
            }

            BufferedReader reader = Files.newBufferedReader(path);
            long currentLine = 0;
            String line;

            while((line = reader.readLine()) != null) {

                currentLine++;
                if (currentLine <= lastProcessedLine) continue;

                UserLog userLog = parseLine(line);

                if (userLog != null) {
                    userLogRepository.save(userLog);
                }

                lastProcessedLine = currentLine;
                log.info("로그 저장 완료 — 처리 라인 수: {}", currentLine);
            }
        } catch (IOException e) {
            log.error("로그 파일 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private UserLog parseLine(String line) {
        try {
            // 예: 2025-06-10 20:17:34.404 [http...] INFO  USER_ACTION - URI=/menu, IP=..., httpMethod=..., currentTime=...
            if (!line.contains("USER_ACTION")) return null;

            String[] parts = line.split(" - ");
            if (parts.length < 2) return null;

            String[] tokens = parts[1].split(", "); // [ URI=/menu, IP=0:0:0:0:0:0:0:1, httpMethod=GET, currentTime=...]
            String uri = tokens[0].split("=")[1]; // /menu
            String ip = tokens[1].split("=")[1]; // 0:0:0:0:0:0:0:1
            String httpMethod = tokens[2].split("=")[1]; // GET
            String timeStr = tokens[3].split("=")[1]; // 2025-07-07 14:24:25

            return UserLog.builder()
                    .uri(uri)
                    .ip(ip)
                    .httpMethod(httpMethod)
                    .logTime(LocalDateTime.parse(timeStr))
                    .build();
        } catch (Exception e) {
            log.error("로그 파싱 실패: " + line);
            return null;
        }
    }
}
