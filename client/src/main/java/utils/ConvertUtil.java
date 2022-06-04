package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ConvertUtil {

    public static final Logger logger = LoggerFactory.getLogger(ConvertUtil.class);

    public static byte[] convertObjectToByteArray(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return bytes;
    }

    public static Object convertByteArrayToObject(byte[] bytes) {
        Object object = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }

        try {
            byteArrayInputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return object;
    }
}
