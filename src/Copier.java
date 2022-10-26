public class Copier implements ICommand {
    public void execute(){
        Buffer buffer = new Buffer();
        buffer.copier();
    }
}
