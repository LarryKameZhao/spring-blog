docker run --name my-mysql-blog -e MYSQL_ROOT_PASSWORD=root  -e MYSQL_DATABASE=blog -p 3306:3306  -d mysql

docker run --name maria-blog -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=blog -p 3308:33062 -d mariadb

docker run 627d2b2751be