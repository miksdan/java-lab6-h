package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;

/**
 * Utility that helps to create xsd scheme for structure validation
 */
public class SchemeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(SchemeGenerator.class);

    private static String scheme = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "\n" +
            "    <xs:element name=\"movies\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:sequence>\n" +
            "                <xs:element minOccurs=\"0\" maxOccurs=\"unbounded\" name=\"movie\">\n" +
            "                    <xs:complexType>\n" +
            "                        <xs:sequence>\n" +
            "                            <xs:element name=\"id\" type=\"xs:integer\"/>\n" +
            "                            <xs:element name=\"name\" type=\"xs:string\"/>\n" +
            "                            <xs:element name=\"coordinates\">\n" +
            "                                <xs:complexType>\n" +
            "                                    <xs:sequence>\n" +
            "                                        <xs:element name=\"x\" type=\"xs:integer\"/>\n" +
            "                                        <xs:element name=\"y\" type=\"xs:long\"/>\n" +
            "                                    </xs:sequence>\n" +
            "                                </xs:complexType>\n" +
            "                            </xs:element>\n" +
            "                            <xs:element name=\"creationDate\" type=\"xs:date\"/>\n" +
            "                            <xs:element name=\"oscarsCount\" type=\"xs:integer\"/>\n" +
            "                            <xs:element name=\"goldenPalmCount\" type=\"xs:integer\"/>\n" +
            "                            <xs:element name=\"length\" type=\"xs:long\"/>\n" +
            "                            <xs:element name=\"mpaaRating\" type=\"xs:string\"/>\n" +
            "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"screenwriter\">\n" +
            "                            <xs:complexType>\n" +
            "                                <xs:sequence>\n" +
            "                                <xs:element name=\"name\" type=\"xs:string\"/>\n" +
            "                                <xs:element name=\"weight\" type=\"xs:integer\"/>\n" +
            "                                <xs:element name=\"eyeColor\" type=\"xs:string\"/>\n" +
            "                                <xs:element name=\"hairColor\" type=\"xs:string\"/>\n" +
            "                                <xs:element name=\"nationality\" type=\"xs:string\"/>\n" +
            "                                </xs:sequence>\n" +
            "                            </xs:complexType>\n" +
            "                        </xs:element>\n" +
            "                        </xs:sequence>\n" +
            "                    </xs:complexType>\n" +
            "                </xs:element>\n" +
            "            </xs:sequence>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "<!--using type=\"LimitedString\"-->\n" +
            "    <xs:simpleType name=\"LimitedString\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:maxLength value=\"100\" />\n" +
            "            <xs:minLength value=\"0\" />\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "</xs:schema>";

    /**
     * creates file with xsd scheme for structure validation
     */
    public static void generateXsdScheme() {
        logger.warn("The XSD scheme has not been detected.");
        String xsdFileName = "config.xsd";
        File newFile = new File(xsdFileName);
        try {
            if (newFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(newFile);
                fileWriter.write(scheme);
                fileWriter.flush();
                fileWriter.close();
                logger.info("XSD scheme created.\n");
            }
        } catch (Exception e) {
            logger.error("Scheme generator error.");
        }
    }
}
