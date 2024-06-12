package com.opendevup.adapters.user.gateways.persistence.mysql.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTokenEntityRepository extends JpaRepository<JpaTokenEntity, String> {
    @Query(value = """
            select t from JpaTokenEntity t inner join JpaUserEntity u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<JpaTokenEntity> findAllValidTokenByUser(String id);

    Optional<JpaTokenEntity> findByAccessToken(String accessToken);
}