exports.list = function(req, res){
  var qlist = [{
    title: "EX 1",
    subtitle: "first exercise",
    image: "",
    text: "test text",
    id: '123',
    vidur: true
  }];
  res.writeHead(200);
  res.write(JSON.stringify(qlist));
  res.end();
};

exports.show = function(req, res){
  console.log(req.params || 'no params');
  var quest = {
    title: "EX 1",
    subtitle: "first exercise",
    image: "",
    text: "test text",
    id: '123',
    vidur: true
  };
  res.writeHead(200);
  res.write(JSON.stringify(quest));
  res.end();
};


exports.new = function(req, res){
  console.log(req.params || 'no params');
  console.log(req.body || 'nobody knows');
  res.writeHead(200);
  res.write("ok");
  res.end();
};

exports.newAnswer = function(req, res){
  console.log(req.params || 'no params');
  console.log(req.body || 'nobody knows');
  console.log(req.files || 'no files');
  res.writeHead(200);
  res.write("ok");
  res.end();
};
