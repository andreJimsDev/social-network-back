package com.opendevup.adapters.user.gateways.persistence.mysql.jpa;

import com.opendevup.adapters.shared.gateways.persistence.jpa.JpaBaseEntityAudit;
import com.opendevup.core.user.models.TokenType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tokens")
public class JpaTokenEntity extends JpaBaseEntityAudit {

    private String accessToken;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;
    private boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public JpaUserEntity user;
}
