import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Color;

public class Interface extends JFrame implements KeyListener, ActionListener {
    // Initialisation des variables
    public Buffer buffer;
    private JLabel zoneTexte;
    private JPanel fenetre;
    private JPanel buttonBar;


    private JButton copierB;
    private JButton couperB;
    private JButton collerB;
    private JButton selectB;

    private Copier copier = new Copier();
    private Couper couper = new Couper();
    private Coller coller = new Coller();
    private Select select = new Select();
    private Back back = new Back();
    private Gauche gauche = new Gauche();
    private Droite droite = new Droite();


    public Interface() {
        // Ajout du titre de la fenêtre
        this.setTitle("Editeur de texte Lequertier-Potin");
        // Fermeture de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Largeur et hauteur de la fenêtre
        this.setPreferredSize(new Dimension(1000, 600));

        // Initialisation du buffer
        buffer = new Buffer();
        buffer.init();

        // initialisation des éléments de l'UI
        zoneTexte = new JLabel("|");
        buttonBar = new JPanel();
        fenetre = new JPanel();
        copierB = new JButton("Copier");
        couperB = new JButton("Couper");
        collerB = new JButton("Coller");
        selectB = new JButton("Select");

        buttonBar.setBackground(Color.BLACK);
        zoneTexte.setBounds(0, 0, 100, 100);
        zoneTexte.setVerticalAlignment(JLabel.TOP);
    

        // Ajoute aux boutons le fait de pouvoir faire une action
        copierB.addActionListener(this);
        couperB.addActionListener(this);
        collerB.addActionListener(this);
        selectB.addActionListener(this);

        fenetre.setLayout(new BorderLayout());
        fenetre.add(buttonBar, BorderLayout.NORTH);
        fenetre.add(zoneTexte);

        //Ajout des bouttons sur la barre à bouttons
        buttonBar.add(copierB);
        buttonBar.add(couperB);
        buttonBar.add(collerB);
        buttonBar.add(selectB);
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
                break;
            case "Coller":
                coller.execute();
                break;
            case "Select":
                select.execute();
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
            back.execute();
        } 
        else if ((int) (t.getKeyChar()) == 10) { // Si la touche pressée est entrée, alors ajoute d'un retour chariot
            buffer.write('\n');
        } 
        //else if ((int) (t.getKeyChar()))
        else { // Sinon, ajoute le caractère pressé
            buffer.write(t.getKeyChar());
        }
        //Update de l'interface
        this.update();
    }

    // Methode non utilisée, pas d'implementation
    @Override
    public void keyPressed(KeyEvent t) {
        if ((int) t.getKeyCode()==37){
            gauche.execute();
        }
        else if ((int) t.getKeyCode()==39){
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
        int cursorPosition = buffer.getPosCurseur();
        int selectPosition = buffer.getPosSelect();

        String texteFinal; 
        
        // Adapte les texte avec des balises html et le curseur pour l'afficher

        if (selectPosition != -1) { // Si il y a une selection, la surligne en bleu
            if (cursorPosition == selectPosition) { // Cas 1: la selection est vide
                texteFinal = text.substring(0, selectPosition) + '|' + text.substring(cursorPosition);
            } else if (cursorPosition > selectPosition) { // Cas 2 : selection effectuée de la gauche vers la droite
                texteFinal = text.substring(0, selectPosition) + "<span bgcolor=\"#87CEFA\">"
                        + text.substring(selectPosition, cursorPosition) + "</span>|" + text.substring(cursorPosition);
            } else { // Cas 3 : selection effectuée de la droite vers la gauche
                texteFinal = text.substring(0, cursorPosition) + "|<span bgcolor=\"#87CEFA\">"
                        + text.substring(cursorPosition, selectPosition) + "</span>" + text.substring(selectPosition);
            }
        } else { // sinon se contente d'afficher le curseur
            texteFinal = text.substring(0, cursorPosition) + '|' + text.substring(cursorPosition);
        }
        // Ajout de la balise de debut et fin et convertit les retours chariot en html
        texteFinal = "<html>" + texteFinal.replace("\n", "<br>") + "</html>";

        // Remplace le texte dans l'UI
        this.zoneTexte.setText(texteFinal);
        zoneTexte.updateUI();

    }
}
