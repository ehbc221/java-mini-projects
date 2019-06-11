package fr.achat.util;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;


//Cette classe va nous permettre de convertir les variable de types LocalDate en xml et vice versa.
public class LocalDateAdapter extends XmlAdapter<String, LocalDate>
{
	
	@Override
	public LocalDate unmarshal(String str)
			throws Exception
	{
		return LocalDate.parse(str);
	}
	
	@Override
	public String marshal(LocalDate date)
		throws Exception
	{
		return date.toString();
	}
	
}
