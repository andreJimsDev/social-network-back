package com.opendevup.adapters.user.controllers.dto;

public record ErrorValidateStructureDto(
        String objectName,
        String fieldName,
        String rejectedValue,
        String errorMessage
) {}
