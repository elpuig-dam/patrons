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

## 3. El patró Factory

El **Factory** és un patró de disseny creacional que encapsula la lògica de
decidir **quina classe concreta instanciar**, de manera que el codi client
només coneix una interfície o classe abstracta comuna i no depèn de les
implementacions concretes.

Al projecte trobem l'exemple a
`src/factory/exemple/com/NotificacioFactory.java`:

```java
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
```

El mètode estàtic `crear()` centralitza la decisió de quina implementació de
`Notificacio` (`NotificacioEmail` o `NotificacioSms`) s'ha de retornar, en
funció d'un paràmetre (`tipus`). El codi client només treballa amb la
interfície `Notificacio`, sense preocupar-se de com es construeix cada
variant.

### 3.1. Quan fer servir el patró Factory

- Quan tens un `if/else` o `switch` que decideix quina subclasse instanciar
  en diversos punts del codi (com el `"email"`/`"sms"` de l'exemple).
- Quan el client no hauria de conèixer les classes concretes, només la
  interfície o classe abstracta comuna.
- Quan vols centralitzar la lògica de creació perquè, si s'afegeix un tipus
  nou, només calgui modificar un sol lloc (el Factory), en comptes de
  cercar tots els `new` escampats pel codi.

### 3.2. Senyals pràctics per detectar que cal un Factory

1. **`new` escampat arreu del codi** amb lògica condicional repetida per
   decidir quina classe instanciar.
2. **Duplicació de la mateixa lògica de decisió** (el mateix `if/else` o
   `switch`) en diversos llocs del projecte.
3. Afegir un tipus nou obliga a tocar **molts fitxers diferents** en comptes
   d'un de sol.
4. El client necessita dependre únicament d'una interfície/abstracció, no
   de les implementacions concretes.

## 4. Combinació amb el patró Factory

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

### 4.1. Quan combinar Factory i Builder

Val la pena combinar-los quan, a més dels senyals del Factory (secció 3.2),
cada objecte creat té:

- Molts paràmetres opcionals o configurables (constructors amb 5+
  paràmetres són un indici de *code smell*).
- Necessitat de validar l'estat intermedi abans de construir l'objecte
  final.
- Interès en una API fluida (`.canal("sms").missatge(...).urgent(...).build()`)
  un cop decidit el tipus.

### 4.2. Senyals pràctics per detectar-ho

1. El Factory ha de **triar el tipus I configurar-lo amb múltiples
   opcions** alhora.
2. Tens **constructors amb molts paràmetres o telescoping constructors**
   per a cada variant creada pel Factory.
3. Vols que el Factory retorni un `Builder` ja preconfigurat (per exemple,
   amb el canal fixat) i deixar la resta de la configuració al codi client,
   com fa `crearBuilderPerTipus`.

## 5. Avantatges observats

- **Llegibilitat:** el codi client expressa clarament quines propietats
  s'estan configurant, gràcies als noms dels mètodes encadenats.
- **Flexibilitat:** es poden ometre propietats opcionals (per exemple, no
  cridar `urgent()` si no cal).
- **Immutabilitat:** un cop construït amb `build()`, l'objecte `NotificacioB`
  no es pot modificar, ja que el seu constructor és privat.
- **Combinable:** com es veu amb `NotificacioFactoryBuilder`, el Builder es
  pot integrar amb altres patrons creacionals com el Factory.
