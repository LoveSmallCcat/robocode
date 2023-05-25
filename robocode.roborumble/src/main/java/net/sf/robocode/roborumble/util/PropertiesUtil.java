/*
 * Copyright (c) 2001-2023 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package net.sf.robocode.roborumble.util;


import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Flemming N. Larsen (original)
 */
public final class PropertiesUtil {

	/**
	 * Returns a new Properties instance that is initialized to the specified properties file.
	 *
	 * @param filename the filename of the properties file to load
	 * @return a new java.util.Properties instance
	 */
	public static Properties getProperties(String filename) {
		Properties props = new Properties();

		if (filename != null && filename.trim().length() > 0) {
			FileInputStream fis = null;

			try {
				fis = new FileInputStream(filename);
				props.load(fis);
			} catch (IOException e) {
				System.err.println("Could not load properties file: " + filename);
				return null; // Return null when failed to load the file
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return props;
	}
	/**
	 * Returns a new Properties instance that is initialized to the specified properties input stream.
	 *
	 * @param is the input stream of the properties to load
	 * @return a new java.util.Properties instance
	 */
	public static Properties getProperties(InputStream is) {
		Properties props = new Properties();

		try {
			props.load(is);
		} catch (Exception e) {
			System.err.println("Could not load properties input stream: " + is);
		}
		return props;
	}

	/**
	 * Stores a Properties instance to the specified properties file.
	 *
	 * @param properties the properties to store
	 * @param filename   the filename of the file to store the properties into
	 * @param comments   comments to include in the properties file
	 * @return true if the properties were stored; false otherwise
	 */
	public static boolean storeProperties(Properties properties, String filename, String comments) {
		if (properties == null || filename == null || filename.trim().length() == 0) {
			return false;
		}

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(filename);
			properties.store(fos, comments);
		} catch (IOException e) {
			System.err.println("Could not store properties to file: " + filename);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Tests the {@link PropertiesUtil#getProperties(String)} method with a valid properties file.
	 * Creates a test properties file with some properties,
	 * calls the getProperties method to load properties from it,
	 * and asserts that the properties are loaded successfully.
	 */
	@Test
	public void testGetPropertiesWithValidFile() {
		String filename = "test.properties";
		// Create a test properties file with some properties
		createTestPropertiesFile(filename);
		// Call the getProperties method
		Properties properties = PropertiesUtil.getProperties(filename);
		// Assert that the properties are loaded successfully
		assertNotNull(properties);
		assertEquals("value1", properties.getProperty("key1"));
		assertEquals("value2", properties.getProperty("key2"));
	}

	/**
	 * Tests the {@link PropertiesUtil#getProperties(String)} method with an invalid properties file.
	 * Calls the getProperties method with a nonexistent file
	 * and asserts that the returned properties are null.
	 */
	@Test
	public void testGetPropertiesWithInvalidFile() {
		String filename = "nonexistent.properties";
		// Call the getProperties method with a nonexistent file
		Properties properties = PropertiesUtil.getProperties(filename);
		// Assert that the properties are null
		assertNull(properties);
	}

	/**
	 * Tests the {@link PropertiesUtil#getProperties(String)} method with a null filename.
	 * Calls the getProperties method with a null filename
	 * and asserts that the returned properties are not null but empty.
	 */
	@Test
	public void testGetPropertiesWithNullFilename() {
		Properties properties = PropertiesUtil.getProperties((String) null);
		assertNotNull(properties);
		assertTrue(properties.isEmpty());
	}

	/**
	 * Tests the {@link PropertiesUtil#getProperties(String)} method with an empty filename.
	 * Calls the getProperties method with an empty filename
	 * and asserts that the returned properties are not null but empty.
	 */
	@Test
	public void testGetPropertiesWithEmptyFilename() {
		Properties properties = PropertiesUtil.getProperties("");
		assertNotNull(properties);
		assertTrue(properties.isEmpty());
	}

	/**
	 * Tests the {@link PropertiesUtil#getProperties(InputStream)} method with a valid input stream.
	 * Creates a test input stream with some properties,
	 * calls the getProperties method to load properties from it,
	 * and asserts that the properties are loaded correctly.
	 */
	@Test
	public void testGetPropertiesWithInputStream() {
		String input = "key1=value1\nkey2=value2";
		InputStream inputStream = new ByteArrayInputStream(input.getBytes());
		Properties properties = PropertiesUtil.getProperties(inputStream);
		assertNotNull(properties);
		assertEquals("value1", properties.getProperty("key1"));
		assertEquals("value2", properties.getProperty("key2"));
	}

	/**
	 * Tests the {@link PropertiesUtil#getProperties(InputStream)} method with a null input stream.
	 * Calls the getProperties method with a null input stream
	 * and asserts that the returned properties are not null but empty.
	 */
	@Test
	public void testGetPropertiesWithNullInputStream() {
		Properties properties = PropertiesUtil.getProperties((InputStream) null);
		assertNotNull(properties);
		assertTrue(properties.isEmpty());
	}

	/**
	 * Tests the {@link PropertiesUtil#storeProperties(Properties, String, String)} method.
	 * Creates a test properties object, stores it to a file,
	 * and then loads the properties from the file to verify the storage.
	 */
	@Test
	public void testStoreProperties() {
		Properties properties = new Properties();
		properties.setProperty("key1", "value1");
		properties.setProperty("key2", "value2");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		boolean success = PropertiesUtil.storeProperties(properties, "test.properties", null);
		assertTrue(success);

		Properties loadedProperties = PropertiesUtil.getProperties("test.properties");
		assertNotNull(loadedProperties);
		assertEquals("value1", loadedProperties.getProperty("key1"));
		assertEquals("value2", loadedProperties.getProperty("key2"));
	}

	/**
	 * Tests the {@link PropertiesUtil#storeProperties(Properties, String, String)} method with null properties.
	 * Calls the storeProperties method with null properties
	 * and asserts that the return value is false.
	 */
	@Test
	public void testStorePropertiesWithNullProperties() {
		boolean success = PropertiesUtil.storeProperties(null, "test.properties", null);
		assertFalse(success);
	}

	/**
	 * Tests the {@link PropertiesUtil#storeProperties(Properties, String, String)} method with null filename.
	 * Calls the storeProperties method with properties and a null filename
	 * and asserts that the return value is false.
	 */
	@Test
	public void testStorePropertiesWithNullFilename() {
		Properties properties = new Properties();
		properties.setProperty("key1", "value1");
		properties.setProperty("key2", "value2");

		boolean success = PropertiesUtil.storeProperties(properties, null, null);
		assertFalse(success);
	}

	/**
	 * Tests the {@link PropertiesUtil#storeProperties(Properties, String, String)} method with an empty filename.
	 * Calls the storeProperties method with properties and an empty filename
	 * and asserts that the return value is false.
	 */
	@Test
	public void testStorePropertiesWithEmptyFilename() {
		Properties properties = new Properties();
		properties.setProperty("key1", "value1");
		properties.setProperty("key2", "value2");

		boolean success = PropertiesUtil.storeProperties(properties, "", null);
		assertFalse(success);
	}

	/**
	 * Helper method to create a test properties file.
	 * Creates a properties object, sets some properties, and stores them in the specified filename.
	 *
	 * @param filename the name of the test properties file
	 */
	private void createTestPropertiesFile(String filename) {
		Properties properties = new Properties();
		properties.setProperty("key1", "value1");
		properties.setProperty("key2", "value2");

		try {
			// Store the properties to the test file
			PropertiesUtil.storeProperties(properties, filename, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
