var Db = require('mongodb').Db
  , Connection = require('mongodb').Connection
  , Server = require('mongodb').Server
  , BSON = require('mongodb').BSON
  , async = require("async")
  , ObjectID = require('mongodb').ObjectID;

exports.opendb = function(dbname){
  db = new Db('ampup', new Server('localhost', 27017, {auto_reconnect: true}, {}));
  db.open(function(){});
  return function(req, res, next){
    req.mydb = db;
    next();
  };
};

exports.getUser = function(db, session, cb){
  db.collection('sessions', function(err, coll){
    coll.findOne({'sess':session}, function(err, user){
      if (err){
        return cb(err);
      }
      return cb(null, user);
    });
  });
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
    coll.findOne({_id:new ObjectID(qid)}, function(err, qs){
      if (err) {
        return cb(err);
      }
      db.collection('answers', function(err, anscoll){
        if (err) {
          return cb(err);
        }
        anscoll.find({question:qs._id}, function(err, ansC){
          if (err) {
            return cb(err);
          }
          ansC.toArray(function(err, arr){
            async.map(arr, 
              function(ele, cb){
                exports.getUser(db, ele.session, function(err, user){
                  db.collection('comments', function(err, comm){
                    comm.count({response:ele._id}, function(err, count){
                      if (err || !user){
                        ele.comments = count;
                        ele.user = 'no-user';
                        return cb(null, ele);
                      }
                      ele.comments = count;
                      ele.user = user.user;
                      return cb(null, ele);
                    });
                  });
                });
              },
              function(err, resArr){
                qs['responses'] = resArr;
                return cb(null, qs);
              });
          });
        });
      });
    });
  });
};

exports.getComments = function(db, response, cb){
  db.collection('comments', function(err, comm){
    comm.find({response:new ObjectID(response)}, function(err, comCur){
      comCur.toArray(function(err, arr){
        if (err){
          return cb(err);
        }
        return cb(null, arr);
      });
    });
  });
};

exports.saveComment = function(db, questOpt, cb){
  db.collection('comments', function(err, coll){
    if (err) {
      return cb(err);
    }
    coll.insert(questOpt, cb);
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

exports.saveAnswer = function(db, questOpt, cb){
  db.collection('answers', function(err, coll){
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
    coll.find({},['title', 'subtitle', '_id'], function(err, qs){
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
