package factory.exemple.com;

public class NotificacioSms implements Notificacio{
    @Override
    public void enviar(String missatge) {
        System.out.println("Enviant sms: " + missatge);

    }
}
