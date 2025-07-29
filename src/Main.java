import builder.exemple.com.NotificacioB;
import factory.builder.com.NotificacioFactoryBuilder;
import factory.exemple.com.Notificacio;
import factory.exemple.com.NotificacioFactory;


/**
 * Factory Pattern: s'encarrega de decidir quin objecte crear,
 * però normalment retorna un objecte ja creat amb valors per defecte o passats com a paràmetres.
 *
 * Builder Pattern: permet construir objectes complexos pas a pas,
 * especialment útil quan hi ha moltes opcions configurables.
 * Suporta method chaining (encadenament de mètodes) com feature1(...).feature2(...).
 */

public class Main {
    public static void main(String[] args) {
        //Patró factory
        System.out.printf("************** Patró factory\n");
        Notificacio notificacio = NotificacioFactory.crear("email");
        notificacio.enviar("Hola des de la Factory!");

        Notificacio altra = NotificacioFactory.crear("sms");
        altra.enviar("Missatge per SMS.");

        System.out.println();
        System.out.printf("*************** Patró builder\n");

        NotificacioB n = new NotificacioB.Builder()
                .canal("email")
                .missatge("Reunió a les 10h")
                .urgent(true)
                .build();

        n.enviar();

        System.out.println();
        System.out.printf("*************** Patró Factory+Builder\n");
        NotificacioB n2 = NotificacioFactoryBuilder
                .crearBuilderPerTipus("sms")
                .missatge("Hola!")
                .urgent(false)
                .build();

        n2.enviar();

    }



}
