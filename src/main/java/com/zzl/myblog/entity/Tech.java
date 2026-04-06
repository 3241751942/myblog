package com.zzl.myblog.entity;

import lombok.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Tech {
    private Long id;
    private String name;
    private String category;
    private String icon;
    private LocalDateTime createdAt;

    public Tech(String name, String category, String icon) {
        this.name = name;
        this.category = category;
        this.icon = icon;
    }
}
