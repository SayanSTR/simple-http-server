package com.sayanstr.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    @DisplayName("Valid method parsing")
    void parseHttpRequest() {
        HttpRequest httpRequest = null;
        try {
            httpRequest = httpParser.parseHttpRequest(
                    generateValidGETTestCase()
            );
        } catch (HttpParsingException e) {
            fail(e);
        }

        assertNotNull(httpRequest);
        assertEquals(HttpMethod.GET, httpRequest.getMethod());
        assertEquals("/", httpRequest.getRequestTarget());
        assertEquals("HTTP/1.1", httpRequest.getOriginalHttpVersion());
        assertEquals(HttpVersion.HTTP_1_1, httpRequest.getBestCompatibleHttpVersion());
    }

    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Linux\"\r\n" +
                "DNT: 1\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Rejecting invalid method")
    void parseHttpRequestBadMethod() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateNegativeTestCaseMethodName()
            );
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }

    }

    private InputStream generateNegativeTestCaseMethodName() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Too long method")
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateNegativeTestCaseMethodName2()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }

    }

    private InputStream generateNegativeTestCaseMethodName2() {
        String rawData = "GETTTTTTTTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Invalid number of items in request line")
    void parseHttpRequestInvalidNumberOfItemsExcess() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateBadTestCaseInvalidNumberOfItemsExcess()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }

    }

    private InputStream generateBadTestCaseInvalidNumberOfItemsExcess() {
        String rawData = "GET / AAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Empty request line")
    void parseHttpRequestEmptyRequestLine() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateBadTestCaseEmptyRequestLine()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }

    }

    private InputStream generateBadTestCaseEmptyRequestLine() {
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("No Line Feed")
    void parseHttpRequestOnlyCRNoLF() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateBadTestCaseOnlyCRNoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }

    }

    private InputStream generateBadTestCaseOnlyCRNoLF() {
        String rawData = "GET / HTTP/1.1\r" + // <-- No LF
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Bad HTTP Version Format")
    void parseHttpRequestBadHttpVersion() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateBadHttpVersionTestCase()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }

    }

    private InputStream generateBadHttpVersionTestCase() {
        String rawData = "GET / HTtP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Unsupported HTTP Version")
    void parseHttpRequestUnsupportedHttpVersion() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateUnsupportedHttpVersionTestCase()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatus.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }

    }

    private InputStream generateUnsupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/3.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }

    @Test
    @DisplayName("Supported HTTP Version")
    void parseHttpRequestSupportedHttpVersion() {
        try {
            HttpRequest httpRequest = httpParser.parseHttpRequest(
                    generateSupportedHttpVersionTestCase()
            );
            assertNotNull(httpRequest);
            assertEquals(HttpVersion.HTTP_1_1, httpRequest.getBestCompatibleHttpVersion());
            assertEquals("HTTP/1.3", httpRequest.getOriginalHttpVersion());
        } catch (HttpParsingException e) {
            e.printStackTrace();
            fail();
        }

    }

    private InputStream generateSupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/1.3\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,bn;q=0.6" +
                "\r\n\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return inputStream;
    }


}