var Db = require('mongodb').Db
  , Connection = require('mongodb').Connection
  , Server = require('mongodb').Server
  , BSON = require('mongodb').BSON
  , ObjectID = require('mongodb').ObjectID;

exports.opendb = function(dbname){
  db = new Db('ampup', new Server('localhost', 27017, {auto_reconnect: true}, {}));
  db.open(function(){});
  return function(req, res, next){
    req.mydb = db;
    next();
  };
};

exports.getSession = function(db, user_name, cb){
  db.collection('sessions', function(err, coll){
    coll.findOne({'user':user_name}, function(err, sess){
      if (err) {
        return cb(err);
      }
      if (sess){
        return cb(null, sess.sess);
      }
      var newsess = Math.round(Math.random()*1000000);
      coll.insert({user:user_name, sess:newsess}, function(err){
        return cb(null, newsess);
      });
    });
  });
};

exports.getQuestion = function(db, qid, cb){
  db.collection('questions', function(err, coll){
    if (err) {
      return cb(err);
    }
    coll.find({id:qid}, function(err, qs){
      if (err) {
        return cb(err);
      }
      qs.toArray(function(err, qsArr){
        if (err) {
          return cb(err);
        }
        if (qsArr.length > 0){
          return cb(null, qsArr[0]);        
        }
        return cb(true);
      });
    });
  });
};

exports.saveQuestion = function(db, questOpt, cb){
  db.collection('questions', function(err, coll){
    if (err) {
      return cb(err);
    }
    coll.insert(questOpt, cb);
  });
};

exports.getQuestions = function(db, cb){
  db.collection('questions', function(err, coll){
    if (err) {
      return cb(err);
    }
    coll.find({},['title', 'subtitle', 'id'], function(err, qs){
      if (err) {
        return cb(err);
      }
      qs.toArray(function(err, qsArr){
        if (err) {
          return cb(err);
        }
        return cb(null, qsArr);        
      });
    });
  });
};
