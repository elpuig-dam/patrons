package builder.exemple.com;

public class NotificacioB {
    private String canal;
    private String missatge;
    private boolean urgent;

    // Constructor privat: només es pot construir des del Builder
    private NotificacioB(String canal, String missatge, boolean urgent) {
        this.canal = canal;
        this.missatge = missatge;
        this.urgent = urgent;
    }

    public void enviar() {
        System.out.println("[" + canal.toUpperCase() + "] " + (urgent ? "URGENT: " : "") + missatge);
    }

    // Builder estàtic
    public static class Builder {
        private String canal;
        private String missatge;
        private boolean urgent;

        public Builder canal(String canal) {
            this.canal = canal;
            return this;
        }

        public Builder missatge(String missatge) {
            this.missatge = missatge;
            return this;
        }

        public Builder urgent(boolean urgent) {
            this.urgent = urgent;
            return this;
        }

        public NotificacioB build() {
            return new NotificacioB(canal, missatge, urgent);
        }
    }
}
