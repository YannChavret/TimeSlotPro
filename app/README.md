/* Cette application est un projet de fin de formation. 

Appli de gestion destinée aux coachs/profs dispensant des cours collectifs

Exemple de cibles : atelier de sculpture, coaches de yoga, profs de chant


Interface admin permettant de :
- Créer des créneaux de cours : Date, horaires, thème, niveau, nombre limite de participant
- Voir le planning global des cours sous forme d’agenda (avec indicateur de cours remplis)
- Voir l’état de reservation d’un cours ( liste des élèves participants)
- Modifier l’adhésion d’un élève

Interface élève permettant de :
- Souscrire un abonnement au mois, trimestre ou année.
- S’inscrire à un cours sur le créneau de son choix en fonction de son niveau, dans la limite de son crédit de cours et du nombre de cours déjà effectué sur le mois glissant.
- Consulter ses reservations, son solde de cours restant
- Annuler ou modifier une inscription (max 24h avant le début du cours)


Contraintes :
- 1 élève peut prendre une adhésion pour 1 mois, 3 mois ou 1 an.
- 1 élève peut reserver un maximum de 4 cours par mois.
- 1 élève peut avoir un niveau débutant ou confirmé
- 1 élève débutant ne peut s’inscrire qu’à des cours débutant, un élève confirmé peut s’inscrire au cours de son choix.
- 1 cours dure une demi journée de 3 heures
- 1 cours est limité à x participants (le créneau est grisé lorsqu’il est rempli)
*/
