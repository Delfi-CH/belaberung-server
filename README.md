# belaberung-server
A minimal chat / forum application

The name "belaberung" comes from the german word "labern", which means to talk unnecessarily lengthily, see
[https://en.wiktionary.org/wiki/labern](https://en.wiktionary.org/wiki/labern) for more info.

## Backend

The backend for belaberung is written in Java using a SpringBoot REST API

## Usage

### Docker

See [github.com/Delfi-CH/belaberung-docker](https://github.com/Delfi-CH/belaberung-docker) on how to run this in docker.

### Manually

#### Run

You will need a existing MariaDB server

```bash 
git clone https://github.com/Delfi-CH/belaberung-server.git
cd belaberung-server
mvn package
java -jar target/belaberung-backend-0.0.1-SNAPSHOT.jar -DBELABERUNG_DB_URL=jdbc:mariadb://<YOUR DATABASE IP>:3306/chatapp -DBELABERUNG_DB_USER=<YOUR DATABASE USERNAME> -DBELABERUNG_DB_PASSWORD=<YOUR DATABASE PASSWORD>
```
