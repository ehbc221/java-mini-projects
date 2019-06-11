select article.idarticle,client.idclient,facture.montant ,Compte.libele,mouvement_compte.entree,
(select 100*mouvement_compte.entree/facture.montant) as Pourcentage,
from article,client,facture,mouvement_compte,compte where
article.idclient=client.idclient and
article.idfacture=facture.idfacture and
mouvement_compte.idcompte=compte.idcompte and
mouvement_compte.idfacture=article.idfacture 
and article.etat='cloturé' AND
 mouvement_compte.date between '2010-4-2' and '2011-3-1'
