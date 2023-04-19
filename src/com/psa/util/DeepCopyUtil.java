package com.psa.util;

import java.io.*;
import java.util.List;

public class DeepCopyUtil {

	/*
	 *  Creates a new object with the same values as the original, 
	 *  but any changes made to the copied object will not affect the original object
	 */
    public static <T extends Serializable> List<T> deepCopy(List<T> original) {
        try {
        	//uses Java's serialization mechanism to achieve deepCopy
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(original);
            objectOutputStream.flush();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            List<T> deepCopy = (List<T>) objectInputStream.readObject();
            return deepCopy; //Returning deepCopy list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}