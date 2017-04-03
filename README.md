![deploying-docker-compose](rsc/docker-compose.png)

## Advance use of Docker-Compose

With this example you will have some *JAVA* rest services running on Docker using a *Postgres* database deployed over a *Tomcat* server in three separate containers, all deployed thanks to Docker-Compose.

![deploying-docker-compose-tomcat](rsc/tomcat.jpg)
#### Webapp Container: Java & Tomcat

To build each container on their folders we use *build* tag. First folder to search a Dockerfile and build it will be 'server', in that Dockerfile we are going to pull an image with Tomcat and Java JDK ready.

```sh
FROM tomcat:8-jre8
```

Then for deploying JAVA application we copy the .war file inside Tomcat's webapps folder. 

```sh
COPY /webapp/emailboot.war /usr/local/tomcat/webapps/ROOT.war
```

Finally it will build *db* and *smtp* containers.  
 
```sh
 web:
   build: server
   ports:
     - "8080:8080"
   links:
     - db
     - smtp
```
![deploying-docker-compose-tomcat](rsc/postgresql.jpg)
#### Database Container: PostgreSQL

For the database container we include emailboot.sql initial script.

```sh
CREATE TABLE email_logger (
  log_serial        SERIAL                      NOT NULL,
  log_subject       VARCHAR(100)                NOT NULL,
  log_delivered     BOOL                        NOT NULL DEFAULT 'false',
  log_content       VARCHAR(2500)               NOT NULL,
  log_timestamp     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp
) WITH (OIDS =FALSE);
ALTER TABLE email_logger OWNER TO testusr;
```

It will we run on Postgres start thanks to this command in the Dockerfile.

```sh
ADD script/emailboot.sql /docker-entrypoint-initdb.d/
```

To setup properties and ports we are going to use Docker-Compose as is follows:
 
```sh
db:
  build: database
  restart: always
  ports:
      - "5432:5432"
  environment:
      - DEBUG=true
      - POSTGRES_USER=testusr
      - POSTGRES_PASSWORD=testpwd
```
![deploying-docker-compose-tomcat](rsc/smtp.jpg)      
#### SMTP Container: Postfix 

Finally we add a simple Postfix SMTP TLS relay docker image with no local authentication enabled (to be run in a secure LAN). To setup properties and ports we are going to use Docker-Compose as is follows:

```sh
smtp:
  build: smtp
  ports:
    - "25:25"
  environment:
    - maildomain=mailboot.net
    - smtp_user=user:pwd
    - messageSizeLimit=40960000
    - mailboxSizeLimit=40960000
```

Original Image: [SMTP Postfix](https://registry.hub.docker.com/u/juanluisbaptiste/postfix/)

#### Testing REST services

#### Initial test service

Once the docker-compose up command is finished you can test the webapp by accessing:
```sh
http://localhost:8080/test
  ```

If it was deployed correctly the output should be like this:
```sh
EmailBoot Rest Service - Test Succeeded! 
```

#### Send an email

Once the docker-compose up command is finished you can test the webapp by accessing:
```sh
POST rest service
    http://localhost:8080/v1/emails

Parameters
    "subject" Suject of the email, it will be used on email inbox. Example: Hello I'm here!
    "content" Text content, please do not add javascript in this filed, mailboxs validate javascript conent and they will reject the email. 
    "recipients" List of emails addresses to send the email, if they are more that one separate them with ';'. Example: test@mail.com; test2@mail.com;.
  ```

If the request was process correctly the output should be like this:
```sh
{
  "code": 202,
  "status": "ACCEPTED",
  "url": "[POST] http://localhost:8080/emailboot/v1/emails?subject=Subject%20test&content=Testing%20content%20on%20email&recipients=mail@mail.com;",
  "message": "Email task was accepted and sent to SMTP",
  "data": {
    "id": 1491218659389,
    "from": "127.0.0.1",
    "mailsList": "test@mail.com;",
    "subject": "Subject test",
    "content": "Testing content on email",
    "warnings": null,
    "malformedDirectEmails": null
  }
} 
```

#### List of emails logged

Another REST service include in the JAVA app is list all delivered emails
```sh
GET rest service
    http://localhost:8080/emailboot/v1/logger?startDate=&endDate=2017-12-01 00:00&onlyDelivered=false

Parameters
    "startDate" Initial date to search. Example: 2017-01-01 00:00
    "endDate" Final date to search. Example: 2017-12-01 00:00
    "onlyDelivered" It will filter the results by only deliverd emails. Example: false *To return all emails on log.
  ```

This is the expected output:

```sh
{
  "code": 200,
  "status": "OK",
  "url": "[GET] http://localhost:8080/emailboot/v1/logger?startDate=2017-01-01%2000:00&endDate=2017-12-01%2000:00&onlyDelivered=false",
  "message": "EmailBoot request response.",
  "data": [
    {
      "subject": "Hello EmailBoot",
      "delivered": true,
      "content": "Working as a charm!",
      "versionDate": "04/03/2017 05:08:51"
    }
  ]
}
```
 