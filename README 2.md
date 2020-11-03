# Vokabeltrainer

In dieser Arbeit wurde ein Vocabeltrainer programmiert, welcher auf Grundlage einer komponentenbasierten Server Client Architektur in Java realisiert wurde. Der Aufbau wird detailiert in der doku.pdf Datei beschreiben. Die Vokabellisten können im Format des gnuVocabTrain (https://de.wikipedia.org/wiki/GnuVocabTrain) eingelesen werden. Das Spielprinzip ähnelt der App Quizduell (siehe https://de.wikipedia.org/wiki/Quizduell).

Die folgenden Absätze geben einen groben Überplick über die Applikationsarchitektur.

## Table of Contents 

- [Start](#Start)

- [Komponentenschnitt](#Komponentenschnitt)

- [Konzeptionelles Datenmodell](#Datenmodell)

- [Präsentationsschicht](#Präsentationsschicht)  

- [Frameworks](#Frameworks) 


<a name="Start"/>

## Start
Um das Projekt zu zu starten, muss in der ServerRestAdapter Komponente eine gewünschte MySQL DB in der Datein ServerRestAdapter/src/main/resources/META-INF/persistence.xml hinterlegt werden. 

Im Anschluss muss erst die ServerRestAdapter Komponente und im Anschluss die VocabularyTrainerGame Komponente gestartet werden (z.b. in Eclipse).

In dem Ordner src/vocabulary befindet sich eine Vokabelliste, welche eingelesen werden kann.

<a name="Komponentenschnitt"/>

## Komponentenschnitt
In diesem Kapitel werden die Komponenten des Informationssystems sowie deren Schnittstellenkommunikation anhand des in Abbildung 1 dargestellten Komponentendiagramms vorgestellt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/Komponentendiagramm.jpg)

Abbildung 1 Schnittstellen ohne RestAdapter

Die Darstellung lässt sich grob in zwei Bereiche unterteilen. Im unteren Bereich befinden sich die Server-Komponenten und im oberen die Client-Komponenten der Anwendung. 

### Server-Komponenten:
Die PlayerManagement-, GameManagment- und VocabularyManagement Komponenten exportieren jeweils ihre Service Schnittstellen, welche von der jeweiligen Service Controller Komponente im RestAdapter importiert werden. Die VocabularyManagement Komponente bietet zusätzlich eine VocabularySupply_Service Schnittstelle an, die von der GameManagement Komponente importiert wird. 

### Client-Komponenten:
Die PlayerClient_Adapter-, GameClient_Adapter- und VocabularyClient_Adapter Komponenten exportieren jeweils ihre Service Schnittstellen, welche von der jeweiligen ViewController Komponente im VocabularyTrainingGame Packet importiert werden. Zusätzlich importiert die PlayerViewController Komponente die GameManagement_Service- und die GameViewController Komponente die PlayerManagement_Service- und VocabularyManagement_Service Schnittstelle.

Der Datenfluss zwischen ServerRestAdapter und ClientRestAdapter wird durch eine HTTP REST-Schnittstelle realisiert.

<a name="Datenmodell"/>

## Konzeptionelles Datenmodell
Wie in Abbildung 2 dargestellt, besteht das Datenmodell aus insgesamt neun Klassen. Die Persistenz des Datenmodells wurde in diesem Projekt mithilfe der Java-Persistence-Api (JPA) durch den JPA-Provider Hibernate in einer MySQL Datenbank realisiert.  

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/Klassendiagramm.jpg)

Abbildung 2 Datenmodell

Die Datenhoheit über die Klassen ist wie folgt auf die Komponenten aufgeteilt:

| Name        | Klassen         | 
| ------------- |-------------| 
| GameManagement:	      | Game, Round, Answer, QuizQuestion | 
| PlayerManagement:    | Player, Score     |  
| VocabularyManagement:    | VocabularyList, Vocabulary, Word     | 
		
<a name="Präsentationsschicht"/> 

## Präsentationsschicht
In diesem Kapitel wird der Aufbau und die Funktionsweise der Präsentationsschicht beschrieben. In dieser Arbeit wurde die Präsentationsschicht in Form einer JavaFX Gui erstellt. Dafür wurden mithilfe des JavaFX Scene Builder  Oberflächen (Panes) erstellt und als fxml Dateien gespeichert. 

Folgend wird die Architektur der gesamten Gui sowie die einzelnen Oberflächen beschrieben.
 
### Architektur
In diesem Unterkapitel wird die Architektur der Präsentationsschicht beschrieben.
#### View und Controller
Insgesamt wurden mit dem JavaFX Scene Builder die in der folgenden Tabelle aufgelisteten fünf Oberflächen gestaltet und als fxml Dateien im Projekt abgelegt. Diese Dateien beinhalten Informationen der Gui Elemente im XML Format. 

| Name        | Controller           | Funktion  |
| ------------- |-------------| -----|
| Main.fxml	      | MainController.java | Stellt das Hauptfenster mit einer Tab-View dar|
| PlayerPane.fxml     | PlayerController.java      |   View für die Spielerverwaltung |
| GamePane.fxml | GameController.java	     |   View für die Spielverwaltung |
| NewGameDialog.fxml | NewGameDialogController.java     |   Dialog zur Erstellung von neuen Spielen |
| VocabPane.fxml | VocabController.java    |  View für das Einlesen von Vokabellisten |
                

Um eine strikte Trennung von Oberflächengestaltung und Funktionslogik zu realisieren, wurde für jede fxml View Datei eine Controllerklasse erstellt. Mithilfe dieser Controllerklasse kann auf die einzelnen Gui Elemente zugegriffen und deren Action-Listenermethoden implementiert werden. 

Beim Start der App wird die Main.fxml Ansicht und deren Main Controller geladen. Innerhalb des Main Controllers werden in Abhängigkeit des ausgewählten Tabs die entsprechende fxml Ansicht sowie der dazugehörige Controller geladen. In Abbildung 3 wird zur Verdeutlichung der Aufrufbaum der einzelnen Ansichten/Controller dargestellt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/Controller.png)

Abbildung 3 Controller Aufrufe

Die NewGameDialog Ansicht wird bei der Erstellung eines neuen Spiels aufgerufen. Diese wird durch den Klick eines Buttons in der GamePane View geladen. 

#### Client Adapter
Um mit dem Server zu kommunizieren, wurden die folgenden Client Adapter erstellt: 

GameClientAdapter	-> GameManagement_Service

PlayerClientAdapter	-> PlayerManagement_Service

VocabularyClientAdapter -> VocabularyManagement_Service

Diese realisieren die Serveranfrage mithilfe ihrer zugehörigen RestService Schnittstellen. Durch die Kommunikation mit dem Restadapter des Servers werden die entsprechenden Services angesprochen. 

Um einen möglichst flüssigen Spielablauf zu gewährleisten, werden die meisten Server-Anfragen in einem separaten Thread durchgeführt. Dies hat den Vorteil, dass die Gui während der Client Server Kommunikation nicht einfriert. Dafür befinden sich im Paket model diverse Worker Klassen, welche von der Klasse Task erben. Diese rufen über einen der Client Adapter jeweils einen Service auf. In den Controllern wurden weiterhin spezifische onWorkerDone Methoden implementiert, welche aufgerufen werden, sobald die Client Server Kommunikation beendet wurde. 

Ein Beispiel für die Erstellung eines Worker Thread: 

```java
DeletePlayerGamesWorker worker = new DeletePlayerGamesWorker(player);
setOnGameDeleteWorkerDone(worker);
Thread workerThread = new Thread(worker);
workerThread.start();
```

### MainPane
Die MainPane stellt eine leere TabPane Oberfläche dar. Die einzigen Gui-Elemente befinden sich mit den Tabbuttons Player, Game und Vocabulary im oberen Bereich. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/mainPane.png)
 
Abbildung 4 MainPane

In diese Oberfläche werden während der Ausführung des Programmes die in den folgenden Unterkapiteln vorgestellten Panes in Abhängigkeit des ausgewählten Tabs geladen. 

### PlayerPane
Auf der PlayerPane können Spieler der Anwendung betrachtet, gelöscht, bearbeitet und neu erstellt werden. In den folgenden Unterkapiteln werden genutzte Methoden der Schnittstellen sowie die Oberfläche und deren Verhalten beschrieben.

#### Angewandte Services der Schnittstellen
Alle im Bezug zur Datenbank stattfindenden Aktionen werden mithilfe von PlayerClientAdapter und GameClientAdapter Instanzen von der Client Seite angestoßen.  

PlayerClientAdapter
Der PlayerClientAdapter spricht über die Rest-Schnittstelle die folgenden Methoden der vorgestellten PlayerManagement_Service Schnittstelle an :

•	createPlayer		-> CreatePlayerWorker

•	editPlayerName (ohne Worker)

•	deletePlayer		-> DeletePlayerWorker

•	getAllPlayer		-> GetAllPlayerWorker


GameClientAdapter 
Der GameClientAdapter spricht über die Rest-Schnittstelle die folgende Methode der in Unterkapitel 2.4 vorgestellten GameManagement_Service Schnittstelle an :


•	deleteAllGamesByPlayer	-> DeletePlayerGamesWorker


#### PlayerPane Oberfläche
Die PlayerPane wird als erste Oberfläche beim Start des Programmes geladen und angezeigt. In Abbildung 5 wird die PlayerPane Ansicht mit exemplarischen Spielerdaten dargestellt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/PlayerPane.png)

Abbildung 5 PlayerPane

Auf der linken Seite ist eine Liste mit allen Spielernamen sowie einem Plus-   , Minus-   und Bearbeitungsbutton    sichtbar. Die Spielerliste wird bei der Initialisierung der Ansicht durch die getAllPlayer Servicemethode mit allen Spielern aus der Datenbank befüllt. Durch einen Klick auf den Plusbutton öffnet sich ein Eingabedialog, welcher den User auffordert, den Namen eines neuen Spielers einzugeben. Nach der Eingabe des Namens und der Bestätigung des OK Buttons wird der Spieler durch die createPlayer Servicemethode in der Datenbank gespeichert und der Liste hinzugefügt. Falls dieser Name bereits an einen anderen Spieler vergeben ist, erhält der User eine Mitteilung, dass der Spieler aufgrund einer Namensdoppelung nicht angelegt werden konnte.  Äquivalent dazu funktioniert der Bearbeitungsbutton, wobei die Auswahl eines Spielers aus der Liste vor dessen Klick vorausgesetzt wird. Durch eine erfolgreiche Bearbeitung wird durch die editPlayerName Servicemethode der neue Name für den Spieler in der Datenbank hinterlegt und in der Spielerliste entsprechend geändert. 

Die Auswahl des Minusbuttons ermöglicht es dem User einen Spieler zu löschen. Auch hierbei wird die Auswahl eines Spielers aus der Liste vorausgesetzt. Vor der Löschung wird der User, wie in Abbildung 6 dargestellt, darauf hingewiesen, dass neben dem Spieler auch alle mit ihm verbundenen Spiele gelöscht werden. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/hinweis1.png)
 
