package com.ups.uearv.servicios;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.ups.uearv.entidades.SegPerfil;
import com.ups.uearv.entidades.SegPerfilMenu;
import com.ups.uearv.entidades.SegUsuario;

/**
 * @author Jerson Armijos - Raysa Solano
 * @version 1.0
 */

@SuppressWarnings("unchecked")
public class DAO {

	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	public static EntityManager em = emf.createEntityManager();	

	// BUSCA Y RETORNA LISTA DE OBJETO
	public static <o> List<Object> nqObject(Object o, String njpql) {
		List<o> list = new ArrayList<o>();
		try {
			list = (List<o>) em.createNativeQuery(njpql, o.getClass()).getResultList();

			for (o tb : list)
				em.refresh(tb);

		} catch (Exception e) {
		}
		return (List<Object>) list;
	}	
	
	// BUSCA Y RETORNA OBJECTO
	public static <o> Object buscarObject(Object o, String jpql) {
		try {
			Query query = em.createQuery(jpql);
			o u = (o) query.getSingleResult();
			em.refresh(u);
			return u;
		} catch (NoResultException e) {
			return null;
		}
	}
		
	// INGRESAR - ACTUALIZAR
	public static boolean saveOrUpdate(Object o, int op, EntityManager em) {
		boolean ban = true;
		try {			
			if (op == 0 || op == 2) {
				em.persist(o);
			} else if (op == 1 || op == 3) {
				em.merge(o);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			ban = false;
		}
		return ban; // retorna true|false
	}

	// ELIMINAR
	public static void deleteSegPerfil(SegPerfil e) {
		em.find(SegPerfil.class, e.getIdPerfil());
		em.getTransaction().begin();
		e = em.merge(e);
		em.remove(e);
		em.getTransaction().commit();
	}

	public static void deleteSegUsuario(SegUsuario e) {
		em.find(SegUsuario.class, e.getIdUsuario());
		em.getTransaction().begin();
		e = em.merge(e);
		em.remove(e);
		em.getTransaction().commit();
	}

	public static void deleteSegPerfilMenu(SegPerfilMenu e) {
		em.find(SegPerfilMenu.class, e.getId());
		em.getTransaction().begin();
		e = em.merge(e);
		em.remove(e);
		em.getTransaction().commit();
	}

	// FUNCIONES SEGURIDAD
	public static List<Object> consultaMenu(String idPerfil, int idMenuPadre) {
		Query query = em.createNativeQuery(" CALL consulta_menu_perfil (" + idPerfil + ", " + idMenuPadre + ") ");
		return query.getResultList();
	}
	
	public static List<Object> obtenerMenu(String usuario) {	
		try {
			SegUsuario u = (SegUsuario) DAO.buscarObject(new SegUsuario(), " from SegUsuario u where u.idUsuario = '" + usuario + "'");
			int idPerfil = u.getIdPerfil();
			Query query = em.createNativeQuery(" SELECT id_menu FROM seg_perfil_menu WHERE id_perfil = " + idPerfil);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}		
	}

	public static String getPerfil(String usuario) {
		Query query = em.createNativeQuery(
				" SELECT k.descripcion FROM seg_usuario u INNER JOIN seg_perfil k ON k.id_perfil = u.id_perfil WHERE id_usuario = '" + usuario + "' ");
		List<Object> result = query.getResultList();
		Iterator<Object> itr = result.iterator();
		Object obj = (Object) itr.next();
		String perfil = String.valueOf(obj);

		return perfil;
	}
	
	// FUNCIONES GENERALES
	public static Query getPerfiles() {
		Query query = em.createNativeQuery(" SELECT id_perfil, descripcion FROM seg_perfil WHERE estado = 'AC' ORDER BY descripcion");
		return query;
	}
		
	public static String getNombre(String usuario) {
		Query query = em.createNativeQuery(
				" SELECT CONCAT(IFNULL(SUBSTRING_INDEX(nombres, ' ', 1), ''), ' ', IFNULL(SUBSTRING_INDEX(apellidos, ' ', 1), '')) FROM seg_usuario WHERE id_usuario = '" + usuario + "' ");
		List<Object> result = query.getResultList();
		Iterator<Object> itr = result.iterator();
		Object obj = (Object) itr.next();
		String nombre = String.valueOf(obj);

		return nombre;
	}	
	
	public static Query getDocentes() {
		Query query = em.createNativeQuery(
				" SELECT id_docente, CONCAT(IFNULL(SUBSTRING_INDEX(nombres, ' ', 1), ''), ' ', IFNULL(SUBSTRING_INDEX(apellidos, ' ', 1), '')) nombre FROM mat_docente WHERE estado = 'AC' order by 2 ");
		return query;
	}

	public static Query getRepresentantes() {
		Query query = em.createNativeQuery(
				" SELECT id_representante, CONCAT(IFNULL(SUBSTRING_INDEX(nombres, ' ', 1), ''), ' ', IFNULL(SUBSTRING_INDEX(apellidos, ' ', 1), '')) nombre FROM mat_representante WHERE estado = 'AC' ORDER BY 2 ");
		return query;
	}

	public static Query getEstudiantes() {
		Query query = em.createNativeQuery(
				" SELECT id_representante, CONCAT(IFNULL(SUBSTRING_INDEX(nombres, ' ', 1), ''), ' ', IFNULL(SUBSTRING_INDEX(apellidos, ' ', 1), '')) nombre FROM mat_estudiante WHERE estado = 'AC' ORDER BY 2 ");
		return query;
	}
	
	public static Query getCursos(String det) {
		Query query = em.createNativeQuery(
				" SELECT id_curso, descripcion FROM mat_curso WHERE nivel = '" + det + "' AND estado = 'AC' ORDER BY descripcion ");
		return query;
	}
	
	public static Query getOfertas(String periodo) {
		Query query = em.createNativeQuery(
				"SELECT o.id_oferta, o.descripcion " + 
				"FROM mat_oferta o " + 
				"  	INNER JOIN mat_curso c ON c.id_curso = o.id_curso " + 
				"	INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " + 
				"WHERE o.id_oferta = '" + periodo + "'  ORDER BY k.codigo_det, c.id_curso ");
		return query;
	}		
	
	public static Query getPeriodos() {
		Query query = em.createNativeQuery(
				" SELECT id_periodo, descripcion FROM mat_periodo WHERE estado = 'AC' ORDER BY descripcion ");
		return query;
	}		
	
	public static Query getCursos() {
		Query query = em.createNativeQuery(
				" SELECT c.id_curso, c.descripcion FROM mat_curso c INNER JOIN catalogo_det k ON k.codigo_det = c.nivel WHERE c.estado = 'AC' ORDER BY k.codigo_det, c.id_curso ");
		return query;
	}
	
	public static Query getParalelos() {
		Query query = em.createNativeQuery(
				" SELECT id_paralelo, descripcion FROM mat_paralelo WHERE estado = 'AC' ORDER BY descripcion ");
		return query;
	}
	
	public static Query getAsignaturas(String det) {
		Query query = em.createNativeQuery(
				" SELECT id_asignatura, nombre FROM cal_asignatura WHERE nivel = '" + det + "' AND estado = 'AC' ORDER BY descripcion ");
		return query;
	}
	
	public static Query getDetCatalogo(String cab) {
		Query query = em.createNativeQuery(
				" SELECT codigo_det, descripcion FROM catalogo_det WHERE codigo_cab = '" + cab + "' AND estado = 'AC' ORDER BY descripcion ");
		return query;
	}
	
	/**********************************
	 * CATALOGO						  *
	 * 								  *
	 * CA001	Grupo Sanguineo	      * 
	 * CA002	Área de Conocimiento  *
	 * CA003	Jornada	Jornada       *
	 * CA004	Nivel                 *
	 * CA005	Estado Civil          *
	 **********************************/
}