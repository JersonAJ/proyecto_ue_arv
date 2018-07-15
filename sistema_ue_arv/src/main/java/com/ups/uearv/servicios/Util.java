package com.ups.uearv.servicios;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;
import javax.persistence.Query;


/**
 * @author Jerson Armijos - Raysa Solano
 */

public class Util {


	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static int diferenciaEnDias(Date fechaMayor, Date fechaMenor) {		
		long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();		
		long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);		
		return (int) dias;
	}

	public static String stringToDate(String fecha) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		Date date = format.parse(fecha);		
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dt1.format(date);	
	}

	@SuppressWarnings("unchecked")
	public static List<SelectItem> llenaCombo(Query query, int valores) {
		List<Object> result = (List<Object>) query.getResultList();
		Iterator<Object> itr = result.iterator();

		List<SelectItem> items = new ArrayList<SelectItem>(result.size());
		for (int k = 0; k < result.size(); k++) {
			if (valores == 1) {
				Object obj = (Object) itr.next();
				items.add(new SelectItem(obj));
			} else if (valores == 2) {
				Object[] obj = (Object[]) itr.next();
				items.add(new SelectItem(String.valueOf(obj[0]), String.valueOf(obj[1])));
			}			
		}
		return items;
	}
	
	public static void main(String[] args) throws IOException {		
		
	}
}
