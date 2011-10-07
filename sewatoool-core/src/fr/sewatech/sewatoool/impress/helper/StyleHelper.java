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

import com.sun.star.awt.FontSlant;
import com.sun.star.drawing.FillStyle;

public class StyleHelper {

	static public FillStyle getFillStyle(String name) throws Exception {
		final String[] values = { "none", "solid" };
		for (int i = 0; i < values.length; i++) {
			if (values[i].equalsIgnoreCase(name)) {
				return FillStyle.fromInt(i);
			}
		}
		throw new Exception("Pas bon");
	}

	static public FontSlant getFontPosture(String name) throws Exception {
		final String[] values = { "none", "oblique", "italic",
				"dontknow", "reverse_oblique", "reverse_italic" };
		for (int i = 0; i < values.length; i++) {
			if (values[i].equalsIgnoreCase(name)) {
				return FontSlant.fromInt(i);
			}
		}
		throw new Exception("Pas bon");
	}

	static public float getFontWeight(String name) throws Exception {
		if ("thin".equals(name)) {
			return 50f;
		} else if ("ultralight".equals(name)) {
			return 60f;
		} else if ("light".equals(name)) {
			return 75f;
		} else if ("semilight".equals(name)) {
			return 90f;
		} else if ("normal".equals(name)) {
			return 100f;
		} else if ("semibold".equals(name)) {
			return 110f;
		} else if ("bold".equals(name)) {
			return 150f;
		} else if ("ultrabold".equals(name)) {
			return 175f;
		} else if ("black".equals(name)) {
			return 200f;
		} else {
			throw new Exception("Pas bon");
		}
	}
}
