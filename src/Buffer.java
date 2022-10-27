import java.util.ArrayList;

public class Buffer {

    // initialisation des variables statics (Permet d'avoir accès au mêmes espaces
    // mémoires dans toutes les instances de la classe)
    private static int posCurseur = 0;
    private static int posSelect = -1;
    private static String texteTmp = "";
    private static String texte = "";
    private static ArrayList<String> EtatTexte = new ArrayList<String>();
    private static ArrayList<Integer> EtatCurseur = new ArrayList<Integer>();
    private static int EtatIndex = 0;

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
        ajouteEtat(Buffer.texte, Buffer.posCurseur);
    }

    // Colle la portion de texte contenue dans texteTmp au niveau du curseur ou a la place de la selection
    public void coller() {
        if (Buffer.posSelect != -1) {
            supprSelection();
        }
        Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur) + texteTmp + Buffer.texte.substring(Buffer.posCurseur);
        Buffer.posCurseur += texteTmp.length();
        ajouteEtat(Buffer.texte, Buffer.posCurseur);
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
            ajouteEtat(Buffer.texte, Buffer.posCurseur);
        } else { // Cas où il n'y a pas de selection
            if (Buffer.posCurseur > 0) {
                Buffer.texte = Buffer.texte.substring(0, Buffer.posCurseur - 1) + Buffer.texte.substring(Buffer.posCurseur);
                Buffer.posCurseur--;
                ajouteEtat(Buffer.texte, Buffer.posCurseur);
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
        ajouteEtat(Buffer.texte, Buffer.posCurseur);
    }

    public void etatInit(){
        Buffer.EtatTexte.add("");
        Buffer.EtatCurseur.add(0);
        Buffer.EtatIndex++;
    }

    // Revenir a l'état précédent, correspond au controle Z
    public void etatPrecedent() {
        Buffer.posSelect = -1;
        if (Buffer.EtatIndex > 0) {
            Buffer.EtatIndex--;
        }
        Buffer.texte = Buffer.EtatTexte.get(Buffer.EtatIndex); // Récupère le texte de l'indexe précédent
        Buffer.posCurseur = Buffer.EtatCurseur.get(Buffer.EtatIndex); // Positionne le curseur à l'endroit du curseur de l'état précédent
    }

    // Retourner à l'état suivant
    public void etatSuivant() {
        Buffer.posSelect = -1;
        if (Buffer.EtatIndex < Buffer.EtatTexte.size() - 1) {
            Buffer.EtatIndex++;
        }
        Buffer.texte = Buffer.EtatTexte.get(Buffer.EtatIndex); // Récupère le texte de l'index précédent
        Buffer.posCurseur = Buffer.EtatCurseur.get(Buffer.EtatIndex); // Positionne le curseur à l'endroit du curseur de l'état précédent
    }

    public void ajouteEtat(String texte, int posCurseur) {
        // Mets a jour la liste des états
        if (Buffer.EtatIndex < Buffer.EtatTexte.size() - 1) {
            Buffer.EtatTexte.subList(Buffer.EtatIndex, Buffer.EtatTexte.size() - 1).clear();
            Buffer.EtatCurseur.subList(Buffer.EtatIndex, Buffer.EtatCurseur.size() - 1).clear();
        }
        Buffer.EtatTexte.add(texte);
        Buffer.EtatCurseur.add(posCurseur);
        Buffer.EtatIndex++;
    }

}