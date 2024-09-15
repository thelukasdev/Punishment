package de.lukaspellny.punishment.logic;

import java.time.LocalDateTime;

public class PunishmentEntry {

    private String reason;
    private LocalDateTime date;

    public PunishmentEntry(String reason) {
        this.reason = reason;
        this.date = LocalDateTime.now();
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getDate() {
        return date;
    }
}