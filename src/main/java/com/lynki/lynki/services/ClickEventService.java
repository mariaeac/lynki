package com.lynki.lynki.services;

import com.lynki.lynki.domain.ClickEvent;
import com.lynki.lynki.domain.dtos.ClickEventResponseDTO;
import com.lynki.lynki.repository.ClickEventRepository;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClickEventService {

    private final ClickEventRepository clickEventRepository;

    public ClickEventService(ClickEventRepository clickEventRepository) {
        this.clickEventRepository = clickEventRepository;
    }


    public void getDeviceData(HttpServletRequest request, String shortUrl) {

        String userAgent = request.getHeader("User-Agent");
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setIp(request.getHeader("X-Forwarded-For"));
        clickEvent.setBrowser(userAgentObj.getBrowser().getName());
        clickEvent.setOs(userAgentObj.getOperatingSystem().getName());
        clickEvent.setDeviceType(userAgentObj.getOperatingSystem().getDeviceType());
        clickEvent.setShortUrl(shortUrl);
        clickEvent.setClickedAt(Instant.now());

        clickEventRepository.save(clickEvent);

    }

    public List<ClickEventResponseDTO> getClickEventsByShortUrl(String shortUrl) {
        List<ClickEvent> events = clickEventRepository.findByShortUrl(shortUrl);
        List<ClickEventResponseDTO> response = events.stream().map(event -> new ClickEventResponseDTO(event.getShortUrl(), event.getIp(), event.getDeviceType(), event.getClickedAt(), event.getOs())).collect(Collectors.toList());
        return response;
    }

}
