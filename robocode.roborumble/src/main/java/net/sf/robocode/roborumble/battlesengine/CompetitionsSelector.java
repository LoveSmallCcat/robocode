/*
 * Copyright (c) 2001-2023 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package net.sf.robocode.roborumble.battlesengine;


import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.parameters;
import static net.sf.robocode.roborumble.util.PropertiesUtil.getProperties;
import static net.sf.robocode.roborumble.util.PropertiesUtil.storeProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import net.sf.robocode.repository.CodeSizeCalculator;
import net.sf.robocode.roborumble.util.PropertiesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


/**
 * This class is used to control which competitions a robot is allowed to
 * participate in.
 * Reads a file with the battles to be runned and outputs the results in
 * another file.
 * Controlled by properties files.
 *
 * @author Albert Perez (original)
 * @author Flemming N. Larsen (contributor)
 */
public class CompetitionsSelector {
	private String repository;
	private String sizesfile;
	private final Properties sizes;

	public CompetitionsSelector(String sizesfile, String repository) {
		this.repository = repository;
		// open sizes file
		this.sizesfile = sizesfile;

		sizes = getProperties(sizesfile);
	}

	public Boolean checkCompetitorForSize(String botName, long maxSize) {
		String name = botName.replace(' ', '_');

		// Read sizes
		long codeSize = Long.parseLong(sizes.getProperty(name, "0"));

		// Find out the size if not in the file
		boolean fileNeedsUpdate = false;

		if (codeSize == 0) {
			File f = new File(repository + name + ".jar");
			if (f.exists()) {
				fileNeedsUpdate = true; // Bug-362

				Integer jarFileCodeSize = CodeSizeCalculator.getJarFileCodeSize(f);
				if (jarFileCodeSize != null) {
					codeSize = jarFileCodeSize;
					sizes.setProperty(name, Long.toString(codeSize));
				}
			}
		}

		if (codeSize > 0) {
			// If the file needs update, then save the file
			if (fileNeedsUpdate) {
				storeProperties(sizes, sizesfile, "Bots code size");
			}

			// Check the code size
			return (codeSize < maxSize); // Bug-362
		} else {
			return null;
		}
	}

	public boolean checkCompetitorsForSize(String bot1, String bot2, long maxsize) {
		return checkCompetitorForSize(bot1, maxsize) && checkCompetitorForSize(bot2, maxsize);
	}

	/**
	 * Tests the getProperties method of the PropertiesUtil class.
	 * It reads properties from the specified file and asserts that the properties object is not null.
	 */
	@Test
	public void testGetProperties() {
		// Provide the path to the sizes file
		String sizesFile = "./path/to/sizesfile.properties";

		// Call the getProperties method
		Properties properties = PropertiesUtil.getProperties(sizesFile);

		// Assert that the properties object is not null
		assertNotNull(properties);

		// Perform further assertions on the properties object if needed
		// For example, you can check if specific properties are present or have expected values
		Assertions.assertTrue(properties.containsKey("property1"));
		Assertions.assertEquals("value1", properties.getProperty("property1"));
	}
}
