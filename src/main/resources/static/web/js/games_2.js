$(function() {
    loadData()
});

function updateViewGames(data) {
  var htmlList = data.map(function (games) {
      return  '<li class="list-group-item">' + new Date(games.creationDate).toLocaleString() + ' ' + games.gamePlayers.map(function(p) { return p.player.userName}).join(', ')  +'</li>';
  }).join('');
  document.getElementById("game-list").innerHTML = htmlList;
}

function updateViewLBoard(data) {
  var htmlList = data.map(function (score) {
      return  '<tr><td>' + score.userName + '</td>'
              + '<td>' + score.total + '</td>'
              + '<td>' + score.won + '</td>'
              + '<td>' + score.lost + '</td>'
              + '<td>' + score.tied + '</td></tr>';
  }).join('');
  document.getElementById("leader-list").innerHTML = htmlList;
}

function loadData() {
  $.get("/api/games")
    .done(function(data) {
      updateViewGames(data);
    })
    .fail(function( jqXHR, textStatus ) {
      alert( "Failed: " + textStatus );
    });
  
  $.get("/api/leaderBoard")
    .done(function(data) {
      updateViewLBoard(data);
    })
    .fail(function( jqXHR, textStatus ) {
      alert( "Failed: " + textStatus );
    });
}