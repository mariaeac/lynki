package com.lynki.lynki.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "urls")
public class Url {

    @Id
    private String id;

    @NotBlank(message = "A URL original não pode estar vazia!")
    @URL(message = "A URL original deve ser uma URL válida!")
    private String originUrl;

    @Indexed(name = "expireAtIndex", expireAfter = "0")
    private Instant expiresAt;

    @Min(value = 0, message = "Contador de cliques não pode ter valor negativo.")
    private Long clickCount;

    private UUID userId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    public Url() {

    }
    public Url(String id, String originUrl, Instant expiresAt, Long clickCount, UUID userId, Instant createdAt, Instant lastModifiedAt) {
        this.id = id;
        this.originUrl = originUrl;
        this.expiresAt = expiresAt;
        this.clickCount = clickCount;
        this.userId = userId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotBlank(message = "A URL original não pode estar vazia!") @URL(message = "A URL original deve ser uma URL válida!") String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(@NotBlank(message = "A URL original não pode estar vazia!") @URL(message = "A URL original deve ser uma URL válida!") String originUrl) {
        this.originUrl = originUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public @Min(value = 0, message = "Contador de cliques não pode ter valor negativo.") Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(@Min(value = 0, message = "Contador de cliques não pode ter valor negativo.") Long clickCount) {
        this.clickCount = clickCount;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(id, url.id) && Objects.equals(userId, url.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }

}
