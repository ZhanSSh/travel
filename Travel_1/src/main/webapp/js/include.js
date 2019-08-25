$(function () {
   $.get({
       url:"header.html",
       async : false ,
       success:function (html) {
          $("#header").html(html);
       }
   });
   $.get({
       url:"footer.html",
       async:false,
       success:function (html) {
           $("#footer").html(html)
       }
   });
});