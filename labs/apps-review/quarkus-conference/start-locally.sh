#!/bin/sh

echo "Starting the 'microservice-schedule' project "
podman run -d --name conference-schedule -p 8083:8080 quay.io/redhattraining/quarkus-conference-schedule
SCHEDULE_PID=$!
sleep 5


echo "Starting the 'microservice-session' project "
cd microservice-session
podman run -d --name postgresql-conference -p 5432:5432 -e POSTGRESQL_PASSWORD=confi_user -e POSTGRESQL_USER=conference_user -e POSTGRESQL_ADMIN_PASSWORD=conference -e POSTGRESQL_DATABASE=conference registry.access.redhat.com/rhscl/postgresql-10-rhel7:1
sleep 5
mvn quarkus:dev &
SESSION_PID=$!
sleep 10
cd ..

echo "Starting the 'microservice-speaker' project "
cd microservice-speaker
mvn quarkus:dev &
SPEAKER_PID=$!
sleep 10
cd ..

echo "Starting the 'microservice-vote' project "
cd microservice-vote
podman run -d --name mongo-vote -p 27017:27017 mongo:4.0
mvn quarkus:dev &
VOTE_PID=$!
sleep 10
cd ..

echo "Starting the web application "
podman run -d --name conference-frontend -p 8080:8080 quay.io/redhattraining/quarkus-conference-frontend &
WEBAPP_PID=$!
sleep 10
cd ..

echo
read -p "Press enter to Terminate"
echo 
kill $SCHEDULE_PID $SESSION_PID $SPEAKER_PID $VOTE_PID $WEBAPP_PID
podman rm -f mongo-vote postgresql-conference conference-frontend conference-schedule
sleep 4
echo "All services terminated"
echo