Abbildung 6 Hinweis bei der Löschung eines Spieler

Durch einen Klick auf OK wird die Löschung mithilfe der deleteAllGamesByPlayer und deletePlayer Servicemethoden durchgeführt.  

Weiterhin ist in Abbildung 5 im mittleren Bereich ein Balkendiagramm sowie der Spielername des ausgewählten Spielers zu sehen. In dem Balkendiagramm wird die jeweilige Anzahl an verlorenen, gewonnen oder in Gleichstand geendeten Spiele des Spielers angezeigt. 

Im unteren rechten Bereich befindet sich ein Aktualisierungsbutton  . Durch einen Klick auf den Button wird die PlayerPane mithilfe der getAllPlayer Servicemethode aktualisiert. Weiterhin dient dieser Button als Indikator für Hintergrundaktivitäten (siehe Abbildung 7). 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/hinweis2.png)
 
Abbildung 7 Spieler Ladeaktivität

In diesem Fall wird links neben dem Button ein Schriftzug mit der Beschreibung der Aktivität angezeigt.  Zusätzlich dreht sich der Pfeil des Buttons. Der Button ist währenddessen deaktiviert und kann nicht angeklickt werden. 
 
  
### GamePane  
Auf der GamePane können Spiele gespielt und gelöscht werden. Ausgehend von der GamePane kann die NewGameDialog Ansicht als Dialogfenster geöffnet werden, um ein neues Spiel zu erstellen. Da diese Panes eng miteinander verbunden sind, werden diese in diesem Unterkapitel gemeinsam betrachtet.

