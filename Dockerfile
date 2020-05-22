FROM jboss/wildfly:17.0.1.Final
COPY target/movie-db-rest-example.war /opt/jboss/wildfly/standalone/deployments/