mvn clean package
# sudo docker stop maze
# sudo docker rm maze
# sudo docker build -t maze .
# sudo docker run -d -p 332:332 --name maze maze
# sudo docker logs maze

sudo docker-compose down
sudo docker-compose up --build -d
sudo docker-compose logs -f
# sudo docker-compose ps