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
package fr.sewatech.sewatoool.impress.exception;

/**
 * 
 * @author "Alexis Hassler (alexis.hassler@sewatech.org)"
 *
 */
public class ImpressWarning extends Exception {

	private static final long serialVersionUID = 1L;

	public ImpressWarning() {
	}

	public ImpressWarning(String message) {
		super(message);
	}

	public ImpressWarning(Throwable cause) {
		super(cause);
	}

	public ImpressWarning(String message, Throwable cause) {
		super(message, cause);
	}

}
