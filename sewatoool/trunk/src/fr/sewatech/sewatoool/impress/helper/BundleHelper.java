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
package fr.sewatech.sewatoool.impress.helper;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author "Alexis Hassler (alexis.hassler@sewatech.org)"
 *
 */
public class BundleHelper {
	private static BundleHelper instance;

	public static BundleHelper getInstance() {
		if (instance == null) {
			instance = new BundleHelper();
			instance.bundle = ResourceBundle.getBundle("sewatoool");
		}
		return instance;
	}

	private ResourceBundle bundle;
	private String lastCharColor;

	public String getExcludedPage() {
		return bundle.getString("preference.toc.page.exclude");
	}

	public String getDocumentUrl() {
		return bundle.getString("document.url");
	}

	public String getTocPageName() {
		return bundle.getString("preference.toc.page.name");
	}

	public String getTocPageStyle() {
		return bundle.getString("preference.toc.page.style");
	}

	public String getTocTitle() {
		return bundle.getString("preference.toc.notes.title.text");
	}

	public int getTocXMargin() {
		return Integer.parseInt(bundle
				.getString("preference.toc.notes.xmargin"));
	}

	public int getTocYMargin() {
		return Integer.parseInt(bundle
				.getString("preference.toc.notes.ymargin"));
	}

	public String getTocFillStyle() {
		return bundle.getString("preference.toc.notes.fill.style");
	}

	public String getTocFillColor() {
		return bundle.getString("preference.toc.notes.fill.color");
	}

	public int[] getTocFontHeight() {
		int[] heights = new int[2];
		heights[0] = Integer.parseInt(bundle
				.getString("preference.toc.notes.toc1.height"));
		heights[1] = Integer.parseInt(bundle
				.getString("preference.toc.notes.toc2.height"));
		return heights;
	}

	public int getTocTitleHeight() {
		return Integer.parseInt(bundle
				.getString("preference.toc.notes.title.height"));
	}

	public String getFont(String prefix) {
		try {
			return bundle.getString(prefix + ".font");
		} catch (MissingResourceException e) {
			return bundle.getString("preference.toc.notes.default.font");
		}
	}

	/** 
	 * Charge la taille de police pour un niveau<br/>
	 * @param prefix specifie le niveau
	 * @return 
	 */
	public int getCharHeight(String prefix) {
		try {
			return Integer.parseInt(bundle.getString(prefix + ".height"));
		} catch (MissingResourceException e) {
			return Integer.parseInt(bundle.getString("preference.toc.notes.default.height"));
		}
	}

	/**
	 * Charge la couleur pour un niveau<br/>
	 * Si aucune couleur n'est specifiee, on reprend la precedente 
	 * 
	 * @param prefix
	 * @return
	 */
	public String getCharColor(String prefix) {
		try {
			lastCharColor = bundle.getString(prefix + ".color");
		} catch (MissingResourceException e) {
		}
		return lastCharColor;
	}
	
	public String getCharPosture(String prefix) {
		try {
			return bundle.getString(prefix + ".posture");
		} catch (MissingResourceException e) {
			return bundle.getString("preference.toc.notes.default.posture");
		}
	}

	public String getCharWeight(String prefix) {
		try {
			return bundle.getString(prefix + ".weight");
		} catch (MissingResourceException e) {
			return bundle.getString("preference.toc.notes.default.weight");
		}
	}
	
	public String getShortTocPageName() {
		return bundle.getString("preference.shorttoc.page.name");
	}

	public String getShortTocPageStyle() {
		return bundle.getString("preference.shorttoc.page.style");
	}

	public String getShortTocTitle() {
		return bundle.getString("preference.shorttoc.page.title");
	}

	public String getShortTocNotesText() {
		return bundle.getString("preference.shorttoc.notes.text");
	}

	public int getShortTocTabulation() {
		return (int) (1000 * Float.parseFloat(bundle.getString("preference.shorttoc.text.tabulation")));
	}
}
