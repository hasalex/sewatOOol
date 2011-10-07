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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Decrit une table des matieres
 * 
 * @author alexis
 *
 */
public class TocDesc {

	private String title;

	private List<SlideDesc> allSlides;

	// Page d'index du sommaire (pour des sommaires multi-pages
	// private int tocIndex = 0;

	public TocDesc(List<SlideDesc> allSlides, String title) {
		this.title = title;
		this.allSlides = allSlides;
	}

	public TocDesc(List<SlideDesc> allSlides, String title, int index) {
		this(allSlides, title);
		setTocIndex(index);
	}

	public TocDesc(TocDesc toc) {
		this.allSlides = new ArrayList<SlideDesc>(toc.allSlides.size());
		this.allSlides.addAll(toc.allSlides);
		this.title = toc.title;
	}

	public List<SlideDesc> getAllSlides() {
		return allSlides;
	}

	public void setTocIndex(int tocIndex) {
		// this.tocIndex = tocIndex;
		title += " (suite)";
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Elimine les titres doublons ou inutiles
	 * 
	 */
	public void purge() {
		if (allSlides.isEmpty()) {
			return;
		}

		// On elimine la page de garde
		SlideDesc mainSlide = allSlides.remove(0);

		// On parcourt la liste de page pour eliminer les titres redondants,
		// le titre de fin et les titres vides
		SlideDesc previousSlide = null;
		Iterator<SlideDesc> iter = allSlides.iterator();
		while (iter.hasNext()) {
			SlideDesc slide = (SlideDesc) iter.next();
			if (slide.sameTitle(previousSlide) || slide.sameTitle(mainSlide) || slide.getTitle().trim().isEmpty()) {
				iter.remove();
			} else {
				previousSlide = slide;
			}
		}
	}
}
