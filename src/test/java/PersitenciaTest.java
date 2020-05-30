import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PersitenciaTest {
/*
	@Test
	void SQLSaveCDRTest() {
		CDR cdr=new CDR("5555","2222",22,2.5);
		PersistenciaCDR persistencia1= new PersistenciaCDRSql();
		persistencia1.guardarCDR(cdr);
		CDR cdr2=persistencia1.getCDR(cdr.getId());
		assertEquals(cdr.getId(),cdr2.getId());
	}
	
	@Test
	void SQLSaveLineaTest() {
		Plan plan=new PlanPrepago();
		Plan plan2=new PlanWow();
		Linea linea=new Linea("5555","Jose",plan);
		PersistenciaLinea persistencia2=new PersistenciaLineaSql();
		persistencia2.guardarLinea(linea);
		Linea linea2=persistencia2.getLinea("5555");
		assertEquals(linea.getNumero(),linea2.getNumero());
		Linea linea3=new Linea("4444","Andres",plan2);
		linea3.addNumeroAmigo("1111");
		persistencia2.guardarLinea(linea3);
		Linea linea4=persistencia2.getLinea("4444");
		assertEquals(linea3.getNumero(),linea4.getNumero());
	}
	
	@Test
	void ArchivosSaveCDRTest() {
		CDR cdr=new CDR("5555","2222",22,2.5);
		PersistenciaCDR persistencia1= new PersistenciaCDRArchivo();
		persistencia1.guardarCDR(cdr);
		CDR cdr2=persistencia1.getCDR(cdr.getId());
		assertEquals(cdr.getId(),cdr2.getId());
	}
	@Test
	void ArchivosSaveLineaTest() {
		Plan plan=new PlanPrepago();
		Plan plan2=new PlanWow();
		Linea linea=new Linea("5555","Jose",plan);
		PersistenciaLinea persistencia2=new PersistenciaLineaArchivo();
		persistencia2.guardarLinea(linea);
		Linea linea2=persistencia2.getLinea("5555");
		assertEquals(linea.getNumero(),linea2.getNumero());
		Linea linea3=new Linea("4444","Andres",plan2);
		linea3.addNumeroAmigo("1111");
		persistencia2.guardarLinea(linea3);
		Linea linea4=persistencia2.getLinea("4444");
		assertEquals(linea3.getNumero(),linea4.getNumero());
	}
	*/
	
	@Test
	void savePlanDTO() {
		PersistenciaPlan persi = new PersistenciaPlanSql();
		//PlanDTO dto = new PlanDTO("PREPAGO","HorarioReducido+HorarioNormal+HorarioSuperReducido","1.45+0.95+0.70");
		//PlanDTO dto = new PlanDTO("POSTPAGO","Tarifa","0.99");
		//PlanDTO dto = new PlanDTO("WOW","Tarifa+amigos","0.99+5");
		//PlanDTO dto = new PlanDTO("FAMILIA","Tarifa+amigos","0.99+5");
		//PlanDTO dto = new PlanDTO("HOGAR","Tarifa","0.99");
		//alm.guardarPlan(dto);
		//persi.savePlan(dto);
		persi.loadPlans();
	}
	
	
	@Test
	void DBshouldReturnPlanTableExistsIfItIsAlreadyStoredinDB() {
		PersistenciaPlan persi = new PersistenciaPlanSql();
		boolean expected = true;
		assertEquals(expected,persi.planExists("PREPAGO"));
	}
	
	@Test
	void DBshouldReturnPlanTableExistsIfItIsNotAlreadyStoredinDB() {
		PersistenciaPlan persi = new PersistenciaPlanSql();
		boolean expected = false;
		assertEquals(expected,persi.planExists("POSTPAGO"));
	}
}
