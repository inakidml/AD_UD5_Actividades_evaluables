import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.xqj.exist.ExistXQDataSource;

public class Actividad_5_10 {

	public static void main(String[] args) {

		String nom = "zonas20.xml";
		File fichero = new File(nom);
		XQDataSource server = new ExistXQDataSource();
		try {
			server.setProperty("serverName", "localhost");
			server.setProperty("port", "8083");
			XQConnection conn = server.getConnection();
			XQPreparedExpression consulta;
			consulta = conn.prepareExpression(
					"for $prod in doc('/db/BDProductosXML/productos.xml')/productos/produc[cod_zona=20]\r\n"
							+ "let $zona := doc('/db/BDProductosXML/zonas.xml')/zonas/zona[cod_zona=20]\r\n"
							+ "return\r\n" + "<zona_20>\r\n" + "<cod_prod>{data($prod/cod_prod)}</cod_prod>\r\n"
							+ "<denominacion>{data($prod/denominacion)}</denominacion>\r\n"
							+ "<precio>{data($prod/precio)}</precio>\r\n"
							+ "<nombre_zona>{data($zona/nombre)}</nombre_zona>\r\n"
							+ "<director>{data($zona/director)}</director>\r\n"
							+ "<stock>{data($prod/stock_actual)-data($prod/stock_minimo)}</stock>\r\n"
							+ "</zona_20>\r\n" + " ");
			XQResultSequence result = consulta.executeQuery();
			if (fichero.exists()) { // borramos y creamos
				if (fichero.delete())
					System.out.println("Archivo borrado, creando nuevo archivo...");
				else
					System.out.println("Error al borrar el archivo...");
			}
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(nom));
				bw.write("<?xml version='1.0' encoding='ISO-8859-1'?>" + "\n");
				while(result.next()) {
				String cad = result.getItemAsString(null);
				System.out.println("Salida: " + cad);
				bw.write(cad + "\n");}
				bw.close();
				System.out.println("Fichero creado");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			conn.close();
		} catch (XQException ex) {
			ex.printStackTrace();
		}
	}
}
