/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javamail;

import entity.Abonnement;
import entity.ChargeCompte;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import session.AbonnementFacade;

/**
 *
 * @author 
 */
public class JavaMail {

    public JavaMail() {
    }

    private void sendMail(String to, String subject, String messsage) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.nom.domain");
        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("service.client@nom.domain"));
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setRecipients(Message.RecipientType.CC, "cc.mail@nom.domain");
            msg.setRecipients(Message.RecipientType.BCC, "cci.mail@nom.domain");
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(messsage, "iso-8859-15", "html");
            Transport.send(msg);
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }

    public void mailRappelEtablierFacture() {  // j-35
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourRappelEtablierFacture();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    j += "<tr>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                            + "</tr>";
                }
                msg += "Les abonnenements des clients suivants toucheront a leurs fin dans 35 jours. Veuillez leurs préparer les factures pour les prochains abonnements" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";
                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";
                msg += j;
                msg += "</table>";
                sendMail(chargeCompte.getEmail(), "Reppel de fin d'abonnemt J-35", msg);
                System.out.println(msg);
            }
        }
    }

    public void mailRappelIntroduireFacture() {  // j-30
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourRappelIntroduireFacture();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                System.out.println(chargeCompte.getNom() + " " + chargeCompte.getNom() + "<" + chargeCompte.getEmail() + ">\n-------------------------------");
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    j += "<tr>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                            + "</tr>";
                }
                msg += "Les abonnenements des clients suivants toucheront a leurs fin dans 30 jours. Veuillez introduir les nouvelles factures pour les prochains abonnements" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";

                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";

                msg += j;
                msg += "</table>";
                sendMail(chargeCompte.getEmail(), "Reppel de fin d'abonnemt J-30", msg);
                System.out.println(msg);
            }
        }
    }

    public void mailRappelReglementFacture() {  // j-15
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourRappelReglementFacture();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                System.out.println(chargeCompte.getNom() + " " + chargeCompte.getNom() + "<" + chargeCompte.getEmail() + ">\n-------------------------------");
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    j += "<tr>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                            + "</tr>";
                }
                msg += "Les abonnenements des clients suivants toucheront a leurs fin dans 15 jours. Veuillez leurs envoyer les factures de leurs prochains abonnements" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";

                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";

                msg += j;
                msg += "</table>";
                sendMail(chargeCompte.getEmail(), "Reppel de fin d'abonnemt J-15", msg);
                System.out.println(msg);
            }
        }
    }

    public void mailDebutAbonnement() {  // j
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourDebutAbonnement();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                System.out.println(chargeCompte.getNom() + " " + chargeCompte.getNom() + "<" + chargeCompte.getEmail() + ">\n-------------------------------");
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    j += "<tr>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                            + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                            + "</tr>";
                }
                msg += "Les abonnenements des clients suivants sont commencés dès aujourd'hui" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";

                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";

                msg += j;
                msg += "</table>";
                sendMail(chargeCompte.getEmail(), "Reppel de fin d'abonnemt J-0", msg);
                System.out.println(msg);
            }
        }
    }

    public void mailRelanceReglementFacture() {  // j+15
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourRelanceReglementFacture();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                System.out.println(chargeCompte.getNom() + " " + chargeCompte.getNom() + "<" + chargeCompte.getEmail() + ">\n-------------------------------");
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                boolean canSendMail = false;
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    if (abonnement.getFactureRegle() == 0) {
                        j += "<tr>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                                + "</tr>";
                        canSendMail = true;
                    }
                }
                msg += "Les factures des abonnenements des clients suivants ne sont pas encore réglées, veuillez relancer vos client et les informer qu'il seront couper dans 7 jours" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";

                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";

                msg += j;
                msg += "</table>";
                if (canSendMail) {
                    sendMail(chargeCompte.getEmail(), "Reglement de la facture J+15", msg);
                    System.out.println(msg);
                }
            }
        }
    }

    public void mailRappelDeCoupure() {  // j+20
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourReppelDeCoupure();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                System.out.println(chargeCompte.getNom() + " " + chargeCompte.getNom() + "<" + chargeCompte.getEmail() + ">\n-------------------------------");
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                boolean canSendMail = false;
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    if (abonnement.getFactureRegle() == 0) {
                        j += "<tr>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                                + "</tr>";
                        canSendMail = true;
                    }
                }
                msg += "Les factures des abonnenements des clients suivants ne sont pas encore réglées, veuillez relancer vos client et les informer qu'ils seront couper demin" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";

                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";

                msg += j;
                msg += "</table>";
                if (canSendMail) {
                    sendMail(chargeCompte.getEmail(), "Reglement de la facture J+20", msg);
                    System.out.println(msg);
                }
            }
        }
    }

    public void mailDeCoupure() {  // j+21
        AbonnementFacade abonnementFacade = new AbonnementFacade();
        List<ChargeCompte> listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getListMailChargeClientPourCoupure();
        for (ChargeCompte chargeCompte : listMailChargeClientPourRappelEtablierFacture) {
            if (!chargeCompte.getAbonnementsList().isEmpty()) {
                System.out.println(chargeCompte.getNom() + " " + chargeCompte.getNom() + "<" + chargeCompte.getEmail() + ">\n-------------------------------");
                String msg = "Bonjour " + chargeCompte.getPrenom() + "<br/>";
                String j = "";
                boolean canSendMail = false;
                for (Abonnement abonnement : chargeCompte.getAbonnementsList()) {
                    if (abonnement.getFactureRegle() == 0) {
                        j += "<tr>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getNomClient() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getContact() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getEmailClient() + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateDebut()) + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + new SimpleDateFormat("dd/MM/YYYY").format(abonnement.getDateFin()) + "</td>"
                                + "<td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\">" + abonnement.getReste() + " Jours </td>"
                                + "</tr>";
                        canSendMail = true;
                    }
                }
                msg += "Les factures des abonnenements des clients suivants ne sont pas encore réglées, par consequant ils seront coupés aujourd'hui" + "<br/>";
                msg += "Merci" + "<br/><br/><br/>";
                msg += "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\">";

                msg += "<tr bgcolor=\"#0acff1\">";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Client</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Contact</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">E-mail</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date debut</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Date fin</font></strong></td>";
                msg += "<td width=\"30%\"><strong><font color=\"#FFFFFF\">Jours restant avant fin d'abonnement</font></strong></td>";
                msg += "</tr>";
                msg += j;
                msg += "</table>";
                if (canSendMail) {
                    sendMail(chargeCompte.getEmail(), "Reglement de la facture J+21", msg);
                    System.out.println(msg);
                }
            }
        }
    }

    public void sendMails() {
        mailRappelEtablierFacture();
        mailRappelIntroduireFacture();
        mailRappelReglementFacture();
        mailDebutAbonnement();
        mailRelanceReglementFacture();
        mailRappelDeCoupure();
        mailDeCoupure();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new JavaMail().sendMails();
    }
}