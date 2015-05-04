- make sure to configure path for WordNet


- Install Java 8 JDK
```sh
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```
- Install Gradle
```sh
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle-2.3
```
- Install npm and nodejs to serve client files
```sh
sudo apt-get install npm
sudo chown -R $USER /usr/local
npm install -g http-server
sudo ln -s /usr/bin/nodejs /usr/bin/node
```
- Reroute port 80 to port 3000 for regular user access
```sh
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 3000
```
- Clone this repository
```sh
cd DropSearch
gradle run
```
```sh
cd DropSearch/client
http-server -p 3000
```