In den folgenden Abschnitten werden genutzte Methoden der Schnittstellen sowie die Oberfläche und deren Verhalten beschrieben.

#### Angewandte Services der Schnittstellen
Alle im Bezug zur Datenbank stattfindenden Aktionen werden mithilfe von PlayerClientAdapter, GameClientAdapter und VocabularyClientAdapter Instanzen von der Client Seite angestoßen.  




PlayerClientAdapter
Der PlayerClientAdapter spricht über die Rest-Schnittstelle die folgenden Methoden der vorgestellten PlayerManagement_Service Schnittstelle an:

•	getAllPlayer -> GetAllPlayerWorker


GameClientAdapter 
Der GameClientAdapter spricht über die Rest-Schnittstelle die folgenden Methoden der vorgestellten GameManagement_Service Schnittstelle an:

•	createGame -> CreateGameWorker

•	deleteGame -> DeleteGameWorker

•	getAllGamesByPlayer -> GetAllGameWorker

•	updateGame -> UpdateGameWorker


Weiterhin werden innerhalb der GameManagement_Service Schnittstelle die folgenden VocabularySupply_Service Schnittstellenmethoden aufgerufen: 

•	getVocabularyList

•	getWords


VocabularyClientAdapter 
Der VocabularyClientAdapter spricht über die Rest-Schnittstelle die folgende Methode der in Unterkapitel 2.2 vorgestellten VocabularyManagement_Service Schnittstelle an:

