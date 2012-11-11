var mon = require("./mongoacc"),
    ObjectID = require('mongodb').ObjectID,
    fs = require("fs");

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
exports.showResComs = function(req, res){
  console.log(req.params || 'no params');
  mon.getComments(req.mydb, req.params['rid'] ,function(err, qs){
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
  console.log(req.files || 'no files');
  var image_path = "";
  if (req.files['image']){
    image_path = '/public/images/' +
      req.body.title.replace(/ /g,'_') + '_image.jpg';
    fs.readFile(req.files['image'].path, function(err, data){
      fs.writeFile( __dirname + image_path, data, function(err){
        console.log('file saved');
      });
    });
  }
  req.body.image = image_path;
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
  req.body['question'] = new ObjectID(req.params['id']);
  var image_path = "", audio_path = '';
  if (req.files['image']){
    image_path = '/public/images/' +
      req.body.question + '_' +  req.body.session + '_image.jpg';
    fs.readFile(req.files['image'].path, function(err, data){
      fs.writeFile( __dirname + image_path, data, function(err){
        console.log('file saved');
      });
    });
  }
  if (req.files['audio']){
    audio_path = '/public/audio/' +
      req.body.question + '_' +  req.body.session + '_audio.3gp';
    fs.readFile(req.files['image'].path, function(err, data){
      fs.writeFile( __dirname + audio_path, data, function(err){
        console.log('file saved');
      });
    });
  }
  req.body.image = image_path;
  req.body.audio = audio_path;
  mon.saveAnswer(req.mydb, req.body, function(err){
    if (err){
      res.writeHead(500);
      return res.end(JSON.stringify({'error':500}));
    }
    res.writeHead(200);
    res.write("ok");
    res.end();
  });
};

exports.newComment = function(req, res){
  console.log(req.params || 'no params');
  console.log(req.body || 'nobody knows');
  console.log(req.files || 'no files');
  req.body['question'] = new ObjectID(req.params['id']);
  req.body['response'] = new ObjectID(req.params['rid']);
  mon.saveComment(req.mydb, req.body, function(err){
    if (err){
      res.writeHead(500);
      return res.end(JSON.stringify({'error':500}));
    }
    res.writeHead(200);
    res.write("ok");
    res.end();

  });
};
