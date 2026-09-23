**NOTE to Professor Wu:** Until the end of the week of September 22, 2026, it will show me (Jason Nguyen) as the only contributor for the GitHub repository. I, the team lead, wanted to be the one responsible for creating the main structure as I believe it is important to maintain consistency in the starting stages. This means a couple things:

- This allows my team members (Andrew and Hailey), to make sure they will have the project set up exactly the way it should be set up. The earlier we can be on the same page, the fewer consistencies and troubleshooting we will encounter later on during the course of the project.
- This is also why I kept the website initially barebones (as submitted on Canvas) since it allowed my team members to test as well as troubleshoot for any problems (on either end) before we work on the actual landing page.

# MindMerge
### Written by Jason Nguyen (Updated as of September 22, 2026)

**What is MindMerge?**

A web platform that records the reasoning behind a team's decisions: the context,
the options considered, and why the final choice was made. So that "why did we do
it this way?" can be answered months later, even after the people involved have left.

**CS157A Team 4** - Jason Nguyen, Andrew Khoi Luu, Hailey Liu

This document covers how to set the project up and run it on macOS or Windows. The application runs locally. Public hosting may be revisited later, since a Tomcat deployment requires a Java-capable host rather than a static or serverless platform.

## Architecture

| Tier | Technology |
|---|---|
| Client | Browser; HTML, CSS, JSP pages |
| Application | Apache Tomcat 9; Java Servlets, JDBC |
| Data | MySQL |

A request for the home page travels:

```
browser → index.jsp (redirect) → HomeServlet → DecisionDao → JDBC → MySQL
                                       ↓
                             WEB-INF/home.jsp → HTML → browser
```

## Requirements

| Tool | Version | Check with |
|---|---|---|
| JDK | 21 or newer | `java -version` |
| Apache Tomcat | **9.x** | folder name, e.g. `apache-tomcat-9.0.122` |
| MySQL Server | 8.0 or newer | `mysql --version` |
| Maven | 3.9 or newer | `mvn -v` |

> **Tomcat 9 is required.** Tomcat 10 and later renamed the `javax.servlet` package
> to `jakarta.servlet`. This project imports `javax.servlet`, so it will not run on
> Tomcat 10 or 11. Download Tomcat 9 at https://tomcat.apache.org/download-90.cgi

**Installing Maven**

- macOS: `brew install maven`
- Windows: download the binary zip from https://maven.apache.org/download.cgi,
  extract it, and add its `bin` folder to your PATH.

**Windows only: two things to set up first**

1. **`JAVA_HOME` must be set,** or Tomcat's startup script exits immediately.
   Settings → System → About → Advanced system settings → Environment Variables →
   New system variable, name `JAVA_HOME`, value your JDK folder
   (e.g. `C:\Program Files\Java\jdk-21`).

2. **Add MySQL to your PATH** if `mysql --version` isn't recognized. The folder is
   usually `C:\Program Files\MySQL\MySQL Server 8.0\bin`. Alternatively, use MySQL
   Workbench for the database steps below.

Throughout this document, `TOMCAT_HOME` means wherever you installed Tomcat 9,
for example `~/tools/apache-tomcat-9.0.122` or `C:\apache-tomcat-9.0.122`.

## Setup

### 1. Get the code

```bash
git clone https://github.com/json-ngu/CS157A-4.git mindmerge
cd mindmerge
```

### 2. Create the database

This creates the `mindmerge_db` database, the `decision` table, and seed rows.

**macOS / Linux**

```bash
mysql -u root -p < sql/schema.sql
```

**Windows: Command Prompt**

```
mysql -u root -p < sql\schema.sql
```

