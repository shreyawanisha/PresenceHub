package org.attendance.dto.request;

import jakarta.validation.constraints.NotBlank;

public class QRMarkAttendanceRequestDTO {
    @NotBlank
    private String qrToken;

    public String getQrToken() { return qrToken; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }
}