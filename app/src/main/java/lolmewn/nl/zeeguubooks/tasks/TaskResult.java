package lolmewn.nl.zeeguubooks.tasks;

/**
 * Created by Lolmewn on 07/01/2016.
 */
public class TaskResult<T> {

    private T result;
    private Exception ex;

    public TaskResult(T result){
        this.result = result;
    }

    public TaskResult(Exception ex){
        this.ex = ex;
    }

    public boolean isError(){
        return ex != null;
    }

    public Exception getError() {
        return ex;
    }

    public T getResult() {
        return result;
    }
}
