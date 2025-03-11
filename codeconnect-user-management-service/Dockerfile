FROM azul/zulu-openjdk-alpine:21-latest

USER root

COPY jenkins_home /var/jenkins_home

RUN chown -R jenkins:jenkins /var/jenkins_home

USER jenkins

EXPOSE 8080 50000

ENTRYPOINT ["/usr/bin/tini", "--", "/usr/local/bin/jenkins.sh"]