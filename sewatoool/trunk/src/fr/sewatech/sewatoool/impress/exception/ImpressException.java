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
public class ImpressException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ImpressException() {
		super();
	}

	public ImpressException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImpressException(String message) {
		super(message);
	}

	public ImpressException(Throwable cause) {
		super(cause);
	}
	
	
}
