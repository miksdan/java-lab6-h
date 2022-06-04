package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileValidator {

    private static final Logger logger = LoggerFactory.getLogger(FileValidator.class);

    public static boolean validateFileAndReadXml(String fileName) throws URISyntaxException {

        boolean isFileValid;
        boolean isFileReadebleWritable;
        String schemaName = "config.xsd";
        String xmlFileName = fileName;
        File xmlSchema;

        {
            xmlSchema = new File(schemaName);

            if (!xmlSchema.exists()) {
                SchemeGenerator.generateXsdScheme();
            }
            if (!xmlSchema.canRead()) {
                xmlSchema.setReadable(true);
            }
        }

        Scanner sc = new Scanner(System.in);
        String userInput = "Y";

        /*
         Interaction logic before starting program
         */
        while (true) {

            File file = new File(xmlFileName);
            if (!file.exists() || file.isDirectory()) {
                logger.info("File '" + xmlFileName + "' does not exist.\n");
                System.out.println("Want to try to use another file? \n['Y' to accept / Any symbol for default file structure]");
                if (sc.hasNext()) {
                    userInput = sc.nextLine();
                } else {
                    logger.info("The program terminated.");
                    System.exit(0);
                }
                if (userInput.equals("Y")) {
                    System.out.println("Enter the name of another file:\n");
                    if (sc.hasNext()) {
                        xmlFileName = sc.nextLine();
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                    continue;
                } else {
                    System.out.println("Want to use default file structure? \n['Y' to accept / Any symbol for cancellation]");
                    if (sc.hasNext()) {
                        userInput = sc.nextLine();
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                    if (userInput.equals("Y")) {
                        System.out.println("Enter free file name.");
                        System.out.println("The file name is required to contain only letters and (or) numbers.");
                        if (sc.hasNext()) {
                            userInput = sc.nextLine();

                            String regex = "^([a-zA-Z0-9])+$";

                            if (!Pattern.matches(regex, userInput)) {
                                logger.info("\nThe file name is required to contain only letters and (or) numbers.");
                                continue;
                            }
                        } else {
                            logger.info("The program terminated.");
                            System.exit(0);
                        }
                        userInput += ".xml";
                        File newFile = new File(userInput);
                        try {
                            if (newFile.createNewFile()) {
                                FileWriter fileWriter = new FileWriter(newFile);
                                fileWriter.write("<?xml version=\"1.0\" ?>\n" +
                                        "<movies>\n</movies>");
                                fileWriter.flush();
                                fileWriter.close();
                                xmlFileName = newFile.getName();
                                isFileValid = true;
                                isFileReadebleWritable = true;
                                logger.info("File ready to work.");
                                break;
                            }
                        } catch (IOException e) {
                            System.out.println("Enter the name of another file:\n");
                            if (sc.hasNext()) {
                                xmlFileName = sc.nextLine();
                            } else {
                                logger.info("The program terminated.");
                                System.exit(0);
                            }
                            continue;
                        }
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                }
            } else {
                logger.info("The file exists.");
            }

            if (!file.canRead() && !file.canWrite()) {
                logger.info("No enough rights of access to file '" + xmlFileName + "");
                try {
                    file.setWritable(true);
                    file.setReadable(true);
                    logger.info("Rights were added successfully to '" + xmlFileName + "");
                    isFileReadebleWritable = true;
                    continue;
                } catch (Exception e) {
                    logger.info("Rights were not added successfully to '" + xmlFileName + "");
                    logger.info("Enter the name of another file:\n");
                    if (sc.hasNext()) {
                        xmlFileName = sc.nextLine();
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                    continue;
                }
            } else {
                logger.info("The file has all rights.");
                isFileReadebleWritable = true;

                /*
                 Checking the XML document using the scheme XSD
                 */
                logger.info("Checking the structure of the document in progress.");
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true); //default value false
                    DocumentBuilder builder = factory.newDocumentBuilder();

                    Document doc = builder.parse(xmlFileName);

                    SchemaFactory aSF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Schema schema = aSF.newSchema(new File(schemaName));
                    Validator validator = schema.newValidator();

                    Source dsource = new DOMSource(doc);
                    validator.validate(dsource);
                    logger.info("Checking is passed.\n");
                    isFileValid = true;
                    break;
                } catch (Throwable e) {
                    isFileValid = false;
                    logger.info("'" + xmlFileName + "'" + " structure is damaged!");
                }

                System.out.println("Want to try to use another file? \n['Y' to accept / Any symbol for default file structure]");
                if (sc.hasNext()) {
                    userInput = sc.nextLine();
                } else {
                    logger.info("The program terminated.");
                    System.exit(0);
                }
                if (userInput.equals("Y")) {
                    System.out.println("Enter the name of another file:\n");
                    if (sc.hasNext()) {
                        xmlFileName = sc.nextLine();
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                    continue;
                } else {
                    logger.info("Want to use default file structure? \n['Y' to accept / Any symbol for cancellation]");
                    if (sc.hasNext()) {
                        userInput = sc.nextLine();
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                    if (userInput.equals("Y")) {
                        System.out.println("Enter free file name.");
                        System.out.println("The file name is required to contain only letters and (or) numbers.");

                        if (sc.hasNext()) {
                            userInput = sc.nextLine();

                            String regex = "^([a-zA-Z0-9])+$";

                            if (!Pattern.matches(regex, userInput)) {
                                logger.info("\nThe file name should contain only letters and (or) numbers.");
                                continue;
                            }
                        } else {
                            logger.info("The program terminated.");
                            System.exit(0);
                        }
                        userInput += ".xml";
                        File newFile = new File(userInput);
                        try {
                            if (newFile.createNewFile()) {
                                FileWriter fileWriter = new FileWriter(newFile);
                                fileWriter.write("<?xml version=\"1.0\" ?>\n" +
                                        "<movies>\n</movies>");
                                fileWriter.flush();
                                fileWriter.close();
                                xmlFileName = newFile.getName();
                                isFileValid = true;
                                isFileReadebleWritable = true;
                                logger.info("File ready to work.\n");
                                break;
                            }
                        } catch (IOException e) {
                            System.out.println("Enter the name of another file:\n");
                            if (sc.hasNext()) {
                                xmlFileName = sc.nextLine();
                            } else {
                                logger.info("The program terminated.");
                                System.exit(0);
                            }
                            continue;
                        }
                    } else {
                        logger.info("The program terminated.");
                        System.exit(0);
                    }
                }
            }
        }

        /*
        The start of the main program
         */
        if (isFileValid && isFileReadebleWritable) {
            FileAccessor.init(xmlFileName);
            FileAccessor.readFromXmlFile();
            return true;
        }
        return false;
    }
}
