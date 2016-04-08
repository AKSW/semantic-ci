'use strict';

const boom = require('boom'),
  exec = require('child-process-promise').exec,
  fs = require('fs');

module.exports = {
  validate: function(request, reply) {
    checksyntax(request) //.checkliterals().checkvocabs()
      .then((result) => {
        reply(result);
      })
      .catch(() => reply(boom.badImplementation()));
  }
};

function checksyntax() {
  return new Promise((resolve, reject) => {
    switch (request.mime) {
      case 'application/rdf+xml':
        fs.writeFile('temp.dat', request.payload.toString());
        exec('rapper -g -o ntriples temp.dat')
          .then((result) => {
            resolve(result);
          })
          .catch((err) => {
            reject(err);
          });
        break;

      default:
        reject('WrongMimeType');
    }
  });
}

function checkliterals() {

}

function checkvocabs() {

}
