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
import com.ups.uearv.entidades.SegUsuario;

/**
 * @author Jerson Armijos - Raysa Solano
 */

@SuppressWarnings("unchecked")
public class DAO {

	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	public static EntityManager em = emf.createEntityManager();	

	public static List<SegPerfil> nqSegPerfil(String njpql) {
		List<SegPerfil> list = new ArrayList<SegPerfil>();
		try {
			list = (List<SegPerfil>) em.createNativeQuery(njpql, SegPerfil.class).getResultList();

			for (SegPerfil tb : list)
				em.refresh(tb);

		} catch (Exception e) {
		}
		return list;
	}

	public static List<SegUsuario> nqSegUsuario(String njpql) {
		List<SegUsuario> list = new ArrayList<SegUsuario>();
		try {
			list = (List<SegUsuario>) em.createNativeQuery(njpql, SegUsuario.class).getResultList();

			for (SegUsuario tb : list)
				em.refresh(tb);

		} catch (Exception e) {
		}
		return list;
	}

	public static SegPerfil buscarSegPerfil(String jpql) {
		try {
			Query query = em.createQuery(jpql);
			SegPerfil u = (SegPerfil) query.getSingleResult();
			em.refresh(u);
			return u;
		} catch (NoResultException e) {
			return null;
		}
	}

	public static SegUsuario buscarSegUsuario(String jpql) {
		try {
			Query query = em.createQuery(jpql);
			SegUsuario u = (SegUsuario) query.getSingleResult();
			em.refresh(u);
			return u;
		} catch (NoResultException e) {
			return null;
		}
	}

	// INGRESAR - ACTUALIZAR
	public static void saveOrUpdate(Object o, int op) {
		try {
			em.getTransaction().begin();
			if (op == 0) {
				em.persist(o);
			} else if (op == 1) {
				em.merge(o);
			}
			em.getTransaction().commit();
		} catch (Exception e) {
		}
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


	// SEGURIDAD
	public static String getClaveUsuario(String usuario) {
		Query query = em.createNativeQuery(
				" SELECT clave FROM seg_usuario WHERE id_usuario = '" + usuario + "' ");
		List<Object> result = query.getResultList();
		Iterator<Object> itr = result.iterator();
		Object obj = (Object) itr.next();
		String clave = String.valueOf(obj);

		return clave;
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
}