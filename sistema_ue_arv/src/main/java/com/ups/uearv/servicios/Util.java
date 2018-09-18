package com.ups.uearv.servicios;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;
import javax.persistence.Query;

/**
 * @author Jerson Armijos - Raysa Solano
 */

public class Util {

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

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

	public static String stringToDate(String fecha) throws ParseException {
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

	public static String generaSHA256(String password) throws NoSuchAlgorithmException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());
		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();

		// convert the byte to hex format method 2
		// StringBuffer hexString = new StringBuffer();
		// for (int i=0;i<byteData.length;i++) {
		// String hex=Integer.toHexString(0xff & byteData[i]);
		// if(hex.length()==1) hexString.append('0');
		// hexString.append(hex);
		// }
		// System.out.println("Hex format : " + hexString.toString());
	}

	public static String validaCedula(String cedula) {
		// Declaración de variables a usar
		String mensaje = "";
		byte primeros2, tercerD, Dverificador, multiplicar, suma = 0, aux;
		byte[] digitos = new byte[9];

		// Primer try comprueba la longitud de cadena que no sea diferente de 10
		try {
			if (cedula.length() != 10) 
				return "La c\u00e9dula debe contener 10 d\u00edgitos sin espacios";

			// Segundo try comprueba que todos los dígitos sean numéricos
			try {
				// Transformación de cada carácter a un byte
				Dverificador = Byte.parseByte("" + cedula.charAt(9));
				primeros2 = Byte.parseByte(cedula.substring(0, 2));
				tercerD = Byte.parseByte("" + cedula.charAt(2));
				for (byte i = 0; i < 9; i++) {
					digitos[i] = Byte.parseByte("" + cedula.charAt(i));
				}

				// Verificar segundo dígito
				if (primeros2 >= 1 & primeros2 <= 24) {
					if (tercerD <= 6) {

						// Módulo 10 multiplicar digitos impares por 2
						for (byte i = 0; i < 9; i = (byte) (i + 2)) {
							multiplicar = (byte) (digitos[i] * 2);
							if (multiplicar > 9) {
								multiplicar = (byte) (multiplicar - 9);
							}
							suma = (byte) (suma + multiplicar);
						}

						// Módulo 10 multiplicar digitos pares por 1
						for (byte i = 1; i < 9; i = (byte) (i + 2)) {
							multiplicar = (byte) (digitos[i] * 1);
							suma = (byte) (suma + multiplicar);
						}

						// Obtener la decena superior de la suma
						aux = suma;
						while (aux % 10 != 0) {
							aux = (byte) (aux + 1);
						}
						suma = (byte) (aux - suma);

						// Comprobar la suma con dígito verificador (Último Dígito)
						if (suma != Dverificador) 						
							return "Revise nuevamente los d\u00edgitos de su c\u00e9dula";

						mensaje = "OK";						
					}
				}
			} catch (NumberFormatException e) {				
				return "La c\u00e9dula debe contener solo d\u00edgitos num\u00e9ricos";
			}
		} catch (Exception e) {
			return e.getMessage();			
		}
		return mensaje;		
	}

	public static String validarCorreo(String correo) {
		String msg = "OK";		
		//Patrón para validar el email
		Pattern pat = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
		// El email a validar
		if (!correo.equals("")) {
			Matcher mather = pat.matcher(correo);
			if (mather.find() != true) 
				msg = "El correo ingresado es inválido.";	
		}		
		return msg;
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		System.out.println(validarCorreo("jarmijos"));
		System.out.println(validarCorreo("rsolano@gmail.com"));		
		System.out.println(validarCorreo("rsolano@asda@sas.asasasa.sasas"));
		System.out.println(validarCorreo(""));		
	}
}
