#!/bin/bash
echo "assumes you have built the image, and you have an aws profile called tacticalproducts with the correct permissions"
echo "see https://tools.hmcts.net/confluence/display/RST/Using+aws+cli for full instructions"


DB_HOST=`aws ecs describe-task-definition --task-definition civil-appeal-staging --query "taskDefinition.containerDefinitions[0].environment[?name=='DB_HOST'].value" --output text`
DB_PORT=`aws ecs describe-task-definition --task-definition civil-appeal-staging --query "taskDefinition.containerDefinitions[0].environment[?name=='DB_PORT'].value" --output text`
DB_USER=`aws ecs describe-task-definition --task-definition civil-appeal-staging --query "taskDefinition.containerDefinitions[0].environment[?name=='DB_USER'].value" --output text`
DB_PASSWORD="`aws ecs describe-task-definition --task-definition civil-appeal-staging --query "taskDefinition.containerDefinitions[0].environment[?name=='DB_PASSWORD'].value" --output text`"




echo DB_HOST $DB_HOST
echo DB_PORT $DB_PORT
echo DB_USER $DB_USER

docker run -e DB_HOST=$DB_HOST -e DB_PORT=$DB_PORT -e DB_USER=$DB_USER -e DB_PASSWORD="$DB_PASSWORD" -p 80:8080 cact
