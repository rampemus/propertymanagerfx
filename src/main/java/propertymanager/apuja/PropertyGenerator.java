package propertymanager.apuja;

import propertymanager.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PropertyGenerator {
    private final ArrayList<Property> asuntoLista = new ArrayList<>();
    private final Object lock = new Object();
    private int tehtäviäAsuntoja = 0;
    private int tehtyjäAsuntoja = 0;

    // lisää listaan vanhat
    public PropertyGenerator(ArrayList<Property> oldList) {
        asuntoLista.addAll(oldList);
    }

    // säieturvallinen, sisäinen rajapinta
    protected void lisääAsunto(Property a) {
        synchronized (lock) {
            asuntoLista.add(a);
            tehtyjäAsuntoja++;
        }
    }

    // säieturvallinen, sisäinen rajapinta
    protected void aloitaUrakka(int kpl) {
        synchronized (lock) {
            tehtäviäAsuntoja = kpl;
            tehtyjäAsuntoja = 0;
        }
    }

    // säieturvallinen
    public int tehtäviäAsuntoja() {
        synchronized (lock) {
            return tehtäviäAsuntoja;
        }
    }

    // säieturvallinen
    public int tehtyjäAsuntoja() {
        synchronized (lock) {
            return tehtyjäAsuntoja;
        }
    }

    // säieturvallinen
    public List<Property> annaAsunnot() {
        synchronized (lock) {
            return new ArrayList<>(asuntoLista);
        }
    }

    // apurutiini
    private static <T> T random(List<T> options) {
        return options.get(new Random().nextInt(options.size()));
    }

    // säieturvallinen
    public static Property luo() {
        List<String> etuetu = List.of("Villa ", "", "", "", "", "");
        List<String> etu = List.of("Metsä", "Kallio", "Nummi", "Lehto", "Maa", "Joki", "Niemi", "Autio", "Erä", "Korpi", "Kuusi");
        List<String> taka = List.of("Laakso", "Hovi", "Rinne", "Ranta", "Koski", "Pirtti", "Linna", "Suo", "Niitty");
        List<String> tie = List.of("tie", "kuja", "katu", "polku");
        List<String> ehdot = List.of("Kissat", "Koirat", "Lapset", "Mölyäminen", "Suihku ja imurointi ennen klo 7", "Kaupustelu", "Kerjääminen");
        List<String> nimi = List.of("Matti", "Mervi", "Olli", "Johanna", "Ilpo", "Jenni");
        List<String> rappu = List.of("A", "B", "Ö", "F", "N", "R");
        List<String> kaikki = new ArrayList<>();
        kaikki.addAll(etu);
        kaikki.addAll(taka);

        int neliömäärä = 8 + 3 * new Random().nextInt(30);
        int rakennusvuosi = 1850 + new Random().nextInt(170);

        return new Property(
                new Main().getClass().getResource("house" + (1 + new Random().nextInt(6)) + ".png").toExternalForm(),
                random(etuetu) + random(etu) + random(taka).toLowerCase() + " " + random(rappu) + " " + new Random().nextInt(100),
                random(kaikki) + random(tie) + " " + (1 + new Random().nextInt(200)),
                rakennusvuosi,
                neliömäärä,
                (int) (neliömäärä * (7f + (rakennusvuosi - 1850) / 20f + new Random().nextDouble() * 9)),
                new Random().nextBoolean() ? random(ehdot) + " kielletty" : "-",
                new String[]{"Hyvä asunto keskeisellä paikalla kaikkien palveluiden äärellä."},
                random(nimi).toLowerCase() + new Random().nextInt(10000) + "@" + random(kaikki).toLowerCase() + "mail.com",
                random(Property.kuntoLuokitukset)
        );
    }

    // säieturvallinen, toimii taustasäikeessä
    public void luoAsuntoja(int kpl) {
        // keskeytetään jos vanha työ kesken
        if (tehtäviäAsuntoja() > tehtyjäAsuntoja()) return;

        aloitaUrakka(kpl);

        new Thread(() -> {
            while (tehtyjäAsuntoja() < tehtäviäAsuntoja()) {
                lisääAsunto(luo());
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }

            }
        }).start();
    }
}
