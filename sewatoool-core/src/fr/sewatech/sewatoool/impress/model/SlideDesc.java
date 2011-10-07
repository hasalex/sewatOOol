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
package fr.sewatech.sewatoool.impress.model;

import fr.sewatech.sewatoool.impress.exception.ImpressWarning;
import fr.sewatech.sewatoool.impress.helper.BundleHelper;

public class SlideDesc {
	private String style;

	private String title;

	private int page;

	public SlideDesc(String text, int page, String style) throws ImpressWarning {
		super();

		// Supprimer les retours a la ligne
		// Remarque : evolution possible = couper apres le retour a la ligne
		text = text.replaceAll("\n", " ");
		// Supprimer les tabulations
		text = text.replaceAll("\t", " ");

		if (text.equals(BundleHelper.getInstance().getExcludedPage())) {
			throw new ImpressWarning("Texte exclu");
		} else if (text == null || "".equals(text)) {
			throw new ImpressWarning("text vide");
		} else {
			this.title = text.trim();
		}

		if (style == null || "".equals(style)) {
			throw new ImpressWarning("style vide");
		} else {
			this.style = style;
		}
		this.page = page;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String text) {
		this.title = text;
	}

	public int getLevel() {
		if ("Titre".equalsIgnoreCase((getStyle()))) {
			return 1;
		} else {
			return 2;
		}
	}

	public boolean sameTitle(SlideDesc slide) {
		if (slide != null) {
			return (this.style.equals(slide.style) && this.title
					.equals(slide.title));
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return page + " - " + title + " (" + style + ")";
	}
}
