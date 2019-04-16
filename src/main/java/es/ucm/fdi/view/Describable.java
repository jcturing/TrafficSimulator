package es.ucm.fdi.view;

import java.util.Map;

/**
 *
 * Interfaz utilizada para la creaccion de las tablas
 *
 */
public interface Describable {
/**
* @param out - a map to fill in with key- value pairs
* @return the passed- in map, with all fields filled out.
*/
	Map<String, String> describe();
}