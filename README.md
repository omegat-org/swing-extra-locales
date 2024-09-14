# Java Swing extra locales library

Java Swing standard library has several UI parts with labels such as File Chooser, Color Chooser, Option dialogs, etc.

Java only supports a limited number of genuine translations.

  Table 1. Supported UI translations on Java Runtime

| Language                | Locale ID |
|-------------------------|-----------|
| Chinese (Simplified)    | zh_CN     |
| Chinese (Traditional)   | zh_TW     |
| English                 | en        |
| French                  | fr        |
| German                  | de        |
| Italian                 | it        |
| Japanese                | ja        |
| Korean                  | ko        |
| Portuguese (Brazillian) | pt_BR     |
| Spanish                 | es        |
| Swedish                 | sv        |

origin: https://www.oracle.com/java/technologies/javase/java9locales.html#translation

## Supported extra locales

The library supports the following languages;

| Language | Locale ID |
|----------|-----------|
| Arabic   | ar        |
| Catalan  | ca        |
| Russian  | ru        |
| Ukrainian| uk        |

You are welcome to contribute the localization.

## Development status

The library is currently under active development, it is considered as a BETA state.

## How to use

Call `org.omegat.swing.extra.ExtraLocales.initialize()` once before starting GUI parts.
It automatically detects a system locale from "user.language" system properties.
When the locale is supported by the library, the library load translations into Java runtime
through `javax.swing.UIManager`.

## How to contribute translations

1. Add or update basic_xx.properties and gtk_xx.properties bundle file under src/main/resources/org/omegat/swing/extra
2. Update the `ExtraLocales.supported` constant array.

## License and copyright

This library is distributed under GPL-2 license with Class Path exception.
Some parts of the project are deliverables of `org.omegat:lib-mnemonics` and `netbeans` project.

Russian Translation copyright 2004-2005 Maxym Mykhalcuk

The library copyright 2024 Hiroshi Miura
