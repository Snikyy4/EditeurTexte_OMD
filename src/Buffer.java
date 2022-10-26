import java.util.LinkedList;

public class Buffer {

    // initialisation des variables statics (Permet d'avoir accès au mêmes espaces
    // mémoires dans toutes les instances de la classe)
    private static int posCurseur = 0;
    private static int posSelect = -1;
    private static String texteTmp = "";
    private static String texte = "";
    private static LinkedList<String> textStates = new LinkedList<String>();
    private static LinkedList<Integer> cursorStates = new LinkedList<Integer>();
    private static int stateIndex = 0;

    public void init(){
        Buffer.textStates.add("");
        Buffer.cursorStates.add(0);
        Buffer.stateIndex++;
    }

    // Accesseurs
    public String getTexte() {
        return Buffer.texte;
    }

    public int getPosCurseur() {
        return Buffer.posCurseur;
    }

    public int getPosSelect() {
        return Buffer.posSelect;
    }

    public void setText(String texte) {
        Buffer.texte = texte;
    }

    /*
     * Implementation de la commande copy
     * 
     * Copie la portion de texte selectionnée dans le clipboard (entre le curseur
     * courant et le
     * curseur de selection)
     */
    public void copier() {

        // Verifie que les curseurs sont bien dans les bornes du texte et qu'il y a bien
        // une selction
        if (Buffer.posSelect >= 0 && Buffer.posSelect != Buffer.posCurseur
                && Buffer.posCurseur <= Buffer.texte.length() && Buffer.posSelect <= Buffer.texte.length()) {

            if (Buffer.posSelect < Buffer.posCurseur) { // Cas 1 : selection effectuée de la gauche vers la
                                                                 // droite
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posSelect, Buffer.posCurseur);
            } else { // Cas 2 : selection effectuée de la droite vers la gauche
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posCurseur, Buffer.posSelect);
            }
        }

        // Annule la selection
        Buffer.posSelect = -1;
    }

    /*
     * Implementation de la commande cut
     * 
     * Copie la portion de texte selectionnée dans le clipboard avant de la retirer
     * du texte (entre le curseur
     * courant et le curseur de selection)
     */
    public void couper() {

        // Verifie que les curseurs sont bien dans les bornes du texte et qu'il y a bien
        // une selction
        if (Buffer.posSelect >= 0 && Buffer.posSelect != Buffer.posCurseur
                && Buffer.posCurseur <= Buffer.texte.length() && Buffer.posSelect <= Buffer.texte.length()) {

            if (Buffer.posSelect < Buffer.posCurseur) {// Cas 1 : selection effectuée de la gauche vers la
                                                                // droite
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posSelect, Buffer.posCurseur);
            } else { // Cas 2 : selection effectuée de la droite vers la gauche
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posCurseur, Buffer.posSelect);
            }
        }

        // Supprime la selection
        rmSelect();

        addState(Buffer.texte, Buffer.posCurseur);
    }

    /*
     * Implementation de la commande paste
     * 
     * colle la portion de texte contenue dans le clipboard au niveau du curseur ou
     * à la place de la selection
     */
    public void coller() {
        if (Buffer.posSelect != -1) {
            rmSelect();
        }

        Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur) + texteTmp
                + Buffer.texte.substring(Buffer.posCurseur);

        Buffer.posCurseur += texteTmp.length();

        addState(Buffer.texte, Buffer.posCurseur);

    }

    /*
     * Implementation de la commande select
     * 
     * Place le curseur de selection qui delimitera la selection (avec le curseur
     * courant)
     */
    public void select() {

        if (Buffer.posSelect == -1) { // Commence une selection si il n'y en a pas
            Buffer.posSelect = Buffer.posCurseur;
        } else { // Annule la selection si il y en a deja une
            Buffer.posSelect = -1;
        }
    }

    /*
     * Supprime la portion de texte selectionnée
     */
    private void rmSelect() {
        if (Buffer.posSelect >= 0 && Buffer.posSelect != Buffer.posCurseur
                && Buffer.posCurseur <= Buffer.texte.length() && Buffer.posSelect <= Buffer.texte.length()) {

            if (Buffer.posSelect < Buffer.posCurseur) { // Cas 1 : selection effectuée de la gauche vers la
                                                                 // droite
                Buffer.texte = Buffer.texte.substring(0, Buffer.posSelect)
                        + Buffer.texte.substring(Buffer.posCurseur);

                Buffer.posCurseur = Buffer.posSelect; // Replace le curseur au debut de la selection
            } else { // Cas 2 : selection effectuée de la droite vers la gauche
                Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur)
                        + Buffer.texte.substring(Buffer.posSelect);
            }
        }

        // Annule la selection
        Buffer.posSelect = -1;
    }

    /*
     * Implementation de la commande goRight
     * 
     * Deplace le curseur vers la droite (si il n'est pas deja en bout de texte)
     */
    public void droite() {
        if (Buffer.posCurseur < Buffer.texte.length()) {
            Buffer.posCurseur++;
        }
    }

    /*
     * Implementation de la commande goLeft
     * 
     * Deplace le curseur vers la gauche (si il n'est pas deja au debut du texte)
     */
    public void gauche() {
        if (Buffer.posCurseur > 0) {
            Buffer.posCurseur--;
        }
    }

    /*
     * Implementation de la commande back
     * 
     * supprime le caractère avant le curseur ou la selection si il y en a une
     */
    public void back() {
        // Cas 1 : il y a une selection
        if (Buffer.posSelect != -1) {
            rmSelect();
            addState(Buffer.texte, Buffer.posCurseur);
        } else { // Cas 2 : il n'y a pas de selection
            if (Buffer.posCurseur > 0) {
                Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur - 1)
                        + Buffer.texte.substring(Buffer.posCurseur);
                Buffer.posCurseur--;

                addState(Buffer.texte, Buffer.posCurseur);
            }
        }

    }

    /*
     * Ajoute un charactère au texte à la place de la selection si il y en a une
     * après le curseur courant sinon
     */
    public void write(char c) {
        // Supprime la selection si il y en a une
        if (Buffer.posSelect != -1) {
            rmSelect();
        }
        Buffer.texte = Buffer.texte + c;
        posCurseur++;

        addState(Buffer.texte, Buffer.posCurseur);
    }

    public void addState(String text, int cursorPosition) {
        // Mets a jour la liste des états
        if (Buffer.stateIndex < Buffer.textStates.size() - 1) {
            Buffer.textStates.subList(Buffer.stateIndex, Buffer.textStates.size() - 1).clear();
            Buffer.cursorStates.subList(Buffer.stateIndex, Buffer.cursorStates.size() - 1).clear();
        }

        Buffer.textStates.add(text);
        Buffer.cursorStates.add(cursorPosition);
        Buffer.stateIndex++;
    }

}
