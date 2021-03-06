package uniandes.isis2304.vacuandes.persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.PlanDeVacunacion;
import uniandes.isis2304.vacuandes.negocio.PuntoVacunacion;

public class SQLPuntoVacunacion {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaVacuandes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLPuntoVacunacion (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	public long adicionarPuntoVacunacion(PersistenceManager pm, String localizacion, int capacidad_de_atencion_simultanea, String infraestructura_para_dosis, int cantidad_de_vacunas_enviables, int cantidad_de_vacunas_actuales, String tipo_punto_vacunacion, String administrador, long oficina_regional_eps, int habilitado)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPuntoVacunacion() + "(localizacion, capacidad_de_atencion_simultanea, infraestructura_para_dosis, cantidad_vacunas_enviables, cantidad_vacunas_actuales, tipo_punto_vacunacion, administrador, oficina_regional_eps, habilitado) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		q.setParameters(localizacion, capacidad_de_atencion_simultanea, infraestructura_para_dosis, cantidad_de_vacunas_enviables, cantidad_de_vacunas_actuales, tipo_punto_vacunacion, administrador, oficina_regional_eps, habilitado);
		return (long) q.executeUnique(); 
	}

	public long eliminarTodosLosPuntosVacunacion(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPuntoVacunacion());
		return (long) q.executeUnique();
	}
	
	public long eliminarPuntoVacunacionPorId(PersistenceManager pm, long id_punto_vacunacion)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPuntoVacunacion() + " WHERE id_punto_vacunacion = ?");
        q.setParameters(id_punto_vacunacion);
        return (long) q.executeUnique();
	}
	
	public long eliminarPuntoVacunacionPorLocalizacion(PersistenceManager pm, String localizacion)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPuntoVacunacion() + " WHERE localizacion = ?");
        q.setParameters(localizacion);
        return (long) q.executeUnique();
	}
	
	public List<PuntoVacunacion> darListPuntoVacunacion(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT id_Punto_Vacunacion,LOCALIZACION,CAPACIDAD_DE_ATENCION_SIMULTANEA,INFRAESTRUCTURA_PARA_DOSIS,CANTIDAD_VACUNAS_ENVIABLES,CANTIDAD_VACUNAS_ACTUALES,TIPO_PUNTO_VACUNACION,ADMINISTRADOR,OFICINA_REGIONAL_EPS, HABILITADO FROM " + pp.darTablaPuntoVacunacion());
		q.setResultClass(PuntoVacunacion.class);
		List<PuntoVacunacion> resp = q.executeList();
		return resp;
	}

	public List<PuntoVacunacion> darListPuntoVacunacionDeLaRegion(PersistenceManager pm, String region) {
		Query q = pm.newQuery(SQL, "\n"
				+ "SELECT ID_PUNTO_VACUNACION, LOCALIZACION, CAPACIDAD_DE_ATENCION_SIMULTANEA, \n"
				+ "INFRAESTRUCTURA_PARA_DOSIS, punto.CANTIDAD_VACUNAS_ENVIABLES, punto.CANTIDAD_VACUNAS_ACTUALES, punto.TIPO_PUNTO_VACUNACION, punto.ADMINISTRADOR, OFICINA_REGIONAL_EPS, habilitado \n"
				+ "FROM PUNTO_VACUNACION punto INNER JOIN OFICINA_REGIONAL_EPS oficina ON punto.oficina_regional_eps = oficina.id_oficina \n"
				+ "WHERE oficina.region = ?");
		q.setParameters(region);
		q.setResultClass(PuntoVacunacion.class);
		List<PuntoVacunacion> resp = q.executeList();
		return resp;
	}

	public PuntoVacunacion darPuntoPorId(PersistenceManager pm, long id_punto_vacunacion) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPuntoVacunacion() + " WHERE id_punto_vacunacion = ?");
		q.setResultClass(PuntoVacunacion.class);
		q.setParameters(id_punto_vacunacion); 
		return (PuntoVacunacion) q.executeUnique();
	}
	
	public PuntoVacunacion darPuntoPorLocalizacion(PersistenceManager pm, String localizacion) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPuntoVacunacion() + " WHERE localizacion = ?");
		q.setResultClass(PuntoVacunacion.class);
		q.setParameters(localizacion); 
		return (PuntoVacunacion) q.executeUnique();
	}

	public long disminuirVacunasDisponibles(PersistenceManager pm, long id_punto_vacunacion) {
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPuntoVacunacion() + " SET CANTIDAD_VACUNAS_ACTUALES= CANTIDAD_VACUNAS_ACTUALES-1 WHERE id_punto_vacunacion = ?");
		q.setParameters(id_punto_vacunacion);
		return (long) q.executeUnique();
	}

	public List<PuntoVacunacion> darListPuntoVacunacionHabilitados(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT id_Punto_Vacunacion,LOCALIZACION,CAPACIDAD_DE_ATENCION_SIMULTANEA,INFRAESTRUCTURA_PARA_DOSIS,CANTIDAD_VACUNAS_ENVIABLES,CANTIDAD_VACUNAS_ACTUALES,TIPO_PUNTO_VACUNACION,ADMINISTRADOR,OFICINA_REGIONAL_EPS, HABILITADO FROM " + pp.darTablaPuntoVacunacion() + " WHERE HABILITADO = 1");
		q.setResultClass(PuntoVacunacion.class);
		List<PuntoVacunacion> resp = q.executeList();
		return resp;
	}
	
	public List<PuntoVacunacion> darListPuntoVacunacionDeshabilitados(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT id_Punto_Vacunacion,LOCALIZACION,CAPACIDAD_DE_ATENCION_SIMULTANEA,INFRAESTRUCTURA_PARA_DOSIS,CANTIDAD_VACUNAS_ENVIABLES,CANTIDAD_VACUNAS_ACTUALES,TIPO_PUNTO_VACUNACION,ADMINISTRADOR,OFICINA_REGIONAL_EPS, HABILITADO FROM " + pp.darTablaPuntoVacunacion() + " WHERE HABILITADO = 0");
		q.setResultClass(PuntoVacunacion.class);
		List<PuntoVacunacion> resp = q.executeList();
		return resp;
	}
	
	public List<PuntoVacunacion> darListPuntoVacunacionHabilitadosPorRegion(PersistenceManager pm, String region) {
		Query q = pm.newQuery(SQL, 
			   "SELECT ID_PUNTO_VACUNACION, LOCALIZACION, CAPACIDAD_DE_ATENCION_SIMULTANEA, "
				+ "INFRAESTRUCTURA_PARA_DOSIS, punto.CANTIDAD_VACUNAS_ENVIABLES, punto.CANTIDAD_VACUNAS_ACTUALES, punto.TIPO_PUNTO_VACUNACION, punto.ADMINISTRADOR, OFICINA_REGIONAL_EPS, habilitado "
				+ "FROM PUNTO_VACUNACION punto INNER JOIN OFICINA_REGIONAL_EPS oficina ON punto.oficina_regional_eps = oficina.id_oficina "
				+ "WHERE oficina.region = ? and punto.habilitado = 1");
		q.setParameters(region);
		q.setResultClass(PuntoVacunacion.class);
		List<PuntoVacunacion> resp = q.executeList();
		return resp;
	}

	public long actualizarEstado(PersistenceManager pm, long punto_vacunacion, int i) {
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPuntoVacunacion() + " SET habilitado = ? WHERE ID_PUNTO_VACUNACION = ?");
		q.setParameters(i, punto_vacunacion);
		return (long) q.executeUnique();
	}

	public long adicionarVacunasAPunto(PersistenceManager pm, long id_Punto_Vacunacion, int cantidad_vacunas) {
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPuntoVacunacion() + " SET CANTIDAD_VACUNAS_ACTUALES= CANTIDAD_VACUNAS_ACTUALES + ? WHERE id_punto_vacunacion = ?");
		q.setParameters(cantidad_vacunas, id_Punto_Vacunacion);
		return (long) q.executeUnique();
	}

	public List<Object> darSobrecupoDiaEspecifico(PersistenceManager pm, String tipo_punto,
			String dia) {
		Query q = pm.newQuery(SQL, "SELECT ID_PUNTO_VACUNACION, LOCALIZACION , CITAS, FECHA, HORA_CITA citas from " + pp.darTablaPuntoVacunacion()
		+ " tabla_punto INNER JOIN (SELECT PUNTO_VACUNACION, FECHA, hora_cita, COUNT(ID_CITA) citas FROM " + pp.darTablaCita() 
		+ " GROUP BY punto_vacunacion,fecha, hora_cita ORDER BY citas DESC) tabla_citas ON tabla_punto.Id_punto_vacunacion = tabla_citas.punto_vacunacion "
		+ " WHERE tabla_citas.citas >= tabla_punto.capacidad_de_atencion_simultanea*0.9 AND tabla_punto.tipo_punto_vacunacion = ? AND tabla_citas.fecha = TO_DATE(?, 'dd/mm/yyyy')");
		q.setParameters( tipo_punto, dia);
		return q.executeList();
	}

	public List<Object> darFaltaDeCupoDiaEspecifico(PersistenceManager pm, String tipo_punto,
			String dia)  {
		Query q = pm.newQuery(SQL, "SELECT ID_PUNTO_VACUNACION, LOCALIZACION , CITAS, FECHA, HORA_CITA citas from " + pp.darTablaPuntoVacunacion()
		+ " tabla_punto INNER JOIN (SELECT PUNTO_VACUNACION, FECHA, hora_cita, COUNT(ID_CITA) citas FROM " + pp.darTablaCita() 
		+ " GROUP BY punto_vacunacion,fecha, hora_cita ORDER BY citas DESC) tabla_citas ON tabla_punto.Id_punto_vacunacion = tabla_citas.punto_vacunacion "
		+ " WHERE tabla_citas.citas <= tabla_punto.capacidad_de_atencion_simultanea*0.1 AND tabla_punto.tipo_punto_vacunacion = ? AND tabla_citas.fecha = TO_DATE(?, 'dd/mm/yyyy')");
		q.setParameters( tipo_punto, dia);
		return q.executeList();
	}

	public List<Object> darSobreCupoEnRangoDeHoras(PersistenceManager pm, String tipo_punto,
			int primera_hora, int segunda_hora) {
		Query q = pm.newQuery(SQL, "SELECT ID_PUNTO_VACUNACION, LOCALIZACION , CITAS, FECHA, HORA_CITA citas from " + pp.darTablaPuntoVacunacion()
		+ " tabla_punto INNER JOIN (SELECT PUNTO_VACUNACION, FECHA, hora_cita, COUNT(ID_CITA) citas FROM " + pp.darTablaCita() 
		+ " GROUP BY punto_vacunacion,fecha, hora_cita ORDER BY citas DESC) tabla_citas ON tabla_punto.Id_punto_vacunacion = tabla_citas.punto_vacunacion "
		+ " WHERE tabla_citas.citas >= tabla_punto.capacidad_de_atencion_simultanea*0.9 AND tabla_punto.tipo_punto_vacunacion = ? AND tabla_citas.hora_cita between ? and ?");
		q.setParameters( tipo_punto, primera_hora, segunda_hora);
		return q.executeList();
	}

	public List<Object> darFaltaDeCupoEnRangoDeHoras(PersistenceManager pm, String tipo_punto,
			int primera_hora, int segunda_hora) {
		Query q = pm.newQuery(SQL, "SELECT ID_PUNTO_VACUNACION, LOCALIZACION , CITAS, FECHA, HORA_CITA citas from " + pp.darTablaPuntoVacunacion()
		+ " tabla_punto INNER JOIN (SELECT PUNTO_VACUNACION, FECHA, hora_cita, COUNT(ID_CITA) citas FROM " + pp.darTablaCita() 
		+ " GROUP BY punto_vacunacion,fecha, hora_cita ORDER BY citas DESC) tabla_citas ON tabla_punto.Id_punto_vacunacion = tabla_citas.punto_vacunacion "
		+ " WHERE tabla_citas.citas <= tabla_punto.capacidad_de_atencion_simultanea*0.1 AND tabla_punto.tipo_punto_vacunacion = ? AND tabla_citas.hora_cita between ? and ?");
		q.setParameters( tipo_punto, primera_hora, segunda_hora);
		return q.executeList();
	}

	public List<Object> darSobreCupoEnRangoFechas(PersistenceManager pm, String tipo_punto,
			String primera_fecha, String segunda_fecha) {
		Query q = pm.newQuery(SQL, "SELECT ID_PUNTO_VACUNACION, LOCALIZACION , CITAS, FECHA, HORA_CITA citas from " + pp.darTablaPuntoVacunacion()
		+ " tabla_punto INNER JOIN (SELECT PUNTO_VACUNACION, FECHA, hora_cita, COUNT(ID_CITA) citas FROM " + pp.darTablaCita() 
		+ " GROUP BY punto_vacunacion,fecha, hora_cita ORDER BY citas DESC) tabla_citas ON tabla_punto.Id_punto_vacunacion = tabla_citas.punto_vacunacion "
		+ " WHERE tabla_citas.citas >= tabla_punto.capacidad_de_atencion_simultanea*0.9 AND tabla_punto.tipo_punto_vacunacion = ? AND tabla_citas.fecha >= TO_DATE(?, 'dd/mm/yyyy') AND tabla_citas.fecha <= TO_DATE(?, 'dd/mm/yyyy')");
		q.setParameters( tipo_punto, primera_fecha, segunda_fecha);
		return q.executeList();
	}

	public List<Object> darFaltaDeCupoEnRangoFechas(PersistenceManager pm, String tipo_punto,
			String primera_fecha, String segunda_fecha) {
		Query q = pm.newQuery(SQL, "SELECT ID_PUNTO_VACUNACION, LOCALIZACION , CITAS, FECHA, HORA_CITA citas from " + pp.darTablaPuntoVacunacion()
		+ " tabla_punto INNER JOIN (SELECT PUNTO_VACUNACION, FECHA, hora_cita, COUNT(ID_CITA) citas FROM " + pp.darTablaCita() 
		+ " GROUP BY punto_vacunacion,fecha, hora_cita ORDER BY citas DESC) tabla_citas ON tabla_punto.Id_punto_vacunacion = tabla_citas.punto_vacunacion "
		+ " WHERE tabla_citas.citas <= tabla_punto.capacidad_de_atencion_simultanea*0.1 AND tabla_punto.tipo_punto_vacunacion = ? AND tabla_citas.fecha >= TO_DATE(?, 'dd/mm/yyyy') AND tabla_citas.fecha <= TO_DATE(?, 'dd/mm/yyyy')");
		q.setParameters( tipo_punto, primera_fecha, segunda_fecha);
		return q.executeList();
	}

	
}
