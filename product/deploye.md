# steps to create aws RDS database for the service

1. create an instance on aws(Aurora and RDS services)
    - choose database type(postgres,mysql,mariaDB etc)  
    - setup the instance properties as you suppose(mode-dev/production,cpu,storage,accsess etc)
    - specially consider security group and vpc
    - finish the instance creation

2. set inbound rules
    - set up new inbound rules for instance
    - go to ec2,select security group
    - choose attached security group for db instance or create new one(later can modify the instance adding new security group)
    - set inbound rules for database type
        ex-: for the postgress select "postgress" for the type,5432 for port and add acsess to relevent ips(0.0.0.0/0-for public/testing)

3. create new database or playaround with instance 
    - instance default create it's own databaeses 
    - create new trough terminal 
        ex for postgres -: psql -h instance_name.ch62tkg2f786.ap-south-1.rds.amazonaws.com -U postgres -d postgres -p 5432(Connect)
                           CREATE DATABASE "db-name";(create db)
                            ***since you are in rds session you are enable to do anything using commands throuh terminal***
    
    - but the esiast way use sw like DataGrip and connect the db and do the things you need using GUI

4. connect with spring boot app
    - add following configurations to application.properties or .ymal file
        ***formated for .properties type***
   
      spring.datasource.url=jdbc:postgresql://your_instance_name.ctg4eyg2s706.ap-south-1.rds.amazonaws.com:5432/db-name
      spring.datasource.username=masterPasswordSetUpForInstance
      spring.datasource.password=SetUpPassword
   
      spring.datasource.hikari.data-source-properties.ssl=true
      spring.datasource.hikari.data-source-properties.sslmode=require


        