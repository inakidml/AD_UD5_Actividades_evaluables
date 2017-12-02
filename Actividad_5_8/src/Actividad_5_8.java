import java.io.File;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

public class Actividad_5_8 {

	private static Collection col = null; // Colecci�n;
	private static String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/"; // URI colecci�n
	private static String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
	private static String usu = "admin"; // Usuario
	private static String usuPwd = "admin"; // Clave

	public static void main(String[] args) {

		crearColeccion("GIMNASIO");

		addXML("./ColeccionGimnasio/actividades_gim.xml");
		addXML("./ColeccionGimnasio/socios_gim.xml");
		addXML("./ColeccionGimnasio/uso_gimnasio.xml");

	}

	public static void crearColeccion(String nombre) {
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la BD
			DatabaseManager.registerDatabase(database); // Registro del driver
			col = DatabaseManager.getCollection(URI, usu, usuPwd);

			// creamos colección GIMNASIO
			CollectionManagementService cserv = (CollectionManagementService) col
					.getService("CollectionManagementService", "1.0");
			cserv.createCollection(nombre);
			col.close();
			System.out.println("Colección " + nombre + " creada");
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}

	}

	public static void addXML(String ruta) {
		// vamos a añadir los xml
		URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/GIMNASIO/"; // URI colecci�n
		try {
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
		} catch (XMLDBException e1) {
			e1.printStackTrace();
		}

		File archivo = new File(ruta);
		if (!archivo.canRead()) {
			System.out.println("ERROR AL LEER EL FICHERO");
		} else {
			try {
				Resource nuevoRecurso;
				nuevoRecurso = col.createResource(archivo.getName(), "XMLResource");
				nuevoRecurso.setContent(archivo); // Asignamos el archivo
				col.storeResource(nuevoRecurso); // Lo almacenamos en la colección
				col.close();
				System.out.println("Recurso '" + ruta + "' añadido");
			} catch (XMLDBException e) {
				e.printStackTrace();
			}
		}
	}
	public static void cuotaSocios(){}

}
