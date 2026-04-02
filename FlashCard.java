public class FlashCard { // Das ist der Bauplan für eine einzelne, leere Pappkarte. Diese Klasse hat den Job zwei Textbausteine festzuhalten: eine Frage und eine Antwort
    private String question; // Jede Karte, die du erstellst, hat genau zwei Speicherplätze. Einen für die Frage, einen für die Antwort.
    private String answer;
    public FlashCard(String q, String a){ // Hier fängt der Konstruktor die beiden Texte aus der FlashCardBuilder Klasse als q und als a auf und speichert sie sicher in seinen eigenen, internen String Variable ab.
        question = q;
        answer = a;
    } // Sobald die Methode fertig ist kommt der Garbage Collector ( Müllabfuhr ), deswegen wird die Karteikarte in die Cardlist gepackt.

    // Setter and Getter

    public String getQuestion() { // Ein Getter ist wie ein kleiner Bankschalter in deinem Tresor. Wenn die Methode aufgerufen wird fragt er höflich am Schalter nach. Die Methode schaut intern in den Tresor, kopiert den Text der Frage und reicht ihn (return) nach draußen. So kannst du die Daten bekommen/auslesen.
        return question;
    }

    public void setQuestion(String question) { // Setter erlauben es, die Daten nachträglich kontrolliert zu ändern.
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}