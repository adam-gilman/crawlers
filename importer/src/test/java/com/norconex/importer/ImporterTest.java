/* Copyright 2010-2023 Norconex Inc.
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
package com.norconex.importer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.norconex.commons.lang.file.ContentType;
import com.norconex.commons.lang.map.Properties;
import com.norconex.commons.lang.text.TextMatcher;
import com.norconex.commons.lang.xml.ErrorHandlerCapturer;
import com.norconex.commons.lang.xml.XML;
import com.norconex.importer.doc.Doc;
import com.norconex.importer.handler.HandlerConsumerAdapter;
import com.norconex.importer.handler.ImporterHandlerException;
import com.norconex.importer.handler.filter.OnMatch;
import com.norconex.importer.handler.filter.impl.TextFilter;
import com.norconex.importer.handler.transformer.DocumentTransformer;
import com.norconex.importer.parser.DocumentParser;
import com.norconex.importer.parser.DocumentParserException;
import com.norconex.importer.parser.ParseOptions;
import com.norconex.importer.response.ImporterStatus;

import lombok.NonNull;

class ImporterTest {

    @TempDir
    private Path tempDir;

    private Importer importer;

    @BeforeEach
    void setUp() throws Exception {
        var config = new ImporterConfig();
        config.getParseConfig().getParseOptions()
            .getEmbeddedConfig().setSplitEmbeddedOf(
                    List.of(TextMatcher.wildcard("*zip")));
        config.getParseConfig().getParseOptions()
            .getEmbeddedConfig().setSkipEmmbbeded(
                    List.of(TextMatcher.wildcard("*jpeg"),
                            TextMatcher.wildcard("*wmf")));
        config.setPostParseConsumer(HandlerConsumerAdapter.fromHandlers(
                (DocumentTransformer) (
                doc, input, output, parseState) -> {
            try {
               // Clean up what we know is extra noise for a given format
               var pattern = Pattern.compile("[^a-zA-Z ]");
               var txt = IOUtils.toString(
                input, StandardCharsets.UTF_8);
               txt = pattern.matcher(txt).replaceAll("");
               txt = txt.replaceAll("DowntheRabbitHole", "");
               txt = StringUtils.replace(txt, " ", "");
               txt = StringUtils.replace(txt, "httppdfreebooksorg", "");
               txt = StringUtils.replace(txt, "filejpg", "");
               txt = StringUtils.replace(txt, "filewmf", "");
               IOUtils.writeBack(txt, output, StandardCharsets.UTF_8);
            } catch (IOException e) {
               throw new ImporterHandlerException(e);
            }
        }));
        importer = new Importer(config);
    }

    @AfterEach
    void tearDown() throws Exception {
        importer = null;
    }

    @Test
    void testImporter() throws IOException {
        // test that it works with empty contructor
        try (var is = getClass().getResourceAsStream(
                "/parser/msoffice/word.docx")) {
            Assertions.assertEquals("Hey Norconex, this is a test.",
                    TestUtil.toString(new Importer().importDocument(
                            new ImporterRequest(is))
                                    .getDocument().getInputStream()).trim());
        }
    }

    @Test
    void testMain() throws IOException {
        var in = TestUtil.getAliceHtmlFile().getAbsolutePath();
        var out =  tempDir.resolve("out.txt").toAbsolutePath().toString();
        Importer.main(new String[]{"-i", in, "-o", out});
        Assertions.assertTrue(FileUtils.readFileToString(
                new File(out), UTF_8).contains("And so it was indeed"));
    }


    @Test
    void testGetters() {
        Assertions.assertSame(importer, Importer.get());
        Assertions.assertNotNull(
                importer.getConfiguration().getPostParseConsumer());
        Assertions.assertNotNull(importer.getEventManager());
    }

    @Test
    void testExceptions() throws IOException {
        // Invalid files
        Assertions.assertEquals(importer.importDocument(new ImporterRequest(
                tempDir.resolve("I-do-not-exist")))
                        .getImporterStatus().getException().getClass(),
                                ImporterException.class);

        Assertions.assertEquals(importer.importDocument(
                new Doc("ref", TestUtil.failingCachedInputStream()))
                        .getImporterStatus().getException().getClass(),
                                ImporterException.class);
    }

    @Test
    void testImportDocument() throws IOException {

        // MS Doc
        var docxOutput = File.createTempFile("ImporterTest-doc-", ".txt");
        var metaDocx = new Properties();
        writeToFile(importer.importDocument(
                new ImporterRequest(TestUtil.getAliceDocxFile().toPath())
                        .setMetadata(metaDocx)).getDocument(),
                        docxOutput);

        // PDF
        var pdfOutput = File.createTempFile("ImporterTest-pdf-", ".txt");
        var metaPdf = new Properties();
        writeToFile(importer.importDocument(
                new ImporterRequest(TestUtil.getAlicePdfFile().toPath())
                        .setMetadata(metaPdf)).getDocument(), pdfOutput);

        // ZIP/RTF
        var rtfOutput = File.createTempFile("ImporterTest-zip-rtf-", ".txt");
        var metaRtf = new Properties();
        var resp = importer.importDocument(
                new ImporterRequest(TestUtil.getAliceZipFile().toPath())
                        .setMetadata(metaRtf));
        writeToFile(resp.getNestedResponses()[0].getDocument(), rtfOutput);

        double doc = docxOutput.length();
        double pdf = pdfOutput.length();
        double rtf = rtfOutput.length();
        if (Math.abs(pdf - doc) / 1024.0 > 0.03
                || Math.abs(pdf - rtf) / 1024.0 > 0.03) {
            Assertions.fail("Content extracted from examples documents are too "
                    + "different from each other. They were not deleted to "
                    + "help you troubleshoot under: "
                    + FileUtils.getTempDirectoryPath() + "ImporterTest-*");
        } else {
            FileUtils.deleteQuietly(docxOutput);
            FileUtils.deleteQuietly(pdfOutput);
            FileUtils.deleteQuietly(rtfOutput);
        }

        Assertions.assertTrue(pdfOutput.length() < 10,
                "Converted file size is too small to be valid.");
    }

    @Test
    void testImportRejected() {
        var config = new ImporterConfig();
        config.setPostParseConsumer(
                HandlerConsumerAdapter.fromHandlers(new TextFilter(
                TextMatcher.basic("Content-Type").setPartial(true),
                TextMatcher.basic("application/pdf").setPartial(true),
                OnMatch.EXCLUDE)));
        var importer = new Importer(config);
        var result = importer.importDocument(
                new ImporterRequest(TestUtil.getAlicePdfFile().toPath())
                        .setContentType(ContentType.PDF)
                        .setReference("n/a"));

        Assertions.assertTrue(result.getImporterStatus().isRejected()
                && result.getImporterStatus().getDescription().contains(
                        "TextFilter"),
                "PDF should have been rejected with proper "
                        + "status description.");
    }

    @Test
    void testResponseProcessor() {
        var config = new ImporterConfig();
        config.setResponseProcessors(Arrays.asList(
                res -> {
                    res.setImporterStatus(new ImporterStatus(
                        ImporterStatus.Status.ERROR, "test"));
                    return null;
                }));
        var imp = new Importer(config);
        var resp = imp.importDocument(
                new ImporterRequest(TestUtil.getAliceZipFile().toPath())
                        .setMetadata(new Properties()));
        Assertions.assertEquals(
                "test", resp.getImporterStatus().getDescription());
    }

    @Test
    void testSaveParseError() throws IOException {
        var config = new ImporterConfig();
        var errorDir = tempDir.resolve("errors");
        Files.createDirectories(errorDir);
        config.getParseConfig().setErrorsSaveDir(errorDir);
        config.getParseConfig().setDefaultParser(new DocumentParser() {
            @Override
            public List<Doc> parseDocument(Doc doc, Writer output)
                    throws DocumentParserException {
                throw new DocumentParserException("TEST");
            }
            @Override
            public void init(@NonNull ParseOptions parseOptions)
                    throws DocumentParserException {
                //NOOP
            }
        });

        var importer = new Importer(config);
        // test that it works with empty contructor
        try (var is = getClass().getResourceAsStream(
                "/parser/msoffice/word.docx")) {
            importer.importDocument(new ImporterRequest(is));
            // 1: *-content.docx;  2: -error.txt;  3: -meta.txt
            Assertions.assertEquals(3, Files.list(errorDir).count());
        }
    }

    @Test
    void testValidation() throws IOException {
        var is = getClass().getResourceAsStream(
                "/validation/importer-full.xml");
        try (Reader r = new InputStreamReader(is)) {
            var ehc = new ErrorHandlerCapturer();
            var xml = XML.of(r).setErrorHandler(ehc).create();
            xml.validate(ImporterConfig.class);
            assertThat(ehc.getErrors()).isEmpty();
        }
    }

    private void writeToFile(Doc doc, File file)
            throws IOException {
        var out = new FileOutputStream(file);
        IOUtils.copy(doc.getInputStream(), out);
        out.close();
    }
}
