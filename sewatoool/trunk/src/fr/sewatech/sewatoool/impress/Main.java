/*
 *  This file is part of sewatOOol.
 *  
 *  sewatOOol is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3
 *  as published by the Free Software Foundation.
 *  
 *  sewatOOol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with sewatOOol; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *  
 * 	Copyright 2008 sewatech
 * 
 */
package fr.sewatech.sewatoool.impress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.sewatech.sewatoool.impress.model.ImpressDocument;
import fr.sewatech.sewatoool.impress.service.ImpressService;

public class Main {

	private static final Log logger = LogFactory.getLog(Main.class);

	private static final String MESSAGE_WRONG_ARGS = "Mauvais usage : essayez avec l'option --help pour plus d'informations";

	/**
	 * @param args cf. usage.txt
	 * 
	 * @author "Alexis Hassler (alexis.hassler@sewatech.org)"
	 */
	public static void main(String[] args) {
		// Analyse des arguments
		if (args.length == 0) {
			message(MESSAGE_WRONG_ARGS);
			logger.warn("Probleme d'arguments : pas d'argument");
		}

		Map<String, String> arguments = new HashMap<String, String>();
		String argName = null;
		String documentLocation = null;
		for (String arg : args) {
			if ("--".equals(arg.substring(0, 2))) {
				argName = arg.substring(2);
				arguments.put(argName, "");
			} else if (argName != null) {
				arguments.put(argName, arg);
				argName = null;
			} else if (documentLocation == null) {
				documentLocation = arg;
			} else {
				message(MESSAGE_WRONG_ARGS);
				logger.warn("Probleme d'arguments : 2 fois le nom du fichier");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Liste des arguments pris en compte : ");
			for (Entry<String, String> option : arguments.entrySet()) {
				logger.debug("  Argument " + option.getKey() + "="
						+ option.getValue());
			}
		}

		if (arguments.containsKey("help")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Affichage de l'aide");
			}
			doHelp();
		}

		try {
			ImpressService service = new ImpressService();

			ImpressDocument document = service.loadDocument(documentLocation,
					arguments.containsKey("hidden"));

			doToc(arguments, service, document);

			doPdf(arguments, service, document);

			if (!arguments.containsKey("no-save")) {
				service.save(document);
			}
			if (!arguments.containsKey("no-close")) {
				service.close(document);
			}

		} catch (Throwable e) {
			logger.error("Il y a un probleme...", e);
		} finally {
			System.exit(0);
		}
	}

	private static void doPdf(Map<String, String> arguments,
			ImpressService service, ImpressDocument document) {
		String argPdf = arguments.get("pdf");
		if ("both".equals(argPdf) || "all".equals(argPdf)) {
			service.print(document, "pdf-printer");
			service.exportPdf(document);
		} else if ("print".equals(argPdf)) {
			service.print(document, "pdf-printer");
		} else if ("slides".equals(argPdf)) {
			service.exportPdf(document);
		} else if (argPdf != null) {
			logger.warn("Probleme d'arguments : " + argPdf
					+ " n'est pas une bonne valeur pour l'argument --pdf");
		}
	}

	private static void doToc(Map<String, String> arguments,
			ImpressService service, ImpressDocument document) throws Exception {
		String argToc = arguments.get("toc");
		boolean longToc = false;
		boolean shortToc = false;

		if ("both".equals(argToc)) {
			longToc = true;
			shortToc = true;
		} else if ("short".equals(argToc)) {
			longToc = false;
			shortToc = true;
		} else if ("long".equals(argToc)) {
			longToc = true;
			shortToc = false;
		} else if (argToc != null) {
			logger.warn("Problème d'arguments : " + argToc
					+ " n'est pas une bonne valeur pour l'argument --toc");
		}
		if (longToc || shortToc) {
			service.generateToc(document, longToc, shortToc);
		}
	}

	/**
	 * Affiche l'aide en ligne
	 */
	private static void doHelp() {
		try {
			ClassLoader loader = Main.class.getClassLoader();
			InputStream usageStream = loader.getResourceAsStream("usage.txt");
			BufferedReader usageReader = new BufferedReader(
					new InputStreamReader(usageStream));
			String line;
			do {
				line = usageReader.readLine();
				message(line == null ? "" : line);

			} while (line != null);
		} catch (IOException e) {
			logger.warn("Probleme pour l'aide", e);
		} finally {
			System.exit(0);
		}
	}

	/**
	 * Affiche un message pour les utilisateurs
	 * 
	 * @param message
	 */
	private static void message(String message) {
		System.out.println(message);
	}

}
