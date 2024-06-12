package com.opendevup.core.shared.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class EntityAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
}
