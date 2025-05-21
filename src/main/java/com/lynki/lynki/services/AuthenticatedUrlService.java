package com.lynki.lynki.services;

import com.lynki.lynki.domain.Url;
import com.lynki.lynki.domain.User;
import com.lynki.lynki.domain.dtos.UrlRequestDTO;
import com.lynki.lynki.domain.dtos.UrlResponseDTO;
import com.lynki.lynki.domain.dtos.UserResponseDTO;
import com.lynki.lynki.domain.dtos.UserUrlsResponseDTO;
import com.lynki.lynki.exceptions.UrlException;
import com.lynki.lynki.exceptions.UserException;
import com.lynki.lynki.repository.UrlRepository;
import com.lynki.lynki.repository.UserRepository;
import com.lynki.lynki.utils.UrlUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticatedUrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final UrlUtilities urlUtils;


    public AuthenticatedUrlService(UrlRepository urlRepository, UserRepository userRepository, UrlUtilities urlUtils) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.urlUtils = urlUtils;
    }

    public UrlResponseDTO urlShortGenerate(UrlRequestDTO urlRequest, HttpServletRequest request, String userId) {

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserException.UserNotFound();
        }

        String id = urlUtils.createUniqueID();

        Url url = new Url();
        url.setId(id);
        url.setOriginUrl(urlRequest.url());
        url.setClickCount(0L);

        if (urlRequest.expirationTime() == 0) {
            url.setExpiresAt(null);
        } else {
            Instant expirationTime = Instant.now().plusSeconds(urlRequest.expirationTime());
            url.setExpiresAt(Instant.from(expirationTime));
        }


        url.setUserId(userId);
        urlRepository.save(url);

        String redirectUrl = urlUtils.buildRedirectAuthLink(request,id);
        return new UrlResponseDTO(redirectUrl, url.getClickCount());
    }

    public Url redirectOriginUrl(String shortId) {
        Url originUrl = urlRepository.findById(shortId).orElseThrow(() -> new UrlException.UrlNotFoundException());
        if (originUrl != null) {
            Long clickCountAux = originUrl.getClickCount();
            originUrl.setClickCount(clickCountAux + 1);
            urlRepository.save(originUrl);
        }

        return originUrl;
    }
    public Page<UserUrlsResponseDTO> getUrlsFromUser(String userId, Pageable pageable) {
        Page<Url> urls = urlRepository.findByUserId(userId, pageable);

        return urls.map(url -> new UserUrlsResponseDTO(
                url.getOriginUrl(),
                url.getId(),
                url.getExpiresAt(),
                url.getClickCount()
        ));
    }

    public void deleteUrlById(String urlId, String userId) {
        if (!urlRepository.existsById(urlId)) {
            throw new UrlException.UrlNotFoundException();
        }

        Url url = urlRepository.findById(urlId).orElseThrow(() -> new UrlException.UrlNotFoundException());
        if (url.getUserId().equals(userId)) {
            urlRepository.delete(url);
        } else {
            throw new UserException.UserNotFound();
        }
    }

    public UserResponseDTO getUserInfo(String userId) {
         User user = userRepository.findById(userId).orElseThrow(() -> new UserException.UserNotFound());
         return new UserResponseDTO(user.getUsername(), user.getEmail());
    }



}
