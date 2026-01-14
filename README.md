# belaberung-server
A minimal chat / forum application

The name "belaberung" comes from the german word "labern", which means to talk unnecessarily lengthily, see
[https://en.wiktionary.org/wiki/labern](https://en.wiktionary.org/wiki/labern) for more info.

## Backend

The backend for belaberung is written in Java using a SpringBoot REST API

### Run

You will need a existing MariaDB server

```bash 
git clone https://github.com/Delfi-CH/belaberung-server.git
cd belaberung-server
mvn package
java -jar
```
