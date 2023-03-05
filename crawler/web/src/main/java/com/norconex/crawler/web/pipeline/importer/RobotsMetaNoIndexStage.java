/* Copyright 2023 Norconex Inc.
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
package com.norconex.crawler.web.pipeline.importer;

import com.norconex.crawler.core.crawler.CrawlerEvent;
import com.norconex.crawler.core.doc.CrawlDocState;
import com.norconex.crawler.web.crawler.WebCrawlerEvent;

/**
 * Robots Meta NoIndex Check.
 */
class RobotsMetaNoIndexStage extends AbstractWebImporterStage {

    @Override
    boolean executeStage(WebImporterPipelineContext ctx) {
        var canIndex = ctx.getConfig().isIgnoreRobotsMeta()
                || ctx.getRobotsMeta() == null
                || !ctx.getRobotsMeta().isNoindex();
        if (!canIndex) {
            ctx.fire(CrawlerEvent.builder()
                    .name(WebCrawlerEvent.REJECTED_ROBOTS_META_NOINDEX)
                    .source(ctx.getCrawler())
                    .subject(ctx.getRobotsMeta())
                    .crawlDocRecord(ctx.getDocRecord())
                    .build());
            ctx.getDocRecord().setState(CrawlDocState.REJECTED);
            return false;
        }
        return canIndex;
    }
}