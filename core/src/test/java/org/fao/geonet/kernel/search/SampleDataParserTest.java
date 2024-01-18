package org.fao.geonet.kernel.search;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.fao.geonet.kernel.SchemaManager;
import org.fao.geonet.domain.MetadataType;
import org.fao.geonet.AbstractCoreIntegrationTest;
import org.fao.geonet.utils.Xml;

import org.jdom.Element;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SampleDataParserTest extends AbstractCoreIntegrationTest {
    @Autowired
    private SchemaManager _schemaManager;

    @Test
    public void sample() throws Exception {
        Path samplePath = Paths.get("src", "test", "resources", "iso19115-3_sample.xml");
        Element sampleData = Xml.loadFile(samplePath);

        final Path schemaDir = _schemaManager.getSchemaDir("iso19115-3.2018");
        String id = "e6f81a53-75fa-485e-9e29-2282221b0e3d";
        Multimap<String, Object> moreFields = ArrayListMultimap.create();
        MetadataType metadataType = MetadataType.METADATA;
        boolean forceRefreshReaders = true;
        IndexingMode indexingMode = IndexingMode.full;

        EsSearchManager toTest = new EsSearchManager();

        toTest.index(
            schemaDir,
            sampleData,
            id,
            moreFields,
            metadataType,
            forceRefreshReaders,
            indexingMode
        );
    }
}
