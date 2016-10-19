package quote;

import env.Environment;
import org.jetbrains.annotations.NotNull;

public interface Quote {

    @NotNull
    String substitute(Environment Enviroment);
}
