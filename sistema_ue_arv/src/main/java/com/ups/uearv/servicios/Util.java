package com.ups.uearv.servicios;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
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

	public static int diferenciaEnMeses(Date fechaInicio, Date fechaFin) throws ParseException {
		try {
			Calendar inicio = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			inicio.setTime(fechaInicio);
			fin.setTime(fechaFin);
			int difA = fin.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
			int difM = difA * 12 + fin.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);		
			return difM;	
		} catch (Exception e) {
			return 0;
		}			
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

		if (result.isEmpty()) {
			List<SelectItem> items = new ArrayList<SelectItem>(1);
			if (valores == 1) {	
				items.add(new SelectItem("No existen datos"));
			} else if (valores == 2) {				
				items.add(new SelectItem("NA","No existen datos"));
			}
			return items;
		}

		Iterator<Object> itr = result.iterator();
		List<SelectItem> items = new ArrayList<SelectItem>(result.size());
		for (int k = 0; k < result.size(); k++) {
			if (valores == 1) {
				Object obj = (Object) itr.next();
				items.add(new SelectItem(obj.toString()));
			} else if (valores == 2) {
				Object[] obj = (Object[]) itr.next();
				items.add(new SelectItem(obj[0].toString(), obj[1].toString()));
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
				} else
					return "Revise nuevamente los d\u00edgitos de su c\u00e9dula";
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
				
	public static List<SelectItem> llenaComboOfertas() {
		return Util.llenaCombo(DAO.getOfertas("1"), 2);
	}
	
	public static BufferedImage decodeToImage(String imageString) {		 
        BufferedImage image = null;
        byte[] imageByte;
        try {            
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {					
	
		decodeToImage("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");
	
	}
}
