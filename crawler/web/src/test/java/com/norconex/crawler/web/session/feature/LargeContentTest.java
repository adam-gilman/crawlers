/* Copyright 2019-2023 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.crawler.web.session.feature;

import static com.norconex.crawler.web.TestWebCrawlSession.forStartReferences;
import static com.norconex.crawler.web.WebsiteMock.serverUrl;
import static com.norconex.crawler.web.WebsiteMock.whenHtml;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerSettings;

/**
 * Test that large files are processed properly (&gt; 2MB).
 */
@MockServerSettings
class LargeContentTest {

    @Test
    void testLargeContent(ClientAndServer client) throws IOException {
        var minSize = 3 * 1024 *1024;
        var path = "/largeContent";

        whenHtml(client, path, RandomStringUtils.randomAlphanumeric(minSize));

        var mem = forStartReferences(serverUrl(client, path)).crawl();

        var txt = IOUtils.toString(
                mem.getUpsertRequests().get(0).getContent(), UTF_8);

        assertThat(txt).hasSizeGreaterThanOrEqualTo(minSize);
    }
}
