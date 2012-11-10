exports.login = function (req, res){
  console.log(req.body || "I have no body; I'm on my own.");
  res.writeHead(200);
  res.write('sessionid=123');
  res.end();
};
