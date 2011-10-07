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
 */
package fr.sewatech.sewatoool.impress.service;

import static fr.sewatech.sewatoool.impress.helper.OOoHelper.unoCast;

import java.io.File;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uri.ExternalUriReferenceTranslator;

import fr.sewatech.sewatoool.impress.exception.ImpressException;
import fr.sewatech.sewatoool.impress.helper.BundleHelper;
import fr.sewatech.sewatoool.impress.model.ImpressDocument;
import fr.sewatech.sewatoool.impress.model.TocDesc;

public class ImpressService {

	private static final Log logger = LogFactory.getLog(ImpressService.class);

	public ImpressService() throws Exception {
	}

	public ImpressDocument loadDocument(String location, boolean hidden) {
		try {

			// get the remote office component context
			XComponentContext xContext = Bootstrap.bootstrap();
			logger.trace("Bootstrap OK");
			XMultiComponentFactory xMCF = xContext.getServiceManager();
			logger.trace("ServiceManager OK");
			Object desktop = xMCF.createInstanceWithContext(
					"com.sun.star.frame.Desktop", xContext);
			XComponentLoader loader = unoCast(XComponentLoader.class, desktop);

			String internalLocation = getSupportedLocation(location, xContext);
			// internalLocation = correctLocation;
			if (logger.isDebugEnabled()) {
				logger.debug("Chargement du fichier : " + internalLocation);
			}

			String mode;
			if (hidden) {
				mode = "_hidden";
			} else {
				mode = "_default";
			}
			XComponent component = loader.loadComponentFromURL(
					internalLocation, mode, 0, null);

			if (component == null) {
				throw new ImpressException("Probleme de chargement du fichier "
						+ internalLocation);
			}

			return new ImpressDocument(component);

		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Transforme un chemin d'acc�s au fichier, au format classique, en chemin
	 * d'acc�s compatible OOo
	 * 
	 * @param requestedlocation
	 * @param xContext
	 * @return
	 */
	private String getSupportedLocation(String requestedlocation,
			XComponentContext xContext) {
		// Construit correctement le chemin du fichier
		URI uri = URI.create(requestedlocation);
		File file = new File(uri.getPath());
		String path = file.getAbsolutePath();
		String correctLocation;

		if (System.getProperty("os.name").startsWith("Windows")) {
			// Format Windows
			correctLocation = "file:/" + path.replace('\\', '/');
		} else {
			// Format Unix
			correctLocation = "file://" + path;
		}

		logger.trace("Tentative d'ouverture du fichier " + correctLocation);
		// Resoud des problemes d'encodage
		String internalLocation = ExternalUriReferenceTranslator.create(
				xContext).translateToInternal(correctLocation);
		return internalLocation;
	}

	/**
	 * Genere la table des matieres<br/> <br/> - on etablit la liste des
	 * pages<br/> - Recherche si une page de sommaire existe, en cree une
	 * sinon<br/> - Ecrit la table des matieres dans la page<br/> - Deborde
	 * eventuellement sur une autre page
	 * 
	 * @param document
	 * @param longTOC
	 * @param shortTOC
	 * 
	 * @throws Exception
	 */
	public void generateToc(ImpressDocument document, boolean longTOC,
			boolean shortTOC) throws Exception {
		BundleHelper bundle = BundleHelper.getInstance();
		TocDesc toc = new TocDesc(document.listPages(), bundle.getTocTitle());
		toc.purge();

		TocDesc shortToc = new TocDesc(toc);

		if (longTOC) {
			logger.info("Génération de la table des matières complète");
			int slideNumber = document.findPageByName(bundle.getTocPageName());

			int tocPageNumber = document.countPages();

			if (slideNumber == 0) {
				slideNumber = tocPageNumber;
				document.addSlide(tocPageNumber, toc.getTitle(), bundle
						.getTocPageStyle());
			}

			document.writeTOC(slideNumber, toc);

			if (toc.getAllSlides().size() > 0) {
				document.addSlide(slideNumber + 1, toc.getTitle() + "1", bundle
						.getTocPageStyle());
				toc.setTocIndex(1);
				document.writeTOC(slideNumber + 1, toc);
			}
		}

		if (shortTOC) {
			logger.info("Génération de la table des matières simplifiée");
			int shortTocNumber = document.findPageByName(bundle
					.getShortTocPageName());
			if (shortTocNumber > 0) {
				document.writeShortTOC(shortTocNumber, shortToc);
			}
		}
	}

	public void exportPdf(ImpressDocument document) {
		document.exportPdf();
	}

	public void print(ImpressDocument document, String printer) {
		document.print(printer);
	}

	public void save(ImpressDocument document) {
		document.save();
	}

	public void close(ImpressDocument document) {
		document.close();
	}

}
