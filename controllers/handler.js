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

function checksyntax(request) {
    switch (request.mime) {
      case 'application/rdf+xml':
        fs.writeFile('temp.dat', request.payload.toString());
        return exec('rapper -g -o ntriples temp.dat');
        break;

      default:
        return (new Promise()).reject('WrongMimeType');
    }
}

function checkliterals() {

}

function checkvocabs() {

}
