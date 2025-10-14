---deploye server to AWS ECS with CI/CD workflow(Docker,ECR)---

#stage 1
Create an ECR repostory
    # Create Repo
1. Use AWS console ECR service or command line:

    aws ecr create-repository \
    --repository-name your-nodejs-app \
    --region us-east-1 
        
    # Authenticate
2. Login to ECR:
    aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com


    # Docker
3. Build and push Docker image to ECR:
    *create appropriate docker file in your project

    *build and push to ECR
        docker build -t repo-name .
        docker tag repo-name:latest YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/repo-name:latest
        docker push YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/repo-name:latest

#stage 2
Create ECS Stuff

    # Set Up ECS Cluster
1. Create ECS Cluster(use console or command):

    aws ecs create-cluster \
    --cluster-name your-nodejs-cluster \
    --region us-east-1

    *also can create service using console or just use command line

# Configure GitHub Secrets
 use AWS console secrete manager service to setup secrets in task-defintion.json 

# Set Up CloudWatch for Monitoring
use AWS console and use correct log group inn task definition

2. Create a log group:
  aws logs create-log-group \
    --log-group-name /ecs/your-nodejs-app \
    --region us-east-1

3. Create Task Definition:
    
    # crate task-definition.json
    Create task-definition.json file that include all nesseary things include how service operate
    (beware ecr image data,roles,health,computation capacity,envs and secrets)

4. Register the task definition:
    # register
aws ecs register-task-definition \
    --cli-input-json file://task-definition.json \
    --region us-east-1

5. create a security group:
    # security group
    *crucial* (use aws console to create sg or command line and use appooriate inbound & outbound rules that match with service operation)

    aws ec2 create-security-group \
    --group-name name-sg \
    --description "Security group for Node.js app" \
    --vpc-id YOUR_VPC_ID

    aws ec2 authorize-security-group-ingress \
    --group-id YOUR_SECURITY_GROUP_ID \
    --protocol tcp \
    --port 3000 \
    --cidr 0.0.0.0/0

6. Create the service:
    # push local project to ECS(use correct subnets and security groups)
    aws ecs create-service \
    --cluster your-ecs-cluster \
    --service-name your-ecs-service \
    --task-definition your--task-definition \
    --desired-count 1 \
    --launch-type FARGATE \
    --network-configuration "awsvpcConfiguration={subnets=[subnet-12345,subnet-67890],securityGroups=[sg-12345],assignPublicIp=ENABLED}" \
    --region us-east-1   

# Set Up Application Load Balancer 
use AWS console to create ALB,Target group and listner or comands like  above

1.  Create Load Balancer:
  aws elbv2 create-load-balancer \
    --name your-alb \
    --subnets subnet-12345 subnet-67890 \
    --security-groups sg-12345 \
    --region us-east-1

2.  Create Target Group:
  aws elbv2 create-target-group \
    --name your-tg \
    --protocol HTTP \
    --port 3000 \
    --vpc-id YOUR_VPC_ID \
    --target-type ip \
    --region us-east-1   

3. Create Listener:
    aws elbv2 create-listener \
    --load-balancer-arn YOUR_LOAD_BALANCER_ARN \
    --protocol HTTP \
    --port 80 \
    --default-actions Type=forward,TargetGroupArn=YOUR_TARGET_GROUP_ARN \
    --region us-east-1

# Create Github Action workflow for the automation
1. Create workflow(.yml) file like deploye.yml 
    .github/workflows

@Becarefule about setting inbound/outbound rules in sg.and setting target groups and albs.
@When use microservice things make sure enable service discovery for services and setup namespace(AWS Cloud Map).
@Use correct sg,log groups,services,repos,namesapaces in project files Docker,task-definitinon and other configuration files.
@And make sure have plugins/packages need to health checks and deployments.
