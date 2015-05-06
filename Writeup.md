# DropSearch
Project by Michael Garate

## What is DropSearch?

General purpose same-domain search engine. 

http://dropsearch.mpgarate.com/

Enter a url from which to crawl, and perform searches on the documents right away, even if the crawl is still ongoing. 

Document relevance is determined by keyword frequency / document length, with an extra weight given to terms that appear in the title. 

First, an attempt is made to search for documents that contain all of the given terms. If this produces zero results, then a more lenient search will return documents that contain any of the keywords. 

Finally, PageRank is applied to document scores for a given query. The benefits of this are limited since the data set is pretty small and skewed by being single-domain (so will prefer meta-information pages that are linked to everywhere). 

The PageRank graph is built and the algorithm re-run in a separate thread while the crawler is still active, allowing a well-rounded search before the crawl is complete. 

## How should I test out the site?

First, I recommend you do some searches with the default url I provide to the Albert Gallatin wikipedia page. This one is already fetched into the database, so it will quickly "crawl" the 2000 pages. 

After that, try a few other websites and queries. 
I looked at:

- Amazon products
- Craigslist ads
- The NYU Search Engines course site
- kenrockwell.com

Notice the quality of search results while the crawler is still active compared to after a crawl is complete. 

## How do I read the code?

I would recommend first looking at ```src/main/resources/dropsearch.properties```

This file contains config values and references to the pertinent classes. 

Feel free to edit this file and re-run the server. 

## Architecture sketch

A SearchEngine instance provides an API to begin a crawl and to perform searches on the crawl. A crawl can by synchronous for testing purposes or asynchronous (in another thread), which is the intended usage. 

A SearchEngine has a crawler, which retrieves pages from the web by breadth first search from the startUrl, stopping at a specified number of pagesVisited. 

Pages are stored in a mongoDB database, and a keyword index is built in memory using a Java hashmap data structure suitable for multithreaded access. In each bucket is a skiplist of urlIds sorted by occ(document). 

At a specified rate, currently set at every 100 pages visited, the pageRank will be recalculated (if it is not already being calculated). 

A SearchEngine has a RetrievalEngine for gathering documents, which first processes pages which contain every keyword in the query. If no pages are found that match this rule, a more lenient search will run with documents matching any query. 

For a given SearchEngine, you will probably have 3 threads doing work at a given time. 

1. Crawler.java continuing to crawl for more documents
2. RetrievalEngine.java handling an incoming query
3. PageRanker.java updating the page rank

## Referenced works

http://infolab.stanford.edu/~backrub/google.html

http://lucene.apache.org/core/3_0_3/api/core/org/apache/lucene/search/Similarity.html

http://blog.algorithmia.com/post/116365814879/how-machines-see-the-web-exploring-the-web

https://github.com/louridas/pagerank/blob/master/java/PageRankCalc.java

## Pertinent software used

WordNet, Jsoup, net.sf.jung (for graph and pagerank), https://rednoise.org/rita/

### A few notes / caveats

PageRank helped improve the search results overall, but for many queries the quality reduced considerably. From the Albert Gallatin Wikipedia example, a search of "usa" or "america" without PageRank applied produced United States at the top. With PageRank, however, the "Wikipedia:General_disclaimer" and similar meta pages took over because it is linked to from everywhere and contains the term 'usa'. 

Since the goal is to search on data while it is still being crawled, there is no cache of search results. 

I originally intended to use keyword stems and synsets from wordnet, but I found these to be unreliable and had a negative impact on the quality of search results.

The crawler does not obey robots.txt files. 

## Using DropSearch

### Noteworthy example queries

Performed on:
```
crawled: 2000
url: http://en.wikipedia.org/wiki/Albert_Gallatin
```

```gallatin school``` will show the page for NYU Gallatin before "Albert Gallatin Area School District", even though the latter contains a much higher occ(school). 

```new york``` returns the page for New York City followed by New York

```new york university``` returns the page for New York University, and it appears twice since there is no document deduplication used here. A crawled link to ```http://en.wikipedia.org/wiki/The_Plague_(magazine)``` redirects to the main NYU article, and the document is retrieved again. 

```university``` returns many pages about New York University, but Harvard's pagerank still manages to push it above NYU. 

```nyu``` returns NYU Local and NYU Violets, followed by New York University

```france``` returns France

```french``` returns France followed by French Revolution

```wikipedia``` returns Wikipedia:About and Wikipedia:Contact us

```united states``` returns United States but ```America``` returns British America. 

```park``` will return Washington Square Park since our mini-web's pagerank revolves around NYU (via Albert Gallatin)

