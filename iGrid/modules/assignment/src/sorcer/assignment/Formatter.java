import com.sun.jini.start.LifeCycle;

import sorcer.core.provider.ServiceProvider;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public class Formatter extends ServiceTasker implements TextService {

    public Formatter(String[] args, LifeCycle lifeCycle) throws Exception
    {
        super(args, lifeCycle);
    }

    @Override
    public Context correctText(Context context) throws ContextException, RemoteException
    {
        String text = (String) context.get("text");

/* Zdanie zaczyna sie od duzej litery. Pierwszy wyraz Stringu staje sie rowniez duza litera.
 * START
 */
		int pos = 0;
		boolean capitalize = true;
		StringBuilder sb = new StringBuilder(text);
		while (pos < sb.length()) {
		    if (sb.charAt(pos) == '.') {
		        capitalize = true;
		    } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
		        sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
		        capitalize = false;
		    }
		    pos++;
		}
		text = sb.toString();
/*
 * END
 */

//      linia powinna zaczynac sie od tabulatury
        text = text.replaceAll("^([A-Z0-9])", "\t$1");

//        \. ([a-z])       -> .toUpperCase()     - jesli po kropce mamy spacje i mala litere to ja powiekszmy zaimplementowac tu
//        TODO

//      linia powinna konczy sie kropka
        text = text.replaceAll("([a-zA-Z0-9])\n", "$1.\n");

//      tekst powinien konczyc sie kropka
        text = text.replaceAll("([a-zA-Z0-9])&", "$1.");


        context.putValue("text", text);
        return context;
    }
}
