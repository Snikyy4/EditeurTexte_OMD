public class Suivant implements ICommand{
    public void execute(){
        Buffer buffer = new Buffer();
        buffer.etatSuivant();
    }
}