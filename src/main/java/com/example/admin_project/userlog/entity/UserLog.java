package com.example.admin_project.userlog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserLog {

    @Comment("식별자")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Comment("ip")
    @Column(name = "ip", nullable = false)
    private String ip;

    @Comment("uri")
    @Column(name = "uri", nullable = false)
    private String uri;

    @Comment("요청 방식")
    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    @Comment("요청 시간")
    @Column(name = "log_time", nullable = false)
    private LocalDateTime logTime;

    @Comment("기록 일자")
    @Column(name = "log_date", nullable = false, length = 8)
    private String logDate;
}
