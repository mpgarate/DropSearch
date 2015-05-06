## What is DropSearch?

General purpose same-domain search engine. 

Enter a url from which to crawl, and perform searches on the documents, even if the crawl is still ongoing. 

Document relevance is determined by keyword frequency / document length, with an extra weight given to terms that appear in the title. 

First, an attempt is made to search for documents that contain all of the given terms. If this produces zero results, then a more lenient search will return documents that contain any of the keywords. 

Finally, PageRank is used to bubble up relevant documents for a given query. The benefits of this are limited since the data set is generally too small and skewed by being single-domain (so will prefer meta-information pages that are linked to everywhere). 

The PageRank graph is built and the algorithm re-run in a separate thread while the crawler is still active, allowing a well-rounded search before the crawl is complete. 

### A few notes / caveats

Since the goal is to search on data while it is still being crawled, there is no cache of search results. 



## Using DropSearch

### Noteworthy example queries

Performed on:
```
crawled: 2000
url: http://en.wikipedia.org/wiki/Albert_Gallatin
```


```gallatin school``` will show the page for NYU Gallatin before "Albert Gallatin Area School District", even though the latter contains a much higher occ(school). 

```new york``` returns the page for New York

```new york university``` returns the page for New York University, and it appears twice since there is no document deduplication used here. A crawled link to ```http://en.wikipedia.org/wiki/The_Plague_(magazine)``` redirects to the main NYU article, and the document is retrieved again. 

```university``` returns many pages about New York University, but Harvard's pagerank still manages to push it above NYU. 

```nyu``` returns NYU Local, NYU Violets, etc rather than New York University
searching ```nyu``` before many pages have been crawled (~300 or so) will return the New York University page, since the term does appear there. But later results have stronger scores with the exact term appearing in the title. 

```france``` returns France
```wikipedia``` returns Wikipedia:About and Wikipedia:Contact us
```united states``` returns United States but ```America``` returns British America. 

```park``` will return Washington Square Park (at crawl 1700 or so), since our mini-web's pagerank revolves around NYU (via Albert Gallatin)

## Reading the Java code


# Installation
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

