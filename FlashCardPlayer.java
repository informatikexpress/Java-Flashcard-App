import javax.swing.*; //Ein großer Werkzeugkasten namens swing wird importiert der * bedeutet alle Werkzeuge.
import java.awt.*; // Abstract Window Toolkit für die Font
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList; // Ein spezielles Werkzeug damit das Array wächst, wenn du Karteikarten hinzufügst.
import java.util.Iterator;
import java.util.StringTokenizer;

public class FlashCardPlayer { //Bauplan

    private JTextArea display; // Es wird Platz für ein einziges, großes Textfeld reserviert.
    private JTextArea answer; // Es wird Platz für ein zweites Textfeld reserviert.
    private ArrayList<FlashCard> cardList; //Es wird eine flexible Liste erstellt.<FlashCard> sagen der Liste das es Objekte vom Typ FlashCard speichern soll.
    private Iterator cardIterator; // Der Zeigefinger, der sich später merkt, bei welcher Karte in der Liste wir gerade sind.
    private FlashCard currentCard; // Reserviert Platz für genau eine Karte. Hier merkt sich das Programm, welche Karteikarte sich in diesem Moment angeschaust wird.
    private int currentCardIndex; // Merkt sich als Zahl, bei der wievielten Karte in der Liste man gerade ist (z. B. Karte 3).
    private JButton showAnswer; // Es wird Platz für den Knopf zum umdrehen der Karte reserviert.
    private JFrame frame; // Es wird ein Platz für das Hauptfenster reserviert.
    private boolean isShowAnswer; // Ein Schalter (kann nur true oder false sein). Er merkt sich: "Sehe ich gerade die Vorderseite oder die Rückseite?"


