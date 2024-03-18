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

| Language | Locale ID |
|----------|-----------|
| Russian  | ru        |


## How to use

Call `Locales.initialize()` once before starting GUI parts.
It automatically detects a system locale from "user.language" system properties.
When the locale is supported by the library, the library load translations into Java runtime
through `javax.swing.UIManager`.

## How to add translations for other languages

1. add basic_xx.properties bundle
2. Update `Locales.supported` constant array.

## License and copyright

This library is distributed under GPL-3 license.
Some parts of the project are deliverables of `org.omegat:lib-mnemonics` and `netbeans` project.

Russian Translation copyright 2004-2005 Maxym Mykhalcuk

The library copyright 2024 Hiroshi Miura
