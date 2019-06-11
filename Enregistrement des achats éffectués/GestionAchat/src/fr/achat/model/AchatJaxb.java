package fr.achat.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//Cette nouvelle classe s'impose pour donner à JAXB notre modèle Achat sans le Observable

@XmlRootElement(name = "achats")
public class AchatJaxb 
{
	private List<Achat> achats;
	
	@XmlElement(name = "achat")
	public List<Achat> getAchats()
	{
		return this.achats;
	}
	
	public void setAchats(List<Achat> achats)
	{
		this.achats = achats;
	}
	
	
}
