package com.accountingsystem.entitys;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Document(collection = "user-login-log")
public class UserLog {

    @MongoId
    private String id;

    private String login;

    @Indexed(unique = true)
    private LocalDateTime time;

    private String ipAddress;
}
