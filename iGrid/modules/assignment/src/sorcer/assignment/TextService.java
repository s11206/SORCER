package sorcer.assignment;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TextService extends Remote {

    Context correctText(Context context) throws RemoteException, ContextException;

}
