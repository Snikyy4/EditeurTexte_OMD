import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Color;

public class Interface extends JFrame implements KeyListener, ActionListener {
    // Initialisation des variables
    public Buffer buffer;
    private JLabel zoneTexte;
    private JPanel fenetre;
    private JPanel buttonBar;
    private JLabel nbMotsBar; // Ajout du nombre de caractères sur la barre d'affichage
    private String nbMotsFichier; // String contenant le nb de caractères du fichier

    private JButton copierB;
    private JButton couperB;
    private JButton collerB;
    private JButton selectB;
    private JButton precedentB;
    private JButton suivantB;


    private Copier copier = new Copier();
    private Couper couper = new Couper();
    private Coller coller = new Coller();
    private Selection selection = new Selection();
    private Back back = new Back();
    private Gauche gauche = new Gauche();
    private Droite droite = new Droite();
    private Precedent precedent = new Precedent();
    private Suivant suivant = new Suivant();

    // Constrcuteur de l'interface
    public Interface() {
        // Ajout du titre de la fenêtre
        this.setTitle("Editeur de texte Lequertier-Potin");
        // Fermeture de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Largeur et hauteur de la fenêtre
        this.setPreferredSize(new Dimension(1000, 600));

        // Initialisation du buffer
        buffer = new Buffer();
        buffer.etatInit();
        
        // initialisation des éléments de l'UI
        zoneTexte = new JLabel("|");
        buttonBar = new JPanel();
        fenetre = new JPanel();
        copierB = new JButton("Copier");
        couperB = new JButton("Couper");
        collerB = new JButton("Coller");
        selectB = new JButton("Selection");
        precedentB = new JButton("↩");
        suivantB = new JButton("↪");

        nbMotsFichier = String.valueOf("0");
        nbMotsBar = new JLabel(nbMotsFichier+ " caractère"); //Initialisation à 0 caractère
        nbMotsBar.setForeground(Color.WHITE);

        buttonBar.setBackground(Color.BLACK);
        zoneTexte.setBounds(0, 0, 100, 100);
        zoneTexte.setVerticalAlignment(JLabel.TOP);
    

        // Ajoute aux boutons le fait de pouvoir faire une action
        copierB.addActionListener(this);
        couperB.addActionListener(this);
        collerB.addActionListener(this);
        selectB.addActionListener(this);
        precedentB.addActionListener(this);
        suivantB.addActionListener(this);

        fenetre.setLayout(new BorderLayout());
        fenetre.add(buttonBar, BorderLayout.NORTH);
        fenetre.add(zoneTexte);

        //Ajout des bouttons sur la barre à bouttons
        buttonBar.add(copierB);
        buttonBar.add(couperB);
        buttonBar.add(collerB);
        buttonBar.add(selectB);
        buttonBar.add(precedentB);
        buttonBar.add(suivantB);
        buttonBar.add(nbMotsBar);
        this.add(fenetre); 
        this.setFocusable(true);

        // Ajout du KeyListener à l'interface
        this.addKeyListener(this);
    }

    
    // Redirige les actions liées aux bouttons vers les commandes
    @Override
    public void actionPerformed(ActionEvent action) {
        switch (action.getActionCommand()) {
            case "Copier":
                copier.execute();
                break;
            case "Couper":
                couper.execute();
                nbMotsFichier = String.valueOf(buffer.getTexte().length());
                nbMotsBar.setText(nbMotsFichier+" caractères");
                break;
            case "Coller":
                coller.execute();
                nbMotsFichier = String.valueOf(buffer.getTexte().length());
                nbMotsBar.setText(nbMotsFichier+" caractères");
                break;
            case "Selection":
                selection.execute();
                break;
            case "↩":
                precedent.execute();
                nbMotsFichier = String.valueOf(buffer.getTexte().length());
                nbMotsBar.setText(nbMotsFichier+" caractères");
                break;
            case "↪":
                suivant.execute();
                nbMotsFichier = String.valueOf(buffer.getTexte().length());
                nbMotsBar.setText(nbMotsFichier+" caractères");
                break;
        }

        // Prend le focus de l'interface et update l'interface
        this.requestFocus();
        this.update();

    }

    // Fonction permettant de faire des actions en fonction de la touche pressée par l'utilisateur
    @Override
    public void keyTyped(KeyEvent t) {
        if ((int) (t.getKeyChar()) == 8) { // Si la touche pressé est le backspace, lancer la commande back
            back.execute(); // Supprime 1 caractère de l'affichage dans le cas du backspace
            nbMotsFichier = String.valueOf(buffer.getTexte().length());
            nbMotsBar.setText(nbMotsFichier+" caractères");
        } 
        else if ((int) (t.getKeyChar()) == 10) { // Si la touche pressée est entrée, alors ajoute d'un retour chariot
            buffer.write('\n');
        }
        else { // Sinon, ajoute le caractère tapé
            buffer.write(t.getKeyChar());
            nbMotsFichier = String.valueOf(buffer.getTexte().length());
            nbMotsBar.setText(nbMotsFichier+" caractères");
        }
        this.update(); //Update de l'interface
    }

    // Methode non utilisée, pas d'implementation
    @Override
    public void keyPressed(KeyEvent t) {
        if ((int) t.getKeyCode()==37){ //Flèche de gauche sur le clavier
            gauche.execute(); 
        }
        else if ((int) t.getKeyCode()==39){ //Flèche de droite sur le clavier
            droite.execute(); 
        }
        this.update();
    }

    // Methode non utilisée, pas d'implementation
    @Override
    public void keyReleased(KeyEvent e) {
    }

    
    //Update de l'interface lorsqu'une commande est excecutée
    private void update() {
        String text = buffer.getTexte();
        int posCurseur = buffer.getPosCurseur();
        int posSelect = buffer.getPosSelect();

        String texteFinal; 
        
        // Adapte les texte avec des balises html et le curseur pour l'afficher

        if (posSelect != -1) { // Si il y a une selection, la surligne en bleu ciel
            if (posCurseur == posSelect) { // Cas où la selection est vide
                texteFinal = text.substring(0, posSelect) + '|' + text.substring(posCurseur);
            } else if (posCurseur > posSelect) { // Cas où la selection est effectuée de la gauche vers la droite
                texteFinal = text.substring(0, posSelect) + "<span bgcolor=\"#87CEFA\">"
                        + text.substring(posSelect, posCurseur) + "</span>|" + text.substring(posCurseur);
            } else { // Cas où selection est effectuée de la droite vers la gauche
                texteFinal = text.substring(0, posCurseur) + "|<span bgcolor=\"#87CEFA\">"
                        + text.substring(posCurseur, posSelect) + "</span>" + text.substring(posSelect);
            }
        } else { // Sinon se contente d'afficher le curseur
            texteFinal = text.substring(0, posCurseur) + '|' + text.substring(posCurseur);
        }
        // Ajout de la balise de debut et fin et convertit les retours chariot en html
        texteFinal = "<html>" + texteFinal.replace("\n", "<br>") + "</html>";

        // Remplace le texte dans l'interface
        this.zoneTexte.setText(texteFinal);
        zoneTexte.updateUI();

    }
}
