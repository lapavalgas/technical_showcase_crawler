package com.challenge.backend.helper;

import com.challenge.backend.Main;
import com.challenge.backend.utils.CrawlConstants;
import com.challenge.backend.utils.LogConstants;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLFormatterHelper {
    private static final Pattern urlStartPattern = Pattern.compile(CrawlConstants.RegexPatterns.URL_START_PATTERN);
    private static final Pattern invalidOrStrangePattern = Pattern.compile(CrawlConstants.RegexPatterns.URL_INVALID_STRANGE_PATTERN);

    public static String formatRelativePathAndAbsolutUrl(String url) {
        return formatUrlWithEncoderClass(checkAndMountRelativePath(notHttpUrlFormatter(url)));
    }

    public static boolean isValidURL(String url) {
        try {
            final URI uri = new URI(url);
            return uri.getScheme() != null && uri.getPath().matches(CrawlConstants.RegexPatterns.URL_VALID_PATTERN);
        } catch (URISyntaxException e) {
            Main.logger.error(LogConstants.Error.ERROR_FAIL_VALIDATED_URL_PATTERN);
            return false;
        }
    }

    public static boolean isSameDomain(String url1, String url2) {
        try {
            final URI a = new URI(url1);
            final URI b = new URI(url2);
            return a.getHost().equals(b.getHost());
        } catch (Exception e) {
            Main.logger.error(LogConstants.Error.ERROR_FAIL_VALIDATED_URL_DOMAIN);
            return false;
        }
    }

    private static String formatUrlWithEncoderClass(String url) {
        try {
            final URI uri = new URI(URLEncoder.encode(url, StandardCharsets.UTF_8));
            String query = uri.getQuery();
            if (query != null) {
                query = URLEncoder.encode(query, StandardCharsets.UTF_8);
            }
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, null).toString();
        } catch (Exception e) {
            Main.logger.info(LogConstants.Error.ERROR_FAIL_VALIDATED_URL, url);
            return null;
        }
    }

    private static String checkAndMountRelativePath(String url) {
        final Matcher matcher = urlStartPattern.matcher(url);
        if (!matcher.matches()) {
            return url;
        }
        if (url.startsWith(CrawlConstants.RegexPatterns.CHAR_SLASH)) {
            url = url.replaceFirst(CrawlConstants.RegexPatterns.CHAR_SLASH, CrawlConstants.RegexPatterns.CHAR_NOTHING);
        }
        return CrawlConstants.URL.BASE + url;
    }

    private static String notHttpUrlFormatter(String url) {
        if (url.startsWith(CrawlConstants.RegexPatterns.URL_MAILTO_SLASH) ||
                url.startsWith(CrawlConstants.RegexPatterns.URL_MAILTO) ||
                url.startsWith(CrawlConstants.RegexPatterns.URL_FTP_SLASH) ||
                url.startsWith(CrawlConstants.RegexPatterns.URL_FTP)) {
            return CrawlConstants.URL.BASE;
        }
        if (url.startsWith(CrawlConstants.RegexPatterns.CHAR_SLASH_SLASH)) {
            url = url.replace(CrawlConstants.RegexPatterns.CHAR_SLASH_SLASH, CrawlConstants.RegexPatterns.URL_START_PATTERN_HTTP);
        }
        if (url.startsWith(CrawlConstants.RegexPatterns.CHAR_SPACE)) {
            url = url.replaceFirst(CrawlConstants.RegexPatterns.CHAR_SPACE, CrawlConstants.RegexPatterns.CHAR_NOTHING);
        }
        return url;
    }

    public static String crawlCustomFormatURLForFixALastProblem(String url) {
        if (invalidOrStrangePattern.matcher(url).find()) {
            return url.replaceAll(CrawlConstants.RegexPatterns.URL_INVALID_STRANGE_PATTERN, CrawlConstants.RegexPatterns.CHAR_SLASH);
        }
        return url;
    }
}
