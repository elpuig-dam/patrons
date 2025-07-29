package factory.builder.com;

import builder.exemple.com.NotificacioB;

public class NotificacioFactoryBuilder {
    public static NotificacioB.Builder crearBuilderPerTipus(String tipus) {
        return new NotificacioB.Builder().canal(tipus);
    }
}
