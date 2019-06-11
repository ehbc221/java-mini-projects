


public class DemandeConges {
	static String ACCORDE ="accorde";
	static String ATTENTE= "attente";
	String dateDebut;
	String dateDemande;
	String dateFin;
	String etatValidation;
	static String REFUS="refus";
	public DemandeConges(String dateDebut, String dateFin) {
		super();
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}
	public DemandeConges(String dateDebut, String dateDemande, String dateFin) {
		super();
		this.dateDebut = dateDebut;
		this.dateDemande = dateDemande;
		this.dateFin = dateFin;
	}
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateDemande() {
		return dateDemande;
	}
	public void setDateDemande(String dateDemande) {
		this.dateDemande = dateDemande;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	public String getEtatValidation() {
		return etatValidation;
	}
	public void setEtatValidation(String etatValidation) {
		this.etatValidation = etatValidation;
	}
	public String toString() {
	
		return etatValidation;	
	}

}

