var mon = require("./mongoacc");

exports.new = function (req, res){
  console.log(req.body || "I have no body; I'm on my own.");
  mon.getSession(req.mydb, req.body.user, function(err, sid){
    if (err){
      res.writeHead(500);
      res.write('nosession');
      res.end();
    }
    res.writeHead(200);
    res.write('{"sid":"'+sid+'"}');
    res.end();
  });
};