**Windows: PowerShell** (PowerShell doesn't support `<` redirection)

```powershell
Get-Content sql\schema.sql | mysql -u root -p
```

**Or in MySQL Workbench, on any platform:** File → Open SQL Script, choose
`sql/schema.sql`, then click the lightning bolt to run it.

Confirm it worked:

```bash
mysql -u root -p -e "SELECT * FROM mindmerge_db.decision;"
```

You should see at least one row. In Workbench, refresh the Schemas panel
(right-click → Refresh All) and expand `mindmerge_db` → Tables → `decision`.

### 3. Add your database credentials

The repository contains `db.properties.example` but not `db.properties`, the real
file holds a password and is excluded from version control, so each
person needs to create their own.

**macOS / Linux**

```bash
cp db.properties.example src/main/resources/db.properties
```

**Windows: Command Prompt**

```
copy db.properties.example src\main\resources\db.properties
```

**Windows: PowerShell**

```powershell
Copy-Item db.properties.example src\main\resources\db.properties
```

Then open `src/main/resources/db.properties` and replace `your_password_here` with
your MySQL root password. No quotes and no trailing spaces:

```properties
db.url=jdbc:mysql://localhost:3306/mindmerge_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=YOUR_PASSWORD
```

### 4. Build

Same command on both platforms, run from the project root:

```bash
mvn clean package
```

The first run downloads dependencies and takes about a minute. Look for
`BUILD SUCCESS` and a new file at `target/mindmerge.war`.

### 5. Deploy and start Tomcat

**macOS / Linux**

```bash
cp target/mindmerge.war TOMCAT_HOME/webapps/
TOMCAT_HOME/bin/startup.sh
```

If you get "permission denied," make the scripts executable once:

```bash
chmod +x TOMCAT_HOME/bin/*.sh
```

**Windows: Command Prompt**

```
copy target\mindmerge.war TOMCAT_HOME\webapps\
TOMCAT_HOME\bin\startup.bat
```

**Windows: PowerShell**

```powershell
Copy-Item target\mindmerge.war TOMCAT_HOME\webapps\
& TOMCAT_HOME\bin\startup.bat
```

On Windows, `startup.bat` opens a second console window showing the server log.
Leave that window open; closing it stops Tomcat.

### 6. Open the application

**http://localhost:8080/mindmerge/**

You should see the decision list and a System status card reading
`Database: connected`.

**To stop the server:**

- macOS / Linux: `TOMCAT_HOME/bin/shutdown.sh`
- Windows: `TOMCAT_HOME\bin\shutdown.bat`

## Troubleshooting

**`db.properties not found`**
The file must be at `src/main/resources/db.properties`, not the project root. Run
`mvn clean package` again after creating it. Maven copies it into the build, so
editing the source file alone changes nothing.

**`Access denied for user 'root'@'localhost'`**
The password in `db.properties` doesn't match your MySQL install. Use the same one
that works for `mysql -u root -p`.

**`Unknown database 'mindmerge_db'`**
Step 2 hasn't run successfully yet.

**`No suitable driver found for jdbc:mysql://...`**
Tomcat is serving an outdated copy of the app. Stop Tomcat, delete both
`webapps/mindmerge/` and `webapps/mindmerge.war`, rebuild, copy the new war in, and
start Tomcat again.

**Compilation errors mentioning `javax.servlet`**
You're running Tomcat 10 or later. Install Tomcat 9.

**Tomcat's startup script exits immediately (Windows)**
`JAVA_HOME` isn't set. See the Windows setup notes above.

**`Address already in use` when starting Tomcat**
Another process holds port 8080.

- macOS / Linux: `lsof -i :8080`
- Windows: `netstat -ano | findstr :8080`, then look up the PID in Task Manager

**`mysql` is not recognized (Windows)**
MySQL isn't on your PATH. Add it, or use MySQL Workbench for the database steps.

**Anything else — check Tomcat's log**, which is where stack traces land:

- macOS / Linux: `tail -50 TOMCAT_HOME/logs/catalina.out`
- Windows: open the newest `TOMCAT_HOME\logs\catalina.YYYY-MM-DD.log`
  (Windows has no `catalina.out`; the startup console window shows the same output)

## Repository layout

```
mindmerge/
├── pom.xml                      Maven build configuration
├── db.properties.example        Template for local database credentials
├── sql/schema.sql               Database definition and seed data
└── src/main/
    ├── java/mindmerge/
    │   ├── util/DbUtil.java     JDBC connections
    │   ├── model/Decision.java  A decision record
    │   ├── dao/DecisionDao.java SQL for the decision table
    │   └── servlet/             Request handlers
    ├── resources/               db.properties goes here (you create it)
    └── webapp/
        ├── index.jsp            Entry point, redirects to /home
        ├── css/style.css
        └── WEB-INF/
            ├── web.xml          Deployment descriptor
            └── home.jsp         Home page view
```
