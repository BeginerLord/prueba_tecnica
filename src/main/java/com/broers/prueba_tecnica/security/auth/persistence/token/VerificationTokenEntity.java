package com.broers.prueba_tecnica.security.auth.persistence.token;

import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_tokens")
public class VerificationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @PrePersist
    protected void prePersist() {
        if (this.token == null) {
            this.token = UUID.randomUUID().toString();
        }
        if (this.expiryDate == null) {
            // Default expiry time: 24 hours
            this.expiryDate = LocalDateTime.now().plusHours(24);
        }
        if (this.used == false) {
            this.used = false;
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isValid() {
        return !isExpired() && !used;
    }
}