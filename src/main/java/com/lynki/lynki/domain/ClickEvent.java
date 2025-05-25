package com.lynki.lynki.domain;

import eu.bitwalker.useragentutils.DeviceType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "clickEvents")
public class ClickEvent {

    @Id
    private String id;
    private String shortUrl;
    private String ip;
    private String browser;
    private String os;
    private DeviceType deviceType;
    private Instant clickedAt;

    public ClickEvent() {

    }

    public ClickEvent(String id, String shortUrl, String ip, String browser, String os, DeviceType deviceType, Instant clickedAt) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.ip = ip;
        this.browser = browser;
        this.os = os;
        this.deviceType = deviceType;
        this.clickedAt = clickedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Instant getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(Instant clickedAt) {
        this.clickedAt = clickedAt;
    }
}
