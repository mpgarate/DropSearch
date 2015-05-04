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
```
- Clone this repository
```sh
cd DropSearch
gradle run
```
```sh
cd DropSearch/client
http-server
```
