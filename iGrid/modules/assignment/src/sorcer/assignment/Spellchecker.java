package sorcer.assignment;

import sorcer.core.SorcerConstants;
import sorcer.core.provider.ServiceTasker;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

import com.sun.jini.start.LifeCycle;

public class Spellchecker extends ServiceTasker implements TextService, SorcerConstants {

    public Spellchecker(String[] args, LifeCycle lifeCycle) throws Exception {
        super(args, lifeCycle);
    }

    @Override
    public Context correctText(Context context) throws RemoteException, ContextException {
        context.putValue("text", "blabla");
        return context;
    }
}