•	getAllVocabularyListsMainInformation-> GetAllVocabularyListDTOWorker


#### GamePane Oberfläche
Die GamePane wird durch einen Klick auf den Tab Game im oberen Bereich des Programmes geladen und angezeigt. In Abbildung 8 wird die GamePane Ansicht mit exemplarischen Spieler- und Spieldaten dargestellt. 

 ![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/GamePane.png)
 
Abbildung 8 GamePane

Grundlegend ähneln sich viele der vorab gezeigten PlayerPane Elemente den GamePane Elementen. Auf der linken Seite ist eine Liste mit allen Spielen sowie einem Plus-    und Minusbutton   sichtbar. 

Durch einen Klick auf den Plusbutton öffnet sich der vorab beschriebene NewGameDialog um eine neues Spiel zu erstellen. Die Auswahl des Minusbuttons ermöglicht es dem User ein Spiel zu löschen. Hierbei wird die Auswahl eines Spielers aus der Liste vorausgesetzt. Die Löschung wird mithilfe der deleteGame Servicemethode in der Datenbank realisiert. 

Weiterhin ist ein Dropdownbutton im rechten oberen Bereich zu sehen. Bei der Initialisierung der GamePane wird dieser Dropdownbutton durch die getAllPlayer Servicemethode mit allen Spielern aus der Datenbank befüllt. Sobald der User über diesen Button einen Spieler auswählt, werden mithilfe der getAllGamesByPlayer Servicemethode alle Spiele eines Spielers, absteigend nach Erstellungsdatum geordnet, in die linke Liste geladen. Dabei wird jedes Spiel mit einer Spiel ID und dem Namen des Gegenspielers angezeigt. Außerdem färbt sich der Hintergrund eines Spieles in eine von drei unterschiedlichen Farben.   

