import javax.swing.*; //Ein großer Werkzeugkasten namens swing wird importiert der * bedeutet alle Werkzeuge.
import java.awt.*; // Abstract Window Toolkit für die Font
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList; // Ein spezielles Werkzeug damit das Array wächst, wenn du Karteikarten hinzufügst.
import java.util.Iterator;

public class FlashCardBuilder { //Bauplan

    private JTextArea question; //Es wird Platz für ein großes Textfeld reserviert.
    private JTextArea answer; // Es wird Platz für ein zweites Textfeld reserviert.
    private ArrayList<FlashCard> cardList; //Es wird eine flexible Liste erstellt.<FlashCard> sagen der Liste das es Objekte vom Typ FlashCard speichern soll.
    private JFrame frame; // Es wird ein Platz für das Hauptfenster reserviert.

    public FlashCardBuilder() { // Der Startknopf, baut das Fenster
        frame = new JFrame("Flash Card"); // Erstellt das eigentliche Anwendungsfenster mit dem Titel "Flash Card".
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ohne diese Zeile würde das Fenster verschwinden, aber das Programm im Hintergrund weiterlaufen.
        JPanel mainPanel = new JPanel(); // Der Hauptcontainer auf den wir später alle unsere Elemente(Knöpfe,Textfelder) platzieren.
        Font greatFont = new Font("Arial", Font.BOLD, 21); // Definiert eine Schriftart, die wir gleich verwenden werden(Arial, Fettgedruckt, Schriftgröße21).
        question = new JTextArea(6, 20); // Erstellt ein mehrzeiliges Textfeld. Es ist so dimensioniert, dass standardmäßig 6Zeilen und 20Zeichen pro Zeile gut hineinpassen.
        question.setLineWrap(true); // Schaltet den automatischen Zeilenumbruch ein, damit der Text nicht über den rechten Rand hinausläuft.
        question.setWrapStyleWord(true); // Sorgt dafür, dass Wörter beim Zeilenumbruch nicht in der Mitte abgeschnitten werden, sondern das ganze Wort in die nächste Zeile rutscht.
        question.setFont(greatFont); // Weist dem Textfeld die oben erstellte "greatFont" Schriftart zu.

        JScrollPane qJScrollPane = new JScrollPane(question); // Da eine JTextArea von selbst keine Scrollbalken hat, wird das Textfeld "eingepackt" in ein Scroll-Panel.
        qJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Der vertikale Scrollbalken wird immer angezeigt, auch wenn noch garnicht genug Text da ist.
        qJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Der horizontale Scrollbalken wird niemals angezeigt (der Text bricht ja ohnehin automatisch um).

        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(greatFont);

        JScrollPane aJScrollPane = new JScrollPane(answer); // Hier passiert haargenau das Gleiche wie bei der Frage, nur eben für das Antwort Textfeld (answer)
        aJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Auch dieses bekommt sein eigenes Scroll-Panel (aJScrollPane).
        aJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextButton = new JButton("Next Card"); // Erstellt einen klickbaren Button mit der Aufschrift "Next Card".
        cardList = new ArrayList<FlashCard>(); // Initialisierung der Liste für Karteikarten als leeres Objekt. Das ist ein Karteikasten aus Plastik, in den du die fertigen Karten einsortierst.

        JLabel qLabel = new JLabel("Question"); // Erstellt einen, nicht bearbeitbaren Text("Question" und "Answer"), der
        JLabel aLabel = new JLabel("Answer"); // später als Beschriftung über dem Textfeld dient.

        mainPanel.add(qLabel); // Hier werden alle zuvor erstellten Bausteine dem mainPanel hinzugefügt.
        mainPanel.add(qJScrollPane); // Wichtig: Du fügst nicht die JTextArea direkt hinzu, sondern die JScrollPane(welche das Textfeld bereits enthält).
        mainPanel.add(aLabel);
        mainPanel.add(aJScrollPane);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener()); // Das ist das Objekt, was zuhört. Die Klasse wird aufgerufen und die actionperformed Methode wird ausgeführt.

