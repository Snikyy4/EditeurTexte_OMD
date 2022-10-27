import java.util.LinkedList;

public class Buffer {

    // initialisation des variables statics (Permet d'avoir accès au mêmes espaces
    // mémoires dans toutes les instances de la classe)
    private static int posCurseur = 0;
    private static int posSelect = -1;
    private static String texteTmp = "";
    private static String texte = "";

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

    //Copie la portion de texte sélectionnée dans le texteTmp entre le curseur courant et le curseur de la séléction
    public void copier() {
        // Verifie qu'il y a bien une sélection et que les curseurs sont dans le texte
        if (Buffer.posSelect >= 0 && Buffer.posSelect != Buffer.posCurseur && Buffer.posCurseur <= Buffer.texte.length() && Buffer.posSelect <= Buffer.texte.length()) {
            if (Buffer.posSelect < Buffer.posCurseur) { // Cas où la selection est effectuée de la gauche vers la droite
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posSelect, Buffer.posCurseur);
            } else { // Cas où la selection est effectuée de la droite vers la gauche
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posCurseur, Buffer.posSelect);
            }
        }
        // Annule la selection
        Buffer.posSelect = -1;
    }

    //Coupe la portion de texte sélectionnée dans le texteTmp entre le curseur courant et le curseur de la séléction
    public void couper() {
        // Verifie qu'il y a bien une sélection et que les curseurs sont dans le texte
        if (Buffer.posSelect >= 0 && Buffer.posSelect != Buffer.posCurseur && Buffer.posCurseur <= Buffer.texte.length() && Buffer.posSelect <= Buffer.texte.length()) {
            if (Buffer.posSelect < Buffer.posCurseur) {// Cas où la selection est effectuée de la gauche vers la droite
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posSelect, Buffer.posCurseur);
            } else { // Cas où la selection est effectuée de la droite vers la gauche
                Buffer.texteTmp = Buffer.texte.substring(Buffer.posCurseur, Buffer.posSelect);
            }
        }
        // Supprime la selection
        supprSelection();
    }

    // Colle la portion de texte contenue dans texteTmp au niveau du curseur ou a la place de la selection
    public void coller() {
        if (Buffer.posSelect != -1) {
            supprSelection();
        }
        Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur) + texteTmp + Buffer.texte.substring(Buffer.posCurseur);
        Buffer.posCurseur += texteTmp.length();
    }

     //Place le curseur de selection 
    public void selection() {

        if (Buffer.posSelect == -1) { // Commence une selection si il n'y en a pas
            Buffer.posSelect = Buffer.posCurseur;
        } else { // Annule la selection si il y en a deja une
            Buffer.posSelect = -1;
        }
    }

    // Supprime la portion de texte sélectionnée
    private void supprSelection() {
        if (Buffer.posSelect >= 0 && Buffer.posSelect != Buffer.posCurseur && Buffer.posCurseur <= Buffer.texte.length() && Buffer.posSelect <= Buffer.texte.length()) {
            if (Buffer.posSelect < Buffer.posCurseur) { // Cas où la selection effectuée de la gauche vers la droite
                Buffer.texte = Buffer.texte.substring(0, Buffer.posSelect)+ Buffer.texte.substring(Buffer.posCurseur);
                Buffer.posCurseur = Buffer.posSelect; // Replace le curseur au debut de la selection
            } else { // Cas où la selection effectuée de la droite vers la gauche
                Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur) + Buffer.texte.substring(Buffer.posSelect); // Ici pas besoin de replacer le curseur
            }
        }
        // Annule la selection
        Buffer.posSelect = -1;
    }

    //Déplace le curseur vers la droite
    public void droite() {
        if (Buffer.posCurseur < Buffer.texte.length()) {
            Buffer.posCurseur++;
        }
    }

    //Déplace le curseur vers la gauche
    public void gauche() {
        if (Buffer.posCurseur > 0) {
            Buffer.posCurseur--;
        }
    }

    //Supprime le caractère avant le curseur
    public void back() {
        // Cas où il y a une selection
        if (Buffer.posSelect != -1) {
            supprSelection();
        } else { // Cas où il n'y a pas de selection
            if (Buffer.posCurseur > 0) {
                Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur - 1) + Buffer.texte.substring(Buffer.posCurseur);
                Buffer.posCurseur--;
            }
        }

    }

    // Ecrit le caractère au niveau de la selection s'il y en a une, sinon au niveau du curseur
    public void write(char c) {
        // Supprime la selection si il y en a une
        if (Buffer.posSelect != -1) {
            supprSelection();
        }
        Buffer.texte = Buffer.texte + c;
        posCurseur++;
    }
}
