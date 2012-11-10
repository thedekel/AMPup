var mon = require("./mongoacc");

exports.list = function(req, res){
  mon.getQuestions(req.mydb, function(err, qsArr){
    if (err){
      res.writeHead(500);
      return res.end(JSON.stringify({'error':500}));
    }
    res.writeHead(200);
    res.write(JSON.stringify({result_array:qsArr}));
    return res.end();
  });
  
};

exports.show = function(req, res){
  console.log(req.params || 'no params');
  mon.getQuestion(req.mydb, req.params['id'] ,function(err, qs){
    if (err){
      res.writeHead(500);
      return res.end(JSON.stringify({'error':500}));
    }
    res.writeHead(200);
    res.write(JSON.stringify(qs));
    return res.end();
  });
};

exports.new = function(req, res){
  console.log(req.params || 'no params');
  console.log(req.body || 'nobody knows');
  mon.saveQuestion(req.mydb, req.body, function(err){
    if (err){
      res.writeHead(500);
      return res.end(JSON.stringify({'error':500}));
    }
    res.writeHead(200);
    res.write("ok");
    res.end();
  });
};

exports.newAnswer = function(req, res){
  console.log(req.params || 'no params');
  console.log(req.body || 'nobody knows');
  console.log(req.files || 'no files');
  res.writeHead(200);
  res.write("ok");
  res.end();
};