        JMenuBar menuBar = new JMenuBar(); // Das ist das Hauptfundament. Es stellt den durchgehenden, meist grauen Balken ganz oben im Fenster. Er ist anfangs noch komplett leer.
        JMenu filemenu = new JMenu("File"); // Das ist ein Hauptreiter in der Menüleiste(wie "Datei", "Bearbeiten", "Ansicht"). Wenn du später darauf klickst, klappt ein DropdownMenü nach unten auf.
        JMenuItem newMenuItem = new JMenuItem("New"); // Das sind die eigentlichen, klickbaren Einträge (Items), die in dem Dropdown-Menü auftauchen sollen.
        JMenuItem saveMenuItem = new JMenuItem("Save");
        filemenu.add(newMenuItem); // Hier nimmst du deine beiden klickbaren Einträge und steckst es in das aufklappbare "File"-Menü
        filemenu.add(saveMenuItem);
        menuBar.add(filemenu); // Jetzt nimmst du das fertige "File"-Menü und steckst es in den großen Menübalken ganz oben.

        newMenuItem.addActionListener(new NewMenuItemListener()); // Hier passiert die Ereignissteuerung, es wird zugehört.
        saveMenuItem.addActionListener(new SaveMenuItemListener());

        frame.setJMenuBar(menuBar); // Nimmt den fertigen Balken und tackert es an das Hauptfenster.
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel); // Legt das fertige mainPanel(mit allen Elementen drauf) in die Mitte(BorderLayout.CENTER) des Hauptfensters.
        frame.setSize(500, 600); // Gibt dem Fenster eine feste Startgröße von 500 Pixeln Breite und 600 Pixeln Höhe.
        frame.setVisible(true); // Macht das Fenster auf dem Bildschirm sichtbar. Ohne diesen Befehl würde das Programm zwar laufen, aber du würdest nichts sehen.

    }

    public static void main(String[] args) { // Diese Methode muss drinn stehen. String[] args ist ein Array, um beim Start über das Terminal eine kleine Information mitzugeben um zum Beispiel ein Programm zu schreiben das Leute begrüßt.
        SwingUtilities.invokeLater(new Runnable() { // Ein Methodenaufruf um etwas in die Warteschlange vom Event Dispatch Thread zu packen. New Runnable() ist ein Packet in dem ein Vertrag drinn ist welches der Thread ausführt.
            @Override // Eine Anmerkung, dass das was im Vertrag steht eingehalten werden muss.
            public void run() { // Muss laut Vertrag diese Methode enthalten.
                new FlashCardBuilder(); // Neues Objekt, welches denn Konstruktor aufruft.
            }
        });
    }

    class NextCardListener implements ActionListener { // Hier wird programmiert, was ausgeführt werden soll, wenn auf dem Button geklickt wird.
        // Hilfsklasse bei der ein Vertrag unterschrieben wird."Ich bin offiziell qualifiziert, auf Benutzer Aktionen (wie Mausklicks) zu reagieren.
        @Override
        // Das ist eine kleine Notiz für den Java Compiler und für dich. Weil du den ActionListener Vertrag unterschrieben hast, musst du eine bestimmte Methode einbauen.
        public void actionPerformed(ActionEvent e) { // Schaut welcher Button gedrückt wurde und speichert es in e ab.
            // Create a flashCard
            FlashCard card = new FlashCard(question.getText(), answer.getText()); // Das Programm liest den Text aus, den der Benutzer in die beiden Textfelder getippt hat.Erst nachdem die zwei Werte herausgelesen wurden, wird er Versand an die FlashCard Klasse weitergeleitet, weil new bedeutet Faubrikauftrag und daraufhin folgt welcher Bauplan genommen werden soll.
            cardList.add(card); // Die Karteikarte wird in die cardList gepackt, weil sonst der Garbage Collektor kommt, nachdem die Methode fertig ist und alles löscht, wenn die Karteikarte in die Liste hinzugefügt wird darf er nicht alles löschen. Die Karteikarte ist trotzdem wichtig damit die Karte richtig zusammengebaut wird.
            clearCard(); // Nachdem die Karte erstellt und sicher in die Liste gepackt wurde, führt das Programm die Säuberungsmethode aus.
        }
    }

    class NewMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class SaveMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        FlashCard card = new FlashCard(question.getText(), answer.getText()); // Wenn "Next" nicht gedrückt wurde, wird die vierte Karte hier gespeichert, bevor der Inhalt verloren geht.
        cardList.add(card);

        // Den Speichern Dialog öffnen
            JFileChooser fileSave = new JFileChooser(); // Es öffnet ein Fenster, in dem du durch deine Ordner klickst und einen Dateinamen eingibst.
            fileSave.showSaveDialog(frame); // Sorgt dafür, dass sich dieses Fenster über das Hauptfenster (frame) legt.
            saveFile(fileSave.getSelectedFile()); // Der Benutzer klickt im Dialogfenster auf "Speichern". Mit .getSelectedFile() holt sich Java exakt den Dateipfad, den der Benutzer grade auswählt hat, und übergibt ihn an die neue Hilfsmethode saveFile.

        }
    }

    // Reinigungsdienst
    private void clearCard() {
    question.setText(""); // Der Befehl setText überschreibt den aktuellen Inhalt. Da es aber nur zwei leere Anführungszeichen "" übergeben werden, löscht er den Text quasi weg.
    answer.setText("");
    question.requestFocus(); // (Profitipp) Der Strich beim Tippen wird sofort wieder in das obere Feld (question) gesetzt. Der Benutzer kann also sofort die nächste Karte tippen, ohne erst wieder zur Maus greifen und in das Feld klicken zu müssen.
    }

    private void saveFile(File selectedFile) {
        try { // Weil das Schreiben auf Festplatten schiefgehen kann (z.B Festplatte voll, keine Berechtigung), zwingt Java dich, das in einem try-catch-Block zu packen.Es bedeutet versuche das zu speichern. Wenn es nicht funktioniert, fang den Fehler ab, damit das Programm nicht abstürzt.

            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile)); // BufferedWriter ist wie ein Zwischenlager (ein Puffer). Er sammelt erst ein paar Sätze, bevor er sie auf die Festplatte schreibt. Das macht das Speichern extrem viel schneller, als wenn man jeden Buchstaben einzeln auf die Festplatte schreiben würde. FileWriter ist der Arbeiter, der die Datei überhaupt erst auf der Festplatte anlegt und öffnet.
            Iterator<FlashCard> cardIterator = cardList.iterator(); // Ein Iterator ist ein Zeiger, der auf die cardList zeigt. Er beginnt ganz oben.
            while(cardIterator.hasNext()){ // Solange der Iterator sieht, dass es in der Liste noch eine nächste Karte gibt ( hasNext() ), mache Folgendes...
                FlashCard card = (FlashCard)cardIterator.next(); // Der Finger rutscht eins nach unten und greift sich die aktuelle Karteikarte aus der Liste.
                writer.write(card.getQuestion() + "/"); // Der Writer schreibt die Frage in die Datei (card.getQuestion()). Direkt dahinter klebt er einen Schrägstrich /. Dieser Strich ist extrem wichtig! Er dient später als Trennzeichen, damit das Programm beim Einlesen der Datei Weiß, wo die Frage aufhört und die Antwort anfängt.
                writer.write(card.getAnswer() + "\n"); // Ganz am Ende hängt er ein /n an. Damit ist das unsichtbare Zeichen für einen Zeilenumbruch (Enter Taste). Die nächste Karteikarte landet also in der nächsten Zeile der Textdatei.
            }

          writer.close(); // Wenn alle Karten geschrieben sind, muss die Datei wieder geschlossen werden. Macht man das nicht, bleibt die Datei vom Programm blockiert und der Text wird eventuell garnicht auf die Festplatte geschrieben (er bleibt im BufferedWriter stecken).

        } catch (Exception e) { // Das ist das sogenannte Sicherheitsnetz des Programms, damit das Programm normal weiterlaufen kann und sich nicht schließt. Wenn java abstürzt, erstellt es automatisch einen detaillierten Fehlerbericht. Dieser Bericht ist ein Objekt vom Typ Exception was (Ausnahme/Fehler) bedeutet. Mit dem e gibst du diesem Bericht einfach einen kurzen Namen, damit du gleich damit arbeiten kannst.

            System.out.println("Couldn't write to file"); // Dieser Befehl druckt einfach nur den Text "Couldn't write to File" in deine Konsole.
            e.printStackTrace(); // Das ist das wichtigste Werkzeug für die Fehlersuche (Debugging)! Mit .printStackTrace() sagst du diesem Bericht "Drucke mir bitte deine exakte Rückverfolgung(Stacktrace) in die Konsole." Anschließend folgt zum Beispiel "Fehler in Zeile 142: Zugriff verweigert."
        }

    }
}