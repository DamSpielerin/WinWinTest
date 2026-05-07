package org.minitest.authapi.process;

import org.hibernate.annotations.UuidGenerator;
import org.minitest.authapi.auth.AppUser;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processing_log")
public class ProcessingLog {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Column(name = "output_text", nullable = false)
    private String outputText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ProcessingLog() {
    }

    public ProcessingLog(AppUser user, String inputText, String outputText) {
        this.user = user;
        this.inputText = inputText;
        this.outputText = outputText;
    }
}
