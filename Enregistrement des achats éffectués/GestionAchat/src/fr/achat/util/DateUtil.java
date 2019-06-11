package fr.achat.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil 
{
	
	//Ce patern date nous servira à convertir la date en tring selon le modèle suivant ou autre (java doc).
	private static final String DATTE_PATERN = "dd/MM/yyyy";
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATTE_PATERN );
	
	public static String format(LocalDate date)
	{
		if(date==null)
		{
			return null;
		}
		
		return DATE_FORMATTER.format(date);
	}
	
	 
	public static LocalDate parse(String stringDate)
	{
		try
		{
			return DATE_FORMATTER.parse(stringDate, LocalDate::from);
		}catch(DateTimeParseException e)
		{
			return null;
		}
	}
	
	
	//Va controler si la conversion en string s'est bien faite dans le format définit par DATTE_PATERN
	public static boolean valideDate(String stringDate)
	{
		return DateUtil.parse(stringDate) != null;
	}
	
	
	
	
	
	
	

}
