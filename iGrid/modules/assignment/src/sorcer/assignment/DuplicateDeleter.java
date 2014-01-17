package sorcer.assignment;

import sorcer.core.SorcerConstants;
import sorcer.core.provider.ServiceTasker;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jini.start.LifeCycle;

public class DuplicateDeleter extends ServiceTasker implements TextService, SorcerConstants {

    public DuplicateDeleter(String[] args, LifeCycle lifeCycle) throws Exception {
        super(args, lifeCycle);
    }

    @Override
    public Context correctText(Context context) throws RemoteException, ContextException {
        String regWord = "(\\b\\w+)[\\p{Blank}]+\\1";
        String regInter = "[\\w+][\\p{Blank}]*(\\p{Punct}+)";
        String stringText= "";
        stringText = (String) context.get("text");
        StringBuffer text = new StringBuffer(stringText);
        Pattern interPattern = Pattern.compile(regInter);
        Matcher interMatcher = interPattern.matcher(text);
        int dotCount = 0;
        int interAt = 0;
        while (interMatcher.find(interAt)) {
            int end = interMatcher.end(1);
            for (int i = interMatcher.start(1) + 1; i <= end && i <= text.length() - 1; ) {

                if (text.charAt(i) == text.charAt(i - 1)) {
                    if (text.charAt(i - 1) == '.') {
                        if (i == interMatcher.start(1) + 1)
                            dotCount++;
                        dotCount++;
                        if (dotCount > 3) {
                            text.deleteCharAt(i);
                            dotCount--;
                            end--;
                        } else
                            i++;
                        if (i == end && dotCount == 2) {
                            text.deleteCharAt(i - 1);
                        }
                    } else {
                        if (dotCount == 2) {
                            text.deleteCharAt(i);
                            end--;

                        }
                        dotCount = 0;
                        text.deleteCharAt(i);
                        end--;
                    }
                } else
                    i++;

            }
            dotCount = 0;
            interAt = interMatcher.start(1);
        }

        Pattern wordPattern = Pattern.compile(regWord, Pattern.CASE_INSENSITIVE);
        Matcher wordMatcher = wordPattern.matcher(text);
        int wordMatcherEnd = 0;
        while (wordMatcher.find(wordMatcherEnd)) {
            text.delete(wordMatcher.start(1), wordMatcher.end(1) + 1);
            wordMatcherEnd = wordMatcher.start();
        }
        context.putValue("text", text.toString());
        return context;
    }
}
