package com.lynki.lynki.services;

import com.lynki.lynki.domain.Url;
import com.lynki.lynki.domain.dtos.UserUrlsResponseDTO;
import com.lynki.lynki.repository.UrlRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportUrlService {

    private final UrlRepository urlRepository;

    public ExportUrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<UserUrlsResponseDTO> generateJson(String id) {
            List<Url> urls = urlRepository.findByUserId(id);

            return urls.stream()
                    .map(url -> new UserUrlsResponseDTO(
                            url.getOriginUrl(),
                            url.getId(),
                            url.getExpiresAt(),
                            url.getClickCount()
                    ))
                    .toList();



    }

    public String generateCsv(String userId) {


        List<Url> urls = urlRepository.findByUserId(userId);

        StringBuilder csv = new StringBuilder("originUrl;shortUrl;expiresAt;clicksCount\n");

        for (Url url : urls) {
            csv.append(url.getOriginUrl()).append(";")
                    .append(url.getId()).append(";")
                    .append(url.getExpiresAt()).append(";")
                    .append(url.getClickCount()).append("\n");
        }


        return csv.toString();

    }

}
