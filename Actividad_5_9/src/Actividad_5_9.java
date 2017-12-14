
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import net.xqj.exist.ExistXQDataSource;

public class Actividad_5_9 {

	public static void main(String[] args) throws XQException {
		XQDataSource server = new ExistXQDataSource();
		server.setProperty("serverName", "localhost");
		server.setProperty("port", "8083");
		server.setProperty("user", "admin");
		server.setProperty("password", "admin");

		XQConnection conn = server.getConnection();
		XQPreparedExpression consulta;
		XQResultSequence resultado;
		System.out.println("-- Consulta documento zonas.xml --");
		System.out.println("-- ---------------------------- --");
		consulta = conn.prepareExpression("for $zona in doc('/db/BDProductosXML/zonas.xml')/zonas/zona\r\n" + 
				"let $prod := collection('/db/BDProductosXML') /productos/produc[cod_zona=$zona/cod_zona]\r\n" + 
				"let $numeroProd := count($prod)\r\n" + 
				"return \r\n" + 
				"<datos>\r\n" + 
				"<nombre_zona>{data($zona/nombre)}</nombre_zona>\r\n" + 
				"<cantidad_prod>{data($numeroProd)}</cantidad_prod>\r\n" + 
				"</datos>");
		resultado = consulta.executeQuery();
		while(resultado.next()){
		System.out.println(resultado.getItemAsString(null));
		} 
		System.out.println();
		System.out.println("-- ---------------------------- --");
		System.out.println("-- Consulta documento universidad.xml --");
		System.out.println("-- ---------------------------- --");
		consulta = conn.prepareExpression("for $depar in doc('/db/ColeccionPruebas/universidad.xml')/universidad/filadepar\r\n" + 
				"let $emple := $depar[@tipo='A']/empleado\r\n" + 
				"return \r\n" + 
				"<datos>\r\n" + 
				"{$emple}\r\n" + 
				"</datos>");
		resultado = consulta.executeQuery();
		while(resultado.next()){
		System.out.println(resultado.getItemAsString(null));
		} 
			
		conn.close();
		
	}

}
