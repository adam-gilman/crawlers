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
package com.norconex.crawler.core.pipeline.committer;

import com.norconex.crawler.core.pipeline.DocumentPipelineContext;
import com.norconex.crawler.core.pipeline.committer.CommitterPipeline;

public class MockCommitterPipeline implements CommitterPipeline {

    @Override
//    public void accept(Crawler t, CrawlDoc u) {
    public void accept(DocumentPipelineContext ctx) {
        // TODO Auto-generated method stub
        // Event triggered by service
        ctx.getCommitterService().upsert(ctx.getDocument());
    }

}
