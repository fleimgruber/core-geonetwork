package org.fao.geonet.kernel.search;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.kernel.SchemaManager;
import org.fao.geonet.domain.MetadataType;
import org.fao.geonet.AbstractCoreIntegrationTest;
import org.fao.geonet.utils.Xml;
import org.fao.geonet.kernel.DataManager;

import org.jdom.Element;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SampleDataParserTest extends AbstractCoreIntegrationTest {
    @Autowired
    private SchemaManager _schemaManager;

    @Autowired
    protected ConfigurableApplicationContext _applicationContext;

    @Test
    public void sample() throws Exception {
        Path samplePath = Paths.get("src", "test", "resources", "iso19115-3_sample.xml");
        Element sampleData = Xml.loadFile(samplePath);

        // final Path schemaDir = _schemaManager.getSchemaDir("iso19115-3.2018");
        final Path schemaDir = _schemaManager.getSchemaDir("iso19139");
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

    @Test
    public void sampleValidate() throws Exception {
        Path samplePath = Paths.get("src", "test", "resources", "iso19115-3_sample.xml");
        Element sampleData = Xml.loadFile(samplePath);

        final DataManager dataManager = _applicationContext.getBean(DataManager.class);

        String schemaDetected = dataManager.autodetectSchema(sampleData);
        dataManager.validate(schemaDetected, sampleData);
    }

    public static final String SCHEMA_INDEX_XSLT_FOLDER = "index-fields";
    public static final String SCHEMA_INDEX_XSTL_FILENAME = "index.xsl";
    public static final String SCHEMA_INDEX_SUBTEMPLATE_XSTL_FILENAME = "index-subtemplate.xsl";

    private Path getXSLTForIndexing(Path schemaDir, MetadataType metadataType) {
        Path xsltForIndexing = schemaDir
            .resolve(SCHEMA_INDEX_XSLT_FOLDER)
            .resolve(
                metadataType.equals(MetadataType.SUB_TEMPLATE) || metadataType.equals(MetadataType.TEMPLATE_OF_SUB_TEMPLATE) ?
                    SCHEMA_INDEX_SUBTEMPLATE_XSTL_FILENAME : SCHEMA_INDEX_XSTL_FILENAME);
        if (!Files.exists(xsltForIndexing)) {
            throw new RuntimeException(String.format(
                "XSLT for schema indexing does not exist. Create file '%s'.",
                xsltForIndexing.toString()));
        }
        return xsltForIndexing;
    }

	@Test
	public void testXmlTransform() throws Exception {
		Path samplePath = Paths.get("src", "test", "resources", "iso19115-3_sample.xml");
        Element sampleData = Xml.loadFile(samplePath);

        MetadataType metadataType = MetadataType.METADATA;
        final Path schemaDir = _schemaManager.getSchemaDir("iso19115-3.2018");
        final Path styleSheet = getXSLTForIndexing(schemaDir, metadataType);

		IndexingMode indexingMode = IndexingMode.full;
		Map<String, Object> indexParams = new HashMap<>();
		indexParams.put("fastIndexMode", indexingMode.equals(IndexingMode.core));

		Element fields = Xml.transform(sampleData, styleSheet, indexParams);
        fields.getAttributeValue("a");
	}

	@Test
	public void testXmlTransform_full() throws Exception {
		Path samplePath = Paths.get("src", "test", "resources", "iso19115-3_mre.xml");
        Element sampleData = Xml.loadFile(samplePath);

        MetadataType metadataType = MetadataType.METADATA;
        final Path schemaDir = _schemaManager.getSchemaDir("iso19115-3.2018");
        final Path styleSheet = getXSLTForIndexing(schemaDir, metadataType);

		IndexingMode indexingMode = IndexingMode.full;
		Map<String, Object> indexParams = new HashMap<>();
		indexParams.put("fastIndexMode", indexingMode.equals(IndexingMode.core));

		Element fields = Xml.transform(sampleData, styleSheet, indexParams);
        fields.getAttributeValue("a");
	}
}