    public FlashCardPlayer() { // Der Konstruktor (Der Startknopf, baut das Fenster auf)
        // Build UI
        frame = new JFrame("Flash Card Player"); // Erstellt das eigentliche Anwendungsfenster mit dem neuen Titel "Flash Card Player".
        JPanel mainPanel = new JPanel(); // Der Hauptcontainer, auf dem wir später alle unsere Elemente platzieren.
        Font mFont = new Font("Arial", Font.BOLD, 22); // Definiert eine Schriftart, die wir verwenden werden (Arial, Fettgedruckt, Schriftgröße 22).

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ohne diese Zeile würde das Fenster verschwinden, aber das Programm im Hintergrund weiterlaufen.

        display = new JTextArea(10, 20); // Erstellt ein mehrzeiliges Textfeld. Es ist so dimensioniert, dass standardmäßig 10Zeilen und 20Zeichen pro Zeile gut hineinpassen.
        display.setFont(mFont); // Weist dem Textfeld die oben erstellte Schriftart zu.

        JScrollPane qJScrollPane = new JScrollPane(display); // Da eine JTextArea von selbst keine Scrollbalken hat, wird das Textfeld "eingepackt" in ein Scroll-Panel.
        qJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Der vertikale Scrollbalken wird immer angezeigt, auch wenn noch garnicht genug Text da ist.
        qJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Der horizontale Scrollbalken wird niemals angezeigt.

        showAnswer = new JButton("Show Answer"); // Erstellt den klickbaren Button mit der Aufschrift "Show Answer".
        // Hier werden alle zuvor erstellten Bausteine dem mainPanel hinzugefügt.
        mainPanel.add(qJScrollPane); // Wichtig: Du fügst nicht die JTextArea direkt hinzu, sondern die JScrollPane (welche das Textfeld bereits enthält).
        mainPanel.add(showAnswer); // Der Button wird dem Panel hinzugefügt.
        showAnswer.addActionListener(new NextCardListener()); // Dem Button werden "Ohren" gegeben. Er wird mit der NextCardListener-Klasse (unten) verkabelt, damit er auf Klicks reagiert.

        // Add Menu
        JMenuBar menuBar = new JMenuBar(); // Das ist das Hauptfundament. Es stellt den durchgehenden, meist grauen Balken ganz oben im Fenster. Er ist anfangs noch komplett leer.
        JMenu fileMenu = new JMenu("File"); // Das ist ein Hauptreiter in der Menüleiste(wie "Datei", "Bearbeiten", "Ansicht"). Wenn du später darauf klickst, klappt ein DropdownMenü nach unten auf.
        JMenuItem loadMenuItem = new JMenuItem("Load Card Set"); // Das sind die eigentlichen, klickbaren Einträge (Items), die in dem Dropdown-Menü auftauchen sollen.
        loadMenuItem.addActionListener(new OpenMenuListener()); // Der "Laden"-Eintrag bekommt "Ohren" und wird mit der OpenMenuListener-Klasse verkabelt.

        fileMenu.add(loadMenuItem); // Der klickbare Eintrag wird in das "File"-Menü gesteckt.
        menuBar.add(fileMenu); // Das fertige "File"-Menü wird in den großen Menübalken gesteckt.

        // Add to frame
        frame.setJMenuBar(menuBar); // Nimmt den fertigen Balken und tackert es an das Hauptfenster.
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel); // Legt das fertige mainPanel(mit allen Elementen drauf) in die Mitte(BorderLayout.CENTER) des Hauptfensters.
        frame.setSize(640, 500); // Gibt dem Fenster eine feste Startgröße von 640 Pixeln Breite und 500 Pixeln Höhe.
        frame.setVisible(true); // Macht das Fenster auf dem Bildschirm sichtbar. Ohne diesen Befehl würde das Programm zwar laufen, aber du würdest nichts sehen.

    }
    public static void main(String[] args) { // Diese Methode muss drinn stehen. String[] args ist ein Array, um beim Start über das Terminal eine kleine Information mitzugeben um zum Beispiel ein Programm zu schreiben das Leute begrüßt.
        SwingUtilities.invokeLater(new Runnable() { // Ein Methodenaufruf um etwas in die Warteschlange vom Event Dispatch Thread zu packen. New Runnable() ist ein Packet in dem ein Vertrag drinn ist welches der Thread ausführt.
            @Override // Eine Anmerkung, dass das was im Vertrag steht eingehalten werden muss.
            public void run() { // Muss laut Vertrag diese Methode enthalten.
                new FlashCardPlayer(); // Neues Objekt, welches denn Konstruktor aufruft.
            }
        });
    }
    class NextCardListener implements ActionListener { // Die Klasse, die zuhört, wenn der "Show Answer" / "Next Card" Button geklickt wird.

        @Override
        public void actionPerformed(ActionEvent e) { // Diese Methode wird bei jedem einzelnen Klick ausgeführt.

            if (isShowAnswer) { // Prüft den Zustand des internen Schalters: Ist isShowAnswer "true"? Das bedeutet: Aktuell wird die Frage auf dem Bildschirm angezeigt.
                display.setText(currentCard.getAnswer()); // Wenn ja: Hole dir mit dem Getter die Antwort aus der aktuellen Karteikarte und zeige sie im großen Textfeld an.
                showAnswer.setText("Next Card"); // Ändere die Aufschrift des Buttons von "Show Answer" zu "Next Card".
                isShowAnswer = false; // Wichtig: Lege den Schalter um auf "false". Beim nächsten Klick weiß das Programm dann, dass die Antwort bereits sichtbar ist.
            } else { // Wenn isShowAnswer "false" ist. Das bedeutet: Aktuell wird bereits die Antwort auf dem Bildschirm angezeigt.

                //show next question
                if (cardIterator.hasNext()) { // Der Iterator (Zeigefinger) schaut in unserer Liste nach: Gibt es noch eine weitere Karteikarte nach der aktuellen?

                    showNextCard(); // Wenn ja: Rufe die Hilfsmethode auf, die die nächste Karte lädt und deren Frage auf dem Bildschirm anzeigt.

                } else { // Wenn der Iterator sagt: Nein, es gibt keine weiteren Karten mehr (wir haben die ganze Liste durchgelernt).
                    //no more cards
                    display.setText("That was last card."); // Zeige diesen Info Text ("That was last card") im großen Textfeld an.
                    showAnswer.setEnabled(false); // Deaktiviere den Button (er wird grau und kann nicht mehr geklickt werden), da das Lernen hier zu Ende ist.
                }
            }

        }
    }
    class OpenMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Den Speichern Dialog öffnen
            JFileChooser fileOpen = new JFileChooser(); // Es öffnet ein Fenster, in dem du durch deine Ordner klickst und einen Dateinamen eingibst.
            fileOpen.showOpenDialog(frame); // Sorgt dafür, dass sich dieses Fenster über das Hauptfenster (frame) legt.
            loadFile(fileOpen.getSelectedFile()); // Holt sich exakt den Pfad der Datei, die der User angeklickt hat, und gibt ihn an die Methode loadFile weiter.

        }
    }
    private void loadFile(File selectedFile) { // Diese Methode bekommt die vom Benutzer ausgewählte Datei (z.B. meine_karten.txt) übergeben.

        cardList = new ArrayList<FlashCard>(); //Erschafft einen komplett neuen, leeren Karteikasten. Falls vorher schon alte Karten geladen waren, werden diese hiermit weggeworfen.

        try { // Ein Sicherheitsnetz (try-catch): Versuche den folgenden Code auszuführen. Beim Lesen von Festplatten passieren oft Fehler (z.B. Datei gelöscht), deshalb ist das Pflicht.
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile)); // FileReader öffnet die Datei physisch. BufferedReader ist ein Hilfsarbeiter, der den Text nicht extrem langsam Buchstabe für Buchstabe, sondern rasend schnell ganze Zeilen auf einmal einliest.
            String line = null; // Wir erstellen eine leere Text-Variable namens "line". Sie dient als Zwischenspeicher für die Zeile, die wir in der Schleife gleich lesen werden.

            while ((line = reader.readLine()) != null) { // Eine Schleife reader.readLine() liest exakt eine Zeile aus der Textdatei und speichert sie in der Variable "line". != null bedeutet: Wiederhole das solange, bis die Textdatei komplett leer gelesen ist (null = nichts mehr da).
                makeCard(line); // Nimm die gerade gelesene Zeile (die jetzt z.B. "Paris/Frankreich" enthält) und gib sie an die Hilfsmethode maceCard weiter, damit diese daraus eine Karteikarte bastelt.

            }

        } catch (Exception e) { // Wenn im try Block irgendetwas schiefgeht (z.B. Datei nicht lesbar), fängt der catch Block den Programm Absturz sicher auf.
            System.out.println("Couldn't read file"); // Dieser Befehl druckt einfach nur den Text "Couldn't write to File" in deine Konsole.
            e.printStackTrace(); // Das ist das wichtigste Werkzeug für die Fehlersuche (Debugging)! Mit .printStackTrace() sagst du diesem Bericht "Drucke mir bitte deine exakte Rückverfolgung(Stacktrace) in die Konsole." Anschließend folgt zum Beispiel "Fehler in Zeile 142: Zugriff verweigert."
        }

        showAnswer.setEnabled(true); // Weckt den Button wieder auf und macht ihn klickbar!

        //show the first card
        cardIterator = cardList.iterator(); // Nachdem alle Karten erfolgreich in der Liste gelandet sind, setzen wir unseren Zeigefinger (Iterator) ganz an den Anfang dieser Liste.
        showNextCard(); // Ruft die Methode auf, die die allererste Karteikarte auf dem Bildschirm anzeigt, damit das Lernen sofort losgehen kann.
    }

    private void makeCard(String lineToParse) { // Diese Methode bekommt exakt eine Textzeile aus der Datei und soll daraus eine FlashCard machen.

        StringTokenizer result = new StringTokenizer(lineToParse, "/"); // StringTokenizer ist ein Werkzeug (wie eine Schere). Es nimmt die Zeile (lineToParse) und zerschneidet sie exakt in zwei Hälften, wo der Schrägstrich ("/") zu finden ist.

        if (result.hasMoreTokens()) { // Prüft zur Sicherheit: Gibt es nach dem Zerschneiden tatsächlich Textstücke (Tokens), die wir greifen können?
            FlashCard card = new FlashCard(result.nextToken(), result.nextToken()); // Hier passiert die Objekt Magie: Das erste result.nextToken() greift sich den Text vor dem Schrägstrich (die Frage). Das zweite result.nextToken() greift sich den Text nach dem Schrägstrich (die Antwort). Beide Texte werden direkt an den Bauplan (Konstruktor) übergeben, um eine neue Karte zu erschaffen.
            cardList.add(card); // Die fertige Karteikarte wird in den Karteikasten (die ArrayList) gelegt und ist somit sicher im Arbeitsspeicher des Programms.

        }
    }

    private void showNextCard() { // Diese Methode ist nur dafür da, die nächste Karteikarte aus der Liste zu holen und die Benutzeroberfläche entsprechend umzubauen.
        currentCard = (FlashCard) cardIterator.next(); // Der Zeigefinger (Iterator) rutscht in der Liste einen Platz weiter, greift sich die dortige Karteikarte und speichert sie in der Variable "currentCard" ab. Das (FlashCard) ist eine Zusicherung für Java, dass es sich dabei auch wirklich um eine Karteikarte handelt.

        display.setText(currentCard.getQuestion()); // Greift auf diese aktuell ausgewählte Karte zu, holt sich mit dem Getter die Frage und zeigt diesen Text im großen Textfeld (display) an.
        showAnswer.setText("Show Answer"); // Ändert den Text auf dem Button zu "Show Answer", da wir ja gerade die Frage lesen.
        isShowAnswer = true; // Legt den Schalter um auf "true". So weiß unser Button (im NextCardListener) beim nächsten Klick: "Aha, aktuell wird die Frage gezeigt, ich muss beim Klick also als nächstes die Antwort einblenden!"
    }
}