$(function() {
  function log( message ) {
    for (var i = 0; i < message.length; i++){
      $( "<div>" ).text( message[i].title).appendTo( "#log" );
      $( "<div>" ).text( message[i].url ).appendTo( "#log" );
      $( "<div>" ).text( message[i].relevanceScore ).appendTo( "#log" );
      $( "#log" ).scrollTop( 0 );
    }
  }
   
  var URL = encodeURI("http://localhost:8080/search");
  var currentRequestNumber = 0;

  var timer;
  var interval = 100;

  function getSearchResults(queryVal, requestNumber){
    $("#log").text("");
    console.log("starting request...");
    $.get(URL, {
      url: $("#startUrl").val(),
      q: queryVal
    }, function(data){
      if (currentRequestNumber != requestNumber){
        console.log("discarding old request");
        return;
      }

      console.log("got");
      console.log(data);
      log(data);
    });
  }


  $("#query").keyup(function(){

    var queryVal = $("#query").val();

    if (queryVal && queryVal.length > 2){
      currentRequestNumber++;
      var requestNumber = currentRequestNumber;
      clearTimeout(timer);
      timer = setTimeout(function(){
        getSearchResults(queryVal, requestNumber);
      }, interval);
    }
  });

  $("#query").keydown(function(){
    clearTimeout(timer);
  });
});
