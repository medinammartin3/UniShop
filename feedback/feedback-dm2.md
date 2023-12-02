# Commentaires DM2

Corrigé par Marion Absalon

Total: 93%


## Description du système opérationnel [5/5]
Bien

## 6 diagrammes d'activité UML [25/25]
Bien

## Diagramme de classes UML [16/20]
- La classe Main a trop de responsabilités
- Il faut mettre les méthodes de gestion dans des contrôleurs
- Certaines relations de composition sont mal utilisées

## 5 diagrammes de séquence UML [21/25]
- Lorsqu'une input d'un acteur a lieu, une flèche doit partir de la lifeline de cet acteur pour qu'il puisse donner l'information au GUI
- Mauvaise utilisation des relations de composition

## Code source Java du programme et fichier JAR [21/25]
- Certains champs de saisie acceptent des caratères invalides
- Le Main a trop de responsabilités, il faut créer des contrôleurs

## Bonus: Utilisation de GitHub [3/5]
- Pas d'utilisation de branches ni de pull requests
