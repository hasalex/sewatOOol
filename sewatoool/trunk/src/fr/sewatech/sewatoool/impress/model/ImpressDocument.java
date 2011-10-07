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
package fr.sewatech.sewatoool.impress.model;

import static fr.sewatech.sewatoool.impress.helper.OOoHelper.unoCast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.star.awt.Point;
import com.sun.star.awt.Size;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XMasterPageTarget;
import com.sun.star.drawing.XMasterPagesSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.presentation.XPresentationPage;
import com.sun.star.style.TabAlign;
import com.sun.star.style.TabStop;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XSimpleText;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.util.XCloseable;
import com.sun.star.view.XPrintable;

import fr.sewatech.sewatoool.impress.exception.ImpressException;
import fr.sewatech.sewatoool.impress.exception.ImpressWarning;
import fr.sewatech.sewatoool.impress.helper.BundleHelper;
import fr.sewatech.sewatoool.impress.helper.StyleHelper;

/**
 * Classe de manipulation de présentations
 * 
 * @author alexis
 * 
 */
public class ImpressDocument {

	private static final Log logger = LogFactory.getLog(ImpressDocument.class);

	private static final String SHAPE_TYPE_TITLETEXT = "com.sun.star.presentation.TitleTextShape";

	private static final String SHAPE_TYPE_NOTES = "com.sun.star.presentation.NotesShape";

	private static final String SHAPE_TYPE_PAGE = "com.sun.star.presentation.PageShape";

	private static final String SHAPE_TYPE_OUTLINER = "com.sun.star.presentation.OutlinerShape";

	private XComponent component;

	private XDrawPages componentPages;

	private TabStop[] tabStops;

	/**
	 * Cree le helper<br/> Le document correspondant a l'URL est ouvert.
	 * 
	 * @param hidden
	 *            TODO
	 */
	public ImpressDocument(XComponent component) throws ImpressException {
		this.component = component;

		XDrawPagesSupplier xDrawPagesSupplier = unoCast(
				XDrawPagesSupplier.class, component);
		this.componentPages = xDrawPagesSupplier.getDrawPages();
	}

	public XComponent getComponent() {
		return component;
	}

