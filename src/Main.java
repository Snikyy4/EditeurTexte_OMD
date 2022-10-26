
public class Main {
    public static void main(String[] args) throws Exception {

        // création de l'interface homme machine 
        Interface textEditor = new Interface();
        // initialise les composants à leur prefered size
        textEditor.pack();
        // rendre la fenêtre visible
        textEditor.setVisible(true);
    }
}
