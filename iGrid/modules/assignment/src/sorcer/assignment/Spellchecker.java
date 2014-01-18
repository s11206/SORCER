package sorcer.assignment;

import sorcer.core.SorcerConstants;
import sorcer.core.provider.ServiceTasker;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.sun.jini.start.LifeCycle;
import sorcer.util.Log;

public class Spellchecker extends ServiceTasker implements TextService, SorcerConstants {

    private Set<String> dictionary = new HashSet<String>();

    private static Logger logger = Log.getTestLog();

    public Spellchecker(String[] args, LifeCycle lifeCycle) throws Exception {
        super(args, lifeCycle);
        String dictionaryLocation = getProperty("dictionary");

        File dictionaryFile = new File(dictionaryLocation);
        loadDictionary(dictionaryFile);
    }

    private void loadDictionary(File dictionaryFile) {
        try {
            InputStream inputStream = new FileInputStream(dictionaryFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Error in loading dictionary file", e);
        }
    }

    @Override
    public Context correctText(Context context) throws RemoteException, ContextException {
        String text = (String) context.get("text");
        text = spellcheck(text);
        context.putValue("text", text);
        return context;
    }

    private String spellcheck(String text) {
        String strippedText = text.replaceAll("[^\\p{L}\\p{Z}]","");
        StringTokenizer tokenizer = new StringTokenizer(strippedText, " ");
        while (tokenizer.hasMoreElements()) {
            int bestMatch = Integer.MAX_VALUE;
            String replacement = null;
            String token = tokenizer.nextToken().trim();
            for (String reference : dictionary) {
                if (reference.length() == token.length()) {
                    int distance = computeHammingDistance(token, reference);
                    if (distance < bestMatch) {
                        bestMatch = distance;
                        replacement = reference;
                    }
                }
            }
            if (replacement != null && bestMatch < Integer.MAX_VALUE && bestMatch > 0) {
                logger.info(String.format("Replacing %s with %s using distance %d", token, replacement, bestMatch));
                text = text.replace(token, replacement);
            }
        }
        return text;
    }

    private static int computeHammingDistance(String word, String reference) {
        if (word.length() != reference.length()) {
            throw new RuntimeException("Words lengths doesn't match");
        }
        word = word.toLowerCase();
        reference = reference.toLowerCase();
        int distance = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != reference.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }
}
