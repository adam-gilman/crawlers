/* Copyright 2014-2023 Norconex Inc.
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
package com.norconex.crawler.web.crawler;

/**
 * HTTP Crawler event names. Those are in addition to event names from
 * dependant libraries.
 */
public final class WebCrawlerEvent {

    public static final String REJECTED_ROBOTS_TXT =
            "REJECTED_ROBOTS_TXT";
    public static final String CREATED_ROBOTS_META = "CREATED_ROBOTS_META";
    public static final String REJECTED_ROBOTS_META_NOINDEX =
            "REJECTED_ROBOTS_META_NOINDEX";
    public static final String URLS_EXTRACTED = "URLS_EXTRACTED";
    /** @since 2.8.0 (renamed from REJECTED_CANONICAL) */
    public static final String REJECTED_NONCANONICAL = "REJECTED_NONCANONICAL";
    /** @since 2.3.0 */
    public static final String REJECTED_REDIRECTED = "REJECTED_REDIRECTED";

    /** @since 3.0.0 */
    public static final String URLS_POST_IMPORTED =
            "URLS_POST_IMPORTED";

    private WebCrawlerEvent() {
    }
}
