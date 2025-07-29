package factory.exemple.com;

public class NotificacioEmail implements Notificacio{
    @Override
    public void enviar(String missatge) {
        System.out.println("Enviant email: " + missatge);

    }
}
