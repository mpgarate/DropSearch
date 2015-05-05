$(function() {
  var searchResultsTemplateSource = $("#search-results-template").html();
  var searchResultsTemplate = Handlebars.compile(searchResultsTemplateSource);

  function log( message ) {

    // for (var i = 0; i < message.length; i++){
    //   $( "<div>" ).text( message[i].title).appendTo( "#log" );
    //   $( "<div>" ).text( message[i].url ).appendTo( "#log" );
    //   $( "<div>" ).text( message[i].relevanceScore ).appendTo( "#log" );
    //   $( "#log" ).scrollTop( 0 );
    // }
  }
   

  var SEARCH_URL = encodeURI("http://localhost:8080/search");
  var START_CRAWL_URL = encodeURI("http://localhost:8080/start_crawl");

  var currentRequestNumber = 0;

  function getSearchResults(queryVal, requestNumber){
    $("#log").text("");
    $.get(SEARCH_URL, {
      url: $("#startUrl").val(),
      q: queryVal
    }, function(data){
      if (currentRequestNumber != requestNumber){
        console.log("discarding old request: " + requestNumber);
        console.log("currentRequestNumber is " + requestNumber);
        return;
      }

      var html = searchResultsTemplate( {searchResults: data} );
      $("#search-results").html(html);
    });
  }

  var searchResultsTimer;
  $("#query").keyup(function(){

    var queryVal = $("#query").val();

    if (queryVal && queryVal.length > 2){
      currentRequestNumber++;
      var requestNumber = currentRequestNumber;
      clearTimeout(searchResultsTimer);
      searchResultsTimer = setTimeout(function(){
        getSearchResults(queryVal, requestNumber);
      }, 100);
    }
  });

  $("#query").keydown(function(){
    clearTimeout(searchResultsTimer);
  });

  function getStartUrl(startUrl){
    $.get(START_CRAWL_URL, {
      url: startUrl
    }, function(data){
      console.log(data);
      if (data.doneCrawling === true){
        clearTimeout(startCrawlTimer);
        $(startCrawlButton).text("Done: " + data.pagesCrawled);
      }

      $(startCrawlButton).text("Crawled " + data.pagesCrawled);
    }).fail(function(){
      clearTimeout(startCrawlTimer);
    });

  }

  var startCrawlTimer;

  var startCrawlButton = "#startCrawl";
  $(startCrawlButton).click(function(){
    $(startCrawlButton).attr("disabled", "disabled");

    var startUrl = $("#startUrl").val();

    getStartUrl(startUrl);

    startCrawlTimer = setInterval(function(){
      getStartUrl(startUrl);
    }, 1600);
  });
});
