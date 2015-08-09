# DropSearch

General purpose same-domain search engine. 

Enter a url from which to crawl, and perform searches on the documents right away, even if the crawl is still ongoing. 

More details in [writeup.md](https://github.com/mpgarate/DropSearch/blob/master/Writeup.md)

## Install on Ubuntu
Install Java 8 JDK
```sh
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```
Install Gradle
```sh
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle-2.3
```
Install npm and nodejs to serve client files
```sh
sudo apt-get install npm
sudo chown -R $USER /usr/local
npm install -g http-server
sudo ln -s /usr/bin/nodejs /usr/bin/node
```
Reroute port 80 to port 3000 for regular user access
```sh
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 3000
```
Install latest mongoDB: http://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/

Clone this repository and run all pertinent services:

- DropSearch
- Static server for client files
- mongod service

```sh
git clone https://github.com/mpgarate/DropSearch
cd DropSearch
gradle run
```
```sh
cd DropSearch/client
http-server -p 3000
```
```sh
mongod
```

Consider creating a swap file:
https://www.digitalocean.com/community/tutorials/how-to-add-swap-on-ubuntu-12-04

