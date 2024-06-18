sudo docker stop maze
sudo docker rm maze
sudo docker build -t maze .
sudo docker run -d -p 332:332 --name maze maze
sudo docker logs maze