Rot 
Die rote Farbe signalisiert, dass ein Spiel beendet ist. Durch einen Klick auf ein rotes Spiel wird, wie in Abbildung 9 dargestellt, das Ergebnis sowie eine Auswertung angezeigt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/gleichstand.png)
 
Abbildung 9 Ansicht Spiel beendet

Die Spiele können einsehen, welche Frage, von wem, wie beantwortet wurde. 

Gelb
Die gelbe Farbe signalisiert, dass der Mitspieler am Zug ist. Durch einen Klick auf ein gelbes Spiel wird dies dem User als Schriftzug angezeigt. 

Grün
Die grüne Farbe signalisiert, dass der User an der Reihe ist um Fragen zu beantworten. Durch einen Klick auf ein grünes Spiel werden, wie in Abbildung 10 dargestellt, vier verschiedene Antwortmöglichkeiten für ein Fragewort in Form von Buttons angezeigt.

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/restaurante.png)
 
Abbildung 10 Ansicht Fragen beantworten

Sobald der User auf eine Antwort klickt, wird diese auf ihre Richtigkeit überprüft und mithilfe der updateGame Servicemethode in der Datenbank hinterlegt. In dieser Implementierung spielt jeder Spieler drei Runden. Sobald die letzte Runde gespielt wurde, wird neben der Richtigkeitsprüfung das Endergebnis bestimmt. 

Wie bereits beschrieben, befindet sich auch auf der GamePane im unteren rechten Bereich ein Aktualisierungsbutton  . Durch einen Klick auf den Button wird die GamePane mithilfe der getAllPlayer Servicemethode aktualisiert. Weiterhin dient dieser Button auch hier als Indikator für Hintergrundaktivitäten.

#### NewGameDialog Oberfläche
Der NewGameDialog wird, wie bereits vorab beschrieben, durch einen Klick auf den Plusbutton   in der GamePane Oberfläche geladen und angezeigt. In Abbildung 11 wird die NewGameDialog Ansicht mit exemplarischen Spieler- und Vokabeldaten dargestellt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/newGame.png)
 
Abbildung 11 NewGameDialog Pane

In dem Dialog werden vier verschiedene Dropdownbuttons angezeigt. Alle Dropdownbuttons werden bei der Initialisierung des Dialoges mithilfe der getAllVocabularyListsMainInformation   und getAllPlayer Servicemethoden aus der Datenbank geladen. Sobald der User nach der Auswahl auf den Start-Button klickt, wird das Spiel durch die createGame Servicemethode in der Datenbank abgelegt und der Dialog geschlossen. Diese greift dabei auf die getVocabularyList und getWords Servicemethoden der VocabularySupply_Service Schnittstelle zu. 

### VocabPane  
Auf der VocabPane können Vokabellisten eingesehen und abgelegt/eingelesen werden. 

In den folgenden Abschnitten werden genutzte Methoden der Schnittstellen sowie die Oberfläche und deren Verhalten beschrieben.

#### Angewandte Services der Schnittstellen
Alle im Bezug zur Datenbank stattfindenden Aktionen werden mithilfe einer VocabularyClientAdapter Instanz von der Client Seite angestoßen.  

