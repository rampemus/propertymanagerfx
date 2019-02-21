package propertymanager.apuja;

import java.util.function.Consumer;

public class Validator {
    public static void validateInt(String s, Consumer<Integer> c) {
        try {
            if (!s.isEmpty())
                c.accept(validateInt(s));
        } catch (NumberFormatException e) {

        }
    }

    public static int validateInt(String s) throws NumberFormatException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            validationWarning(s, "kokonaisluvuksi");
            throw e;
        }
    }

    public static void validateFloat(String s, Consumer<Float> c) {
        try {
            if (!s.isEmpty())
                c.accept(validateFloat(s));
        } catch (NumberFormatException e) {

        }
    }

    public static float validateFloat(String s) throws NumberFormatException {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            validationWarning(s, "liukuluvuksi");
            throw e;
        }
    }

    private static void validationWarning(String s, String kohde) {
        propertymanager.apuja.Dialogs.warning("Validointivirhe", "Seuraavaa ei voitu muuttaa" + kohde, s);
    }
}