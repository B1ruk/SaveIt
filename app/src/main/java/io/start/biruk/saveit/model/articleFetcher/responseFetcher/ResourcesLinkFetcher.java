package io.start.biruk.saveit.model.articleFetcher.responseFetcher;

import com.annimon.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by biruk on 5/10/2018.
 * provides static methods to return
 * resources link
 */
public class ResourcesLinkFetcher {
    public List<String> getAllCssLinks(Document rawHtml) {
        return Stream.of(rawHtml.select("link"))
                .map(element -> element.attr("href"))
                .filter(csslink -> !csslink.isEmpty())
                .filter(cssExt -> cssExt.endsWith(".css"))
                .toList();

    }

    public List<String> getAllImgLinks(Document rawHtml) {
        return Stream.of(rawHtml.select("img"))
                .map(element -> element.attr("src"))
                .filter(imglink -> !imglink.isEmpty())
                .toList();

    }

    public List<String> getAllJsLinks(Document rawHtml) {
        return Stream.of(rawHtml.select("script"))
                .map(js -> js.attr("src"))
                .filter(jsSrc -> !jsSrc.isEmpty())
                .filter(jsExt -> jsExt.endsWith(".js"))
                .toList();
    }
}
