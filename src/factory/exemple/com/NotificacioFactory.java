package factory.exemple.com;

public class NotificacioFactory {

    public static Notificacio crear(String tipus) {
        if (tipus.equalsIgnoreCase("email")) {
            return new NotificacioEmail();
        } else if (tipus.equalsIgnoreCase("sms")) {
            return new NotificacioSms();
        } else {
            throw new IllegalArgumentException("Tipus de notificació no suportat: " + tipus);
        }
    }
}
