import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XPathQueryService;

public class Actividad_5_8 {

	private static Collection col = null; // Colecci�n;
	private static String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/"; // URI colecci�n
	private static String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
	private static String usu = "admin"; // Usuario
	private static String usuPwd = "admin"; // Clave

	public static void main(String[] args) {
		// crea una nueva colección
		crearColeccion("GIMNASIO");
		// Añade los ficheros del ejercicio
		addXML("./ColeccionGimnasio/actividades_gim.xml");
		addXML("./ColeccionGimnasio/socios_gim.xml");
		addXML("./ColeccionGimnasio/uso_gimnasio.xml");
		// crea un fichero para añadir el documento a la colección
		crearFicheroXML("socios_cuotaadicional.xml");
		// Consulta y añade al documento
		cuotaAdicionalSocios();
		System.out.println("Creado documento con cuotas adicionales");
		System.out.println("---------------------------------------");
		// muestra el documento creado y actualizado
		System.out.println("CUOTAS ADICIONALES LEÍDAS DEL DOCUMENTO");
		mostrarXQuery("/socios_cuotaadicional");
		//mostramos los totales
		String consulta= "for $soc in distinct-values(/socios_cuotaadicional/datos/COD)\n" + 
						 "let $total_adic:= sum(/socios_cuotaadicional/datos[COD=$soc]/cuota_adicional)\n" + 
						 "let $nombre:=distinct-values(/socios_cuotaadicional/datos[COD=$soc]/NOMBRESOCIO)\n" + 
						 "let $total:=data($total_adic)+(data(/SOCIOS_GIM/fila_socios[COD=$soc]/CUOTA_FIJA))\n" + 
						 "return\n" + 
						 "<datos>\n" + 
						 "<COD>{data($soc)}</COD>\n" + 
						 "<NOMBRESOCIO>{data($nombre)}</NOMBRESOCIO>\n" + 
						 "<suma_cuota_adic>{data($total_adic)}</suma_cuota_adic>"
						 + "<cuota_total>{data($total)}</cuota_total>\n" + 
						 "</datos>";
		
		System.out.println("CUOTAS TOTALES POR CONSULTA");
		mostrarXQuery(consulta);
		System.out.println("FIN");
		

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

	public static void cuotaAdicionalSocios() {
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/GIMNASIO/"; // URI colecci�n
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la BD
			DatabaseManager.registerDatabase(database); // Registro del driver
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			if (col == null)
				System.out.println(" *** LA COLECCION NO EXISTE. ***");
			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			String consultaCompleja = "for $socio in (/SOCIOS_GIM/fila_socios), $act_uso in (/USO_GIMNASIO/fila_uso[CODSOCIO=($socio/COD)])\n"
					+ " \n" + "let $act_tipo:=(/ACTIVIDADES_GIM/fila_actividades[@cod=$act_uso/CODACTIV]/@tipo)\n"
					+ "let $act_nomb:=(/ACTIVIDADES_GIM/fila_actividades[@cod=$act_uso/CODACTIV]/NOMBRE)\n"
					+ "let $hora_ini:=($act_uso/HORAINICIO)\n" + "let $hora_fin:=($act_uso/HORAFINAL)\n"
					+ "let $horas:=data($hora_fin)-data($hora_ini)\n" + "\n" + "where $socio/COD=$act_uso/CODSOCIO\n"
					+ "\n" + "return\n" + "if(data($act_tipo)=1)then\n" + "<datos>\n"
					+ "<COD>{data($socio/COD)}</COD>\n" + "<NOMBRESOCIO>{data($socio/NOMBRE)}</NOMBRESOCIO>\n"
					+ "<CODACTIV>{data($act_uso/CODACTIV)}</CODACTIV>\n"
					+ "<NOMBREACTIVIDAD>{data($act_nomb)}</NOMBREACTIVIDAD>\n" + "<horas>{data($horas)}</horas>\n"
					+ "<tipoact>{data($act_tipo)}</tipoact>\n" + "<cuota_adicional>0</cuota_adicional>\n" + "</datos>\n"
					+ "else if(data($act_tipo)=2) then\n" + "<datos>\n" + "<COD>{data($socio/COD)}</COD>\n"
					+ "<NOMBRESOCIO>{data($socio/NOMBRE)}</NOMBRESOCIO>\n"
					+ "<CODACTIV>{data($act_uso/CODACTIV)}</CODACTIV>\n"
					+ "<NOMBREACTIVIDAD>{data($act_nomb)}</NOMBREACTIVIDAD>\n" + "<horas>{data($horas)}</horas>\n"
					+ "<tipoact>{data($act_tipo)}</tipoact>\n" + "<cuota_adicional>{data($horas)*2}</cuota_adicional>\n"
					+ "</datos>\n" + "else if(data($act_tipo)=3) then\n" + "<datos>\n"
					+ "<COD>{data($socio/COD)}</COD>\n" + "<NOMBRESOCIO>{data($socio/NOMBRE)}</NOMBRESOCIO>\n"
					+ "<CODACTIV>{data($act_uso/CODACTIV)}</CODACTIV>\n"
					+ "<NOMBREACTIVIDAD>{data($act_nomb)}</NOMBREACTIVIDAD>\n" + "<horas>{data($horas)}</horas>\n"
					+ "<tipoact>{data($act_tipo)}</tipoact>\n" + "<cuota_adicional>{data($horas)*4}</cuota_adicional>\n"
					+ "</datos>\n" + "else()";
			ResourceSet result = servicio.query("update insert (" + consultaCompleja + ") into /socios_cuotaadicional");
			col.close(); // cerramos

		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}

	}

	public static void mostrarXQuery(String consulta) {
		System.out.println("********************************************************************");
		String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/GIMNASIO"; // URI colecci�n
		try {
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			if (col == null)
				System.out.println(" *** LA COLECCION NO EXISTE. ***");
			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

			ResourceSet result = servicio.query(consulta);

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
		System.out.println("********************************************************************");
	}

	public static void crearFicheroXML(String nombre) {

		File archivo = new File(nombre);
		FileWriter f = null;

		if (!archivo.canRead()) {
			System.out.println("ERROR AL LEER EL FICHERO, Creando uno...");

			try {
				f = new FileWriter(archivo, true);
				String[] bufStrings = { "<socios_cuotaadicional>", "</socios_cuotaadicional>" };
				for (String string : bufStrings) {
					System.out.println("Escribiendo en el fichero");
					f.write(string);
				}
				// cerrar flujo, muy importante
				f.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			Resource nuevoRecurso;
			try {
				nuevoRecurso = col.createResource(archivo.getName(), "XMLResource");
				nuevoRecurso.setContent(archivo); // Asignamos el archivo
				col.storeResource(nuevoRecurso); // Lo almacenamos en la colección
			} catch (XMLDBException e) {
				e.printStackTrace();
			}
		}

	}
}
