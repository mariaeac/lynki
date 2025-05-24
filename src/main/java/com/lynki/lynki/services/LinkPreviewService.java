package com.lynki.lynki.services;

import com.lynki.lynki.domain.dtos.LinkPreviewResponseDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.io.IOException;

import java.net.Proxy;
import java.util.List;

@Service
public class LinkPreviewService {

    private final List<Proxy> proxyList;

    public LinkPreviewService(List<Proxy> proxyList) {
        this.proxyList = proxyList;
    }

    public LinkPreviewResponseDTO fetchLinkPreview(String url) {

        for (int i = 0; i < proxyList.size(); i++) {
            Proxy proxy = proxyList.get(i);

            try {
                Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get();

                String title = getMetaTag(document, "og:title");
                 if (isEmpty(title)) {
                     title = document.title();
                 }

                 String description = getMetaTag(document, "og:description");

                 if (isEmpty(description)) {
                     Element descriptionTag = document.selectFirst("meta[name=description]");
                     description = descriptionTag != null ? descriptionTag.attr("content") : "";
                 }

                 String image = getMetaTag(document, "og:image");
                 if (isEmpty(image)) {
                     Element img = document.selectFirst("img");
                     image = img != null ? img.absUrl("src") : "";
                 }

                return new LinkPreviewResponseDTO(title, description, image, url);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return new LinkPreviewResponseDTO("Falha ao gerar preview", "", "", url);
    }

    private String getMetaTag(Document document, String attr) {
        Element tag = document.selectFirst("meta[property=" + attr + "]");
        return tag != null ? tag.attr("content") : "";

    }

    private boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }
}
