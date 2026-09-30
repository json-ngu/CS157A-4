# MindMerge — Tomcat 9 Copy

This copy is configured for Apache Tomcat 9 and Java EE 8 APIs.

Compatibility notes:

- Servlets use the `javax.servlet.*` namespace.
- JSP/JSTL uses the Java EE JSTL URI and `javax.servlet:jstl:1.2`.
- `pom.xml` targets Java 8 bytecode for broader Tomcat 9 compatibility.
- `web.xml` uses Dynamic Web Module 4.0 and the Java EE namespace.

This directory intentionally excludes `db.properties`, build output in `target`, Eclipse metadata/server files, and the Git metadata from the original repository. Copy `db.properties.example` to `db.properties` and fill in local database settings before running.

This is a separate compatibility copy. It has not been committed or pushed.