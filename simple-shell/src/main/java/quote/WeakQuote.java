package quote;

import env.Environment;
import org.jetbrains.annotations.NotNull;

public class WeakQuote implements Quote {
    private static final char VARIABLE_PREFIX = '$';
    private final String myString;

    public WeakQuote(@NotNull String string) {
        myString = string;
    }

    @NotNull
    @Override
    public String substitute(Environment Enviroment) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < myString.length()) {
            if (myString.charAt(i) == VARIABLE_PREFIX && i + 1 < myString.length() &&
                    Character.isLetter(myString.charAt(i + 1))) {
                int start = i + 1;
                i += 2;
                while (i < myString.length() && Character.isLetterOrDigit(myString.charAt(i))) {
                    i++;
                }

                String variableName = myString.substring(start, i);
                sb.append(Enviroment.getVariableValue(variableName));
            } else {
                sb.append(myString.charAt(i++));
            }
        }

        return sb.toString();
    }

}
