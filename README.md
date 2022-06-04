#TamaGame
TamaGame est une rédapation du jeu tamagotchi avec un gameplay simplifié.<br>
Dates : Octobre à Janvier (Partie Logique), Janvier à Avril   (Partie Graphique)

##Description
Pour réaliser le projet, j'ai utilisé :

* Les loggers
>Afin d'avoir une traçabilité de ce qu'il se passe dans le programme.<br>
Il y a une sortie console et fichier (TamaGame_log.txt).
Les loggers sont présents uniquement dans les classes dont j'ai jugé nécessaire de tracer.


* Le javaFX avec les FXML
>JavaFX pour la partie graphique bien évidemment.<br>
J'ai préféré utiliser les FXML pour avoir un résultat en temps réel via le Scene Builder.


* Les try/catch
>Afin d'éviter des crashs de l'application sans explication, vallait mieux utiliser les try/catch.<br>
Bien évidemment, le logger affichera l'exception et en fonction de cette dernière, soit c'est l'arrêt direct du jeu, soit il continue comme si de rien n'était.


* L'internationalisation (français, anglais et allemand)
>Le jeu est jouable en trois langue (merci Google Traduction).<br>
Petit +, on peut changer la langue sans devoir relancer toute le programme. Seule la partie en cours sera relancée.

* Les properties pour la sauvegarde
>Dans les sauvegardes on y trouve : les meilleurs scores (3 par difficulté, y'en a 8), la langue choisie, la difficulté choisie et s'il faut utiliser des noms aléatoires.

* Un peu de Collections
>Les Tamagoshis sont stockés dans une ArrayList pour les enlever plus facilement.<br>
J'utilise Collections.sort pour trier les scores par ordre décroissants.


##Installation
* Java 17.0.1
* JavaFX 17.0.1

## Licence

Copyright [2022] [Giaccaglia Matthieu]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.