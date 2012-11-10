var Db = require('mongodb').Db
  , Connection = require('mongodb').Connection
  , Server = require('mongodb').Server
  , BSON = require('mongodb').BSON
  , ObjectID = require('mongodb').ObjectID
  , db;

exports.opendb = function(dbname){
  db = new Db('ampup', new Server('localhost', 27017, {auto_reconnect: true}, {}));
  db.open(function(){});
  return function(req, res, next){
    req.mydb = db;
    next();
  };
};

exports.getSession(db, user_name){
};

exports.getQuestion(db, qid){
};

exports.saveQuestion(db, questOpt){
};

exports.getQuestions(db){
};
