package Controllers;

import static spark.Spark.post;
import static spark.Spark.get;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;


import Entities.CDR;

import Entities.Tarificacion;

import Interactors.AgregarTarificacion;
import Interactors.GestionarConfiguracionPersistencia;
import Interactors.ObtenerYValidarCDRsDeArchivo;
import Interactors.ObtenerCDRsSegunTarificacion;
import Interactors.ObtenerTarificacion;
import Interactors.TarificarYGuardarCDRs;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.utils.IOUtils;

public class CDRController {
	private static ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();

	public void main() {
		
		post("/:tipo/UploadCDR",(Request request,Response response) ->{
			ObtenerYValidarCDRsDeArchivo obtenerCDR=new ObtenerYValidarCDRsDeArchivo();
			
			String path = "CDRsUpload/";
		 	request.raw().setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
		 	InputStream input = request.raw().getPart("upfile").getInputStream();
		 	String fileName = path + request.raw().getPart("upfile").getSubmittedFileName();
            OutputStream outputStream = new FileOutputStream(fileName);
            IOUtils.copy(input, outputStream);
            outputStream.close();
		 	
			Path out = Paths.get(fileName);
			
			List<CDR> CDRs=obtenerCDR.ObtenerCDRsDeArchivo(out);
			
			Map<String, Object> viewObjects = new HashMap<String, Object>();
			viewObjects.put("filePath", out.toString());
	        viewObjects.put("CDRSIngresados", CDRs);
	        viewObjects.put("tipo", request.params(":tipo"));
	        return engine.render(new ModelAndView(viewObjects, "CDRsDeArchivo"));
		});
		
		post("/:tipo/saveCDRs",(Request request,Response response)->{
			ObtenerYValidarCDRsDeArchivo obtenerCDR=new ObtenerYValidarCDRsDeArchivo();
			TarificarYGuardarCDRs tarificarYGuardar = new TarificarYGuardarCDRs();
			GestionarConfiguracionPersistencia configuracion=new GestionarConfiguracionPersistencia();
			configuracion.seleccionarPersistencia(request.params(":tipo"));
			
			AgregarTarificacion agregarTarificacion=new AgregarTarificacion();
			Tarificacion tarificacion=agregarTarificacion.agregarTarificacion(request.params(":tipo"));

			String ruta=request.queryParams("filePath");
			Path out = Paths.get(ruta);
			
			List<CDR> cdrsDeArchivo=obtenerCDR.ObtenerCDRsDeArchivo(out);
			List<CDR> CDRsRegistrados=tarificarYGuardar.agregarCDR(cdrsDeArchivo, configuracion.getPersistenciaCDR(), configuracion.getPersistenciaLinea(), tarificacion.getId());
			
			String message="";
			if(cdrsDeArchivo.size()>CDRsRegistrados.size())
				message="existian CDR de lineas no registradas";
			
			Map<String, Object> viewObjects = new HashMap<String, Object>();
			viewObjects.put("cant", cdrsDeArchivo.size());
			viewObjects.put("message", message);
			viewObjects.put("CDRSIngresados", CDRsRegistrados);
	        viewObjects.put("tipo", request.params(":tipo"));
			return engine.render(new ModelAndView(viewObjects, "CDRsIngresados"));
		});
		
		get("/:tipo/getCDRsRegistered/:id",(request, response) ->{
			ObtenerCDRsSegunTarificacion obtenerCDRs=new ObtenerCDRsSegunTarificacion();
			ObtenerTarificacion obtenerTarificacion=new ObtenerTarificacion();
			Tarificacion tarificacion=obtenerTarificacion.getTarificacion(Integer.parseInt(request.params(":id")),request.params(":tipo"));
			List<CDR> cdrsTarificacion=obtenerCDRs.obtenerCDRS(tarificacion);
			Iterable<CDR> listaCDRs=cdrsTarificacion;
			Map<String, Object> viewObjects = new HashMap<String, Object>();
			viewObjects.put("listaCDRs", listaCDRs);
			viewObjects.put("cant", tarificacion.getId());
			viewObjects.put("tipo", request.params(":tipo"));
			return engine.render(new ModelAndView(viewObjects, "TarificationCDRs"));
		});
		
	}

}
