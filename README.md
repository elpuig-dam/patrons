# El Patró de Disseny Builder

Explicació i exemples extrets del projecte **patrons**.

## 1. Què és el patró Builder?

El **Builder** és un patró de disseny creacional que permet construir objectes
complexos pas a pas, separant el procés de construcció de la representació
final de l'objecte. És especialment útil quan un objecte té moltes propietats
configurables (algunes opcionals) i quan volem evitar constructors amb molts
paràmetres (l'anomenat *telescoping constructor*).

Les seves característiques principals són:

- Permet construir un objecte mitjançant una sèrie de crides a mètodes
  encadenats (*method chaining*), com `feature1(...).feature2(...)`.
- El constructor de la classe final sol ser **privat**, de manera que només
  es pot crear l'objecte a través del Builder.
- Cada mètode del Builder retorna `this`, permetent encadenar configuracions
  de forma llegible.
- Un mètode final, normalment anomenat `build()`, crea i retorna l'objecte
  definitiu ja configurat.

## 2. Exemple del projecte: NotificacioB

Al projecte trobem l'exemple a
`src/builder/exemple/com/NotificacioB.java`. La classe `NotificacioB`
representa una notificació (canal, missatge i si és urgent o no) i incorpora
una classe interna estàtica `Builder` encarregada de construir-la pas a pas.

### 2.1. Codi de la classe NotificacioB

```java
package builder.exemple.com;

public class NotificacioB {
    private String canal;
    private String missatge;
    private boolean urgent;

    // Constructor privat: nomes es pot construir des del Builder
    private NotificacioB(String canal, String missatge, boolean urgent) {
        this.canal = canal;
        this.missatge = missatge;
        this.urgent = urgent;
    }

    public void enviar() {
        System.out.println("[" + canal.toUpperCase() + "] "
            + (urgent ? "URGENT: " : "") + missatge);
    }

    // Builder estatic
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
```

Observem els elements claus del patró: el constructor de `NotificacioB` és
privat, i cada mètode del `Builder` (`canal()`, `missatge()`, `urgent()`)
retorna `this`, permetent l'encadenament. Finalment, `build()` crida al
constructor privat per generar l'objecte ja completament configurat.

### 2.2. Ús del Builder a Main.java

```java
System.out.printf("*************** Patro builder\n");

NotificacioB n = new NotificacioB.Builder()
        .canal("email")
        .missatge("Reunio a les 10h")
        .urgent(true)
        .build();

n.enviar();
// Sortida: [EMAIL] URGENT: Reunio a les 10h
```

Gràcies al method chaining, la construcció de l'objecte es llegeix de forma
molt clara: primer definim el canal, després el missatge i finalment si és
urgent, abans de cridar `build()` per obtenir la notificació llesta per
enviar.

## 3. Combinació amb el patró Factory

El projecte també mostra com combinar el Builder amb el patró **Factory**.
La classe `NotificacioFactoryBuilder` (a
`src/factory/builder/com/NotificacioFactoryBuilder.java`) actua com una
fàbrica que retorna un `Builder` ja preconfigurat amb el canal indicat,
deixant la resta de la configuració (missatge, urgència, etc.) a càrrec del
codi client mitjançant method chaining.

```java
package factory.builder.com;

import builder.exemple.com.NotificacioB;

public class NotificacioFactoryBuilder {
    public static NotificacioB.Builder crearBuilderPerTipus(String tipus) {
        return new NotificacioB.Builder().canal(tipus);
    }
}
```

Ús combinat a Main.java:

```java
System.out.printf("*************** Patro Factory+Builder\n");

NotificacioB n2 = NotificacioFactoryBuilder
        .crearBuilderPerTipus("sms")
        .missatge("Hola!")
        .urgent(false)
        .build();

n2.enviar();
// Sortida: [SMS] Hola!
```

Aquesta combinació aprofita els avantatges dels dos patrons: la **Factory**
decideix quin tipus de canal s'utilitza (encapsulant la lògica de creació),
mentre que el **Builder** permet configurar la resta de propietats de forma
flexible i llegible, sense necessitat de crear múltiples constructors o
mètodes de fàbrica per a cada combinació possible d'opcions.

## 4. Avantatges observats

- **Llegibilitat:** el codi client expressa clarament quines propietats
  s'estan configurant, gràcies als noms dels mètodes encadenats.
- **Flexibilitat:** es poden ometre propietats opcionals (per exemple, no
  cridar `urgent()` si no cal).
- **Immutabilitat:** un cop construït amb `build()`, l'objecte `NotificacioB`
  no es pot modificar, ja que el seu constructor és privat.
- **Combinable:** com es veu amb `NotificacioFactoryBuilder`, el Builder es
  pot integrar amb altres patrons creacionals com el Factory.
