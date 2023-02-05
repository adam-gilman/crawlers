/* Copyright 2022 Norconex Inc.
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
package com.norconex.importer.handler.tagger.impl;

import static com.norconex.importer.parser.ParseState.PRE;
import static java.io.InputStream.nullInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.norconex.commons.lang.map.Properties;
import com.norconex.commons.lang.xml.XML;
import com.norconex.importer.TestUtil;
import com.norconex.importer.handler.ImporterHandlerException;

class FieldReportTaggerTest {

    @TempDir
    private Path tempDir;

    @Test
    void testFieldReportTagger() throws ImporterHandlerException, IOException {
        var t = new FieldReportTagger();

        var reportFile = tempDir.resolve("report.csv");
        t.setFile(reportFile);
        t.setMaxSamples(2);
        t.setTruncateSamplesAt(3);
        t.setWithHeaders(true);
        t.setWithOccurences(true);

        assertThatNoException().isThrownBy(
                () -> XML.assertWriteRead(t, "handler"));

        t.setWithHeaders(false);
        var props = new Properties();
        props.add("a", "a1111111");
        props.add("b", "b1111111");
        props.add("c", "c1111111");
        props.add("d", "d1111111");
        props.add("e", "e1111111");
        props.add("f", "f1111111");
        t.tagDocument(TestUtil.newHandlerDoc(props), nullInputStream(), PRE);
        t.tagDocument(TestUtil.newHandlerDoc(props), nullInputStream(), PRE);

        props = new Properties();
        props.add("a", "a2222222");
        props.add("b", "b2222222");
        props.add("g", "g2222222");
        t.tagDocument(TestUtil.newHandlerDoc(props), nullInputStream(), PRE);

        props = new Properties();
        props.add("a", "a3333333");
        t.tagDocument(TestUtil.newHandlerDoc(props), nullInputStream(), PRE);
        t.tagDocument(TestUtil.newHandlerDoc(props), nullInputStream(), PRE);
        t.tagDocument(TestUtil.newHandlerDoc(props), nullInputStream(), PRE);

        assertThat(Files.readString(reportFile)).isEqualToIgnoringNewLines("""
            a,6,a11,a22
            b,3,b11,b22
            c,2,c11,
            d,2,d11,
            e,2,e11,
            f,2,f11,
            g,1,g22,
            """);
    }
}