VocabularyClientAdapter
Der VocabularyClientAdapter spricht über die Rest-Schnittstelle die folgenden Methoden der in Unterkapitel 2.2VocabularyManagement – VocabularyManagement_Service vorgestellten VocabularyManagement_Service Schnittstelle an:

•	createVocabularyList -> CreateVocabularyListWorker

•	getAllVocabularyLists -> GetAllVocabularyListsWorker


#### VocabPane Oberfläche
Die VocabPane wird durch einen Klick auf den Tab Vocabulary im oberen Bereich des Programmes geladen und angezeigt. In Abbildung 12 wird die VocabPane Ansicht mit exemplarischen Vokabeldaten dargestellt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/vocabPane.png)
 
Abbildung 12 VocabPane

Die VocabularyPane ist in ihrem Funktionsumfang recht einfach aufgebaut. Mittig ist ein DataTree Fenster, welches die in der Datenbank verfügbaren Vokabellisten beinhaltet, zu sehen. Bei der Initialisierung der VocabularyPane werden diese VocabularyList Objekte mithilfe der getAllVocabularyLists Servicemethode geladen und in das DataTree Objekt abgelegt. 

Der Tree wird dabei folgendermaßen aufgebaut:

Buchtitel

--> Kapiteltitel

----> VokabelID

------> Sprache1

--------> Wort

--------> Wort

------> Sprache2

--------> Wort

--------> Wort


Durch einen Klick auf den Uploadbutton öffnet sich ein FileChooser Fenster. Dadurch hat der User die Möglichkeit die gewünschte Vokabelliste (TXT Datei im entsprechenden Format) auszuwählen. Sobald er die Auswahl mit OK bestätigt, wird mithilfe der createVocabularyList Servicemethode die Vokabelliste erstellt und in der Datenbank abgelegt. Dabei wird überprüft, ob sich bereits ein Buchtitel mit dem Kapiteltitel in der Datenbank befindet. Duplikate werden nicht zugelassen und mit einer Nachricht angezeigt.

Wie bereits bei den anderen Panes beschrieben, befindet sich auch auf der VocabPane im unteren rechten Bereich ein Aktualisierungsbutton  . Durch einen Klick auf den Button wird die VocabPane mithilfe der getAllVocabularyLists Servicemethode aktualisiert. Weiterhin dient dieser Button auch hier als Indikator für Hintergrundaktivitäten.

<a name="Frameworks"/>

## Frameworks
In diesem Kapitel werden die in dem Projekt verwendeten Frameworks beschrieben. Um einen Überblick über die verwendeten Frameworks zu erhalten, werden diese, repräsentiert durch ihr Logo, in Abbildung 13 verteilt auf die einzelnen Maven Projekte dargestellt. 

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/frameworks.png)

Abbildung 13 Frameworks in Projekten

Für den Überblick wurden die Maven Projekte in Gruppen zusammengefasst. Jede Gruppe wurde wiederum der Server- oder Client-Seite zugeordnet. 

In den folgenden Unterkapiteln werden die verwendeten Frameworks und ihre Konfiguration genauer beschrieben. 

## Ablaufumgebung
Die Ablaufumgebung der Anwendung wird in Abbildung 14 dargestellt.

![alt text](https://github.com/SwiftJimmy/Vokabeltrainer/blob/main/src/images/Anwendungsumgebung.png)

Abbildung 14 Ablaufumgebung

Der Rest-Server dient in der Anwendung als zentrales Element.  Dieser nimmt die eintreffenden HTTP Requests der Clients entgegen, arbeitet diese mit einem schreibenden und lesenden Zugriff auf der MySQL Datenbank ab und sendet eine HTTP JSON Respons als Antwort an den entsprechenden Client zurück. Bei dem Vokabel-Trainer-Spiel handelt es sich um eine Mehrbenutzeranwendung. Das bedeutet, dass eine Vielzahl an Clients gleichzeitig Anfragen an den Server senden können und den aktuellen State der Datenbank als Antwort bekommen. 





















