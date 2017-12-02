
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;


public class Actividad_5_7_1 {

	public static void main(String[] args) throws XMLDBException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int dep =-1;
		while (dep<0) {
			System.out.print("Introduce el número de departamento: ");
			try {
				dep =Integer.parseInt( br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("introduce un valor válido");			
			}			
		}
		verEmpleadosDep(dep);
	}

	public static void verEmpleadosDep(int dep) throws XMLDBException {
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		Collection col = null; // Colección
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colección
		String usu = "admin"; // Usuario
		String usuPwd = "admin"; // Clave
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la BD
			DatabaseManager.registerDatabase(database); // Registro del driver
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			if (col == null)
				System.out.println(" *** LA COLECCION NO EXISTE. ***");
			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			ResourceSet result = servicio.query("for $em in /EMPLEADOS/EMP_ROW[DEPT_NO=" + dep + "] return $em");
			System.out.println("Se han obtenido " + result.getSize() + " elementos.");
			// recorrer los datos del recurso.
			ResourceIterator i;
			i = result.getIterator();
			if (!i.hasMoreResources())
				System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println((String) r.getContent());
			}
			col.close(); // cerramos
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}
	}// FIN verempleados

}
