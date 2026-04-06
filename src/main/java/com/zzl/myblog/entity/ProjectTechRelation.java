package com.zzl.myblog.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectTechRelation {
    private Long techId;
    private Integer sortOrder;
}
