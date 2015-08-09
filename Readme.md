# DropSearch

General purpose same-domain search engine. 

Enter a url from which to crawl, and perform searches on the documents right away, even if the crawl is still ongoing. 

More details in [writeup.md](https://github.com/mpgarate/DropSearch/blob/master/Writeup.md)

## Accessing the server and running the code

```sh
ssh mike@dropsearch.mpgarate.com
```

enter password ```nyu_search_engines```

Resume my screen session:
```sh
screen -x
```

This will contain screens with a few active processes. Switch between them with 'ctrl + a' followed by 'NUMBER'

- ```0``` : Java service
- ```1``` : nodejs server for client-side files
- ```2``` : bash

You may want to play around with the values in ```src/main/resources/dropsearch.properties``` and see them in action. To do so:

1. Edit and save dropsearch.properties
2. Switch to screen 0 and ctrl+c to kill running java service
3. Start it again with ```gradle run```
4. Visit dropsearch.mpgarate.com
5. Enter your test URL and query

## Install on your own machine
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

