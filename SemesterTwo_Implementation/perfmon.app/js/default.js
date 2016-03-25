var selectedNode = 0;

$(function() {

  $.ajax({
        url: "http://127.0.0.1:8080/dashboard?context=nodes",
        dataType: 'json',
        crossDomain : true,
    }).done( function(data) {
        nodes(data);
    });

  var nodes = function(data) {

    $("#node").html("< value=\"-1\">Select Node</option>");

    for (i = 0; i < data.length; i++) {
      if (i == 1) {
        $("#node").append("<option value=\""+ data[i].id +"\" selected=\"selected\">"+ data[i].hostname + "</option>");
        selectedNode = data[i].id;
      } else {
        $("#node").append("<option value=\""+ data[i].id +"\">"+ data[i].hostname + "</option>");
      }
    }
  }
});