	/**
	 * Construction d'un sommaire complet
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SlideDesc> listPages() throws ImpressException {
		logger.info("Construction de la liste de slides");
		try {
			List<SlideDesc> result = new ArrayList<SlideDesc>();

			// Parcours des pages du document
			for (int i = 0; i < componentPages.getCount(); i++) {
				Object objPage = componentPages.getByIndex(i);
				// Chaque page est une page !!!
				XDrawPage page = unoCast(XDrawPage.class, objPage);

				XPropertySet pageSet = unoCast(XPropertySet.class, page);
				Short pageNumber = (Short) pageSet.getPropertyValue("Number");

				// Shapes de titre
				XShape shape = findShapeByType(page, SHAPE_TYPE_TITLETEXT);

				if (shape != null) {
					XSimpleText xSimpleText = unoCast(XSimpleText.class, shape);

					// Recherche du nom de la page maitresse (= style de
					// page)
					XMasterPageTarget xMasterPageTarget = unoCast(
							XMasterPageTarget.class, page);
					XDrawPage xMasterPage = xMasterPageTarget.getMasterPage();
					XPropertySet masterSet = unoCast(XPropertySet.class,
							xMasterPage);

					// Ajout de la page a la liste de resultat
					try {
						result.add(new SlideDesc(xSimpleText.getString(),
								pageNumber, (String) masterSet
										.getPropertyValue("LinkDisplayName")));
					} catch (ImpressWarning e) {
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new ImpressException(e);
		}

	}

	public int addSlide(int page, String title, String style)
			throws ImpressException {
		XDrawPage newPage = componentPages.insertNewByIndex(page);

		XMasterPageTarget xMasterPageTarget = unoCast(XMasterPageTarget.class,
				newPage);
		xMasterPageTarget.setMasterPage(findMasterPage(style));

		return componentPages.getCount();
	}

	/**
	 * Recherche une page maitresse par son nom
	 * 
	 * @param style
	 * @return page maitresse
	 * @throws ImpressException
	 */
	private XDrawPage findMasterPage(String style) throws ImpressException {
		try {
			XMasterPagesSupplier xMasterPageSupplier = unoCast(
					XMasterPagesSupplier.class, component);
			XDrawPages xMasterPages = xMasterPageSupplier.getMasterPages();
			for (int i = 0; i < xMasterPages.getCount(); i++) {
				XDrawPage xMasterPage = unoCast(XDrawPage.class, xMasterPages
						.getByIndex(i));
				XPropertySet masterSet = unoCast(XPropertySet.class,
						xMasterPage);

				if (style.equals(masterSet.getPropertyValue("LinkDisplayName"))) {
					return xMasterPage;
				}
			}
			return null;
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Recherche une page par son numero
	 * 
	 * @param index
	 * @return
	 * @throws ImpressException
	 */
	public XDrawPage findPageByIndex(int index) throws ImpressException {
		try {
			return unoCast(XDrawPage.class, componentPages.getByIndex(index));
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Recherche une page de notes par son numero
	 * 
	 * @param index
	 * @return
	 * @throws ImpressException
	 */
	public XDrawPage findNotesPageByIndex(int index) throws ImpressException {
		XDrawPage page = findPageByIndex(index);
		XPresentationPage xPresentationPage = unoCast(XPresentationPage.class,
				page);
		return xPresentationPage.getNotesPage();

	}

	/**
	 * Recherche une page par son nom
	 * 
	 * @param name
	 * @return
	 * @throws ImpressException
	 */
	public int findPageByName(String name) throws ImpressException {
		try {
			for (int i = 0; i < componentPages.getCount(); i++) {
				Object objPage = componentPages.getByIndex(i);

				XPropertySet xPageProperty = unoCast(XPropertySet.class,
						objPage);
				if (name.equalsIgnoreCase((String) xPageProperty
						.getPropertyValue("LinkDisplayName"))) {
					return i;
				}
			}
			return 0;
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Ecrit le contenu d'une table des matieres dans une page
	 * 
	 * @param sommaireIndex
	 *            Numero de page de la table des matieres
	 * @param toc
	 *            Table des matieres
	 * @throws ImpressException
	 */
	public void writeTOC(int sommaireIndex, TocDesc toc)
			throws ImpressException {
		BundleHelper bundle = BundleHelper.getInstance();

		XDrawPage notesPage = findNotesPageByIndex(sommaireIndex);

		// Suppression du shape principal
		notesPage.remove(findShapeByType(notesPage, SHAPE_TYPE_PAGE));
		// Mise en forme du shape de notes
		XShape notesShape = findShapeByType(notesPage, SHAPE_TYPE_NOTES);
		int xmargin = bundle.getTocXMargin() * 100;
		int ymargin = bundle.getTocYMargin() * 100;
		try {
			notesShape.setPosition(new Point(xmargin, ymargin));
			notesShape.setSize(new Size(21000 - 2 * xmargin,
					29700 - 2 * ymargin));
			XPropertySet xNotesShapeProps = unoCast(XPropertySet.class,
					notesShape);
			xNotesShapeProps.setPropertyValue("FillStyle", StyleHelper
					.getFillStyle(bundle.getTocFillStyle()));
			xNotesShapeProps.setPropertyValue("FillColor", bundle
					.getTocFillColor());
			xNotesShapeProps.setPropertyValue("ZOrder", 999);

		} catch (Exception e) {
			logger.warn("Probleme non prévu, mais pas forcément grace", e);
		}

		if (logger.isTraceEnabled()) {
			traceShapes(notesPage);
		}

		// Zone de texte des notes
		XText xNoteText = unoCast(XText.class, notesShape);
		XTextCursor cursor = xNoteText.createTextCursor();
		cursor.getStart();

		XPropertySet xCursorProps = unoCast(XPropertySet.class, cursor);
		tabStops = new TabStop[2];
		tabStops[0] = new TabStop(xmargin, TabAlign.LEFT, ',', ' ');
		tabStops[1] = new TabStop(21000 - 2 * xmargin - 2000, TabAlign.RIGHT,
				',', '.');
		// Remarque : je ne sais plus comment ça marche...

		// Titre de la table de matieres
		try {
			applyTextStyle(xCursorProps, "preference.toc.notes.title");
			xNoteText.insertString(cursor, toc.getTitle(), true);
			xNoteText.insertControlCharacter(cursor,
					ControlCharacter.PARAGRAPH_BREAK, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator<SlideDesc> iter = toc.getAllSlides().iterator();
		int maxSize = (29700 - 2 * ymargin) / 40 - 18; // en nombre de points
		int[] heights = bundle.getTocFontHeight();
		int textSize = 2 * bundle.getTocTitleHeight();

		while (iter.hasNext() && (textSize < maxSize)) {
			SlideDesc slide = (SlideDesc) iter.next();
			textSize += heights[slide.getLevel() - 1];
			addContentEntry(xNoteText, cursor, slide);
			iter.remove();
		}
	}

	/**
	 * Ecrit le plan du cours
	 * 
	 * @param planIndex
	 *            Numero de page du plan
	 * @param toc
	 *            Table des matieres
	 * @throws ImpressException
	 */
	public void writeShortTOC(int planIndex, TocDesc toc)
			throws ImpressException {

		try {
			BundleHelper bundle = BundleHelper.getInstance();

			XDrawPage planPage = findPageByIndex(planIndex);

			// Shape de titre
			XShape titleShape = findShapeByType(planPage, SHAPE_TYPE_TITLETEXT);
			if (titleShape != null) {
				XSimpleText titleText = unoCast(XSimpleText.class, titleShape);
				titleText.setString(bundle.getShortTocTitle());
			}

			// Shape principal
			XShape mainShape = findShapeByType(planPage, SHAPE_TYPE_OUTLINER);
			if (mainShape != null) {
				XSimpleText mainText = unoCast(XSimpleText.class, mainShape);

				XTextCursor cursor = mainText.createTextCursor();
				cursor.getStart();

				XPropertySet xCursorProps = unoCast(XPropertySet.class, cursor);
				TabStop[] tabStops = new TabStop[1];
				tabStops[0] = new TabStop(bundle.getShortTocTabulation(),
						TabAlign.RIGHT, ',', '.');
				xCursorProps.setPropertyValue("ParaTabStops", tabStops);

				StringBuilder planBuilder = new StringBuilder();
				String newLine = "";
				for (SlideDesc slide : toc.getAllSlides()) {
					if (slide.getLevel() == 1) {
						planBuilder.append(newLine);
						planBuilder.append(slide.getTitle());
						planBuilder.append("\t");
						planBuilder.append(slide.getPage());
						newLine = "\n";
					}
				}
				mainText.setString(planBuilder.toString());

			}

			// Shape de notes
			XPresentationPage xPresentationPage = unoCast(
					XPresentationPage.class, planPage);
			XDrawPage planNotesPage = xPresentationPage.getNotesPage();
			XShape notesShape = findShapeByType(planNotesPage, SHAPE_TYPE_NOTES);
			if (notesShape != null) {
				XSimpleText notesText = unoCast(XSimpleText.class, notesShape);
				notesText.setString(bundle.getShortTocNotesText());
			}

		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Recherche un shape dans une page, par son type
	 * 
	 * @param page
	 *            Page dans laquelle on recherche le shape
	 * @param shapeType
	 *            Type de shape (exemple : "com.sun.star.presentation.PageShape"
	 *            ou "com.sun.star.presentation.NotesShape")
	 * @return Le shape trouve, ou null en cas de probl�me de recherche ou si
	 *         aucun shape ne correspond au type dans la page
	 * @throws ImpressException
	 */
	private XShape findShapeByType(XDrawPage page, String shapeType)
			throws ImpressException {
		try {
			for (int i = 0; i < page.getCount(); i++) {
				XShape shape = unoCast(XShape.class, page.getByIndex(i));
				if (shapeType.equals(shape.getShapeType())) {
					return shape;
				}
			}
			return null;
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Trace la liste des shapes d'une page<br>
	 * Utilisé uniquement pour le debug
	 * 
	 * @param page
	 * @return
	 * @throws ImpressException
	 */
	private XShape traceShapes(XDrawPage page) throws ImpressException {
		try {
			for (int i = 0; i < page.getCount(); i++) {
				XShape shape = unoCast(XShape.class, page.getByIndex(i));
				logger.trace(shape.getShapeType() + " " + shape.getPosition().X);
			}
			return null;
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Ajoute une ligne a la table des matieres
	 * 
	 * @param xText
	 * @param xCursor
	 * @param desc
	 * @throws ImpressException
	 */
	private void addContentEntry(XText xText, XTextCursor xCursor,
			SlideDesc desc) throws ImpressException {

		try {
			XPropertySet xCursorProps = unoCast(XPropertySet.class, xCursor);

			xCursor.getEnd();
			xCursorProps.setPropertyValue("ParaTabStops", tabStops);

			xText.insertControlCharacter(xCursor,
					com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
			applyTextStyle(xCursorProps, "preference.toc.notes.toc"
					+ desc.getLevel());
			char[] chars = new char[desc.getLevel() - 1];
			Arrays.fill(chars, '\t');
			xText.insertString(xCursor, new String(chars) + desc.getTitle(),
					false);
			xText.insertString(xCursor, "\t" + desc.getPage(), true);
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Applique un style de texte
	 * 
	 * @param xCursorProps
	 *            proprietes du curseur de texte
	 * @param prefix
	 *            prefixe des cles dans le fichier properties
	 * @throws Exception
	 */
	private void applyTextStyle(XPropertySet xCursorProps, String prefix)
			throws ImpressException {
		try {
			BundleHelper bundle = BundleHelper.getInstance();
			xCursorProps.setPropertyValue("CharFontName", bundle
					.getFont(prefix));
			xCursorProps.setPropertyValue("CharHeight", bundle
					.getCharHeight(prefix));
			xCursorProps.setPropertyValue("CharColor", bundle
					.getCharColor(prefix));
			xCursorProps.setPropertyValue("CharPosture", StyleHelper
					.getFontPosture(bundle.getCharPosture(prefix)));
			xCursorProps.setPropertyValue("CharWeight", StyleHelper
					.getFontWeight(bundle.getCharWeight(prefix)));
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Compte le nombre de pages dans le document
	 * 
	 * @return Nombre de pages
	 */
	public int countPages() {
		return componentPages.getCount();
	}

	/**
	 * Ferme le document
	 */
	public void close() {
		try {
			XCloseable xCloseable = unoCast(XCloseable.class, component);
			xCloseable.close(true);

			component.dispose();
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Sauvegarde le document
	 */
	public void save() {
		try {
			XStorable xStorable = unoCast(XStorable.class, component);
			xStorable.storeAsURL(xStorable.getLocation(), null);
			// Attention : ne pas utiliser la methode store, elle verole le
			// fichier
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	/**
	 * Impime le document (NON TESTE)
	 * 
	 * @param printer
	 */
	public void print(String printer) {
		try {
			XPrintable xPrintable = unoCast(XPrintable.class, component);
			PropertyValue[] printerDesc = new PropertyValue[1];
			printerDesc[0] = new PropertyValue();
			printerDesc[0].Name = "Name";
			printerDesc[0].Value = printer;
			xPrintable.setPrinter(printerDesc);

			PropertyValue[] printOpts = new PropertyValue[1];
			printOpts[0] = new PropertyValue();
			printOpts[0].Name = "Wait";
			printOpts[0].Value = true;
			xPrintable.print(printOpts);
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

	public void exportPdf() {
		try {
			XStorable xStorable = unoCast(XStorable.class, component);

			// Set properties for conversions
			PropertyValue[] convertProperties = new PropertyValue[2];

			convertProperties[0] = new PropertyValue();
			convertProperties[0].Name = "Overwrite";
			convertProperties[0].Value = true;

			convertProperties[1] = new PropertyValue();
			convertProperties[1].Name = "FilterName";
			convertProperties[1].Value = "writer_pdf_Export";

			String docLocation = xStorable.getLocation();
			String pdfLocation = docLocation.substring(0, docLocation
					.lastIndexOf('.'))
					+ ".pdf";
			xStorable.storeToURL(pdfLocation, convertProperties);
		} catch (Exception e) {
			throw new ImpressException(e);
		}
	}

}
