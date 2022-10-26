public class Select implements ICommand{
    public void execute(){
        Buffer buffer = new Buffer();
        buffer.select();
    }
}
