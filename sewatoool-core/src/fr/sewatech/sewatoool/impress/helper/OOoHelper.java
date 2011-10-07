package fr.sewatech.sewatoool.impress.helper;

import com.sun.star.uno.UnoRuntime;

/**
 * Ensemble de methodes utilitaires pour UNO
 * 
 * @author "Alexis Hassler (alexis.hassler@sewatech.org)"
 * 
 */
public class OOoHelper {

	/**
	 * Transtypage de type UNO, avec utilisation de generics
	 * 
	 * @param clazz Type cible
	 * @param object objet à transtyper
	 * @return objet dans son nouveau type
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unoCast(Class<T> clazz, Object object) {
		// Je sais que ce type de transtypage n'est pas conseille,
		// mais c'est tellement pratique...
		return (T) UnoRuntime.queryInterface(clazz, object);
	}

}
