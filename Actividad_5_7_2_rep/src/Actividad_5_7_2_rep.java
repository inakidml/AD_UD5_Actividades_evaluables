import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XPathQueryService;

public class Actividad_5_7_2_rep {

	public static void main(String[] args) throws IOException {
		insertarDep();
		borrarDep();
		modificarDep();
		
		System.out.println("El archivo se queda así: ");
		verDepts();
		System.out.println("FIN");

	}

	public static void insertarDep() throws IOException {
		// Mostramos todos los depts
		verDepts();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// TODO leer� de teclado un departamento, su nombre y su localidad, y deber�
		// a�adirlo al documento.
		// Si el c�digo de departamento existe visualiza que no se puede insertar porque
		// ya existe.

		System.out.println("Vamos a insertar un departamento");

		int dept_no = -1;
		while (dept_no < 0) {
			System.out.print("Introduce el n�mero de departamento: ");
			try {
				dept_no = Integer.parseInt(br.readLine());
				if (existeDept(dept_no)) {
					System.out.println("Ese departamento ya existe!");
					dept_no = -1;
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("introduce un valor v�lido");
			}
		}
		System.out.print("Introduce el nombre:");
		String nombre = br.readLine();
		System.out.print("Introduce la ciudad:");
		String localidad = br.readLine();

		/////////////////////

		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		Collection col = null; // Colecci�n
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colecci�n
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
			ResourceSet result = servicio
					.query("update insert " + "<DEP_ROW>" + "<DEPT_NO>" + dept_no + "</DEPT_NO>" + "<DNOMBRE>" + nombre
							+ "</DNOMBRE>" + "<LOC>" + localidad + "</LOC>" + "</DEP_ROW>" + "into /departamentos");
			col.close(); // cerramos

		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}
	}

	public static void borrarDep() {

		// Mostramos todos los depts
		verDepts();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// TODO leer� de teclado un departamento y lo borrar� si existe, si no existe
		// visualizar� que no se puede borrar porque no existe.
		int dep = -1;
		while (dep < 0) {
			System.out.println("Borrado de un departamento");
			System.out.print("Introduce el n�mero de departamento: ");
			try {
				dep = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("introduce un valor v�lido");
			}
		}
		if (!existeDept(dep)) {
			System.out.println("No existe el departamento");
		} else {
			String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
			Collection col = null; // Colecci�n
			String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colecci�n
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
				ResourceSet result = servicio.query("update delete /departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
				col.close(); // cerramos

			} catch (Exception e) {
				System.out.println("Error al inicializar la BD eXist");
				e.printStackTrace();
			}

		}
	}

	public static void modificarDep() {
		// TODO leer� de teclado un departamento, su nombre nuevo y la localidad nueva y
		// deber� actualizar todos los datos si existe, si no existe visualizar� que no
		// se puede modificar porque no existe.
		// Mostramos todos los depts
		verDepts();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// TODO leer� de teclado un departamento y lo borrar� si existe, si no existe
		// visualizar� que no se puede borrar porque no existe.
		int dep = -1;
		while (dep < 0) {
			System.out.println("Modificar un departamento");
			System.out.print("Introduce el n�mero de departamento: ");
			try {
				dep = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("introduce un valor v�lido");
			}
		}
		if (!existeDept(dep)) {
			System.out.println("no se puede modificar porque no existe.");
		} else {
			String newNombre = "";
			String newLoc = "";
			try {
				System.out.print("Introduce el nombre del departamento: ");
				newNombre = br.readLine();
				System.out.print("Introduce la localidad del departamento: ");
				newLoc = br.readLine();
			} catch (IOException e1) {
				System.out.println("Fallo");
			}
			String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
			Collection col = null; // Colecci�n
			String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colecci�n
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
				ResourceSet result = servicio.query("update value /departamentos/DEP_ROW[DEPT_NO=" + dep
						+ "]/DNOMBRE with " + "'" + newNombre + "'");
				result = servicio.query("update value /departamentos/DEP_ROW[DEPT_NO=" + dep + "]/LOC with " + "'" + newLoc + "'");
				col.close(); // cerramos

			} catch (Exception e) {
				System.out.println("Error al inicializar la BD eXist");
				e.printStackTrace();
			}

		}
	}

	public static int[] getCods() {

		int[] cods = null;
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		Collection col = null; // Colecci�n
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colecci�n
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
			ResourceSet result = servicio.query("for $cod in /departamentos/DEP_ROW/DEPT_NO return data($cod)");
			System.out.println("Se han obtenido " + result.getSize() + " elementos.");

			cods = new int[(int) result.getSize()];

			// recorrer los datos del recurso.
			ResourceIterator i;
			i = result.getIterator();
			if (!i.hasMoreResources())
				System.out.println(" LA CONSULTA NO DEVUELVE NADA O EST� MAL ESCRITA");
			int cont = 0;
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				cods[cont] = ((int) r.getContent());
			}
			col.close(); // cerramos
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}
		return cods;

	}

	public static boolean existeDept(int dept_no) {
		boolean encontrado = false;
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		Collection col = null; // Colecci�n
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colecci�n
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
			ResourceSet result = servicio
					.query("for $dept in /departamentos/DEP_ROW[DEPT_NO=" + dept_no + "] return $dept");

			// System.out.println("Se han obtenido " + result.getSize() + " elementos.");

			if (result.getSize() > 0) {

				encontrado = true;
			} else {

				encontrado = false;
			}
			col.close(); // cerramos
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}
		return encontrado;
	}

	public static void verDepts() {
		System.out.println("**********************************************************************");
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		Collection col = null; // Colecci�n
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; // URI colecci�n
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
			ResourceSet result = servicio.query("for $dep in /departamentos return $dep");
			System.out.println("Se han obtenido " + result.getSize() + " elementos.");
			// recorrer los datos del recurso.
			ResourceIterator i;
			i = result.getIterator();
			if (!i.hasMoreResources())
				System.out.println(" LA CONSULTA NO DEVUELVE NADA O EST� MAL ESCRITA");
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println((String) r.getContent());
			}
			col.close(); // cerramos
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}
		System.out.println("**********************************************************************");
	}
}
